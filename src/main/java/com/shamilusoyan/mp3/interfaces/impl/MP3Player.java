package com.shamilusoyan.mp3.interfaces.impl;

import com.shamilusoyan.mp3.interfaces.PlayControlListener;
import com.shamilusoyan.mp3.interfaces.Player;
import com.shamilusoyan.mp3.objects.BasicPlayerListenerAdapter;
import com.shamilusoyan.mp3.utils.FileUtils;
import javazoom.jlgui.basicplayer.*;

import java.io.File;
import java.util.Map;

//классс для проигрывания mp3 файлов
public class MP3Player implements Player {


    private static final String MP3_FILE_EXTENSION = "mp3";
    private static final String MP3_FILE_DESCRIPTION = "файлы mp3";
    public static int MAX_VOLUME = 100;

    private long duration; //длительность песни в секундах
    private long secondsAmount; //сколько секунд с начала проигрывания
    private int bytesLen; //размер песни в байтах

    private BasicPlayer basicPlayer = new BasicPlayer();

    private String currentFileName; //текущая песня
    private double currentVolume; //текущий уровень звука

    private final PlayControlListener playControlListener;


    public MP3Player(PlayControlListener playControlListener) {
        this.playControlListener = playControlListener;
        basicPlayer.addBasicPlayerListener(new BasicPlayerListenerAdapter() {
            @Override
            public void opened(Object o, Map map) {
                duration = (long) Math.round(( (long)map.get("duration")) / 1000000);
                bytesLen = (int) Math.round(((Integer) map.get("mp3.length.bytes")));

                String songName = map.get("title") != null ? map.get("title").toString() : FileUtils.getFileNameWithoutExtension(new File(o.toString()).getName());

                if (songName.length() > 30) {
                    songName = songName.substring(0, 30) + "...";
                }

                MP3Player.this.playControlListener.playStarted(songName);
            }

            @Override
            public void progress(int i, long l, byte[] bytes, Map map) {
                float progress = -1.0f;

                if ((i > 0) && (duration > 0)) {
                    progress = i * 1.0f / bytesLen * 1.0f;
                }

                secondsAmount = (long) (duration * progress);

                if (duration != 0) {
                    int length = (((int) Math.round(secondsAmount * 100 / duration)));
                    MP3Player.this.playControlListener.processScroll(length);
                }
            }

            @Override
            public void stateUpdated(BasicPlayerEvent basicPlayerEvent) {
                int state = basicPlayerEvent.getCode();

                if(state == BasicPlayerEvent.EOM){
                    MP3Player.this.playControlListener.playFinished();
                }
            }

        });
    }

    public void play(String fileName) {

        try {
            // если включают ту же самую песню, которая была на паузе
            if (currentFileName != null && currentFileName.equals(fileName) && basicPlayer.getStatus() == BasicPlayer.PAUSED) {
                basicPlayer.resume();
                return;
            }

            File mp3File = new File(fileName);

            currentFileName = fileName;
            basicPlayer.open(mp3File);
            basicPlayer.play();
            basicPlayer.setGain(currentVolume); // устанавливаем уровень звука
        } catch (BasicPlayerException ex) {
            ex.printStackTrace();
        }

    }


    public void stop() {
        try {
            basicPlayer.stop();
        } catch (BasicPlayerException ex) {
            ex.printStackTrace();
        }
    }

    public void pause() {
        try {
            basicPlayer.pause();
        } catch (BasicPlayerException ex) {
            ex.printStackTrace();
        }
    }

    public void setVolume(double controlValue){
        try {
                currentVolume = calcVolume(controlValue);
                basicPlayer.setGain(currentVolume);
        } catch (BasicPlayerException ex) {
            ex.printStackTrace();
        }
    }

    private double calcVolume(double currentValue) {
        currentVolume = (double) currentValue / MAX_VOLUME;
        return currentVolume;
    }

    public void jump(double controlPosition) {
        try {
            long skipBytes = (long) Math.round(((Integer) bytesLen)*controlPosition);
            basicPlayer.seek(skipBytes);
            basicPlayer.setGain(currentVolume);
        } catch (BasicPlayerException e) {
            e.printStackTrace();
        }
    }
}
