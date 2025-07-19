package org.abitware.kingbox.ui;

import com.formdev.flatlaf.FlatClientProperties;
import org.abitware.kingbox.plugin.KingBoxPlugin;

import javax.swing.*;
import java.awt.*;

public class TrayPopupWindow extends JDialog {
    public TrayPopupWindow() {
        super((Frame)null, false);
        setUndecorated(true); // 无边框
        setAlwaysOnTop(true);
        setSize(320, 400);    // 自定大小

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.putClientProperty(FlatClientProperties.STYLE, "arc:20");
        panel.setBorder(BorderFactory.createEmptyBorder(16,16,16,16));

        // 示例内容
        panel.add(new JLabel("🌟 KingBox 小工具箱", JLabel.CENTER));
        panel.add(Box.createVerticalStrut(16));
        JButton openMainBtn = new JButton("打开主界面");
        openMainBtn.addActionListener(e -> {
            MainWindow.show();
            setVisible(false);
        });
        panel.add(openMainBtn);

        // 示例插件菜单
        panel.add(Box.createVerticalStrut(16));
        panel.add(new JLabel("插件/工具", JLabel.LEFT));
        for (KingBoxPlugin plugin : org.abitware.kingbox.plugin.PluginManager.getPlugins()) {
            JButton btn = new JButton(plugin.getName(), plugin.getIcon());
            btn.addActionListener(ev -> {
                plugin.start(this);
                setVisible(false);
            });
            panel.add(btn);
        }
        // 可继续加入更多功能项...

        setContentPane(panel);
    }

    // 靠屏幕右下角弹出
    public void showPopup() {
        Dimension scr = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(scr.width - getWidth() - 30, scr.height - getHeight() - 50);
        setVisible(true);
    }
}
