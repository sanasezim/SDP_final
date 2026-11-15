import java.util.Objects;

public final class BasicAudioStream implements AudioStream {
    private static final double AMPLITUDE = 0.25;
    private static final int FADE_SAMPLES = SAMPLE_RATE / 100;

    private final Track track;

    public BasicAudioStream(Track track) {
        this.track = Objects.requireNonNull(track);
    }

    @Override
    public double[] getSamples() {
        double[] samples =
                new double[track.getDurationSeconds() * SAMPLE_RATE];

        for (int index = 0; index < samples.length; index++) {
            int noteIndex = index / SAMPLE_RATE;
            int notePosition = index % SAMPLE_RATE;

            double frequency = track.getNotes().get(noteIndex);

            samples[index] = createSample(frequency, notePosition);
        }

        return samples;
    }

    private double createSample(double frequency, int position) {
        double time = (double) position / SAMPLE_RATE;

        double fadeIn = Math.min(
                1.0,
                (double) position / FADE_SAMPLES
        );

        double fadeOut = Math.min(
                1.0,
                (double) (SAMPLE_RATE - position - 1) / FADE_SAMPLES
        );

        return AMPLITUDE
                * fadeIn
                * fadeOut
                * Math.sin(2 * Math.PI * frequency * time);
    }
}