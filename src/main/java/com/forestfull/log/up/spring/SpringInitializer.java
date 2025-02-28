package com.forestfull.log.up.spring;

import com.forestfull.log.up.util.Log;
import com.forestfull.log.up.util.LogUpFactoryBean;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

public class SpringInitializer extends Log.LogFactory implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        applicationContext.getBeanFactory().registerSingleton("observableConfig", new ObservableConfig());
        Log.LogFactory.initConsole();
    }
}