package org.abitware.kingbox.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogManager {
    private static Logger logger;
    public static void init() {
        logger = LoggerFactory.getLogger(LogManager.class);
        logger.info("日志系统启动");
    }
    public static Logger getLogger() {
        return logger;
    }
}
