package org.abitware.kingbox;

import com.formdev.flatlaf.FlatLightLaf;
import org.abitware.kingbox.core.SingleInstanceManager;
import org.abitware.kingbox.core.LogManager;
import org.abitware.kingbox.core.TrayManager;
import org.abitware.kingbox.plugin.AntiIdlePlugin;
import org.abitware.kingbox.plugin.PluginManager;
import org.abitware.kingbox.ui.MainWindow;

import javax.swing.*;

public class AppMain {
    // AppMain.java 的 main 方法
    public static void main(String[] args) {

        if (!SingleInstanceManager.lock()) {
            System.out.println("已有程序运行！");
            return;
        }
        LogManager.init();
        try {
            // FlatLaf 主题
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            System.out.println("FlatLaf 设置失败，使用系统默认风格");
        }
        javax.swing.SwingUtilities.invokeLater(() -> {
            TrayManager.init();
            PluginManager.init();
            MainWindow.show();
        });
    }
}
