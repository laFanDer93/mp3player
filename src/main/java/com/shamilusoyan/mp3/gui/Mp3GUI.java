package com.shamilusoyan.mp3.gui;

import com.shamilusoyan.mp3.interfaces.PlayControlListener;
import com.shamilusoyan.mp3.interfaces.PlayList;
import com.shamilusoyan.mp3.interfaces.Player;
import com.shamilusoyan.mp3.interfaces.impl.MP3PlayList;
import com.shamilusoyan.mp3.objects.MP3File;
import com.shamilusoyan.mp3.interfaces.impl.MP3Player;
import com.shamilusoyan.mp3.utils.FileUtils;
import com.shamilusoyan.mp3.utils.MP3PlayerFileFilter;
import com.shamilusoyan.mp3.utils.SkinUtils;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.event.*;
import java.io.File;

public class Mp3GUI implements PlayControlListener {
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
    private JPanel jInfoPanel;
    private JLabel txtCurrentMp3;
    private JSlider slidePlay;
    private JFileChooser fileChooser = new JFileChooser();
    private MP3PlayerFileFilter fileFilter;
    private JFrame frame;
    private DefaultListModel mp3ListModel = new DefaultListModel();

    private static final String PLAYLIST_FILE_EXTENSION = "pls";
    private static final String PLAYLIST_FILE_DESCRIPTION = "файлы плейлиста";
    private static final String EMPTY_STRING = "";
    private static final String MP3_FILE_EXTENSION = "mp3";
    private static final String MP3_FILE_DESCRIPTION = "файлы mp3";
    private static final String INPUT_SONG_NAME = "введите имя песни";

    private FileFilter mp3FileFilter = new MP3PlayerFileFilter(MP3_FILE_EXTENSION, MP3_FILE_DESCRIPTION);
    private FileFilter playlistFileFilter = new MP3PlayerFileFilter(PLAYLIST_FILE_EXTENSION, PLAYLIST_FILE_DESCRIPTION);

    private int currentVolume;
    private double posValue = 0.0;
    private boolean moveAutomatic = true;
    private PlayList playList;
    private Player player;

    public Mp3GUI() {

        fileChooser.setMultiSelectionEnabled(true);
        frame = new JFrame("MP3Player");
        player = new MP3Player(this);
        playList = new MP3PlayList(player, lstPlayList, mp3ListModel, slideVolume);

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
                FileUtils.addFileFilter(fileChooser, fileFilter);
                int result = fileChooser.showOpenDialog(panel1);
                if (result == JFileChooser.APPROVE_OPTION) {
                    playList.openFiles(fileChooser.getSelectedFiles());
                }
            }
        });
        btnDeleteSong.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playList.delete();
            }
        });
        btnSelectPrev.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playList.prev();
            }
        });
        btnSelectNext.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int nextIndex = lstPlayList.getSelectedIndex() - 1;
                if (nextIndex >= 0) {
                    lstPlayList.setSelectedIndex(nextIndex);
                }
            }
        });
        btnPlaySong.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playList.playFile();
            }
        });
        menuSavePlaylist.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FileUtils.addFileFilter(fileChooser, playlistFileFilter);
                int result = fileChooser.showOpenDialog(panel1);
                if (result == JFileChooser.APPROVE_OPTION)// если нажата клавиша ОК или YES
                {
                    File selectedFile = fileChooser.getSelectedFile();
                    if (selectedFile.exists()) //если такой файл существует
                    {
                        int resultOvveride = JOptionPane.showConfirmDialog(panel1, "Файл существует", "Перезаписать?", JOptionPane.YES_NO_CANCEL_OPTION);
                        switch (resultOvveride) {
                            case JOptionPane.NO_OPTION:
                                menuSavePlaylist.addActionListener(this);
                                break;
                            case JOptionPane.CANCEL_OPTION:
                                fileChooser.cancelSelection();
                                break;
                        }
                        fileChooser.approveSelection();
                    }
                    String fileExtension = FileUtils.getFileExtension(selectedFile);

                    //File's name (нужно ли добавлять расширение к имени файлу при сохранении)
                    String fileNameForSave = (fileExtension != null && fileExtension.equals(PLAYLIST_FILE_EXTENSION)) ? selectedFile.getName() : selectedFile.getName() + "." + PLAYLIST_FILE_EXTENSION;

                    FileUtils.serialize(mp3ListModel, fileNameForSave);
                }
            }
        });
        menuOpenPlaylist.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FileUtils.addFileFilter(fileChooser, playlistFileFilter);
                int result = fileChooser.showOpenDialog(panel1);// result хранит результат: выбран файл или нет

                if (result == JFileChooser.APPROVE_OPTION) {// если нажата клавиша OK или YES
                    playList.openPlaylist(fileChooser.getSelectedFile());
                }

            }
        });
        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchSong();
            }
        });
        btnStopSong.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player.stop();
            }
        });
        btnPauseSong.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player.pause();
            }
        });

        slideVolume.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (slideVolume.getValue() == 0) {
                    player.setVolume(0);
                    tglbtnVolume.setSelected(true);
                } else {
                    player.setVolume(slideVolume.getValue());
                    tglbtnVolume.setSelected(false);
                }
            }
        });
        tglbtnVolume.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (tglbtnVolume.isSelected()) {
                    currentVolume = slideVolume.getValue();
                    player.setVolume(0);
                    slideVolume.setValue(0);
                } else {
                    slideVolume.setValue(currentVolume);
                    player.setVolume(slideVolume.getValue());
                }
            }
        });

        slidePlay.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                moveAutomatic = false;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                posValue = slidePlay.getValue() * 1.0 / 100;
                player.jump(posValue);
                moveAutomatic = true;
            }
        });
        btnPrevSong.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playList.prev();
            }
        });
        btnNextSong.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playList.next();
            }
        });
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_ENTER) {
                    playList.search(txtSearch.getText());
                }
            }
        });
        slidePlay.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                moveAutomatic = false;
            }
        });
    }

    public void startGui() {
        frame.setContentPane(new Mp3GUI().panel1);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {

        Mp3GUI mp3GUI = new Mp3GUI();
        mp3GUI.startGui();
    }


    public void searchSong() {
        String name = txtSearch.getText().trim();
        if (!playList.search(name)) {
            JOptionPane.showMessageDialog(panel1, "Поиск по строке \'" + name + "\' не дал результатов");
            txtSearch.requestFocus();
            txtSearch.selectAll();
        }
    }

    @Override
    public void playStarted(String name) {
        txtCurrentMp3.setText(name);
    }

    @Override
    public void processScroll(int position) {
        if (moveAutomatic) {
            slidePlay.setValue(position);
        }
    }

    @Override
    public void playFinished() {
        playList.next();
    }
}

