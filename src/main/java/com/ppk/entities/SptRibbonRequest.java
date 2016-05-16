package com.ppk.entities;

import java.util.Map;

public class SptRibbonRequest {

	private String resourceGroupName;
	private String commaSeparatedServerList;
	private String templateName;
	private String templateURI;
	private int maxAutoRetryForNextServe;
	private Map<String, String> headerMap;
	private String httpMethod;
	private Map<String, String> pathVariablesMap;
	private String authHeaderParameter;
	private Object postBody;

	public static SptRibbonRequest getRibbonRequest(String resourceGroupName, String commaSeparatedServerList,
			String templateName, String uriTemplate, String maxRetry, String httpMethod, Map<String, String> headerMap,
			Map<String, String> pathVariablesMap, String authHeaderParameter, Object postBody) {
		SptRibbonRequest ribbonRequestVO = new SptRibbonRequest();
		ribbonRequestVO.setResourceGroupName(resourceGroupName);
		ribbonRequestVO.setCommaSeparatedServerList(commaSeparatedServerList);
		ribbonRequestVO.setTemplateName(templateName);
		ribbonRequestVO.setTemplateURI(uriTemplate);
		ribbonRequestVO.setMaxAutoRetryForNextServe(Integer.parseInt(maxRetry));
		ribbonRequestVO.setHttpMethod(httpMethod);
		ribbonRequestVO.setHeaderMap(headerMap);
		ribbonRequestVO.setPathVariablesMap(pathVariablesMap);
		ribbonRequestVO.setAuthHeaderParameter(authHeaderParameter);
		ribbonRequestVO.setPostBody(postBody);
		return ribbonRequestVO;
	}

	public String getResourceGroupName() {
		return resourceGroupName;
	}

	public void setResourceGroupName(String resourceGroupName) {
		this.resourceGroupName = resourceGroupName;
	}

	public String getCommaSeparatedServerList() {
		return commaSeparatedServerList;
	}

	public void setCommaSeparatedServerList(String commaSeparatedServerList) {
		this.commaSeparatedServerList = commaSeparatedServerList;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getTemplateURI() {
		return templateURI;
	}

	public void setTemplateURI(String templateURI) {
		this.templateURI = templateURI;
	}

	public int getMaxAutoRetryForNextServe() {
		return maxAutoRetryForNextServe;
	}

	public void setMaxAutoRetryForNextServe(int maxAutoRetryForNextServe) {
		this.maxAutoRetryForNextServe = maxAutoRetryForNextServe;
	}

	public Map<String, String> getHeaderMap() {
		return headerMap;
	}

	public void setHeaderMap(Map<String, String> headerMap) {
		this.headerMap = headerMap;
	}

	public String getHttpMethod() {
		return httpMethod;
	}

	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}

	public Map<String, String> getPathVariablesMap() {
		return pathVariablesMap;
	}

	public void setPathVariablesMap(Map<String, String> pathVariablesMap) {
		this.pathVariablesMap = pathVariablesMap;
	}

	public String getAuthHeaderParameter() {
		return authHeaderParameter;
	}

	public void setAuthHeaderParameter(String authHeaderParameter) {
		this.authHeaderParameter = authHeaderParameter;
	}

	public Object getPostBody() {
		return postBody;
	}

	public void setPostBody(Object postBody) {
		this.postBody = postBody;
	}
}
