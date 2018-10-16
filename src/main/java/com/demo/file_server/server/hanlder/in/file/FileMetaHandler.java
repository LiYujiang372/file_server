package com.demo.file_server.server.hanlder.in.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.demo.file_server.context.cach.FileInfoCach;
import com.demo.file_server.dao.FileStorageRepository;
import com.demo.file_server.dao.entity.FileStorage;

import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

@Component
public class FileMetaHandler {
	
	@Autowired
	private FileStorageRepository repository;
	
	private static Logger logger = LoggerFactory.getLogger(FileMetaHandler.class);


	/**
	 * 解析文件元数据
	 * type 1:jpg	2:png	3:bmp
	 */
	public void initFile(ChannelHandlerContext ctx, ByteBuf buf) throws Exception {
		System.out.println("FileMetaHandler.channelRead()");
		
		boolean b = false; //数据是否正确解析
		
		long fileId = buf.readLong();//文件id
		byte type = buf.readByte();//文件类型
		int fileSize = buf.readInt();//文件总大小
		byte[] md5Bytes = new byte[16];
		buf.readBytes(md5Bytes);//文件校验码
		int nameLength = buf.readInt();//文件名长度
		
		//长度校验后读取文件名
		int restLength = buf.readableBytes();
		if (restLength != nameLength) {
			return;
		}
		byte[] nameBytes = new byte[nameLength];
		buf.readBytes(nameBytes);
		
		String fileName = new String(nameBytes, CharsetUtil.UTF_8);	//文件名
		
		
		repository.findByFileId(fileId);
		
		//文件信息写入数据库,加入缓存
		FileStorage info = new FileStorage();
		info.setProject_id(1);
		info.setTask_id(2);
		info.setFile_type(type);
		info.setFile_id(fileId);
		info.setFile_size(fileSize);
		info.setFile_name(fileName);
		info = repository.save(info);
		
		if (info != null) {
			
			//初始化后加入缓存
			info.setOldMD5(md5Bytes);
			info.init();
			FileInfoCach.infoMap.put(fileId, info);
			logger.info("存入数据库info的id:{}", info.getId());
			
			//回复客户端
			ctx.write(info);
		}
		ReferenceCountUtil.release(buf);
	}

}
