package com.demo.file_server.context;

import java.io.File;
import java.nio.ByteOrder;

import com.ceph.rados.IoCTX;
import com.ceph.rados.Rados;
import com.ceph.rados.exceptions.RadosException;
import com.demo.file_server.server.hanlder.in.TcpFrameDecoder;


public class AppBeans {
	
	private static final String CEPH_USER_NAME = "admin";
	
	private static final String CEPH_CONF_FILE_PATH = "/etc/ceph/ceph.conf";
	
	public static final String CEPH_POOL_NAME = "demo_pool";
	
	public static Rados rados;
	
	public static IoCTX io;
	
	static {
		try {
			Rados cluster = new Rados(CEPH_USER_NAME);
			File f = new File(CEPH_CONF_FILE_PATH);
			cluster.confReadFile(f);
			cluster.connect();
			rados = cluster;
			io = rados.ioCtxCreate(CEPH_POOL_NAME);
		} catch (RadosException e) {
			e.printStackTrace();
		}
	}
	
	public static TcpFrameDecoder tcpFrameDecoder() {
		/*
		 * ByteOrder.BIG_ENDIAN 表示字节序使用大端
		 * 5 lengthFieldOffset  长度字段开始位置的索引
		 * 4 lengthFieldLength 	长度字段字节长度
		 * 0 lengthAdjustment 计算长度的位置调整量
		 * 0 initialBytesToStrip 表示除去字段为0,保留完整原始字段
		 */
		return new TcpFrameDecoder(ByteOrder.BIG_ENDIAN, Integer.MAX_VALUE, 5, 4, 0, 0, true);
	}
	
	public static IoCTX getIO() {
		return io;
	}
	
}
