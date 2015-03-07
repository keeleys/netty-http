package com.szmsd.util;

import com.ttianjun.common.kit.StrKit;

public class UriKit {
	public static String getUrl(String uri){
		if(StrKit.notBlank(uri)){
			return uri.split("[?]")[0];
		}
		return null;
	}
}	
