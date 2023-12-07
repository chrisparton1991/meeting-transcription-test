import com.microsoft.cognitiveservices.speech.audio.PullAudioInputStreamCallback;

public class FileAudioStreamCallback extends PullAudioInputStreamCallback {

    private final byte[] mp3Bytes;
    private int byteIndex = 0;

    public FileAudioStreamCallback(byte[] mp3Bytes) {
        this.mp3Bytes = mp3Bytes;
    }

    @Override
    public int read(byte[] buffer) {
        int startIndex = byteIndex;

        for (int i = 0; i < buffer.length; i++) {
            if (byteIndex + i < mp3Bytes.length- 1) {
                buffer[i] = mp3Bytes[byteIndex + i];
                byteIndex++;
            }
        }

        return byteIndex - startIndex;
    }

    @Override
    public void close() {
        // Nothing to do.
    }
}
