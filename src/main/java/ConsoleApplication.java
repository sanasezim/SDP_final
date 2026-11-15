import javax.sound.sampled.LineUnavailableException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public final class ConsoleApplication {
    private final MusicServiceFacade service;
    private final Scanner input;
    private final LyricsDisplay lyricsDisplay;

    private TrackSortStrategy sorting = new TitleSortStrategy();

    private boolean echoEnabled;
    private boolean lyricsEnabled = true;
    private boolean silent;
    private double volume = 0.5;

    public ConsoleApplication(
            MusicServiceFacade service,
            Scanner input,
            LyricsDisplay lyricsDisplay
    ) {
        this.service = service;
        this.input = input;
        this.lyricsDisplay = lyricsDisplay;
    }

    public void run() {
        boolean running = true;

        while (running) {
            printMenu();

            try {
                running = handleChoice(read("Choose: "));
            } catch (IllegalArgumentException exception) {
                System.out.println(
                        "Invalid input or unavailable audio: "
                                + exception.getMessage()
                );
            } catch (LineUnavailableException exception) {
                System.out.println(
                        "Audio unavailable. Enable silent mode with option 8."
                );
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
                running = false;
            } catch (NoSuchElementException exception) {
                running = false;
            }
        }

        System.out.println("Goodbye!");
    }

    private void printMenu() {
        System.out.println();
        System.out.println("=== MUSIC STREAMING SERVICE ===");
        System.out.println("1. Show library");
        System.out.println("2. Search");
        System.out.println("3. Change sorting");
        System.out.println("4. Play track");
        System.out.println("5. Toggle echo: " + echoEnabled);
        System.out.println("6. Set volume: " + volume);
        System.out.println("7. Toggle lyrics: " + lyricsEnabled);
        System.out.println("8. Toggle silent mode: " + silent);
        System.out.println("0. Exit");
    }

    private boolean handleChoice(String choice)
            throws LineUnavailableException, InterruptedException {
        switch (choice) {
            case "1" -> showTracks(service.getTracks(sorting));
            case "2" -> search();
            case "3" -> changeSorting();
            case "4" -> playTrack();
            case "5" -> echoEnabled = !echoEnabled;
            case "6" -> changeVolume();
            case "7" -> toggleLyrics();
            case "8" -> silent = !silent;
            case "0" -> {
                return false;
            }
            default -> System.out.println("Choose a number from 0 to 8.");
        }

        return true;
    }

    private void search() {
        String query = read("Title or artist: ");
        showTracks(service.search(query, sorting));
    }

    private void changeSorting() {
        String choice = read("1. Title, 2. Duration: ");

        sorting = switch (choice) {
            case "1" -> new TitleSortStrategy();
            case "2" -> new DurationSortStrategy();
            default -> throw new IllegalArgumentException(
                    "Choose 1 or 2."
            );
        };

        showTracks(service.getTracks(sorting));
    }

    private void playTrack()
            throws LineUnavailableException, InterruptedException {
        List<Track> tracks = service.getTracks(sorting);

        showTracks(tracks);

        int index = Integer.parseInt(read("Track number: ")) - 1;

        if (index < 0 || index >= tracks.size()) {
            throw new IllegalArgumentException("Track number is invalid.");
        }

        Command command = new PlayTrackCommand(
                service,
                tracks.get(index),
                echoEnabled,
                volume,
                silent
        );

        command.execute();
    }

    private void changeVolume() {
        double selectedVolume =
                Double.parseDouble(read("Volume from 0 to 1: "));

        if (!Double.isFinite(selectedVolume)
                || selectedVolume < 0
                || selectedVolume > 1) {
            throw new IllegalArgumentException(
                    "Volume must be between 0 and 1."
            );
        }

        volume = selectedVolume;
    }

    private void toggleLyrics() {
        lyricsEnabled = !lyricsEnabled;

        if (lyricsEnabled) {
            service.subscribe(lyricsDisplay);
        } else {
            service.unsubscribe(lyricsDisplay);
        }
    }

    private void showTracks(List<Track> tracks) {
        if (tracks.isEmpty()) {
            System.out.println("No tracks found.");
            return;
        }

        for (int index = 0; index < tracks.size(); index++) {
            Track track = tracks.get(index);

            System.out.printf(
                    "%d. %s — %s [%d seconds]%n",
                    index + 1,
                    track.getTitle(),
                    track.getArtist(),
                    track.getDurationSeconds()
            );
        }
    }

    private String read(String prompt) {
        System.out.print(prompt);
        return input.nextLine().strip();
    }
}