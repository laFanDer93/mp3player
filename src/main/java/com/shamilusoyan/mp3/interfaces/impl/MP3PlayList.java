package com.shamilusoyan.mp3.interfaces.impl;

import com.shamilusoyan.mp3.interfaces.PlayList;
import com.shamilusoyan.mp3.interfaces.Player;
import com.shamilusoyan.mp3.objects.MP3File;
import com.shamilusoyan.mp3.utils.FileUtils;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;

public class MP3PlayList implements PlayList {

    private static final String PLAYLIST_FILE_EXTENSION = "pls";
    private static final String PLAYLIST_FILE_DESCRIPTION = "файлы плейлиста";
    private static final String EMPTY_STRING = "";

    private Player player;
    private JSlider slideVolume;
    private JList playlist;
    private DefaultListModel<MP3File> model;

    public MP3PlayList(Player player, JList playlist, DefaultListModel model, JSlider slideVolume) {
        this.playlist = playlist;
        this.model = model;
        this.player = player;
        this.slideVolume = slideVolume;
        initPlayList();
    }
    @Override
    public void next() {
        int nextIndex = playlist.getSelectedIndex() + 1;
        if (nextIndex <= model.getSize() - 1) {// если не вышли за пределы плейлиста
            playlist.setSelectedIndex(nextIndex);
            playFile();
        }
    }

    @Override
    public void prev() {
        int nextIndex = playlist.getSelectedIndex() - 1;
        if (nextIndex >= 0) {// если не вышли за пределы плейлиста
            playlist.setSelectedIndex(nextIndex);
            playFile();
        }
    }

    @Override
    public boolean search(String name) {

        // если в поиске ничего не ввели - выйти из метода и не производить поиск
        if (name == null || name.trim().equals(EMPTY_STRING)) {
            return false;
        }

        // все индексы объектов, найденных по поиску, будут храниться в коллекции
        ArrayList<Integer> mp3FindedIndexes = new ArrayList<Integer>();

        // проходим по коллекции и ищем соответствия имен песен со строкой поиска
        for (int i = 0; i < model.getSize(); i++) {
            MP3File mp3 = (MP3File) model.getElementAt(i);
            // поиск вхождения строки в название песни без учета регистра букв
            if (mp3.getName().toUpperCase().contains(name.toUpperCase())) {
                mp3FindedIndexes.add(i);// найденный индексы добавляем в коллекцию
            }
        }

        // коллекцию индексов сохраняем в массив
        int[] selectIndexes = new int[mp3FindedIndexes.size()];

        if (selectIndexes.length == 0) {// если не найдено ни одной песни, удовлетворяющей условию поиска
            return false;
        }
        // преобразовать коллекцию в массив, т.к. метод для выделения строк в JList работает только с массивом
        for (int i = 0; i < selectIndexes.length; i++) {
            selectIndexes[i] = mp3FindedIndexes.get(i).intValue();
        }

        // выделить в плелисте найдные песни по массиву индексов, найденных ранее
        playlist.setSelectedIndices(selectIndexes);

        return true;
    }

    @Override
    public boolean savePlaylist(File file) {
        try {
            String fileExtension = FileUtils.getFileExtension(file);

            // имя файла (нужно ли добавлять раширение к имени файлу при сохранении)
            String fileNameForSave = (fileExtension != null && fileExtension.equals(PLAYLIST_FILE_EXTENSION)) ? file.getPath() : file.getPath() + "." + PLAYLIST_FILE_EXTENSION;

            FileUtils.serialize(model, fileNameForSave);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean openPlaylist(File file) {
        try {
            DefaultListModel mp3ListModel = (DefaultListModel) FileUtils.deserialize(file.getPath());
            this.model = mp3ListModel;
            playlist.setModel(mp3ListModel);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean openFiles(File[] files) {

        boolean status = false;

        for (File file : files) {
            MP3File mp3 = new MP3File(file.getName(), file.getPath());

            // если эта песня уже есть в списке - не добавлять ее
            if (!model.contains(mp3)) {
                model.addElement(mp3);
                status = true;
            }
        }

        return status;
    }

    @Override
    public void playFile() {
        int[] indexPlayList = playlist.getSelectedIndices();// получаем выбранные индексы(порядковый номер) песен
        if (indexPlayList.length > 0) {// если выбрали хотя бы одну песню
            Object selectedItem = model.getElementAt(indexPlayList[0]);
            if (!(selectedItem instanceof MP3File)) {
                return;
            }
            MP3File mp3 = (MP3File) selectedItem;// находим первую выбранную песню (т.к. несколько песен нельзя проиграть одновременно
            System.out.println(mp3.getPath() + "-> path  +  " + mp3.getName() + "-> name");
            player.play(mp3.getPath());
            player.setVolume(slideVolume.getValue());
        }

    }


    @Override
    public void delete() {
        int[] indexPlayList = playlist.getSelectedIndices();// получаем выбранные индексы(порядковый номер) песен

        if (indexPlayList.length > 0) {// если выбрали хотя бы одну песню

            ArrayList<MP3File> mp3ListForRemove = new ArrayList<MP3File>();// сначала сохраняем все mp3 для удаления в отдельную коллекцию

            for (int i = 0; i < indexPlayList.length; i++) {// удаляем все выбранные песни из плейлиста
                MP3File mp3 = (MP3File) model.getElementAt(indexPlayList[i]);
                mp3ListForRemove.add(mp3);
            }

            // удаляем mp3 в плейлисте
            for (MP3File mp3 : mp3ListForRemove) {
                model.removeElement(mp3);
            }

        }
    }

    @Override
    public void clear() {
        model.clear();
    }

    private void initPlayList() {

        playlist.setModel(model);
        playlist.setToolTipText("Список песен");

        playlist.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getModifiers() == InputEvent.BUTTON1_MASK && evt.getClickCount() == 2) {
                    playFile();
                }
            }
        });

        playlist.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                int key = evt.getKeyCode();
                if (key == KeyEvent.VK_ENTER) {
                    playFile();
                }
            }
        });
    }

}

