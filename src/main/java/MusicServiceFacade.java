import javax.sound.sampled.LineUnavailableException;
import java.util.List;

public final class MusicServiceFacade {
    private final MusicLibrary library;
    private final MusicPlayer player;

    public MusicServiceFacade(MusicLibrary library, MusicPlayer player) {
        this.library = library;
        this.player = player;
    }

    public List<Track> getTracks(TrackSortStrategy sorting) {
        return sorting.sort(library.getAll());
    }

    public List<Track> search(String query, TrackSortStrategy sorting) {
        return sorting.sort(library.search(query));
    }

    public void subscribe(TrackObserver observer) {
        player.subscribe(observer);
    }

    public void unsubscribe(TrackObserver observer) {
        player.unsubscribe(observer);
    }

    public void play(
            Track track,
            boolean echoEnabled,
            double volume,
            boolean silent
    ) throws LineUnavailableException, InterruptedException {
        AudioStream stream = new BasicAudioStream(track);

        if (echoEnabled) {
            stream = new EchoDecorator(stream, 250, 0.4);
        }

        stream = new VolumeDecorator(stream, volume);

        player.play(track, stream, silent);
    }
}