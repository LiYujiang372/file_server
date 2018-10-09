package com.demo.file_server.hanlder;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.demo.file_server.service.FileWriter;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class FileHandler extends SimpleChannelInboundHandler<ByteBuf> {
	
	private static Logger logger = LoggerFactory.getLogger(FileHandler.class);
	
	//用于处理拆包的缓冲区
	private ByteBuf tempBuf;
	
	private FileWriter fileWriter = new FileWriter();

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		//连接活跃时,分配缓冲区,用于处于拆包情况
		tempBuf = Unpooled.buffer();
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ByteBuf buf) throws Exception {
		
		logger.info("============================================");
		
		ByteBuf newBuf = buf.copy();
		//拼包
		logger.info("开始拼包, 半包的数据大小:{}", tempBuf.readableBytes());
		tempBuf.writeBytes(newBuf);
		logger.info("拼包后的总包大小:{}", tempBuf.readableBytes());
		
		//判断是否到达文件流末尾
		boolean toEnd = isToEnd(tempBuf);
		if (toEnd) {
			readFileData(tempBuf);
			//重置缓冲区,开始下一个文件处理
			tempBuf.release();
			tempBuf = Unpooled.buffer();
		}
		
		//ctx.writeAndFlush(Unpooled.copiedBuffer("收到文件,谢谢!", CharsetUtil.UTF_8));
	}
	
	
	/**
	 * 读取一包数据,判断是否能够读取成功
	 * 如果读取失败,说明是半包,会将该半包加入缓存中
	 * @param buf
	 * @return 读取成功返回true,否则返回false
	 */
	private boolean isToEnd(ByteBuf buf) {
		
		//复制一份进行处理判断,不改变原来的buf
		ByteBuf copyBuf = buf.copy();
		
		/*
		 * 判断是否达到文件流末尾
		 */
		int totalSize = copyBuf.readInt();
		logger.info("包总大小:{}", totalSize);
		int restLength = copyBuf.readableBytes();
		logger.info("包剩余数据大小:{}", restLength);
		if (restLength + 4 == totalSize) {
			logger.info("到达文件流末尾");
			return true;
		}
		logger.info("还没有到达文件流末尾");
		return false;
	}
	
	/**
	 * 读取文件数据
	 */
	public void readFileData(ByteBuf buf) {
		
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
		
		//数据区
		byte[] bytes = new byte[dataLengh];
		buf.readBytes(bytes);
		try {
			fileWriter.writeFile(id, bytes);
			logger.info("成功将文件数据写入!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
