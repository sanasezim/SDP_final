public final class EchoDecorator extends AudioDecorator {
    private final int delaySamples;
    private final double strength;

    public EchoDecorator(
            AudioStream source,
            int delayMillis,
            double strength
    ) {
        super(source);

        if (delayMillis < 1 || delayMillis > 1000) {
            throw new IllegalArgumentException(
                    "Echo delay must be between 1 and 1000 ms."
            );
        }

        if (!Double.isFinite(strength)
                || strength < 0
                || strength > 1) {
            throw new IllegalArgumentException(
                    "Echo strength must be between 0 and 1."
            );
        }

        delaySamples = SAMPLE_RATE * delayMillis / 1000;
        this.strength = strength;
    }

    @Override
    public double[] getSamples() {
        double[] original = source.getSamples();
        double[] processed = original.clone();

        for (int index = delaySamples; index < processed.length; index++) {
            processed[index] += original[index - delaySamples] * strength;
        }

        return processed;
    }
}