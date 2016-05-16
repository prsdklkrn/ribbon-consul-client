package com.spt.service.consul;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import javax.management.MalformedObjectNameException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.agent.model.NewService;
import com.ecwid.consul.v1.agent.model.NewService.Check;
import com.google.common.collect.Lists;
import com.spt.constants.Constants;

@Configuration
@PropertySource(value = { "classpath:consul.client.properties" })
public class RegisterService {
	
	@Autowired
	private Environment env;

	@Bean
	public ConsulClient consulClient() {
		ConsulClient consulClient = new ConsulClient(
				env.getProperty("consul.server.host"), Integer.parseInt(env
						.getProperty("consul.server.port")));
		return consulClient;
	}

	@Bean
	public String registerConsul(ConsulClient consulClient)
			throws UnknownHostException, MalformedObjectNameException {
		System.out.println("Registering service..");
		if (env.getProperty(Constants.CONSUL_REGISTER_SERVICE_ENABLED,
				Boolean.class)) {
			String serviceName = env
					.getProperty(Constants.CONSUL_REGISTER_SERVICE_NAME);
			String serverport = env.getProperty(Constants.SERVER_PORT);
			String ipaddress = InetAddress.getLocalHost().getHostAddress();

			System.out.println("Registering service - " + serviceName);
			NewService newService = new NewService();
			newService.setName(serviceName);
			newService.setPort(Integer.valueOf(serverport));
			System.out.println("Host address-->> " + ipaddress);
			Check check = new Check();
			check.setHttp("http://127.0.0.1:" + serverport
					+ env.getProperty(Constants.CONSUL_HEALTH_CHECK_ENDPOINT));
			check.setInterval(env
					.getProperty(Constants.CONSUL_HEALTH_CHECK_INTERVAL));
			newService.setCheck(check);
			List<String> tags = Lists.newArrayList();
			tags.add(env.getProperty(Constants.SPT_ENV));
			newService.setTags(tags);
			consulClient.agentServiceRegister(newService);
			System.out.println("Registered service - " + serviceName);
		}
		System.out.println("Done registering service..");
		return "";
	}

}
