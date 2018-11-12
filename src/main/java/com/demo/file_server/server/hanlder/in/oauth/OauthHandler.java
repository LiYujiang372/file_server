package com.demo.file_server.server.hanlder.in.oauth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.demo.file_server.context.AppBeans;
import com.demo.file_server.server.hanlder.in.file.FileDataHandler;
import com.demo.file_server.server.hanlder.in.file.FileFrameDecoder;

import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

@Sharable
@Component
public class OauthHandler extends ChannelInboundHandlerAdapter {
	
	private static Logger logger = LoggerFactory.getLogger(OauthHandler.class);
	
	@Autowired
	private FileFrameDecoder fileFrameDecoder;
	
	@Autowired
	private FileDataHandler fileDataHandler;
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("OauthHandler.channelRead()");
		ByteBuf buf = (ByteBuf) msg;
		ByteBuf sendBuf = buf.copy();
		boolean suc = false;
		
		byte head = buf.readByte();
		if (head == 0x12 && buf.readableBytes() == 8) {
			int uid = buf.readInt();
			int tid = buf.readInt();
			//uid和tid的判断逻辑
			if (uid == 1 && tid == 2) {
				suc = true;
				logger.info("鉴权成功, 可以建立连接");
				/*
				 * 回复鉴权成功数据包
				 */
				ChannelFuture future = ctx.writeAndFlush(sendBuf);
				future.addListener(new ChannelFutureListener() {
					@Override
					public void operationComplete(ChannelFuture future) throws Exception {
						if (future.isSuccess()) {
							logger.info("鉴权消息报回复成功");
							/*
							 * 动态更新handler,切换协议
							 */
							ctx.pipeline().remove(OauthHandler.class);
							ctx.pipeline().addLast(AppBeans.tcpFrameDecoder());
							ctx.pipeline().addLast(fileFrameDecoder);
							ctx.pipeline().addLast(fileDataHandler);
						}else {
							logger.error("异常信息:[{}]", future.cause().getMessage(), future.cause());
						}
					}
				});
			}
		}
		/*
		 * 鉴权失败,主动关闭连接
		 */
		if (!suc) {
			ChannelFuture future = ctx.close();
			future.addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					if (future.isDone()) {
						logger.info("主动关闭不合法的连接");
					}
				}
			});
		}
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		System.out.println("OauthHandler.channelReadComplete()");
		super.channelReadComplete(ctx);
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		System.out.println("OauthHandler.channelRegistered()");
		super.channelRegistered(ctx);
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		System.out.println("OauthHandler.channelUnregistered()");
		super.channelUnregistered(ctx);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("OauthHandler.channelActive()");
		super.channelActive(ctx);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("OauthHandler.channelInactive()");
		super.channelInactive(ctx);
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		System.out.println("OauthHandler.userEventTriggered()");
		super.userEventTriggered(ctx, evt);
	}

	@Override
	public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
		System.out.println("OauthHandler.channelWritabilityChanged()");
		super.channelWritabilityChanged(ctx);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		System.out.println("OauthHandler.exceptionCaught()");
		logger.error("异常信息:{}", cause.getMessage(), cause);
	}

}
