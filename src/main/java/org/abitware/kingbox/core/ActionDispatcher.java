package org.abitware.kingbox.core;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import org.abitware.kingbox.ui.MainWindow;

import javax.swing.*;

public class ActionDispatcher {
    public static void handle(String action) {
        if (action == null || action.isEmpty()) {
            return;
        }
        switch (action) {
            case "showMessageA":
                JOptionPane.showMessageDialog(null, "功能A触发！");
                break;
            case "showMessageB":
                JOptionPane.showMessageDialog(null, "功能B触发！");
                break;
            case "themeLight":
                try {
                    UIManager.setLookAndFeel(new FlatLightLaf());
                    SwingUtilities.updateComponentTreeUI(MainWindow.getFrame());
                } catch (Exception e) {
                }
                break;
            case "themeDark":
                try {
                    UIManager.setLookAndFeel(new FlatDarkLaf());
                    SwingUtilities.updateComponentTreeUI(MainWindow.getFrame());
                } catch (Exception e) {
                }
                break;
            case "langZh":
                I18nManager.setLocale("zh");
                ThemeManager.refreshUI();
                MainWindow.reload(); // 主题切换后刷新菜单显示选中
                break;
            case "langEn":
                I18nManager.setLocale("en");
                ThemeManager.refreshUI();
                MainWindow.reload(); // 主题切换后刷新菜单显示选中
                break;
            case "langJa":
                I18nManager.setLocale("ja");
                ThemeManager.refreshUI();
                MainWindow.reload(); // 主题切换后刷新菜单显示选中
                break;
            case "exit":
                System.exit(0);
                break;
            case "themeIntellij":
                ThemeManager.setTheme("intellij");
                break;
            case "themeDarcula":
                ThemeManager.setTheme("darcula");
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + action);
        }
    }


}
