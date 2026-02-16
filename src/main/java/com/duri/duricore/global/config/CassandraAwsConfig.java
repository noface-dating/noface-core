package com.duri.duricore.global.config;

import org.springframework.boot.cassandra.autoconfigure.CqlSessionBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("prod")
public class CassandraAwsConfig {

    @Bean
    public CqlSessionBuilderCustomizer cassandraSessionBuilderCustomizer() {
        return builder -> builder
                .withAuthProvider(
                        new software.aws.mcs.auth.SigV4AuthProvider("us-east-1")
                );
    }
}