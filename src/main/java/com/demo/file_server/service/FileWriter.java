package com.demo.file_server.service;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 处理写文件业务
 * @author tyjw
 *
 */
@Component
public class FileWriter {
	
	
	private static Map<Long, OutputStream> osMap = new ConcurrentHashMap<>();
	
	@Autowired
	private FileChecker fileChecker;
	
	private static Logger logger = LoggerFactory.getLogger(FileWriter.class);
	
	
	/**
	 * 写入文件数据
	 * @param fileId 文件唯一Id
	 * @param bytes 文件内容
	 * @param fileSize 文件总字节数
	 * @throws IOException
	 */
	public void writeData(long fileId, byte[] bytes, int fileSize) throws IOException {
		Path path = Paths.get("E:/file_server/" + fileId + ".jpg");
		OutputStream os = osMap.get(fileId);
		if (os == null) {
			os = Files.newOutputStream(path, StandardOpenOption.CREATE);
			osMap.put(fileId, os);
		}
		os.write(bytes);
		fileChecker.setSize(fileId, bytes.length);
		
		/*
		 * 显示上传进度
		 * 处理文件写完的情况
		 */
		int saveSize = fileChecker.getSize(fileId);
		logger.info("文件上传进度:{}%", saveSize * 100.0/fileSize);
		
		if (saveSize == fileSize) {
			os.close();
			osMap.remove(fileId);
		}
	}
	
	/**
	 * 一次性将文件全部写入
	 * @param fileId
	 * @param bytes
	 * @throws IOException
	 */
	public void writeFile(long fileId, byte[] bytes) throws IOException {
		Path path = Paths.get("E:/file_server/" + fileId + ".jpg");
		OutputStream os = Files.newOutputStream(path, StandardOpenOption.CREATE);
		os.write(bytes);
		os.close();
	}
}
