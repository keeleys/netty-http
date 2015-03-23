package com.szmsd.test;

import org.junit.Test;

import com.ttianjun.common.kit.http.HttpKit;

public class TestCosco {
	@Test
	public void testSelectOrder(){
		String url = "http://127.0.0.1:8088/selectOrder";
		String data = "sign=N2E3ZjdlN2UxOGE4Yzk3YWQwOGE4ZDk4MWQzNzlkMjc=&content={\"order_code\":\"coc333\"}";
		String result = HttpKit.post(url, data);
		System.out.println(result);
	}
}
