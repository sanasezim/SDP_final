import java.util.Comparator;
import java.util.List;

public final class TitleSortStrategy implements TrackSortStrategy {
    @Override
    public List<Track> sort(List<Track> tracks) {
        return tracks.stream()
                .sorted(Comparator.comparing(
                        Track::getTitle,
                        String.CASE_INSENSITIVE_ORDER
                ))
                .toList();
    }
}