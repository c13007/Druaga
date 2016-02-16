package System;

import java.io.IOException;
import java.util.HashMap;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class WaveEngine {
    
    private int maxClips;
    public static HashMap clipMap;
    private HashMap nameMap;
    private int counter = 0;

    public WaveEngine(int maxClips) {
        this.maxClips = maxClips;
        clipMap = new HashMap(maxClips);
        nameMap = new HashMap(maxClips);
    }

    public void load(String name, String filename) {
        
        if (counter == maxClips) return;
        
        try {
            
            AudioInputStream stream = AudioSystem
                    .getAudioInputStream(getClass().getResource(filename));
            AudioFormat format = stream.getFormat();
            
            if ((format.getEncoding() == AudioFormat.Encoding.ULAW)
                    || (format.getEncoding() == AudioFormat.Encoding.ALAW)) {
                AudioFormat newFormat = new AudioFormat(
                        AudioFormat.Encoding.PCM_SIGNED,
                        format.getSampleRate(),
                        format.getSampleSizeInBits() * 2, format.getChannels(),
                        format.getFrameSize() * 2, format.getFrameRate(), true);
                stream = AudioSystem.getAudioInputStream(newFormat, stream);
                format = newFormat;
            }
            
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            if (!AudioSystem.isLineSupported(info)) {
                System.exit(0);
            }

            Clip clip = (Clip) AudioSystem.getLine(info);
            clip.open(stream);
            clipMap.put(name, clip);
            nameMap.put(name, filename);
            
            stream.close();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
        }
        
        
    }

    public void play(String name) {
        Clip clip = (Clip)clipMap.get(name);
        if (clip != null) {
            clip.start();
        }
    }

    public void stop(String name){
        Clip clip = (Clip)clipMap.get(name);
        if (clip != null) {
            clip.stop();
            clip.setFramePosition(0);
            load(name,(String)nameMap.get(name));
        }
    }
    
    public void loopPlay(String name,int count){
        Clip clip = (Clip)clipMap.get(name);
        if(clip != null){
            clip.loop(count);
        }
    }
    
    
}

