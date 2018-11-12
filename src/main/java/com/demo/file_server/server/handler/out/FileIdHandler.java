package com.demo.file_server.server.handler.out;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.demo.file_server.dao.entity.FileStorage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;

/**
 * 文件元数据回复
 * @author tyjw
 *
 */
@Component
public class FileIdHandler {
	
	public static Logger logger = LoggerFactory.getLogger(FileIdHandler.class);

	public void write(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("FileIdHandler.write()");
		FileStorage info = (FileStorage) msg;
		ByteBuf buf = ctx.alloc().buffer();
		buf.writeByte(0x22);
		buf.writeInt(info.getLocalId());
		buf.writeInt(info.getId());
		ChannelFuture future = ctx.writeAndFlush(buf);
		future.addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				if (future.isSuccess()) {
					logger.info("文件元数据回复成功:{}", info);
				}
			}
		});
	}

}
