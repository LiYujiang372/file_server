package com.demo.file_server.context;

import java.nio.ByteOrder;

import com.demo.file_server.server.hanlder.in.TcpFrameDecoder;


public class AppBeans {
	
	public static TcpFrameDecoder tcpFrameDecoder() {
		/*
		 * ByteOrder.BIG_ENDIAN 表示字节序使用大端
		 * 0 4   表示长度字段位置 0,4
		 * -4 表示开始计算长度的位置 -4 表示最开始位置
		 * 0 表示除去字段为0,保留完整原始字段
		 */
		return new TcpFrameDecoder(ByteOrder.BIG_ENDIAN, Integer.MAX_VALUE, 0, 4, -4, 0, true);
	}
	
}
