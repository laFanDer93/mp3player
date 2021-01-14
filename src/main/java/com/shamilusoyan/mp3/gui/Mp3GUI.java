package com.shamilusoyan.mp3.gui;

import com.shamilusoyan.mp3.objects.MP3File;
import com.shamilusoyan.mp3.objects.MP3Player;
import com.shamilusoyan.mp3.utils.FileUtils;
import com.shamilusoyan.mp3.utils.MP3PlayerFileFilter;
import com.shamilusoyan.mp3.utils.SkinUtils;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

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
    private DefaultListModel mp3ListModel = new DefaultListModel();


    private static final String MP3_FILE_EXTENSION = "mp3";
    private static final String MP3_FILE_DESCRIPTION = "файлы mp3";
    private static final String PLAYLIST_FILE_EXTENSION = "pls";
    private static final String PLAYLIST_FILE_DESCRIPTION = "файлы плейлиста";
    private static final String EMPTY_STRING = "";
    private static final String INPUT_SONG_NAME = "введите имя песни";

    private FileFilter mp3FileFilter = new MP3PlayerFileFilter(MP3_FILE_EXTENSION, MP3_FILE_DESCRIPTION);
    private FileFilter playlistFileFilter = new MP3PlayerFileFilter(PLAYLIST_FILE_EXTENSION, PLAYLIST_FILE_DESCRIPTION);
    private MP3Player player = new MP3Player();

    public Mp3GUI() {
        lstPlayList.setModel(mp3ListModel);
        lstPlayList.setToolTipText("Список песен");
        fileChooser.setMultiSelectionEnabled(true);

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
                FileUtils.addFileFilter(fileChooser, fileFilter);
                int result = fileChooser.showOpenDialog(panel1);
                if (result == JFileChooser.APPROVE_OPTION) {

                    File[] selectedFiles = fileChooser.getSelectedFiles();

                    for (File file : selectedFiles) {
                        MP3File mp3 = new MP3File(file.getName(), file.getPath());
                        for(int i = 0; i<= mp3ListModel.getSize()-1; i++){
                            MP3File mp3File = (MP3File) mp3ListModel.getElementAt(i);
                            if(mp3File.getName() == mp3.getName()){
                                return;
                            }
                        }
                        mp3ListModel.addElement(mp3);
                }
                }
            }
        });
        btnDeleteSong.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] indexPlayList = lstPlayList.getSelectedIndices(); // получаю выбранные индексы(порядковый номер) песен
                if (indexPlayList.length > 0) {
                    ArrayList<MP3File> mp3FileListForRemove = new ArrayList<>();
                    for (int i = 0; i < indexPlayList.length; i++) {
                        MP3File mp3File = (MP3File) mp3ListModel.getElementAt(indexPlayList[i]);
                        mp3FileListForRemove.add(mp3File);
                    }

                    for (MP3File mp3File : mp3FileListForRemove) {
                        mp3ListModel.removeElement(mp3File);
                    }
                }
            }
        });
        btnSelectPrev.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int nextIndex = lstPlayList.getSelectedIndex() + 1;
                if (nextIndex <= lstPlayList.getModel().getSize() - 1) { //если не вышли за пределы списка
                    lstPlayList.setSelectedIndex(nextIndex);
                }
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
                int[] indexPlayList = lstPlayList.getSelectedIndices();
                if (indexPlayList.length > 0) {
                    MP3File mp3File = (MP3File) mp3ListModel.getElementAt(indexPlayList[0]);
                    player.play(mp3File.getName());
                    player.setVolume(slideVolume.getValue(),slideVolume.getMaximum());
                }
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
                                //todo Написать реализацию, чтобы еще раз вызвался этот метод
                                break;
                            case JOptionPane.CANCEL_OPTION:
                                fileChooser.cancelSelection();
                                break;
                        }
                        fileChooser.approveSelection();
                    }
                    String fileExtension = FileUtils.getFileExtension(selectedFile);

                    //File's name (нужно ли добавлять расширение к имени файлу при сохранении)
                    String fileNameForSave = (fileExtension != null && fileExtension.equals(PLAYLIST_FILE_EXTENSION)) ? selectedFile.getPath() : selectedFile.getPath() + "." + PLAYLIST_FILE_EXTENSION;

                    FileUtils.serialize(mp3ListModel, fileNameForSave);
                }
            }
        });
        menuOpenPlaylist.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FileUtils.addFileFilter(fileChooser,playlistFileFilter);
                int result = fileChooser.showOpenDialog(panel1);

                if(result == JFileChooser.APPROVE_OPTION){
                    File selectedFile = fileChooser.getSelectedFile();
                    DefaultListModel mp3ListModelOpen = (DefaultListModel) FileUtils.deserialize(selectedFile.getPath());
                    mp3ListModel = mp3ListModelOpen;
                    lstPlayList.setModel(mp3ListModel);
                }
            }
        });
        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchStr = txtSearch.getText();
                //если в поиске ничего не ввели - выйти из метода и не производить поиск
                if(searchStr == null || searchStr.trim().equals(EMPTY_STRING)){
                    return;
                }
                //все индексы объектов, найденных п опоиску, будут храниться в коллекции
                ArrayList<Integer> mp3FindedIndexes = new ArrayList<>();

                //проходим по коллекции и ищем соответсвия имен песен со строкой поиска
                for (int i = 0; i<mp3ListModel.size();i++){
                    MP3File mp3File = (MP3File) mp3ListModel.getElementAt(i);
                    //поиск вхождения строки в название песни без учёта регистра букв
                    if(mp3File.getName().toUpperCase().contains(searchStr.toUpperCase())){
                        mp3FindedIndexes.add(i); //найденный индексы добавляем в коллекцию
                    }
                }

                //коллекцию индексов сохраняем в массив
                int[] selectIndexes = new int[mp3FindedIndexes.size()];

                if(selectIndexes.length == 0){  //если не найдено ни одной песни, удовлетворяющей условию поиска
                    JOptionPane.showMessageDialog(panel1,"Поиск по строке " + searchStr + " не дал результатов");
                    txtSearch.requestFocus();
                    txtSearch.selectAll();
                    return;
                }

                //преобразовать коллекцию в массив, т.к. метод для выделения строк в JList работает только с массивом
                for(int i = 0; i<selectIndexes.length; i++){
                    selectIndexes[i] = mp3FindedIndexes.get(i).intValue();
                }


                //выделить в прейлисте найденные песни по массиву индексов, найденных ранее
                lstPlayList.setSelectedIndices(selectIndexes);


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

}