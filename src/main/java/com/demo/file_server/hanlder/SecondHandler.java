package com.demo.file_server.hanlder;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.demo.file_server.service.FileWriter;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@Component
public class SecondHandler extends SimpleChannelInboundHandler<ByteBuf> {
	
	private static Logger logger = LoggerFactory.getLogger(SecondHandler.class);
	
	@Autowired
	private FileWriter fileWriter;
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ByteBuf buf) throws Exception {
		//包总大小
		int totalSize = buf.readInt();
		logger.info("数据包总大小:{}", totalSize);
		
		//类型标识符
		byte type = buf.readByte();
		logger.info("文件类型:{}", type);
		
		//文件总大小
		int fileSize = buf.readInt();
		logger.info("文件总大小:{}", fileSize);
		
		//文件唯一id
		long id = buf.readLong();
		logger.info("文件唯一id:{}", id);
		
		//数据区长度
		int dataLengh = buf.readInt();
		logger.info("数据区长度:{}", dataLengh);
		if (dataLengh != buf.readableBytes()) {
			throw new Exception("数据区长度不一致");
		}
		
		//数据区
		byte[] bytes = new byte[dataLengh];
		buf.readBytes(bytes);
		
		fileWriter.writeData(id, bytes, fileSize);
	}

}
