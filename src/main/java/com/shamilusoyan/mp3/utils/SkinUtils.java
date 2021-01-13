package com.shamilusoyan.mp3.utils;

import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.*;
import java.awt.event.ActionListener;

public class SkinUtils {

    public static void changeSkin(Component actionListener, String systemLookAndFeelClassName) {
        try {
            UIManager.setLookAndFeel(systemLookAndFeelClassName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        SwingUtilities.updateComponentTreeUI(actionListener);


    }

    public static void changeSkin(Component actionListener, NimbusLookAndFeel nimbusLookAndFeel) {
        try {
            UIManager.setLookAndFeel(nimbusLookAndFeel);
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        SwingUtilities.updateComponentTreeUI(actionListener);

    }

}
