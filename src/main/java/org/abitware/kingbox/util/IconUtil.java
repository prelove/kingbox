package org.abitware.kingbox.util;

import javax.swing.*;
import java.net.URL;

public class IconUtil {
    public static Icon load(String iconName) {
        URL url = IconUtil.class.getClassLoader().getResource("static/images/" + iconName);
        return url != null ? new ImageIcon(url) : null;
    }
}
