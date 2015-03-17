package com.szmsd.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.szmsd.core.aware.ValidAware;
import com.szmsd.core.aware.XmlAware;
import com.ttianjun.common.kit.PropKit;
import com.ttianjun.common.kit.parse.XJDataNode;
import com.ttianjun.common.kit.security.Base64Utils;
import com.ttianjun.common.kit.security.MD5Util;

public abstract class NettyXmlProcess  implements XmlAware,ValidAware{
	protected Logger log = LoggerFactory.getLogger(this.getClass());
	protected XJDataNode dataNode ;
	
	public void setXmlDataNode(XJDataNode  dataNode){
		this.dataNode = dataNode;
	}
	public String checkSign(String sign, String content) {
		log.debug("传入的sign:"+sign);
		log.debug("传入的content:"+content);
		String newSign=null;
		String key  =PropKit.getProp("conf.properties").get("key");
		try {
			newSign = Base64Utils.encode(MD5Util.md5Sign(content+key, "utf-8").getBytes("utf-8"));
		} catch (Exception e) {
			log.debug("sign加密失败:"+sign);
			e.printStackTrace();
		}
		log.debug("加密后的sign:"+newSign);
		boolean flag = newSign.equals(sign);
		if(flag)return null;
		return "<response><errmsg>签名验证失败<errmsg/></response>";
	}
	
}
