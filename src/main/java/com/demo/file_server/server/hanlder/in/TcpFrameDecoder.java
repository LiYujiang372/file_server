package com.demo.file_server.server.hanlder.in;

import java.nio.ByteOrder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * 处理tcp数据帧
 * @author liyujiang
 *
 */
public class TcpFrameDecoder extends LengthFieldBasedFrameDecoder {
	
	public TcpFrameDecoder(ByteOrder byteOrder, int maxFrameLength, int lengthFieldOffset, int lengthFieldLength,
			int lengthAdjustment, int initialBytesToStrip, boolean failFast) {
		super(byteOrder, maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip, failFast);
	}
	
	/**
	 * 解码数据包
	 */
	@Override
	protected Object decode(ChannelHandlerContext ctx, ByteBuf buf) throws Exception {
		System.out.println("TcpFrameDecoder.decode()");
		return (ByteBuf) super.decode(ctx, buf);
	}

}
