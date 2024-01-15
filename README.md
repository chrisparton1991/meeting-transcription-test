# meeting-transcription-test

I followed the documentation at https://learn.microsoft.com/en-us/azure/ai-services/speech-service/how-to-async-meeting-transcription?pivots=programming-language-java.

If you run `App.java` (with or without a valid subscription key), the JVM will crash with a segfault on
`transcriber.startTranscribingAsync().get();`:

```
# A fatal error has been detected by the Java Runtime Environment:
#
#  SIGSEGV (0xb) at pc=0x0000000105922710, pid=63988, tid=29955
#
# JRE version: OpenJDK Runtime Environment Zulu21.30+15-CA (21.0.1+12) (build 21.0.1+12-LTS)
# Java VM: OpenJDK 64-Bit Server VM Zulu21.30+15-CA (21.0.1+12-LTS, mixed mode, sharing, tiered, compressed oops, compressed class ptrs, g1 gc, bsd-aarch64)
# Problematic frame:
# C  [libMicrosoft.CognitiveServices.Speech.core.dylib+0x1ba710]  GetModuleObject+0x10016c
```

See the `hs_err_pid63988.log` error report in the root directory.
