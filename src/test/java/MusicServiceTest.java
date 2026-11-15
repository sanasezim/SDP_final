import java.util.List;

public final class MusicServiceTest {
    public static void main(String[] args) {
        testTrackCreation();
        testSearch();
        testVolumeDecorator();
        testInvalidTrack();

        System.out.println("\nAll 4 tests passed!");
    }

    private static void testTrackCreation() {
        // Arrange and Act
        Track track = createTrack();

        // Assert
        check(
                track.getTitle().equals("Test Song"),
                "Builder must save the title."
        );

        check(
                track.getDurationSeconds() == 2,
                "Two notes must produce a two-second track."
        );

        System.out.println("PASS: track creation");
    }

    private static void testSearch() {
        // Arrange
        MusicLibrary library = new MusicLibrary(
                List.of(createTrack())
        );

        // Act
        List<Track> result = library.search("TEST ARTIST");

        // Assert
        check(
                result.size() == 1
                        && result.get(0).getTitle().equals("Test Song"),
                "Search must find the artist regardless of case."
        );

        check(
                library.search("unknown").isEmpty(),
                "Unknown search must return an empty list."
        );

        System.out.println("PASS: library search");
    }

    private static void testVolumeDecorator() {
        // Arrange: a predictable signal instead of a real audio device.
        AudioStream original = () -> new double[]{0.8, -0.4, 0.0};

        AudioStream quieter = new VolumeDecorator(original, 0.5);

        // Act
        double[] result = quieter.getSamples();

        // Assert
        check(result.length == 3, "Sample count must stay unchanged.");

        checkClose(0.4, result[0]);
        checkClose(-0.2, result[1]);
        checkClose(0.0, result[2]);

        System.out.println("PASS: volume decorator");
    }

    private static void testInvalidTrack() {
        boolean rejected = false;

        try {
            new Track.Builder()
                    .title("Incomplete Song")
                    .build();
        } catch (IllegalArgumentException exception) {
            rejected = true;
        }

        check(rejected, "Builder must reject an incomplete track.");

        System.out.println("PASS: invalid track rejected");
    }

    private static Track createTrack() {
        return new Track.Builder()
                .title("Test Song")
                .artist("Test Artist")
                .notes(440.0, 493.88)
                .lyrics("First line", "Second line")
                .build();
    }

    private static void checkClose(double expected, double actual) {
        check(
                Math.abs(expected - actual) < 0.000001,
                "Expected " + expected + ", received " + actual
        );
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}