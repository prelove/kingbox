package org.abitware.kingbox.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.*;

public class I18nManager {
    private static Locale locale = Locale.getDefault();
    private static ResourceBundle bundle;

    public static void setLocale(String lang) {
        switch (lang) {
            case "en":
                locale = Locale.ENGLISH;
                break;
            case "zh":
                locale = Locale.SIMPLIFIED_CHINESE;
                break;
            case "ja":
                locale = Locale.JAPANESE;
                break;
            default:
                locale = Locale.ENGLISH;
        }
        try {
            bundle = ResourceBundle.getBundle("i18n/messages", locale, new ResourceBundle.Control() {
                @Override
                public java.util.ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload) throws IllegalAccessException, InstantiationException, IOException {
                    String bundleName = toBundleName(baseName, locale);
                    String resourceName = toResourceName(bundleName, "properties");
                    try (InputStream stream = loader.getResourceAsStream(resourceName)) {
                        if (stream != null) {
                            // 强制按 UTF-8 解析
                            return new PropertyResourceBundle(new InputStreamReader(stream, StandardCharsets.UTF_8));
                        }
                    }
                    return null;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String tr(String key) {
        if (bundle == null) setLocale("en");
        return bundle.containsKey(key) ? bundle.getString(key) : key;
    }

    public static String trf(String key, Object... args) {
        return MessageFormat.format(I18nManager.tr(key), args);
    }

    public static String getLang() {
        return locale.getLanguage();
    }

}
