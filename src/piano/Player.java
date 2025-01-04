package piano;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import kuusisto.tinysound.Music;
import kuusisto.tinysound.TinySound;

public class Player {
    //private static Clip[] clip;
    private static Music[] music;
    public static void init () {
        TinySound.init();
        //clip = new Clip[64];
        music = new Music[64];
        for (int i = 0; i < music.length; ++i) {
                //clip[i] = AudioSystem.getClip();
                //AudioInputStream inputStream;
                //inputStream = AudioSystem.getAudioInputStream(new File("files/" + (i+1) + ".wav"));
                //clip[i].open(inputStream);
                music[i] = TinySound.loadMusic(new File("files/" + (i+1) + ".wav"));
        }
    }
    public static void play (int index) {
        music[index].rewind();
        music[index].play(false);
        //clip[index].setFramePosition(0);
        //clip[index].start();
    }
}