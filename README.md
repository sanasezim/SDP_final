# Music Streaming Service

A console-based Java application that demonstrates **six design patterns** in one music playback workflow.
Users can browse a music library, search tracks, change sorting, choose audio effects and play a melody.

Audio is generated mathematically: musical notes are frequencies converted to sine-wave samples and played
with the Java Sound API. No external music files, online services or third-party libraries are required.

> Course: Software Design Patterns — Astana IT University
> Group: SE-2504 · Instructor: Ospanov Almas

## Features

- Music library with 3 melodies in short and extended versions (6 tracks)
- Search by title or artist (case-insensitive)
- Sorting by title or duration
- Echo effect (delay 250 ms, strength 0.4) and volume control
- Instrumental progress display (can be switched off at runtime)
- Silent mode: playback progress without audio output
- Input validation: wrong input shows a message instead of crashing

## Design patterns

| Pattern | Classes | Role in the project |
|---|---|---|
| Builder | `Track`, `Track.Builder` | Creates validated immutable tracks |
| Strategy | `TrackSortStrategy`, `TitleSortStrategy`, `DurationSortStrategy` | Interchangeable sorting algorithms |
| Decorator | `AudioStream`, `BasicAudioStream`, `AudioDecorator`, `VolumeDecorator`, `EchoDecorator` | Adds effects to audio samples |
| Facade | `MusicServiceFacade` | One simple interface for library, audio chain and player |
| Observer | `TrackObserver`, `MusicPlayer`, `PlaybackDisplay`, `LyricsDisplay` | Notifies displays about playback events |
| Command | `Command`, `PlayTrackCommand`, `ConsoleApplication` | Represents a playback request as an object |

## Playback workflow

1. `ConsoleApplication` shows the library using the current sorting **Strategy**.
2. The user selects a track; the app creates a `PlayTrackCommand` (**Command**).
3. `command.execute()` calls `MusicServiceFacade.play()` (**Facade**).
4. The facade builds the audio chain: `BasicAudioStream → EchoDecorator → VolumeDecorator` (**Decorator**).
5. `MusicPlayer` plays the audio and notifies subscribers about start, position and finish (**Observer**).

## Project structure

```
SDP_final/
├── pom.xml
└── src/
    ├── main/java/     # all application classes (Main is the entry point)
    └── test/java/     # MusicServiceTest
```

## Requirements

- JDK 17
- IntelliJ IDEA (recommended) or Maven

## How to run

**IntelliJ IDEA:** open the project and run `Main`.

**Terminal:**
```bash
mvn compile
java -cp target/classes Main
```

If your computer has no audio device, turn on silent mode with menu option 8.

## Menu

| Option | Action |
|---|---|
| 1 | Show library |
| 2 | Search by title or artist |
| 3 | Change sorting (title / duration) |
| 4 | Play track |
| 5 | Toggle echo |
| 6 | Set volume (from 0 to 1, for example 0.5 = 50%) |
| 7 | Toggle instrumental display |
| 8 | Toggle silent mode |
| 0 | Exit |

## Tests

`MusicServiceTest` is a lightweight test program without external libraries. It checks track creation,
library search, the volume decorator and invalid track rejection.

Run `MusicServiceTest` from IntelliJ, or:
```bash
mvn test-compile
java -cp target/classes:target/test-classes MusicServiceTest
```
Expected output: `All 4 tests passed!`

## Limitations

- Every note lasts one second (simplified arrangements, not full compositions)
- Playback is synchronous: no pause or stop while a track is playing
- No user accounts, persistent storage, network streaming or command history

## Authors

Aibyn Mukhametsharip, Ali Toretay
