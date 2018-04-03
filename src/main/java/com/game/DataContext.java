package com.game;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractRefreshableApplicationContext;

public class DataContext {

    private static ApplicationContext applicationContext;

    public static void setApplicationContext(ApplicationContext context) {
        applicationContext = context;
    }

    public static ApplicationContext getContext() {
        return applicationContext;
    }

    /**
     * reload spring mvc
     */
    public static void reloadSpring() {
        AbstractRefreshableApplicationContext abstractRefreshableApplicationContext = (AbstractRefreshableApplicationContext) applicationContext;
        abstractRefreshableApplicationContext.refresh();
    }

}
