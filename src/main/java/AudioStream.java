public interface AudioStream {
    int SAMPLE_RATE = 22050;

    double[] getSamples();
}