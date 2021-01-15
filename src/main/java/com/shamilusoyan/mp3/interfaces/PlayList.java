package com.shamilusoyan.mp3.interfaces;

import java.io.File;

public interface PlayList {

    void next();

    void prev();

    void delete();

    void clear();

    boolean search(String name);

    boolean savePlaylist(File file);

    boolean openPlaylist(File file);


    boolean openFiles(File[] files);

    void playFile();


}
