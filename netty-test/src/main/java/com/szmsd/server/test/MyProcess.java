package com.szmsd.server.test;

import java.util.Map;

import com.szmsd.anno.MsdCtrl;
import com.szmsd.core.RequestAware;
import com.szmsd.util.UriKit;


/**
 * 
 * @author TianJun
 * @Date 2015年3月2日
 * @MsdCtrl 注解成action服务，
 * 
 */
@MsdCtrl(method="process",url="/pro")
public class MyProcess implements RequestAware{
	private Map<String ,String> request;
	
	/**
	 * 
	 * @Description: 在这里写自己的业务，然后返回String给调用方
	 * @author TianJun
	 * @date 2015年3月2日
	 * @return
	 */
	public String process() throws Exception {
		String uri = request.get("uri");
		uri =UriKit.getUrl(uri);
		String content = request.get("content");
		return String.format("'uri':'%s','content':'%s'", uri,content);
	}

	public void setRequest(Map<String, String> request) {
		this.request = request;
	}
	
}
