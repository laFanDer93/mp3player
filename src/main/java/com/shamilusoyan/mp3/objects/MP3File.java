package com.shamilusoyan.mp3.objects;

import com.shamilusoyan.mp3.utils.FileUtils;

import javax.swing.*;
import java.io.Serializable;

public class MP3File implements Serializable {

    private String path;
    private String name;

    public MP3File(String path, String name) {
        this.path = path;
        this.name = name;
    }


    public static MP3File searchByName(String name, DefaultListModel files){
        for (int i = 0; i<=files.getSize();i++){
            MP3File mp3File = (MP3File) files.getElementAt(i);
            if(mp3File.getName() == name){
                return mp3File;
        }}

        return null;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return FileUtils.getFileNameWithoutExtension(name);
    }
}
