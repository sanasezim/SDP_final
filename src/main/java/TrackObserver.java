public interface TrackObserver {
    void onTrackStarted(Track track);

    void onPositionChanged(Track track, int second);

    void onTrackFinished(Track track);
}