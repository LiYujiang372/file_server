package com.demo.file_server.context;

import java.nio.ByteOrder;

import com.demo.file_server.server.hanlder.in.TcpFrameDecoder;


public class AppBeans {
	
	public static TcpFrameDecoder tcpFrameDecoder() {
		/*
		 * ByteOrder.BIG_ENDIAN 表示字节序使用大端
		 * 9 lengthFieldOffset  长度字段开始位置的索引
		 * 4 lengthFieldLength 	长度字段字节长度
		 * 0 lengthAdjustment 计算长度的位置调整量
		 * 0 initialBytesToStrip 表示除去字段为0,保留完整原始字段
		 */
		return new TcpFrameDecoder(ByteOrder.BIG_ENDIAN, Integer.MAX_VALUE, 9, 4, 0, 0, true);
	}
	
}
