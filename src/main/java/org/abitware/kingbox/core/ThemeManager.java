package org.abitware.kingbox.core;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatDarculaLaf;

import javax.swing.*;
import java.awt.*;

/**
 * @author Administrator
 */
public class ThemeManager {
    private static String currentTheme = "light";
    public static void setTheme(String theme) {
        try {
            currentTheme = theme;
            switch (theme) {
                case "light":
                    UIManager.setLookAndFeel(new FlatLightLaf());
                    break;
                case "dark":
                    UIManager.setLookAndFeel(new FlatDarkLaf());
                    break;
                case "intellij":
                    UIManager.setLookAndFeel(new FlatIntelliJLaf());
                    break;
                case "darcula":
                    UIManager.setLookAndFeel(new FlatDarculaLaf());
                    break;
                default:
                    UIManager.setLookAndFeel(new FlatLightLaf());
            }
            // 刷新所有窗口和组件
            for (Window window : Window.getWindows()) {
                SwingUtilities.updateComponentTreeUI(window);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void refreshUI() {
        // 刷新所有窗口，更新界面
        for (Window window : Window.getWindows()) {
            SwingUtilities.updateComponentTreeUI(window);
            // 可选：重绘窗口
            window.invalidate();
            window.validate();
            window.repaint();
        }
    }

    public static String getCurrentTheme() {
        return currentTheme;
    }

}
