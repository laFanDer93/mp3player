package com.shamilusoyan.mp3.objects;

import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerException;

import java.io.File;

//классс для проигрывания mp3 файлов
public class MP3Player {

    private BasicPlayer player = new BasicPlayer();

    private String currentFileName; //текущая песня
    private double currentVolumeValue; //текущий уровень звука

    public void play(String fileName) {

        try {
            // если включают ту же самую песню, которая была на паузе
            if (currentFileName != null && currentFileName.equals(fileName) && player.getStatus() == BasicPlayer.PAUSED) {
                player.resume();
                return;
            }

            currentFileName = fileName;
            player.open(new File(fileName));
            player.play();
            player.setGain(currentVolumeValue); // устанавливаем уровень звука
        } catch (BasicPlayerException ex) {
            ex.printStackTrace();
        }

    }


    public void stop() {
        try {
            player.stop();
        } catch (BasicPlayerException ex) {
            ex.printStackTrace();
        }
    }

    public void pause() {
        try {
            player.pause();
        } catch (BasicPlayerException ex) {
            ex.printStackTrace();
        }
    }

    public void setVolume(int currentValue, int maximumValue) {
        try {
            this.currentVolumeValue = currentValue;

            if (currentValue == 0) {
                player.setGain(0);
            } else {
                player.setGain(calcVolume(currentValue,maximumValue));
            }
        } catch (BasicPlayerException ex){
            ex.printStackTrace();
        }
    }

    private double calcVolume(int currentValue, int maximumValue){
        currentVolumeValue = (double) currentValue/(double) maximumValue;
        return currentVolumeValue;
    }
}
