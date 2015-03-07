package com.szmsd.server.cosco;

import java.util.List;

import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.szmsd.anno.MsdCtrl;
import com.szmsd.core.JsonAware;
import com.ttianjun.common.kit.parse.XJDataNode;

@MsdCtrl(url = "/selectOrder")
public class SelectOrderProcess implements JsonAware{
	private XJDataNode node ;
	
	/**
	 * 例子 http://127.0.0.1:8088/selectOrder?content={"order_code":"coc333"}
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
	public void setJsonDataNode(XJDataNode node) {
		this.node= node;
	}
}
