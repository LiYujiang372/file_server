package com.demo.file_server.hanlder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class FileHandler extends SimpleChannelInboundHandler<ByteBuf> {
	
	private static Logger logger = LoggerFactory.getLogger(FileHandler.class);

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ByteBuf buf) throws Exception {
		int length = buf.readableBytes();
		byte[] bytes = new byte[length];
		buf.readBytes(bytes);
		logger.info("接收到数据为:{}", new String(bytes, CharsetUtil.UTF_8));
		ctx.writeAndFlush(Unpooled.copiedBuffer("收到文件,谢谢!", CharsetUtil.UTF_8));
	}

}
