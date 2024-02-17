package com.kamar.issuemanagementsystem.global_configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;

import java.util.concurrent.TimeUnit;

@Configuration
public class GlobalCacheControlConfiguration {

    @Bean
    public CacheControl cacheControlConfiguration(){

        return CacheControl.maxAge(2, TimeUnit.DAYS)
                .staleIfError(1, TimeUnit.DAYS)
                .staleWhileRevalidate(2, TimeUnit.MINUTES)
                .mustRevalidate()
                .cachePrivate();
    }
}
