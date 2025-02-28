package com.forestfull.log.up.spring;

import com.forestfull.log.up.util.LogUpConfigLoader;
import com.forestfull.log.up.util.LogUpFactoryBean;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

public class SpringInitializer extends LogUpFactoryBean implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        applicationContext.getBeanFactory().registerSingleton("observableConfig", new ObservableConfig());

        try {
            final LogUpProperties logUpProperties = applicationContext.getBean(LogUpProperties.class);
            configureProperties(logUpProperties.toProperties());
            System.out.println("Successfully configured LogUpProperties.");
        } catch (Exception e) {
            System.out.println("Starting default configuration: LogUpProperties not found.");
            configureProperties();
        } finally {
            if (LogUpFactoryBean.level == null)
                LogUpConfigLoader.loggingInitializeManual();

            defaultInitialize();
        }
    }
}