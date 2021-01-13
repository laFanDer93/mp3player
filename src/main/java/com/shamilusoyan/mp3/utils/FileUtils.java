package com.shamilusoyan.mp3.utils;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.*;

public class FileUtils {

    public static String getFileNameWithoutExtension(String fileName){
        File file = new File(fileName);
        int index = file.getName().lastIndexOf('.');
        if(index>0&&index<=file.getName().length()-2){
            return file.getName().substring(0,index);
        }
        return "noname";
    }


//    public static void addFileFilter(JFileChooser jfc, FileFilter ff){
//        jfc.removeChoosableFileFilter(jfc.getFileFilter());
//        jfc.setFileFilter(ff);
//        jfc.setSelectedFile(new File(""));
//    }

    public static void serialize(Object obj, String fileName){
        FileOutputStream fos;
        ObjectOutputStream oos;
        try{
            fos = new FileOutputStream(fileName);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(obj);
            oos.close();
            fos.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static Object deserialize(String fileName){
        try{
            FileInputStream fis = new FileInputStream(fileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Object ts = (Object) ois.readObject();
            fis.close();
            return ts;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e){
            e.printStackTrace();
        }

        return null;
    }
}
