package com.shamilusoyan.mp3.interfaces;

public interface PlayControlListener {

    void playStarted(String name);

    void processScroll(int position);

    void playFinished();
}
