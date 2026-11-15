import javax.sound.sampled.LineUnavailableException;

public final class PlayTrackCommand implements Command {
    private final MusicServiceFacade service;
    private final Track track;
    private final boolean echoEnabled;
    private final double volume;
    private final boolean silent;

    public PlayTrackCommand(
            MusicServiceFacade service,
            Track track,
            boolean echoEnabled,
            double volume,
            boolean silent
    ) {
        this.service = service;
        this.track = track;
        this.echoEnabled = echoEnabled;
        this.volume = volume;
        this.silent = silent;
    }

    @Override
    public void execute()
            throws LineUnavailableException, InterruptedException {
        service.play(track, echoEnabled, volume, silent);
    }
}