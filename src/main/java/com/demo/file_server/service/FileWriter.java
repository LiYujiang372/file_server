package com.demo.file_server.service;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.springframework.stereotype.Component;

/**
 * 处理写文件业务
 * @author tyjw
 *
 */
@Component
public class FileWriter {
	
	
	/**
	 * 写入文件数据
	 * @param fileId 文件唯一Id
	 * @param bytes 文件内容
	 * @throws IOException
	 */
	public void writeFileData(long fileId, byte[] bytes) throws IOException {
		Path path = Paths.get("E:/file_server/" + fileId + ".jpg");
		OutputStream os = Files.newOutputStream(path, StandardOpenOption.CREATE);
		os.write(bytes);
		os.close();
	}

}
