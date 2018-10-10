package com.demo.file_server.server.hanlder.in;

import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.demo.file_server.pojo.FileInfo;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelHandler.Sharable;

@Sharable
@Component
public class FileSaveHandler extends ChannelInboundHandlerAdapter {
	
	private static Logger logger = LoggerFactory.getLogger(FileSaveHandler.class);

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		FileInfo info = (FileInfo) msg;
		byte[] bytes = info.getFrameBytes();
		OutputStream os = info.getOs();
		os.write(bytes);
		info.setSaveSize(bytes.length + info.getSaveSize());
		logger.info("文件上传进度:{}%", info.getProcess());
		
		ctx.write(info);
	}
	
}
