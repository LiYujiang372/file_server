package com.demo.file_server.hanlder;

import java.nio.ByteOrder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * 基于长度字段的解码
 * @author tyjw
 *
 */
public class FirstHandler extends LengthFieldBasedFrameDecoder{
	
	
	private static final int HEAD_SIZE = 21;
	
	public FirstHandler(ByteOrder byteOrder, int maxFrameLength, int lengthFieldOffset, int lengthFieldLength,
			int lengthAdjustment, int initialBytesToStrip, boolean failFast) {
		super(byteOrder, maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip, failFast);
	}
	

	/**
	 * 解码数据包
	 */
	@Override
	protected Object decode(ChannelHandlerContext ctx, ByteBuf buf) throws Exception {
		
		ByteBuf newBuf = (ByteBuf) super.decode(ctx, buf);
		if (newBuf == null) {
			return null;
		}
		
		if (newBuf.readableBytes() < HEAD_SIZE) {
			throw new Exception("解码出错");
		}
		return newBuf;
	}

}
