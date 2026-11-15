import java.util.Comparator;
import java.util.List;

public final class DurationSortStrategy implements TrackSortStrategy {
    @Override
    public List<Track> sort(List<Track> tracks) {
        return tracks.stream()
                .sorted(Comparator.comparingInt(
                        Track::getDurationSeconds
                ))
                .toList();
    }
}