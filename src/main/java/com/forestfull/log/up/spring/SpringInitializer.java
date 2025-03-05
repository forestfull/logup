package com.forestfull.log.up.spring;

import com.forestfull.log.up.util.Log;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

public class SpringInitializer extends Log.LogFactory implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        Log.LogFactory.initConsole();
    }
}