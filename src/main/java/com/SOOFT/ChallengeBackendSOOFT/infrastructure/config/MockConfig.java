package com.SOOFT.ChallengeBackendSOOFT.infrastructure.config;

import com.SOOFT.ChallengeBackendSOOFT.infrastructure.outputAdapter.persistence.repository.DevTransferenciaRepositoryImpl;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("mock")
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class MockConfig {

}
