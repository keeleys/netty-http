package com.szmsd.server.cosco;

import java.sql.SQLException;

import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.szmsd.anno.MsdCtrl;
import com.szmsd.core.JsonAware;
import com.ttianjun.common.kit.parse.XJDataNode;

@MsdCtrl(url = "/updateOrder")
public class UpdateOrderProcess implements JsonAware{
	private XJDataNode node ;
	
	/**
	 * 例子 http://127.0.0.1:8088/updateOrder?content={"order_code":"coc333","order_ename":"TianJun12312"}
	 * @Description:
	 * @author TianJun
	 * @date 2015年3月5日
	 * @return
	 */
	public String execute(){
		
		//修改操作用事物完成 ,Db.tx方法里面的全部共享一个事物
		boolean flag=Db.tx(new IAtom() {
			public boolean run() throws SQLException {
				String order_code = node.get("order_code");
				String order_ename = node.get("order_ename");
				int flag=Db.update("update tab_vip_order set order_ename=? where order_code=?",order_ename,order_code);
				return flag>=0;
			}
		});
		return JsonKit.toJson(flag);
	}
	public void setJsonDataNode(XJDataNode node) {
		this.node= node;
	}
}
