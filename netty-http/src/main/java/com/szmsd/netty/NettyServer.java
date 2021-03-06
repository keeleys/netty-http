package com.szmsd.netty;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.dialect.OracleDialect;
import com.jfinal.plugin.c3p0.C3p0Plugin;
import com.szmsd.core.AutoBindCtrl;
import com.ttianjun.common.kit.PropKit;
public final class NettyServer {
	
	public static void start(int prop) throws Exception {
		new NettyServer().setPort(prop).run();
	}
	private Integer port = 8088; 
	private String dialect="oracle";
	private static Logger log = LoggerFactory.getLogger(NettyServer.class);
	
	/**
	 * 
	 * @Description:启动服务
	 * @author TianJun
	 * @date 2015年3月2日
	 * @return
	 */
	public void run() throws InterruptedException{
		runDb();
		runAutoBind();
		runNetty();
	}
	
	public void runAutoBind(){
		PropKit.use("conf.properties");
		AutoBindCtrl autoBindCtrl = new AutoBindCtrl();
		autoBindCtrl.addScanPackages(PropKit.get("ctrl_scan_path"));
		autoBindCtrl.bind();
	}
	
	public NettyServer runDb(){
		PropKit.use("jdbc.properties");
		C3p0Plugin c3p0Plugin = new C3p0Plugin(PropKit.get("db.oracle.url"), PropKit.get("db.oracle.username"), 
				PropKit.get("db.oracle.password"),PropKit.get("driverClassName"));

		// 配置ActiveRecord插件
        ActiveRecordPlugin arp = new ActiveRecordPlugin(c3p0Plugin);
        if(!dialect.equals("mysql"))
        	arp.setDialect(new OracleDialect());
        arp.setShowSql(true);
        arp.setDevMode(false);

        c3p0Plugin.start();
        arp.start();
        return this;
	}
	public void runNetty() throws InterruptedException{
		  
		EventLoopGroup parentGroup = new NioEventLoopGroup(1);
		EventLoopGroup childGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap sb = new ServerBootstrap();
			sb.option(ChannelOption.SO_BACKLOG, 1024);
			sb.group(parentGroup, childGroup)
					.channel(NioServerSocketChannel.class)
					.handler(new LoggingHandler(LogLevel.INFO))
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch)throws Exception {
							ChannelPipeline p = ch.pipeline();
							p.addLast(new HttpServerCodec());
							p.addLast(new NettyHandler());
						}

					});
			Channel ch = sb.bind(port).sync().channel();
			log.info("server 127.0.0.1:"+ port+" is starting");
			ch.closeFuture().sync();
			
			
		} finally {
			parentGroup.shutdownGracefully();
			childGroup.shutdownGracefully();
		}
	}
	
	public NettyServer setPort(Integer port) {
		this.port = port;
		return this;
	}

	/**
	 * 
	 * @author TianJun
	 * @date 2015年3月19日
	 * @param dialect : mysql , oracle
	 */
	public NettyServer setDialect(String dialect) {
		this.dialect = dialect;
		return this;
	}
	
	
	
}
