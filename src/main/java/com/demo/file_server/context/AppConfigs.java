package com.demo.file_server.context;

import java.io.File;
import java.nio.ByteOrder;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.ceph.rados.IoCTX;
import com.ceph.rados.Rados;
import com.ceph.rados.exceptions.RadosException;
import com.demo.file_server.server.hanlder.in.TcpFrameDecoder;

@SpringBootApplication
public class AppConfigs {
	
	public static final int CORE_COUNT = Runtime.getRuntime().availableProcessors();
	
	private static final String CEPH_USER_NAME = "admin";
	
	public static final String CEPH_CONF_FILE_PATH = "/etc/ceph/ceph.conf";
	
	public static final String CEPH_POOL_NAME = "demo_pool";
	
	@Bean
	TcpFrameDecoder tcpFrameDecoder() {
		/*
		 * ByteOrder.BIG_ENDIAN 表示字节序使用大端
		 * 9 lengthFieldOffset  长度字段开始位置的索引
		 * 4 lengthFieldLength 	长度字段字节长度
		 * 0 lengthAdjustment 计算长度的位置调整量
		 * 0 initialBytesToStrip 表示除去字段为0,保留完整原始字段
		 */
		return new TcpFrameDecoder(ByteOrder.BIG_ENDIAN, Integer.MAX_VALUE, 9, 4, 0, 0, true);
	}
	
	/**
	 * 获取ceph集群中 图片存储池(pool)的io
	 * @return
	 * @throws RadosException
	 */
	@Bean
	IoCTX getPoolIO() throws RadosException {
		Rados cluster = new Rados(CEPH_USER_NAME);
		File f = new File(CEPH_CONF_FILE_PATH);
		cluster.confReadFile(f);
		cluster.connect();
		IoCTX io = cluster.ioCtxCreate(CEPH_POOL_NAME);
		return io;
	}
	
}
