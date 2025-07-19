package org.abitware.kingbox.core;

import org.abitware.kingbox.menu.MenuConfig;
import org.abitware.kingbox.plugin.KingBoxPlugin;
import org.abitware.kingbox.plugin.PluginManager;
import org.abitware.kingbox.ui.MainWindow;
import org.abitware.kingbox.ui.TrayPopupWindow;
import org.abitware.kingbox.util.IconUtil;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * @author Administrator
 */
public class TrayManager {
    private static TrayIcon trayIcon;
    private static TrayPopupWindow popupWindow = new TrayPopupWindow();

    public static void init() {
        if (!SystemTray.isSupported()) return;
        try {
            SystemTray tray = SystemTray.getSystemTray();

            // 推荐资源方式加载托盘图标
            Image image = Toolkit.getDefaultToolkit().createImage(
                    TrayManager.class.getClassLoader().getResource("static/images/kingbox.png")
            );
            if (image == null) {
                // fallback（开发环境）
                image = Toolkit.getDefaultToolkit().createImage("static/images/kingbox.png");
            }

            PopupMenu popup = buildPopupMenu(MenuService.loadMenu());
            trayIcon = new TrayIcon(image, "KingBox", popup);

            // FlatLaf建议：让小图标更清晰（可选）
            trayIcon.setImageAutoSize(true);

            trayIcon.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    // 左键双击：弹主窗口
                    if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 1) {
                        MainWindow.show();
                    }
                    // 右键单击：弹 FlatLaf 风格弹窗
                    if (e.getButton() == MouseEvent.BUTTON3 && e.getClickCount() == 1) {
                        popupWindow.showPopup();
                    }
                }
            });

            tray.add(trayIcon);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static PopupMenu buildPopupMenu(List<MenuConfig> menus) {
        PopupMenu popup = new PopupMenu();
        for (MenuConfig cfg : menus) {
            popup.add(buildTrayMenu(cfg));
        }
        // 插件菜单单独加入
        Menu pluginMenu = new Menu(I18nManager.tr("menu.plugins"));
        for (KingBoxPlugin plugin : PluginManager.getPlugins()) {
            MenuItem item = new MenuItem(plugin.getName());
            // 给item加ActionListener，调用 plugin.createMenuItem().doClick() 或 plugin.start()/stop() 等
            pluginMenu.add(item);
        }
        popup.add(pluginMenu);
        return popup;
    }

    // 新增：重建托盘菜单
    public static void rebuildTrayMenu() {
        try {
            SystemTray tray = SystemTray.getSystemTray();
            if (trayIcon != null) {
                tray.remove(trayIcon);
            }
            Image image = Toolkit.getDefaultToolkit().createImage(
                    TrayManager.class.getClassLoader().getResource("static/images/kingbox.png")
            );
            PopupMenu popup = buildPopupMenu(MenuService.loadMenu()); // 这里内部也应每次动态生成
            trayIcon = new TrayIcon(image, "KingBox", popup);
            trayIcon.setImageAutoSize(true);
            tray.add(trayIcon);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static MenuItem menuItem(MenuConfig cfg) {
        MenuItem item = new MenuItem(I18nManager.tr(cfg.key));

        if (cfg.action != null && !cfg.action.isEmpty()) {
            item.addActionListener(e -> handleAction(cfg.action));
        } else {
            // 父级菜单/无action的只显示，不响应
            item.setEnabled(false); // 或 item.setFocusable(false);
        }

        return item;
    }

    private static void handleAction(String action) {
        switch (action) {
            case "langZh":
                I18nManager.setLocale("zh");
                ThemeManager.refreshUI();
                TrayManager.rebuildTrayMenu();
                break;
            case "langEn":
                I18nManager.setLocale("en");
                ThemeManager.refreshUI();
                TrayManager.rebuildTrayMenu();
                break;
            case "langJa":
                I18nManager.setLocale("ja");
                ThemeManager.refreshUI();
                TrayManager.rebuildTrayMenu();
                break;
            case "showMessageA":
                Toolkit.getDefaultToolkit().beep();
                break;
            case "showMessageB":
                Toolkit.getDefaultToolkit().beep();
                break;
            case "switchTheme":
                // 后续实现
                break;
            case "exit":
                System.exit(0);
                break;
            default:
                break;
        }
    }

    private static MenuItem buildTrayMenu(MenuConfig cfg) {
        if (cfg.children != null && !cfg.children.isEmpty()) {
            Menu menu = new Menu(I18nManager.tr(cfg.key));
            for (MenuConfig child : cfg.children) {
                menu.add(buildTrayMenu(child)); // 递归
            }
            return menu;
        } else {
            MenuItem item = new MenuItem(I18nManager.tr(cfg.key));
            if (cfg.action != null && !cfg.action.isEmpty()) {
                item.addActionListener(e -> ActionDispatcher.handle(cfg.action));
            }
            return item;
        }
    }

}
