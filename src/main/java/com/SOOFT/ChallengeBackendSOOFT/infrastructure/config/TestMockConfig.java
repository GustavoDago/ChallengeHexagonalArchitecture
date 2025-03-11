package com.SOOFT.ChallengeBackendSOOFT.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@Profile("mock")
@EnableTransactionManagement(proxyTargetClass = false) //Desactiva la transacción
public class TestMockConfig {

}