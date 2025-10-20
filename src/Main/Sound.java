package Main;

import javax.sound.sampled.*;
import java.io.*;

public class Sound {
    private Clip clip;
    private FloatControl volumeControl;

    public void setFile(String filePath) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(new File(filePath));
            clip = AudioSystem.getClip();
            clip.open(ais);

            // volum control
            volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play() {
        if (clip != null) {
            clip.start();
            clip.loop(Clip.LOOP_CONTINUOUSLY); // loop the music
        }
    }

    public void stop() {
        if (clip != null) {
            clip.stop();
            clip.close();
        }
    }

    public void setVolume(float volume) {
        if (volumeControl != null) {
            float min = volumeControl.getMinimum(); // usually -80.0 dB
            float max = volumeControl.getMaximum(); // usually 6.0 dB
            float dB = min + (max - min) * volume;
            volumeControl.setValue(dB);
        }
    }
}