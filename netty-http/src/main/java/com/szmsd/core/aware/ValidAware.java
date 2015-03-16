package com.szmsd.core.aware;

public interface ValidAware {
	/**
	 * 
	 * @Description:检查签名
	 * @author TianJun
	 * @date 2015年3月16日
	 * @return
	 */
	public String checkSign(String sign,String content);
}
