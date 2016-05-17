package com.ppk.consul.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ecwid.consul.v1.ConsulClient;

@Configuration
@EnableConfigurationProperties(ConsulBootstrapConfig.class)
public class ConsulConfig {

	@Bean
	public ConsulClient consulClient(ConsulBootstrapConfig consulBootstrapConfig) {
		ConsulClient consulClient = new ConsulClient(consulBootstrapConfig.getHost(), consulBootstrapConfig.getPort());
		return consulClient;
	}

}
