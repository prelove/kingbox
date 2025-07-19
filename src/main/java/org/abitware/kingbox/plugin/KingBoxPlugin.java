package org.abitware.kingbox.plugin;

import javax.swing.*;
import java.awt.*;

// 插件接口
public interface KingBoxPlugin {
    String getName();            // 插件名称
    Icon getIcon();              // 菜单图标
    boolean isRunning();         // 是否正在运行
    JMenuItem createMenuItem();  // 构建主菜单菜单项
    void start(Component parent);// 启动（传父窗口，便于弹窗）
    void stop();                 // 停止
}
