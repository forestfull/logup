package com.forestfull.log.up.spring;

import com.forestfull.log.up.util.ObservableAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Configuration class for setting up AOP and component scanning.
 *
 * @author <a href="https://vigfoot.com">Vigfoot</a>
 */
@Configuration
@EnableAspectJAutoProxy
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
}
