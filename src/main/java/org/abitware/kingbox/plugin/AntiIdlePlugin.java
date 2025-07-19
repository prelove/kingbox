package org.abitware.kingbox.plugin;

import org.abitware.kingbox.core.I18nManager;
import org.abitware.kingbox.ui.MainWindow;
import org.abitware.kingbox.util.IconUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.concurrent.*;

public class AntiIdlePlugin implements KingBoxPlugin {
    private volatile boolean running = false;
    private ScheduledExecutorService executor;
    private int intervalMinutes = 5; // 默认5分钟
    private JMenuItem menuItem;

    @Override
    public String getName() {
        return I18nManager.tr("plugin.antiidle.name");
    }

    @Override
    public Icon getIcon() {
        return IconUtil.load("antiidle.png");
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    // 构建菜单项，可自动适配主菜单/托盘菜单
    @Override
    public JMenuItem createMenuItem() {
        JMenuItem item = new JCheckBoxMenuItem(getName(), getIcon(), running);
        item.addActionListener(this::handleMenuClick);
        return item;
    }


    private void handleMenuClick(ActionEvent e) {
        if (!running) {
            start(MainWindow.getFrame());
        } else {
            stop();
        }
        // 更新菜单状态
        if (menuItem != null) menuItem.setSelected(running);
    }

    @Override
    public void start(Component parent) {
        String input = JOptionPane.showInputDialog(parent, I18nManager.tr("plugin.antiidle.interval.tip"), intervalMinutes);
        if (input == null) return;
        try {
            int min = Integer.parseInt(input.trim());
            if (min < 1 || min > 120) throw new NumberFormatException();
            intervalMinutes = min;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(parent, I18nManager.tr("plugin.antiidle.interval.invalid"), getName(), JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (running) stop(); // 重新启动时先关闭旧任务

        executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(this::moveMouse, intervalMinutes, intervalMinutes, TimeUnit.MINUTES);
        running = true;
        JOptionPane.showMessageDialog(parent,
                I18nManager.trf("plugin.antiidle.started", intervalMinutes),
                getName(), JOptionPane.INFORMATION_MESSAGE);

        // 刷新菜单勾选
        if (menuItem != null) menuItem.setSelected(true);
    }

    @Override
    public void stop() {
        running = false;
        if (executor != null) {
            executor.shutdownNow();
            executor = null;
        }
        JOptionPane.showMessageDialog(MainWindow.getFrame(), "防锁屏已关闭。", getName(), JOptionPane.INFORMATION_MESSAGE);

        // 刷新菜单勾选
        if (menuItem != null) menuItem.setSelected(false);
    }

    // 用Robot模拟鼠标移动
    private void moveMouse() {
        try {
            Robot robot = new Robot();
            PointerInfo pointerInfo = MouseInfo.getPointerInfo();
            if (pointerInfo == null) return; // 部分远程桌面下会null
            Point loc = pointerInfo.getLocation();
            robot.mouseMove(loc.x + 1, loc.y); // 轻微移动
            Thread.sleep(20);
            robot.mouseMove(loc.x, loc.y);     // 再移回来
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
