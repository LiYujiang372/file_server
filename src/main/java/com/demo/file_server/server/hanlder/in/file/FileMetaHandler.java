package com.demo.file_server.server.hanlder.in.file;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.demo.file_server.context.cach.FileInfoCach;
import com.demo.file_server.dao.FileStorageRepository;
import com.demo.file_server.dao.entity.FileStorage;
import com.demo.file_server.server.handler.out.FileIdHandler;

import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

@Component
public class FileMetaHandler {
	
	@Autowired
	private FileStorageRepository repository;
	
	@Autowired
	private FileIdHandler fileIdHandler;
	
	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(FileMetaHandler.class);


	/**
	 * 解析文件元数据
	 * type 1:jpg	2:png	3:bmp
	 */
	public void initFile(ChannelHandlerContext ctx, ByteBuf buf) throws Exception {
		System.out.println("FileMetaHandler.channelRead()");
		
		int localId = buf.readInt();//文件id(客户端文件本地id)
		long createTime = buf.readLong();//文件创建时间
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
		String fileName = new String(nameBytes, CharsetUtil.UTF_8);	//文件名,字符编码utf-8
		
		FileStorage info = new FileStorage();
		info.setProject_id(1);
		info.setTask_id(2);
		info.setLocalId(localId);
		info.setCreate_date(new Date(createTime));
		info.setFile_type(type);
		info.setFile_size(fileSize);
		info.setFile_name(fileName);
		info = repository.save(info);
		//初始化后加入缓存
		info.setOldMD5(md5Bytes);
		info.init();
		FileInfoCach.infoMap.put(info.getId(), info);
		
		//回复客户端
		fileIdHandler.write(ctx, info);
		
		ReferenceCountUtil.release(buf);
	}

}
