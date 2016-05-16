package com.spt.service.consul;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.catalog.model.CatalogService;
import com.spt.constants.Constants;

@Service
public class DiscoverService {

	@Autowired
	private Environment env;

	@Autowired
	ConsulClient consulClient;

	public String discoverService() {
		String serviceName = env
				.getProperty(Constants.CONSUL_REGISTER_SERVICE_NAME);
		return discoverService(serviceName);
	}

	public String discoverService(String serviceName) {
		List<CatalogService> catalogList = consulClient.getCatalogService(
				serviceName, null).getValue();
		List<String> serverList = new ArrayList<String>();
		for (CatalogService catalogService : catalogList) {
			serverList.add(catalogService.getServiceAddress() + ":"
					+ catalogService.getServicePort());
		}
		String servers = "";
		if (!CollectionUtils.isEmpty(serverList)) {
			servers = StringUtils.join(serverList.toArray(), ",");
		}
		return servers;
	}

}
