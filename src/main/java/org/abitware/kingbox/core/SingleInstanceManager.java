package org.abitware.kingbox.core;

import java.io.IOException;
import java.net.ServerSocket;

public class SingleInstanceManager {
    private static ServerSocket lockSocket;
    public static boolean lock() {
        try {
            lockSocket = new ServerSocket(65432); // 端口随便选一个
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
