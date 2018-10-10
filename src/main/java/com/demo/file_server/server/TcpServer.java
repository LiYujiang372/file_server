package com.demo.file_server.server;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.demo.file_server.context.AppBeans;
import com.demo.file_server.context.AppConfigs;
import com.demo.file_server.server.handler.out.ProcessHandler;
import com.demo.file_server.server.hanlder.in.FileFrameDecoder;
import com.demo.file_server.server.hanlder.in.FileSaveHandler;

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

	private EventLoopGroup parentGroup = new NioEventLoopGroup(1);
	
	private EventLoopGroup childGroup = new NioEventLoopGroup(AppConfigs.CORE_COUNT * 2);
	
	private static Logger logger = LoggerFactory.getLogger(TcpServer.class);
	
	@Autowired
	private FileFrameDecoder fileFrameDecoder;
	
	@Autowired
	private FileSaveHandler fileSaveHandler;
	
	@Autowired
	private ProcessHandler processHandler;
	
	private final static int SERVER_PORT = 2345;
	
	/**
	 * 初始化服务器
	 */
	@PostConstruct
	public void init() {
		bootstrap.group(parentGroup, childGroup)
			.channel(NioServerSocketChannel.class)
			.localAddress(SERVER_PORT)
			.childHandler(new ChannelInitializer<Channel>() {
				@Override
				protected void initChannel(Channel ch) throws Exception {
					ch.pipeline().addLast(processHandler);
					ch.pipeline().addLast(AppBeans.tcpFrameDecoder());
					ch.pipeline().addLast(fileFrameDecoder);
					ch.pipeline().addLast(fileSaveHandler);
				}
			});
	}
	
	/**
	 * 启动服务器
	 * @throws InterruptedException
	 */
	public void start() throws InterruptedException {
		logger.info("启动服务器中...");
		ChannelFuture future = bootstrap.bind().sync();
		if (future.isSuccess()) {
			logger.info("服务器启动成功!");
			future.channel().closeFuture().sync();
		}
	}
}
