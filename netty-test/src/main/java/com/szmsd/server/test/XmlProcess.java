package com.szmsd.server.test;

import com.szmsd.anno.MsdCtrl;
import com.szmsd.core.aware.XmlAware;
import com.ttianjun.common.kit.parse.XJDataNode;

@MsdCtrl(url = "/xml")
public class XmlProcess implements XmlAware{
	private XJDataNode dataNode ;
	/**
	 * 
	 * @Description:
	 * @author TianJun
	 * @date 2015年3月2日
	 * @return
	 * <?xml version="1.0" encoding="UTF-8"?>
		<order>
		  <order_id>cosco1423462879387</order_id>
		  <order_no>ZY-1423462879387</order_no>
		  <order_ename>zhangshan</order_ename>
		  <telphone>13524518845</telphone>
		  <order_item>
		    <order_item_id>cosco1423462879387</order_item_id>
		    <product_id>productId0</product_id>
		    <product_name>productName0</product_name>
		    <code>code0</code>
		    <price>
		     <p1>2</p1>
		    </price>
		  </order_item>
		  <order_item>
		    <order_item_id>cosco1423462879387</order_item_id>
		    <product_id>productId1</product_id>
		    <product_name>productName1</product_name>
		    <code>code1</code>
		    <price>1.0</price>
		  </order_item>
		</order>
	 */
	public String execute(){
		String result ="";
		String order_no = dataNode.getNode("order_no").get("text");
		String order_item_id = dataNode.getNodeList("order_item").get(0).getNode("order_item_id").get("text");
		String p1 =  dataNode.getNodeList("order_item").get(0).getNode("price").getNode("p1").get("text");
		result = String.format("orderno:%s , order_item_id:%s , p1:%s", order_no,order_item_id,p1);
		return result;
	}

	public void setXmlDataNode(XJDataNode  dataNode){
		this.dataNode = dataNode;
	}
}
