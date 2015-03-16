package com.szmsd.server.test;

import com.szmsd.anno.MsdCtrl;
import com.szmsd.core.aware.JsonAware;
import com.ttianjun.common.kit.parse.XJDataNode;

@MsdCtrl(url = "/json")
public class JsonProcess implements JsonAware{
	private XJDataNode node;
	
	/**
	 * 例子 http://localhost:8080/json?content={"name":18,"CityName":[{"name":"tianjun1"},{"name":"tianjun2"}],"code":{"name":"123"}}
	 * @Description:
	 * @author TianJun
	 * @date 2015年3月2日
	 * @return
	 */
	public String execute(){
		String result ="";
		String name = node.get("name");
		String cityName2 =node.getNodeList("CityName").get(1).get("name");
		String codeName = node.getNode("code").get("name");
		result = "name:"+name+" , cityName2 ="+cityName2+", code = "+codeName;
		return result;
	}
	public void setJsonDataNode(XJDataNode node) {
		this.node = node;
	}

}
