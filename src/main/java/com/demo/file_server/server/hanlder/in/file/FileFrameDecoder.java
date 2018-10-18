package com.demo.file_server.server.hanlder.in.file;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.demo.file_server.context.cach.FileInfoCach;
import com.demo.file_server.dao.entity.FileStorage;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.ChannelHandler.Sharable;

@Sharable
@Component
public class FileFrameDecoder extends SimpleChannelInboundHandler<ByteBuf> {
	
	@Autowired
	private FileMetaHandler fileMetaHandler;
	
	private static Logger logger = LoggerFactory.getLogger(FileFrameDecoder.class);
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ByteBuf buf) throws Exception {
		
		System.out.println("FileFrameDecoder.channelRead0()");
		//类型标识符
		byte head = buf.readByte();
		logger.info("数据包类型:{}", head);
		
		//文件唯一id
		long id = buf.readLong();
		logger.info("文件唯一id:{}", id);
		
		int dataLength = buf.readInt();
		int restLength = buf.readableBytes();
		if (dataLength != restLength) {
			return;
		}
		
		//数据区内容
		byte[] bytes = new byte[dataLength];
		buf.readBytes(bytes);
		
		/*
		 *数据区解析根据包类型来定 
		 *0x12 表示文件数据
		 *0x22 表示文件元数据
		 */
		if (head == 0x22) {
			//如果是文件元数据,交给下一个handler处理
			ByteBuf metaBuf = ctx.alloc().buffer();
			metaBuf.writeBytes(bytes);
			fileMetaHandler.initFile(ctx, metaBuf);
		}else if (head == 0x12) {
			FileStorage info = FileInfoCach.infoMap.get(id);
			if (info != null) {
				//更新帧字节内容
				info.setFrameBytes(bytes);
				ctx.fireChannelRead(info);
			}
		}
	}

}
