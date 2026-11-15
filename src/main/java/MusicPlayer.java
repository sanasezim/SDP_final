import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public final class MusicPlayer {
    private static final int UPDATE_INTERVAL_MILLIS = 50;

    private final Set<TrackObserver> observers = new LinkedHashSet<>();

    public void subscribe(TrackObserver observer) {
        observers.add(Objects.requireNonNull(observer));
    }

    public void unsubscribe(TrackObserver observer) {
        observers.remove(observer);
    }

    public void play(
            Track track,
            AudioStream stream,
            boolean silent
    ) throws LineUnavailableException, InterruptedException {
        if (silent) {
            playSilently(track);
        } else {
            playWithAudio(track, stream);
        }
    }

    private void playSilently(Track track) throws InterruptedException {
        notifyStarted(track);

        long started = System.nanoTime();
        long durationMillis = track.getDurationSeconds() * 1000L;

        while (true) {
            long elapsedMillis =
                    (System.nanoTime() - started) / 1_000_000;

            if (elapsedMillis >= durationMillis) {
                break;
            }

            notifyPosition(track, (int) (elapsedMillis / 1000));
            Thread.sleep(UPDATE_INTERVAL_MILLIS);
        }

        notifyPosition(track, track.getDurationSeconds() - 1);
        notifyFinished(track);
    }

    private void playWithAudio(
            Track track,
            AudioStream stream
    ) throws LineUnavailableException, InterruptedException {
        byte[] audioData = encode(stream.getSamples());

        AudioFormat format = new AudioFormat(
                AudioStream.SAMPLE_RATE,
                16,
                1,
                true,
                false
        );

        try (Clip clip = AudioSystem.getClip()) {
            clip.open(format, audioData, 0, audioData.length);

            notifyStarted(track);
            clip.start();

            waitForPlayback(track, clip);

            notifyPosition(track, track.getDurationSeconds() - 1);
            notifyFinished(track);
        }
    }

    private void waitForPlayback(
            Track track,
            Clip clip
    ) throws InterruptedException, LineUnavailableException {
        long timeoutMillis =
                track.getDurationSeconds() * 1000L + 3000;

        long deadline =
                System.nanoTime() + timeoutMillis * 1_000_000;

        while (clip.getLongFramePosition() < clip.getFrameLength()) {
            if (System.nanoTime() > deadline) {
                throw new LineUnavailableException(
                        "Audio device stopped responding."
                );
            }

            int second =
                    (int) (clip.getMicrosecondPosition() / 1_000_000);

            notifyPosition(track, second);
            Thread.sleep(UPDATE_INTERVAL_MILLIS);
        }
    }

    private byte[] encode(double[] samples) {
        byte[] bytes = new byte[samples.length * 2];

        for (int index = 0; index < samples.length; index++) {
            double bounded = Math.max(-1, Math.min(1, samples[index]));
            int value = (int) Math.round(bounded * Short.MAX_VALUE);

            bytes[index * 2] = (byte) (value & 0xff);
            bytes[index * 2 + 1] = (byte) ((value >>> 8) & 0xff);
        }

        return bytes;
    }

    private void notifyStarted(Track track) {
        for (TrackObserver observer : List.copyOf(observers)) {
            observer.onTrackStarted(track);
        }
    }

    private void notifyPosition(Track track, int second) {
        for (TrackObserver observer : List.copyOf(observers)) {
            observer.onPositionChanged(track, second);
        }
    }

    private void notifyFinished(Track track) {
        for (TrackObserver observer : List.copyOf(observers)) {
            observer.onTrackFinished(track);
        }
    }
}