package com.demo.file_server.server.handler.out;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.demo.file_server.dao.entity.FileStorage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;

@Sharable
@Component
public class ProcessHandler extends ChannelOutboundHandlerAdapter {
	
	private static Logger logger = LoggerFactory.getLogger(ProcessHandler.class);

	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		System.out.println("ProcessHandler.write()");
		FileStorage info = (FileStorage) msg;
		
		ByteBuf buf = ctx.alloc().buffer();
		buf.writeLong(info.getFile_id());
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

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		logger.error("异常信息:{}", cause.getMessage(), cause);
	}

}
