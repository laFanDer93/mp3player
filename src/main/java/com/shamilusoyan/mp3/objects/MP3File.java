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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MP3File mp3File = (MP3File) o;
        return path.equals(mp3File.path) &&
                name.equals(mp3File.name);
    }

    @Override
    public int hashCode() {
        int result = path != null ? path.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
