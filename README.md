# meeting-transcription-test

I followed the documentation at https://learn.microsoft.com/en-us/azure/ai-services/speech-service/how-to-async-meeting-transcription?pivots=programming-language-java.

If you run `App.java` (with or without a valid subscription key), the JVM will crash on
`transcriber.startTranscribingAsync().get();`:

```
Process finished with exit code -1073741819 (0xC0000005)
```

There appears to be a segfault occurring in some native code pulled in by the client SDK.