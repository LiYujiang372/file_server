package com.demo.file_server.server.handler.out;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.demo.file_server.dao.entity.FileStorage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

/**
 * 文件数据回复
 * @author tyjw
 *
 */
@Component
public class ProcessHandler {
	
	private static Logger logger = LoggerFactory.getLogger(ProcessHandler.class);

	public void write(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("ProcessHandler.write()");
		FileStorage info = (FileStorage) msg;
		
		ByteBuf buf = ctx.alloc().buffer();
		buf.writeByte(0x32);
		buf.writeInt(info.getId());
		buf.writeInt(info.getSave_size());
		
		ChannelFuture future = ctx.writeAndFlush(buf);
		future.addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				if (!future.isSuccess()) {
					logger.error("异常信息:{}", future.cause().getMessage(), future.cause());
				}
			}
		});
	}

}
