package com.szmsd.core;


public class MsdCtrlRef {
	private String url;
	private String method;
	private Class<? extends Object> modelClass;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Class<? extends Object> getModelClass() {
		return modelClass;
	}
	public void setModelClass(Class<? extends Object> modelClass) {
		this.modelClass = modelClass;
	}
	public MsdCtrlRef(String url,String method, Class<? extends Object> modelClass) {
		super();
		this.url = url;
		this.method = method;
		this.modelClass = modelClass;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	
}
