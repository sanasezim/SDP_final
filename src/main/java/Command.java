import javax.sound.sampled.LineUnavailableException;

public interface Command {
    void execute() throws LineUnavailableException, InterruptedException;
}