package com.forestfull.logger.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
@ComponentScan(basePackages = "com.forestfull.logger.util")
public class ObservableConfig {

    @Bean
    ObservableAspect observableAspect() {
        return new ObservableAspect();
    }

    @Bean
    BeanFactoryPostProcessor beanFactoryPostProcessor() {
        return new BeanFactoryPostProcessor() {
            @Override
            public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
                String basePackage = System.getProperty("base.package");
                if (basePackage != null && !basePackage.isEmpty()) {
                    beanFactory.registerSingleton("basePackage", basePackage);
                }
            }
        };
    }
}
