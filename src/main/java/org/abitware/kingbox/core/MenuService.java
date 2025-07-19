package org.abitware.kingbox.core;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.abitware.kingbox.menu.MenuConfig;

import java.io.InputStream;
import java.util.List;

public class MenuService {
    private static List<MenuConfig> menus;

    public static List<MenuConfig> loadMenu() {
        if (menus != null) return menus;
        try (InputStream in = MenuService.class.getClassLoader().getResourceAsStream("config/menu.json")) {
            ObjectMapper mapper = new ObjectMapper();
            menus = mapper.readValue(in, new TypeReference<List<MenuConfig>>() {});
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return menus;
    }
}
