package com.duri.duricore.global.config;

import org.springframework.boot.cassandra.autoconfigure.CqlSessionBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
public class CassandraLocalConfig {

    @Bean
    public CqlSessionBuilderCustomizer cassandraSessionBuilderCustomizer() {
        return builder -> {};
    }
}
