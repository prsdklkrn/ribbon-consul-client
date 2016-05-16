package com.spt.service.consul;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.annotation.PreDestroy;
import javax.management.MalformedObjectNameException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.ecwid.consul.v1.ConsulClient;
import com.spt.constants.Constants;

@Service
public class DeregisterConsul {

	@Autowired
	Environment env;

	@Autowired
	ConsulClient consulClient;

	@PreDestroy
	public String destroyConsul() throws UnknownHostException,
			MalformedObjectNameException {
		if (env.getProperty(Constants.CONSUL_REGISTER_SERVICE_ENABLED,
				Boolean.class)) {
			String serviceName = env
					.getProperty(Constants.CONSUL_REGISTER_SERVICE_NAME);
			consulClient.agentServiceDeregister(serviceName);
			System.out.println("UnRegistering service - " + serviceName);
			String ipaddress = InetAddress.getLocalHost().getHostAddress();
			System.out.println("UnRegistered Host address-->> " + ipaddress);
			System.out.println("UnRegistered service - " + serviceName);
		}
		return "";
	}
}
