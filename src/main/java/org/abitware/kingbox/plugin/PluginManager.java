package org.abitware.kingbox.plugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ServiceLoader;

public class PluginManager {
    private static final List<KingBoxPlugin> plugins = new ArrayList<>();

    public static void init() {
        ServiceLoader<KingBoxPlugin> loader = ServiceLoader.load(KingBoxPlugin.class);
        for (KingBoxPlugin plugin : loader) {
            plugins.add(plugin);
        }
    }

    public static List<KingBoxPlugin> getPlugins() {
        return Collections.unmodifiableList(plugins);
    }
}