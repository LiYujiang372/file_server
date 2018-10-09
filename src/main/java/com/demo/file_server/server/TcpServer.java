package com.demo.file_server.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.demo.file_server.hanlder.FirstHandler;
import com.demo.file_server.hanlder.SecondHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
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
	
	@Autowired
	private FirstHandler firstHandler;
	
	@Autowired
	private SecondHandler secondHandler;
	
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
					ch.pipeline().addLast(firstHandler);
					ch.pipeline().addLast(secondHandler);
				}
			});
	}
	
	/**
	 * 启动服务器
	 * @throws InterruptedException 
	 */
	public void start() throws InterruptedException {
		init();
		logger.info("启动服务器中...");
		ChannelFuture future = bootstrap.bind().sync();
		if (future.isSuccess()) {
			logger.info("服务器启动成功!");
			future.channel().closeFuture().sync();
		}
	}
}
