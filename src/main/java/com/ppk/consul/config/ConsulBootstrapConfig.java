package com.ppk.consul.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(ConsulBootstrapConfig.PREFIX)
public class ConsulBootstrapConfig {
	public static final String	PREFIX	= "consul.server";

	private String				host;
	private Integer				port;
	private String				apiVersion;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getApiVersion() {
		return apiVersion;
	}

	public void setApiVersion(String apiVersion) {
		this.apiVersion = apiVersion;
	}
}
