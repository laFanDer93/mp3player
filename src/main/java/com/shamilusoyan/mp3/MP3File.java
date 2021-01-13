package com.shamilusoyan.mp3;

import java.io.Serializable;
import java.util.List;

public class MP3File implements Serializable {

    private String path;
    private String name;

    public MP3File(String path, String name) {
        this.path = path;
        this.name = name;
    }


    public static MP3File searchByName(String name, List<MP3File> files){
        for(MP3File MP3File : files){
            if(MP3File.getName() == name){
                return MP3File;
            }
        }

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
        return "FileMP3{" +
                "name='" + name + '\'' +
                '}';
    }
}
