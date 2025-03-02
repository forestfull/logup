package com.forestfull.log.up.spring;

import com.forestfull.log.up.util.ObservableAspect;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Configuration class for setting up AOP and component scanning.
 *
 * @author <a href="https://vigfoot.com">Vigfoot</a>
 */
@Configuration
@EnableAspectJAutoProxy
@ComponentScan(basePackages = "com.forestfull.log.up.util")
public class ObservableConfig {

    /**
     * Registers the {@link ObservableAspect} bean.
     *
     * @return a new instance of {@link ObservableAspect}
     * @author <a href="https://vigfoot.com">Vigfoot</a>
     */
    @Bean
    public ObservableAspect observableAspect() {
        return new ObservableAspect();
    }

    /**
     * Registers a {@link BeanFactoryPostProcessor} bean that adds a singleton for the base package property.
     *
     * @return a {@link BeanFactoryPostProcessor} instance
     * @author <a href="https://vigfoot.com">Vigfoot</a>
     */
    @Bean
    public static BeanFactoryPostProcessor beanFactoryPostProcessor() {
        return beanFactory -> {
            String basePackage = System.getProperty("base.package");
            if (basePackage != null && !basePackage.isEmpty()) {
                beanFactory.registerSingleton("basePackage", basePackage);
            }
        };
    }
}
