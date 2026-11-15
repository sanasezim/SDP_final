import java.util.List;
import java.util.Map;
import java.util.Scanner;

public final class Main {
    private static final Map<String, Double> NOTE_FREQUENCIES =
            Map.ofEntries(
                    Map.entry("C4", 261.63),
                    Map.entry("D4", 293.66),
                    Map.entry("E4", 329.63),
                    Map.entry("F4", 349.23),
                    Map.entry("G4", 392.00),
                    Map.entry("G#4", 415.30),
                    Map.entry("A4", 440.00),
                    Map.entry("B4", 493.88),
                    Map.entry("C5", 523.25),
                    Map.entry("D5", 587.33),
                    Map.entry("D#5", 622.25),
                    Map.entry("E5", 659.25)
            );

    private static final String ODE_TO_JOY =
            "E4 E4 F4 G4 G4 F4 E4 D4 C4 C4 D4 E4 E4 D4 D4 "
                    + "E4 E4 F4 G4 G4 F4 E4 D4 C4 C4 D4 E4 D4 C4 C4";

    private static final String FUR_ELISE =
            "E5 D#5 E5 D#5 E5 B4 D5 C5 A4 "
                    + "C4 E4 A4 B4 "
                    + "E4 G#4 B4 C5 "
                    + "E4 E5 D#5 E5 D#5 E5 B4 D5 C5 A4 "
                    + "C4 E4 A4 B4 "
                    + "E4 C5 B4 A4";

    private static final String AH_VOUS_DIRAI_JE_MAMAN =
            "C4 C4 G4 G4 A4 A4 G4 "
                    + "F4 F4 E4 E4 D4 D4 C4 "
                    + "G4 G4 F4 F4 E4 E4 D4 "
                    + "G4 G4 F4 F4 E4 E4 D4 "
                    + "C4 C4 G4 G4 A4 A4 G4 "
                    + "F4 F4 E4 E4 D4 D4 C4";

    public static void main(String[] args) {
        MusicLibrary library = new MusicLibrary(createTracks());
        MusicPlayer player = new MusicPlayer();

        MusicServiceFacade service =
                new MusicServiceFacade(library, player);

        LyricsDisplay lyricsDisplay = new LyricsDisplay();

        service.subscribe(new PlaybackDisplay());
        service.subscribe(lyricsDisplay);

        ConsoleApplication application = new ConsoleApplication(
                service,
                new Scanner(System.in),
                lyricsDisplay
        );

        application.run();
    }

    private static List<Track> createTracks() {
        return List.of(
                createMelody(
                        "Ode to Joy - Short",
                        "Ludwig van Beethoven",
                        ODE_TO_JOY,
                        1
                ),
                createMelody(
                        "Ode to Joy - Extended",
                        "Ludwig van Beethoven",
                        ODE_TO_JOY,
                        2
                ),
                createMelody(
                        "Fur Elise - Short",
                        "Ludwig van Beethoven",
                        FUR_ELISE,
                        1
                ),
                createMelody(
                        "Fur Elise - Extended",
                        "Ludwig van Beethoven",
                        FUR_ELISE,
                        2
                ),
                createMelody(
                        "Ah vous dirai-je, maman - Short",
                        "Traditional / Theme used by Mozart",
                        AH_VOUS_DIRAI_JE_MAMAN,
                        1
                ),
                createMelody(
                        "Ah vous dirai-je, maman - Extended",
                        "Traditional / Theme used by Mozart",
                        AH_VOUS_DIRAI_JE_MAMAN,
                        2
                )
        );
    }

    private static Track createMelody(
            String title,
            String artist,
            String melody,
            int repetitions
    ) {
        if (repetitions < 1) {
            throw new IllegalArgumentException(
                    "Repetitions must be positive."
            );
        }

        String[] noteNames = melody.strip().split("\\s+");
        int totalNotes = noteNames.length * repetitions;

        Double[] frequencies = new Double[totalNotes];
        String[] displayLines = new String[totalNotes];

        for (int index = 0; index < totalNotes; index++) {
            String noteName = noteNames[index % noteNames.length];

            frequencies[index] = frequencyOf(noteName);

            displayLines[index] = String.format(
                    "Instrumental | %d/%d sec | Note: %s",
                    index + 1,
                    totalNotes,
                    noteName
            );
        }

        return new Track.Builder()
                .title(title)
                .artist(artist)
                .notes(frequencies)
                .lyrics(displayLines)
                .build();
    }

    private static double frequencyOf(String noteName) {
        Double frequency = NOTE_FREQUENCIES.get(noteName);

        if (frequency == null) {
            throw new IllegalArgumentException(
                    "Unknown note: " + noteName
            );
        }

        return frequency;
    }
}