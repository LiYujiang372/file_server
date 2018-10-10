package com.demo.file_server.server.handler.out;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.demo.file_server.pojo.FileInfo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.channel.ChannelHandler.Sharable;

@Sharable
@Component
public class ProcessHandler extends ChannelOutboundHandlerAdapter {
	
	private static Logger logger = LoggerFactory.getLogger(ProcessHandler.class);

	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		FileInfo info = (FileInfo) msg;
		ByteBuf buf = Unpooled.buffer();
		buf.writeLong(info.getFileId());
		buf.writeByte(info.getProcess());
		ctx.writeAndFlush(buf);
	}
	
	
	
	

}
