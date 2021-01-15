package com.shamilusoyan.mp3.objects;

import com.shamilusoyan.mp3.utils.FileUtils;

import javax.swing.*;
import java.io.Serializable;

public class MP3File implements Serializable {

    private String path;
    private String name;

    public MP3File(String name, String path) {
        this.path = path;
        this.name = name;
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



    @Override
    public int hashCode() {
        int result = path != null ? path.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
