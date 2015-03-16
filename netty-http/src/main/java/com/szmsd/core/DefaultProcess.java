package com.szmsd.core;

import java.lang.reflect.Method;
import java.util.Map;

import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.szmsd.core.aware.JsonAware;
import com.szmsd.core.aware.RequestAware;
import com.szmsd.core.aware.ValidAware;
import com.szmsd.core.aware.XmlAware;
import com.szmsd.util.UriKit;
import com.ttianjun.common.kit.PropKit;
import com.ttianjun.common.kit.parse.JsonUtil;
import com.ttianjun.common.kit.parse.XJDataNode;
import com.ttianjun.common.kit.parse.XmlUtils;

/**
 * 默认处理器
 * @author TianJun
 * @Date 2015年3月2日
 */
public class DefaultProcess {
	private static Logger log = LoggerFactory.getLogger(DefaultProcess.class);
	public String process(Map<String ,String> request) throws Exception {
		String uri = request.get("uri");
		String result="";
		String content = request.get(PropKit.use("conf.properties").get("content_param"));
		String sign = request.get(PropKit.use("conf.properties").get("sign_param"));
		
		uri =UriKit.getUrl(uri);
		for(MsdCtrlRef msdCtrlRef : AutoBindCtrl.msdCtrlRefList){
			if(uri.equals(msdCtrlRef.getUrl())){
				Object o =msdCtrlRef.getModelClass().newInstance();
				
				if( o instanceof ValidAware){
					String checkStr=((ValidAware)o).checkSign(sign, content);
					if(checkStr!=null) return checkStr;
				}
				if( o instanceof RequestAware){
					((RequestAware)o).setRequest(request);
				}
				if(o instanceof XmlAware){
					((XmlAware)o).setXmlDataNode(dealBusinessXml(content));
				}else if(o instanceof JsonAware){
					((JsonAware)o).setJsonDataNode(dealBusinessJson(content));
				}
				
				Method method=msdCtrlRef.getModelClass().getMethod(msdCtrlRef.getMethod());
				Object resultObj = method.invoke(o);
				
				result = "访问uri:"+uri+",处理类"+o.getClass().getName();
				log.debug(result);
				return resultObj.toString();
			}
		}
		result = "访问uri:"+uri+",找不到处理类";
		log.error(result);
		return result;
		
	}
	public XJDataNode dealBusinessXml(String xmlStr) {
		try {
			return XmlUtils.parseXml(xmlStr);
		} catch (DocumentException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		return null;
	}
	public XJDataNode dealBusinessJson(String jsonStr){
		return JsonUtil.parseJson(jsonStr);
	}
}
