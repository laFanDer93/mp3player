package com.shamilusoyan.mp3;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Mp3GUI {
    private JPanel panel1;
    private JPanel jMenuPanel;
    private JMenu menuFile;
    private JMenu menuPrefs;
    private JButton btnPrevSong;
    private JButton btnPlaySong;
    private JButton btnPauseSong;
    private JButton btnStopSong;
    private JButton btnNextSong;
    private JPanel jActionPanel;
    private JList lstPlayList;
    private JSlider slideVolume;
    private JToggleButton tglbtnVolume;
    private JButton btnAddSong;
    private JButton btnDeleteSong;
    private JButton btnSelectNext;
    private JButton btnSelectPrev;
    private JPanel jSearchPanel;
    private JTextField txtSearch;
    private JButton btnSearch;
    private JMenuItem menuOpenPlaylist;
    private JMenuItem menuSavePlaylist;
    private JMenuItem menuExit;
    private JMenuItem menuSkin1;
    private JMenuItem menuSkin2;


    public Mp3GUI() {
        menuSkin1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

            }
        });
    }
}
