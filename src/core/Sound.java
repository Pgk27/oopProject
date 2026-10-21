package core;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

/**
 * Class xử lí âm thanh (Music + SFX)
 */
public class Sound {
    private Clip musicClip;
    
    // luồng nền riêng cho sfx để không bị freezing nữa
    private final ExecutorService sfxExecutor = Executors.newFixedThreadPool(2);

    // Pool lưu sẵn các Clip đã nạp vào RAM
    private final Map<URL, Clip[]> clipPools = new ConcurrentHashMap<>();
    private final Map<URL, Integer> poolIndices = new ConcurrentHashMap<>();
    
    // map này để chống sfx chồng chéo lên nhau --> nhiễu tai
    private Map<URL, Long> lastPlayTimes = new ConcurrentHashMap<>();

    URL themeSong; // nhạc nền, 
    public URL sfxSlash;
    URL sfxClaw;
    public URL sfxBite;
    public URL sfxDeath;
    public URL sfxPierce;
    public URL bowRelease;
    public URL spellCast;

    public Sound(){
        try{
            themeSong = readAudio("music/song.wav");
            sfxSlash = readAudio("sfx/17_orc_atk_sword_2.wav");
            sfxClaw = readAudio("sfx/03_Claw_03.wav");
            sfxBite = readAudio("sfx/08_Bite_04.wav");
            sfxDeath = readAudio("sfx/roblox-death-sound-effect.wav");
            sfxPierce = readAudio("sfx/zapsplat_warfare_spear_land_ground_004_43629.wav");
            bowRelease = readAudio("sfx/bow-release.wav");
            spellCast = readAudio("sfx/04_Fire_explosion_04_medium.wav");

            preloadSfx(sfxBite, 3);
            preloadSfx(sfxClaw, 3);
            preloadSfx(sfxSlash, 3);
            preloadSfx(sfxDeath, 2);
            preloadSfx(sfxPierce, 3);
            preloadSfx(bowRelease, 3);
            preloadSfx(spellCast, 3);

        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private void preloadSfx(URL soundURL, int poolSize){
        if (soundURL == null) return;
        try{
            Clip[] clips = new Clip[poolSize];
            for (int i = 0; i < poolSize; i++){
                AudioInputStream ais = getPlayableStream(soundURL);
                clips[i] = AudioSystem.getClip();
                clips[i].open(ais);
            }
            clipPools.put(soundURL, clips);
            poolIndices.put(soundURL, 0);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setClipVolume(Clip clip, int volumeLevel){
        if (clip == null || !clip.isOpen()) return;
        try{
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            if (volumeLevel <= 0){
                gainControl.setValue(gainControl.getMinimum());
            } else{
                float fraction = volumeLevel / 10.0f;
                float dB = (float) (Math.log10(fraction) * 20.0); // công thức chuẩn:)

                dB = Math.max(gainControl.getMinimum(), Math.min(gainControl.getMaximum(), dB));
                gainControl.setValue(dB);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    // cập nhật âm lượng music
    public void setMusicVolume(int volumeLevel){
        setClipVolume(musicClip, volumeLevel);
    }

    public void setMusicVolume(int volumeLevel, boolean instant) {
        if (musicClip == null || !musicClip.isOpen()) return;
        try {
            FloatControl gainControl = (FloatControl) musicClip.getControl(FloatControl.Type.MASTER_GAIN);
            if (volumeLevel <= 0) {
                gainControl.setValue(gainControl.getMinimum());
            } else {
                float fraction = volumeLevel / 10.0f;
                float dB = (float) (Math.log10(fraction) * 20.0);
                dB = Math.max(gainControl.getMinimum(), Math.min(gainControl.getMaximum(), dB));
                gainControl.setValue(dB);
            }
            // Xả sạch 1 giây buffer cũ để âm lượng ăn ngay tức thì
            if (instant && musicClip.isRunning()) {
                int currentFrame = musicClip.getFramePosition();
                musicClip.stop();
                musicClip.setFramePosition(currentFrame);
                musicClip.loop(Clip.LOOP_CONTINUOUSLY);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // cập nhật âm lượng cho từng pool sfx
    public void setSfxVolume(int volumeLevel){
        for (Clip[] pool : clipPools.values()){
            for (Clip c : pool){
                setClipVolume(c, volumeLevel);
            }
        }
    }

    public void playMusic(URL soundURL){
        if (soundURL == null) return;
        try{
            stopMusic(); // dừng bài cũ

            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL);
            musicClip = AudioSystem.getClip();
            musicClip.open(ais);

            setClipVolume(musicClip, Screen.musicVolume);

            musicClip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void playSfx(URL soundURL){
        if (soundURL == null) return;

        long now = System.currentTimeMillis();
        Long lastTime = lastPlayTimes.get(soundURL);
        if (lastTime != null && (now - lastTime) < 40){
            return;
        }
        lastPlayTimes.put(soundURL, now);

        sfxExecutor.submit(() -> {
            try{
                Clip[] pool = clipPools.get(soundURL);
                if (pool != null){
                    int idx = poolIndices.get(soundURL);
                    Clip clip = pool[idx];
                    poolIndices.put(soundURL, (idx++) % pool.length);

                    if (clip.isRunning()){
                        clip.stop();
                    }
                    clip.setFramePosition(0);
                    setClipVolume(clip, Screen.sfxVolume);
                    clip.start();
                }
                else{
                    AudioInputStream ais = getPlayableStream(soundURL);
                    Clip tempClip = AudioSystem.getClip();
                    tempClip.open(ais);
                    setClipVolume(tempClip, Screen.sfxVolume);
                    tempClip.start();
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        });
    }

    public void stopMusic(){
        if (musicClip != null && musicClip.isRunning()){
            musicClip.stop();
            musicClip.close();
        }
    }

    private URL readAudio(String relativePath) throws Exception {
        File file = new File("res/sound/" + relativePath);
        if (!file.exists()) {
            file = new File(relativePath);
        }
        if (!file.exists()) {
            throw new FileNotFoundException("Missing sound asset: " + relativePath);
        }
        return file.toURI().toURL();
    }

    private AudioInputStream getPlayableStream(URL soundURL) throws Exception{
        AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL);
        AudioFormat baseFormat = ais.getFormat();

        if (baseFormat.getSampleSizeInBits() != 16 || baseFormat.getEncoding() != AudioFormat.Encoding.PCM_SIGNED){
            AudioFormat targeFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 
                                            16, baseFormat.getChannels(), baseFormat.getChannels() * 2,
                                            baseFormat.getSampleRate(), false);
            return AudioSystem.getAudioInputStream(targeFormat, ais);
        }
        return ais;
        
    }
}


