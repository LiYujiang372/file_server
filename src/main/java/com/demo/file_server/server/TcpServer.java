package com.demo.file_server.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.demo.file_server.hanlder.FileHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * tcp服务器
 * @author tyjw
 *
 */
@Component
public class TcpServer {
	
	private ServerBootstrap bootstrap = new ServerBootstrap();

	private EventLoopGroup group = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors());
	
	private static Logger logger = LoggerFactory.getLogger(TcpServer.class);
	
	private final static int SERVER_PORT = 2345;
	
	/**
	 * 初始化服务器
	 */
	public void init() {
		bootstrap.group(group)
			.channel(NioServerSocketChannel.class)
			.localAddress(SERVER_PORT)
			.childHandler(new ChannelInitializer<Channel>() {
				@Override
				protected void initChannel(Channel ch) throws Exception {
					ch.pipeline().addLast(new FileHandler());
				}
			});
	}
	
	/**
	 * 启动服务器
	 * @throws InterruptedException 
	 */
	public void start() throws InterruptedException {
		init();
		bootstrap.bind().sync().channel().closeFuture().sync();
	}
}
