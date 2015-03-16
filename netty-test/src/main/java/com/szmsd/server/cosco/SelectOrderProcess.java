package com.szmsd.server.cosco;

import java.util.List;

import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.szmsd.anno.MsdCtrl;
import com.szmsd.core.aware.JsonAware;
import com.szmsd.core.service.NettyJsonProcess;
import com.ttianjun.common.kit.parse.XJDataNode;

@MsdCtrl(url = "/selectOrder")
public class SelectOrderProcess extends NettyJsonProcess{
	
	/**
	 * 例子 http://127.0.0.1:8088/selectOrder?sign=N2E3ZjdlN2UxOGE4Yzk3YWQwOGE4ZDk4MWQzNzlkMjc=&content={"order_code":"coc333"}
	 * {"order_code":"coc333"}szmsd
	 * 7a7f7e7e18a8c97ad08a8d981d379d27
	 * N2E3ZjdlN2UxOGE4Yzk3YWQwOGE4ZDk4MWQzNzlkMjc=
	 * @Description:
	 * @author TianJun
	 * @date 2015年3月5日
	 * @return
	 */
	public String execute(){
		final String orderCode = node.get("order_code");
		List<Record> result = Db.find("select * from tab_vip_order where order_code =?", orderCode);
		return JsonKit.toJson(result);
	}
}
