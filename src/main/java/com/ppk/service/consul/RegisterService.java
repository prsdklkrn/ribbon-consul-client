package com.ppk.service.consul;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.management.MalformedObjectNameException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.agent.model.NewService;
import com.ecwid.consul.v1.agent.model.NewService.Check;
import com.google.common.collect.Lists;
import com.ppk.consul.config.ConsulProperties;

@ConditionalOnProperty(name = "consul.register.service.enabled", havingValue = "true")
@Component
public class RegisterService {

	private final ConsulProperties	consulProperties;
	private final ConsulClient		consulClient;

	@Autowired
	public RegisterService(ConsulProperties consulProperties, ConsulClient consulClient) {
		this.consulProperties = consulProperties;
		this.consulClient = consulClient;
	}

	@PostConstruct
	public void registerConsul() throws UnknownHostException, MalformedObjectNameException {
		System.out.println("Registering service..");
		String ipaddress = InetAddress.getLocalHost().getHostAddress();
		System.out.println("Registering service - " + consulProperties.getRegisterServiceName());
		NewService newService = new NewService();
		newService.setName(consulProperties.getRegisterServiceName());
		newService.setPort(consulProperties.getServerPort());
		System.out.println("Host address-->> " + ipaddress);
		Check check = new Check();
		check.setHttp("http://127.0.0.1:" + consulProperties.getServerPort() + consulProperties.getServerContextPath()
				+ consulProperties.getConsulHealthEndpoint());
		check.setInterval(consulProperties.getConsulHealthCheckInterval());
		newService.setCheck(check);
		List<String> tags = Lists.newArrayList();
		tags.add(consulProperties.getEnvironment());
		newService.setTags(tags);
		consulClient.agentServiceRegister(newService);
		System.out.println("Registered service - " + consulProperties.getRegisterServiceName());
		System.out.println("Done registering service..");
	}

	@PreDestroy
	public void destroyConsul() throws UnknownHostException, MalformedObjectNameException {
		String serviceName = consulProperties.getRegisterServiceName();
		consulClient.agentServiceDeregister(serviceName);
		System.out.println("UnRegistering service - " + serviceName);
		String ipaddress = InetAddress.getLocalHost().getHostAddress();
		System.out.println("UnRegistered Host address-->> " + ipaddress);
		System.out.println("UnRegistered service - " + serviceName);
	}

}
