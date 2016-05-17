package com.ppk.service.consul;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.catalog.model.CatalogService;
import com.ppk.consul.config.ConsulProperties;

@ConditionalOnProperty(name = "consul.register.service.enabled", havingValue = "true")
@Service
public class DiscoverService {

	private final ConsulProperties	consulProperties;
	private final ConsulClient		consulClient;

	@Autowired
	public DiscoverService(ConsulProperties consulProperties, ConsulClient consulClient) {
		this.consulProperties = consulProperties;
		this.consulClient = consulClient;
	}

	public String discoverService() {
		return discoverService(consulProperties.getRegisterServiceName());
	}

	public String discoverService(String serviceName) {
		List<CatalogService> catalogList = consulClient.getCatalogService(serviceName, null).getValue();
		List<String> serverList = new ArrayList<String>();
		for (CatalogService catalogService : catalogList) {
			serverList.add(catalogService.getServiceAddress() + ":" + catalogService.getServicePort());
		}
		String servers = "";
		if (!CollectionUtils.isEmpty(serverList)) {
			servers = StringUtils.join(serverList.toArray(), ",");
		}
		return servers;
	}

}
