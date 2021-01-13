package com.shamilusoyan.mp3.gui;

import com.shamilusoyan.mp3.MP3File;
import com.shamilusoyan.mp3.utils.FileUtils;
import com.shamilusoyan.mp3.utils.MP3PlayerFileFilter;
import com.shamilusoyan.mp3.utils.SkinUtils;

import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

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
    private JFileChooser fileChooser = new JFileChooser();
    private MP3PlayerFileFilter fileFilter;
    private JFrame frame;

    public Mp3GUI() {

        frame = new JFrame("MP3Player");


        menuSkin1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SkinUtils.changeSkin(panel1, UIManager.getSystemLookAndFeelClassName());
            }
        });

        menuSkin2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SkinUtils.changeSkin(panel1, new NimbusLookAndFeel());
            }
        });

        btnAddSong.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                FileUtils.addFileFilter(fileChooser, fileFilter);
                int result = fileChooser.showOpenDialog(panel1);
                fileChooser.setMultiSelectionEnabled(true);

                if (result == JFileChooser.APPROVE_OPTION) {

                    File[] selectedFiles = fileChooser.getSelectedFiles();

                    for (File file : selectedFiles) {
                        MP3File mp3 = new MP3File(file.getName(), file.getPath());
                    }

                }
            }
        });
    }

    public void startGui(){
        frame.setContentPane(new Mp3GUI().panel1);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }

    public static void main(String[] args) {
            Mp3GUI mp3GUI = new Mp3GUI();
            mp3GUI.startGui();
    }

}