package com.ppk.consul.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@ConditionalOnProperty(name = "consul.register.service.enabled", havingValue = "true")
@Order(100)
@Component
@PropertySource(value = { "classpath:consul.client.properties" })
public class ConsulProperties {

	private final String	environment;
	private final String	consulServerHost;
	private final Integer	consulServerPort;
	private final String	consulHealthEndpoint;
	private final String	consulHealthCheckInterval;
	private final String	consulAjpPortKey;
	private final String	consulHttpPortKey;
	private final String	consulContextPathKey;
	private final String	consulKeyFolderPath;
	private final String	registerServiceName;

	@Autowired
	public ConsulProperties(
			@Value("${ppk.env}") String environment,
			@Value("${consul.server.host:localhost}") String consulServerHost,
			@Value("${consul.server.port:8500}") Integer consulServerPort,
			@Value("${consul.health.check.endpoint:/health}") String consulHealthEndpoint,
			@Value("${consul.health.check.interval:10s}") String consulHealthCheckInterval,
			@Value("${consul.keys.ajp.port}") String consulAjpPortKey,
			@Value("${consul.keys.http.port}") String consulHttpPortKey,
			@Value("${consul.keys.context.path}") String consulContextPathKey,
			@Value("${consul.keys.base.folder}") String consulKeyFolderPath,
			String registerServiceName) {
		this.environment = environment;
		this.consulServerHost = consulServerHost;
		this.consulServerPort = consulServerPort;
		this.consulHealthEndpoint = consulHealthEndpoint;
		this.consulHealthCheckInterval = consulHealthCheckInterval;
		this.consulAjpPortKey = consulAjpPortKey;
		this.consulHttpPortKey = consulHttpPortKey;
		this.consulContextPathKey = consulContextPathKey;
		this.consulKeyFolderPath = consulKeyFolderPath;
		this.registerServiceName = registerServiceName;
	}

	public String getEnvironment() {
		return environment;
	}

	public String getConsulServerHost() {
		return consulServerHost;
	}

	public Integer getConsulServerPort() {
		return consulServerPort;
	}

	public String getConsulHealthEndpoint() {
		return consulHealthEndpoint;
	}

	public String getConsulHealthCheckInterval() {
		return consulHealthCheckInterval;
	}

	public String getConsulAjpPortKey() {
		return consulAjpPortKey;
	}

	public String getConsulHttpPortKey() {
		return consulHttpPortKey;
	}

	public String getConsulContextPathKey() {
		return consulContextPathKey;
	}

	public String getConsulKeyFolderPath() {
		return consulKeyFolderPath;
	}

	public String getRegisterServiceName() {
		return registerServiceName;
	}

}
