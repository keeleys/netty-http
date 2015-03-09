package com.szmsd.client;

import com.szmsd.netty.NettyServer;

public class Client {
	public static void main(String[] args) throws Exception {
		NettyServer.start(8088);
	}
}
