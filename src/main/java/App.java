import com.microsoft.cognitiveservices.speech.ServicePropertyChannel;
import com.microsoft.cognitiveservices.speech.SpeechConfig;
import com.microsoft.cognitiveservices.speech.audio.*;
import com.microsoft.cognitiveservices.speech.transcription.Meeting;
import com.microsoft.cognitiveservices.speech.transcription.MeetingTranscriber;

import java.io.InputStream;
import java.util.UUID;
import java.util.concurrent.Future;

public class App {
    public static void main(String[] args) throws Exception {
        // Create the speech config object
        // Substitute real information for "YourSubscriptionKey" and "Region"
        SpeechConfig speechConfig = SpeechConfig.fromSubscription("TODO", "australiaeast");
        speechConfig.setProperty("ConversationTranscriptionInRoomAndOnline", "true");

        // Set the property for asynchronous transcription
        speechConfig.setServiceProperty("transcriptionMode", "async", ServicePropertyChannel.UriQueryParameter);

        // Set the property for real-time plus asynchronous transcription
        //speechConfig.setServiceProperty("transcriptionMode", "RealTimeAndAsync", ServicePropertyChannel.UriQueryParameter);

        // pick a meeting Id that is a GUID.
        String meetingId = UUID.randomUUID().toString();

        // Create a Meeting
        Future<Meeting> meetingFuture = Meeting.createMeetingAsync(speechConfig, meetingId);
        Meeting meeting = meetingFuture.get();

        // Create an audio stream from a wav file or from the default microphone if you want to stream live audio from the supported devices
        // Replace with your own audio file name and Helper class which implements AudioConfig using PullAudioInputStreamCallback
        PullAudioInputStreamCallback callback;

        try (InputStream stream = App.class.getResourceAsStream("/sample.mp3")) {
            assert stream != null;
            callback = new FileAudioStreamCallback(stream.readAllBytes());
        }

        // Create an audio stream format assuming the file used above is 16kHz, 16 bits and 8 channel pcm wav file
        AudioStreamFormat audioStreamFormat = AudioStreamFormat.getCompressedFormat(AudioStreamContainerFormat.MP3);
        // Create an input stream
        AudioInputStream audioStream = AudioInputStream.createPullStream(callback, audioStreamFormat);

        // Create a meeting transcriber
        MeetingTranscriber transcriber = new MeetingTranscriber(AudioConfig.fromStreamInput(audioStream));

        // join a meeting
        transcriber.joinMeetingAsync(meeting);

        // Add the event listener for the real-time events
        transcriber.transcribed.addEventListener((o, e) -> {
            System.out.println("Meeting transcriber Recognized:" + e.toString());
        });

        transcriber.canceled.addEventListener((o, e) -> {
            System.out.println("Meeting transcriber canceled:" + e.toString());
            try {
                transcriber.stopTranscribingAsync().get();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        transcriber.sessionStopped.addEventListener((o, e) -> {
            System.out.println("Meeting transcriber stopped:" + e.toString());

            try {
                transcriber.stopTranscribingAsync().get();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // start the transcription.
        transcriber.startTranscribingAsync().get();
        System.out.println("Finished");
    }
}
