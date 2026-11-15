public final class PlaybackDisplay implements TrackObserver {
    @Override
    public void onTrackStarted(Track track) {
        System.out.println();
        System.out.println(
                "Now playing: "
                        + track.getTitle()
                        + " — "
                        + track.getArtist()
        );
    }

    @Override
    public void onPositionChanged(Track track, int second) {
        // This display only handles the start and end of playback.
    }

    @Override
    public void onTrackFinished(Track track) {
        System.out.println("Playback finished.");
    }
}