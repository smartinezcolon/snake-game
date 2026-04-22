import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;

public class SoundTest {
    public static void playTone(int hz, int msecs, double vol) {
        try {
            float sampleRate = 44100;
            byte[] buf = new byte[1];
            AudioFormat af = new AudioFormat(sampleRate, 8, 1, true, false);
            SourceDataLine sdl = AudioSystem.getSourceDataLine(af);
            sdl.open(af);
            sdl.start();
            for (int i = 0; i < msecs * sampleRate / 1000; i++) {
                double angle = i / (sampleRate / hz) * 2.0 * Math.PI;
                buf[0] = (byte) (Math.sin(angle) * 127.0 * vol);
                sdl.write(buf, 0, 1);
            }
            sdl.drain();
            sdl.stop();
            sdl.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws Exception {
        System.out.println("Eating food...");
        playTone(1200, 100, 0.5);
        Thread.sleep(500);

        System.out.println("Dying...");
        playTone(300, 500, 0.5);
        Thread.sleep(500);

        System.out.println("Starting game...");
        playTone(880, 150, 0.5);
        playTone(1046, 200, 0.5);
    }
}
