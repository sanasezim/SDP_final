public final class VolumeDecorator extends AudioDecorator {
    private final double volume;

    public VolumeDecorator(AudioStream source, double volume) {
        super(source);

        if (!Double.isFinite(volume) || volume < 0 || volume > 1) {
            throw new IllegalArgumentException(
                    "Volume must be between 0 and 1."
            );
        }

        this.volume = volume;
    }

    @Override
    public double[] getSamples() {
        double[] samples = source.getSamples().clone();

        for (int index = 0; index < samples.length; index++) {
            samples[index] *= volume;
        }

        return samples;
    }
}