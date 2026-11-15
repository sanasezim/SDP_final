public final class LyricsDisplay implements TrackObserver {
    private int nextLine;

    @Override
    public void onTrackStarted(Track track) {
        nextLine = 0;
        System.out.println("Lyrics:");
    }

    @Override
    public void onPositionChanged(Track track, int second) {
        while (nextLine <= second
                && nextLine < track.getLyrics().size()) {
            System.out.println(track.getLyrics().get(nextLine));
            nextLine++;
        }
    }

    @Override
    public void onTrackFinished(Track track) {
        System.out.println("End of lyrics.");
    }
}