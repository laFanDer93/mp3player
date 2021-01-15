package com.shamilusoyan.mp3.utils;

import javax.swing.filechooser.FileFilter;
import java.io.File;

public class MP3PlayerFileFilter extends FileFilter {


    private final String fileExtension;
    private final String fileDescription;

    public MP3PlayerFileFilter(String fileExtension, String fileDescription) {
        this.fileExtension = fileExtension;
        this.fileDescription = fileDescription;
    }

    @Override
    public boolean accept(File f) {
        return f.isDirectory() || f.getAbsolutePath().endsWith(fileExtension);
    }

    @Override
    public String getDescription() {
        return fileDescription + " (*." + fileExtension + ")";
    }
}
