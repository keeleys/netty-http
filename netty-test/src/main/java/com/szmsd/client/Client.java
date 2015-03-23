package com.szmsd.client;

import com.szmsd.netty.NettyServer;

public class Client {
	public static void main(String[] args) throws Exception {
		new NettyServer().setPort(8088).run();
	}
}
