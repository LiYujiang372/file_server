package com.demo.file_server.server.hanlder.in.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.demo.file_server.dao.FileStorageRepository;
import com.demo.file_server.dao.entity.FileStorage;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;

@Sharable
@Component
public class FileDataHandler extends ChannelInboundHandlerAdapter {
	
	private static Logger logger = LoggerFactory.getLogger(FileDataHandler.class);
	
	@Autowired
	private FileStorageRepository repository;
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("FileDataHandler.channelRead()");
		
		/* 这里添加存储逻辑 */
		FileStorage info = (FileStorage) msg;
		info.wirteBytes();
		logger.info("存储信息:{}", info);
		
		/* 存储完成更新数据库 */
		if (info.getFinish() > 0) {
			repository.update(info.getId(), info.getSave_size(), info.getFinish());
		}
		
		ctx.write(info);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.error("异常信息:{}", cause.getMessage(), cause);
		ChannelFuture future = ctx.close();
		future.addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				if (future.isSuccess()) {
					logger.error("关闭异常连接");
				}
			}
		});
	}
	
}
