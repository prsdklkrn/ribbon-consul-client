package com.ppk.consul.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ecwid.consul.v1.ConsulClient;

@Configuration
public class ConsulConfig {

	@Bean
	public ConsulClient consulClient(ConsulBootstrapConfig consulBootstrapConfig) {
		ConsulClient consulClient = new ConsulClient(consulBootstrapConfig.getHost(), consulBootstrapConfig.getPort());
		return consulClient;
	}

}
