import java.util.Objects;

public abstract class AudioDecorator implements AudioStream {
    protected final AudioStream source;

    protected AudioDecorator(AudioStream source) {
        this.source = Objects.requireNonNull(source);
    }
}