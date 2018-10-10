package com.demo.file_server.server.hanlder.in;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.demo.file_server.context.cach.FileInfoCach;
import com.demo.file_server.pojo.FileInfo;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.ChannelHandler.Sharable;

@Sharable
@Component
public class FileFrameDecoder extends SimpleChannelInboundHandler<ByteBuf> {
	
	private static Logger logger = LoggerFactory.getLogger(FileFrameDecoder.class);
	
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
		int dataLength = buf.readInt();
		int restLength = buf.readableBytes();
		if (dataLength != restLength) {
			throw new Exception("数据区长度不一致");
		}
		
		//数据区
		byte[] bytes = new byte[dataLength];
		buf.readBytes(bytes);
		
		/*
		 * 缓存逻辑
		 */
		FileInfo info = FileInfoCach.infoMap.get(id);
		if (info == null) {
			//初始化操作
			info = new FileInfo();
			info.setFileId(id);
			info.setFileSize(fileSize);
			info.setSaveSize(0);
			info.initOs();
			FileInfoCach.infoMap.put(id, info);
		}
		//更新帧字节内容
		info.setFrameBytes(bytes);
		
		ctx.fireChannelRead(info);
	}

}
