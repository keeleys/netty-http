package com.szmsd.server.cosco;

import java.sql.SQLException;

import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.szmsd.anno.MsdCtrl;
import com.szmsd.core.aware.JsonAware;
import com.szmsd.core.service.NettyJsonProcess;
import com.ttianjun.common.kit.parse.XJDataNode;

@MsdCtrl(url = "/updateOrder")
public class UpdateOrderProcess extends NettyJsonProcess{
	
	/**
	 * 例子
	 * 1  http://127.0.0.1:8088/updateOrder?sign=NTcxNzNhMTlhNmI5MGU0MjAwM2FlNzgyMjEzZmU3ODM=&content={"order_code":"coc333","order_ename":"TianJun12312"}
	 * {"order_code":"coc333","order_ename":"TianJun12312"}szmsd
	 * 57173a19a6b90e42003ae782213fe783
	 * NTcxNzNhMTlhNmI5MGU0MjAwM2FlNzgyMjEzZmU3ODM=
	 * 2 http://127.0.0.1:8088/updateOrder?sign=ODQ4OGNhYjUwNjA2ZGJjZTBiYmRiNzIzODEwZmQzNzI=&content={"order_code":"coc333","order_ename":"田俊"}
	 * {"order_code":"coc333","order_ename":"田俊"}szmsd
	 * 8488cab50606dbce0bbdb723810fd372
	 * ODQ4OGNhYjUwNjA2ZGJjZTBiYmRiNzIzODEwZmQzNzI=
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
}
