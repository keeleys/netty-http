package com.szmsd.db;

import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.dialect.OracleDialect;
import com.jfinal.plugin.c3p0.C3p0Plugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.ttianjun.common.kit.PropKit;

public class JfinalDb {
	public static void initDb(){
		PropKit.use("jdbc.properties");
		
		C3p0Plugin c3p0Plugin = new C3p0Plugin(PropKit.get("db.oracle.url"), PropKit.get("db.oracle.username"), 
				PropKit.get("db.oracle.password"),PropKit.get("driverClassName"));


		// 配置ActiveRecord插件
        ActiveRecordPlugin arp = new ActiveRecordPlugin(c3p0Plugin);
        arp.setDialect(new OracleDialect()) ;
        arp.setDevMode(false);

        c3p0Plugin.start();
        arp.start();
        
	}
}
