import java.util.List;
import java.util.Locale;

public final class MusicLibrary {
    private final List<Track> tracks;

    public MusicLibrary(List<Track> tracks) {
        this.tracks = List.copyOf(tracks);
    }

    public List<Track> getAll() {
        return tracks;
    }

    public List<Track> search(String query) {
        String searchText = query.strip().toLowerCase(Locale.ROOT);

        return tracks.stream()
                .filter(track -> matches(track, searchText))
                .toList();
    }

    private boolean matches(Track track, String query) {
        String description = track.getTitle() + " " + track.getArtist();

        return description.toLowerCase(Locale.ROOT).contains(query);
    }
}