package org.abitware.kingbox.ui;

import org.abitware.kingbox.core.TrayManager;
import org.abitware.kingbox.menu.MenuConfig;
import org.abitware.kingbox.core.MenuService;
import org.abitware.kingbox.core.I18nManager;
import org.abitware.kingbox.core.ThemeManager;
import org.abitware.kingbox.plugin.KingBoxPlugin;
import org.abitware.kingbox.plugin.PluginManager;
import org.abitware.kingbox.util.IconUtil;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class MainWindow {
    private static JFrame frame;

    // 主题和语言action常量
    private static final List<String> THEME_ACTIONS = Arrays.asList(
            "themeLight", "themeDark", "themeIntellij", "themeDarcula"
    );
    private static final List<String> LANG_ACTIONS = Arrays.asList(
            "langZh", "langEn", "langJa"
    );

    // 全局单选组（避免多次 add 失效）
    private static ButtonGroup themeGroup = new ButtonGroup();
    private static ButtonGroup langGroup = new ButtonGroup();

    // 启动窗口，支持重复调用（每次都刷新菜单栏）
    public static void show() {
        if (frame == null) {
            frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            frame.setSize(800, 520);
            frame.setLocationRelativeTo(null);
            // 可选美化：FlatLaf菜单栏嵌入样式
            frame.getRootPane().putClientProperty("JRootPane.menuBarEmbedded", Boolean.TRUE);
        }
        frame.setTitle("KingBox " + I18nManager.tr("window.title"));
        frame.setJMenuBar(buildMenuBar(MenuService.loadMenu()));
        frame.setVisible(true);
        frame.toFront();
    }

    // 语言/主题切换或菜单配置变动时可调用此方法刷新
    public static void reload() {
        if (frame != null) {
            // 全部重建，不复用老的menuBar对象
            frame.setJMenuBar(buildMenuBar(MenuService.loadMenu()));
            frame.setTitle("KingBox " + I18nManager.tr("window.title"));
            frame.revalidate();
            frame.repaint();
        }
    }

    // 提供全局frame引用（如托盘/弹窗可用）
    public static JFrame getFrame() {
        return frame;
    }

    // ------- 递归构建菜单栏（主逻辑，核心美化风格） -------
    private static JMenuBar buildMenuBar(List<MenuConfig> menus) {
        JMenuBar menuBar = new JMenuBar();
        for (MenuConfig menu : menus) {
            if (menu.children != null && !menu.children.isEmpty()) {
                JMenu jMenu = buildMenu(menu);
                menuBar.add(jMenu);
            } else {
                JMenuItem item = buildMenuItem(menu);
                menuBar.add(item);
            }
        }
        // 可选美化：让菜单栏更窄/更贴边
        menuBar.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

        menuBar.add(buildPluginMenu());

        return menuBar;
    }

    private static JMenu buildPluginMenu() {
        JMenu pluginMenu = new JMenu(I18nManager.tr("menu.plugins"));
        for (KingBoxPlugin plugin : PluginManager.getPlugins()) {
            pluginMenu.add(plugin.createMenuItem());
        }
        return pluginMenu;
    }

    private static JMenu buildMenu(MenuConfig menu) {
        JMenu jMenu = new JMenu(I18nManager.tr(menu.key));
        if (menu.icon != null) jMenu.setIcon(IconUtil.load(menu.icon));
        for (MenuConfig child : menu.children) {
            if (child.children != null && !child.children.isEmpty()) {
                jMenu.add(buildMenu(child));
            } else {
                jMenu.add(buildMenuItem(child));
            }
        }
        return jMenu;
    }

    private static JMenuItem buildMenuItem(MenuConfig menu) {
        String text = I18nManager.tr(menu.key);
        Icon icon = (menu.icon != null) ? IconUtil.load(menu.icon) : null;

        // 主题菜单项
        if (isThemeMenu(menu)) {
            JRadioButtonMenuItem item = new JRadioButtonMenuItem(text);
            if (icon != null) item.setIcon(icon);
            item.setSelected(menu.action.equals(getCurrentThemeAction()));
            item.addActionListener(e -> handleAction(menu.action));
            themeGroup.add(item);
            // FlatLaf自动高亮蓝底圆点
            return item;
        }
        // 语言菜单项
        if (isLanguageMenu(menu)) {
            JRadioButtonMenuItem item = new JRadioButtonMenuItem(text);
            if (icon != null) item.setIcon(icon);
            item.setSelected(menu.action.equals(getCurrentLanguageAction()));
            item.addActionListener(e -> handleAction(menu.action));
            langGroup.add(item);
            return item;
        }
        // 普通功能项
        JMenuItem item = new JMenuItem(text);
        if (icon != null) item.setIcon(icon);
        item.addActionListener(e -> handleAction(menu.action));
        return item;
    }

    private static boolean isThemeMenu(MenuConfig menu) {
        return menu.action != null && THEME_ACTIONS.contains(menu.action);
    }
    private static boolean isLanguageMenu(MenuConfig menu) {
        return menu.action != null && LANG_ACTIONS.contains(menu.action);
    }
    private static String getCurrentThemeAction() {
        String t = ThemeManager.getCurrentTheme();
        if ("dark".equals(t)) return "themeDark";
        if ("intellij".equals(t)) return "themeIntellij";
        if ("darcula".equals(t)) return "themeDarcula";
        return "themeLight";
    }
    private static String getCurrentLanguageAction() {
        String l = I18nManager.getLang();
        if ("zh".equals(l)) return "langZh";
        if ("ja".equals(l)) return "langJa";
        return "langEn";
    }

    // ------- 菜单事件处理（功能菜单只举例，实际用你的ActionDispatcher） -------
    private static void handleAction(String action) {
        if (action == null) return;
        switch (action) {
            case "themeLight": ThemeManager.setTheme("light"); reload(); break;
            case "themeDark": ThemeManager.setTheme("dark"); reload(); break;
            case "themeIntellij": ThemeManager.setTheme("intellij"); reload(); break;
            case "themeDarcula": ThemeManager.setTheme("darcula"); reload(); break;
            case "langZh": I18nManager.setLocale("zh"); ThemeManager.refreshUI(); reload(); TrayManager.rebuildTrayMenu(); break;
            case "langEn": I18nManager.setLocale("en"); ThemeManager.refreshUI(); reload(); TrayManager.rebuildTrayMenu(); break;
            case "langJa": I18nManager.setLocale("ja"); ThemeManager.refreshUI(); reload(); TrayManager.rebuildTrayMenu(); break;
            case "exit": System.exit(0); break;
            default:
                // 其它action：你可以分发到 ActionDispatcher
                JOptionPane.showMessageDialog(frame, "Action: " + action, "Demo", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
