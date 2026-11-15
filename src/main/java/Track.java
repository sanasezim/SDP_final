import java.util.List;

public final class Track {
    private final String title;
    private final String artist;
    private final List<Double> notes;
    private final List<String> lyrics;

    private Track(Builder builder) {
        title = requireText(builder.title, "Title");
        artist = requireText(builder.artist, "Artist");

        if (builder.notes.isEmpty()) {
            throw new IllegalArgumentException("Track must contain notes.");
        }

        for (double frequency : builder.notes) {
            if (!Double.isFinite(frequency)
                    || frequency < 40
                    || frequency > 4000) {
                throw new IllegalArgumentException(
                        "Frequency must be between 40 and 4000 Hz."
                );
            }
        }

        if (builder.lyrics.size() != builder.notes.size()) {
            throw new IllegalArgumentException(
                    "Each note must have one lyric line."
            );
        }

        for (String line : builder.lyrics) {
            requireText(line, "Lyric line");
        }

        notes = List.copyOf(builder.notes);
        lyrics = List.copyOf(builder.lyrics);
    }

    private static String requireText(String value, String name) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(name + " is required.");
        }

        return value.strip();
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public List<Double> getNotes() {
        return notes;
    }

    public List<String> getLyrics() {
        return lyrics;
    }

    public int getDurationSeconds() {
        return notes.size();
    }

    public static final class Builder {
        private String title;
        private String artist;
        private List<Double> notes = List.of();
        private List<String> lyrics = List.of();

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder artist(String artist) {
            this.artist = artist;
            return this;
        }

        public Builder notes(Double... notes) {
            this.notes = List.of(notes);
            return this;
        }

        public Builder lyrics(String... lyrics) {
            this.lyrics = List.of(lyrics);
            return this;
        }

        public Track build() {
            return new Track(this);
        }
    }
}