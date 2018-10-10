package com.demo.file_server.pojo;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * 文件数据帧
 * @author tyjw
 *
 */
public class FileInfo {
	
	//文件id
	private long fileId;
	
	//文件总大小
	private int fileSize;
	
	//已经存储的文件大小
	private int saveSize;
	
	//已经存储的百分比
	private byte process;
	
	//文件状态 0:存储未完成   1:存储已经完成
	private int status = 0;
	
	//临时文件内容
	private byte[] frameBytes;
	
	//文件对应的输出流
	private OutputStream os;
	
	/**
	 * 穿件输出流,该流在文件写完时关闭
	 */
	public void initOs() {
		try {
			Path path = Paths.get("E:/file_server/" + fileId + ".jpg");
			this.os = Files.newOutputStream(path, StandardOpenOption.CREATE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public long getFileId() {
		return fileId;
	}

	public void setFileId(long fileId) {
		this.fileId = fileId;
	}

	public int getFileSize() {
		return fileSize;
	}

	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}

	public int getSaveSize() {
		return saveSize;
	}

	/**
	 * 更新已经存储大小的同时更新进度和状态
	 */
	public void setSaveSize(int saveSize) {
		this.saveSize = saveSize;
		setProcess((byte) (saveSize * 100.0/fileSize));
		if (saveSize == fileSize) {
			setStatus(1);
			try {
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public byte[] getFrameBytes() {
		return frameBytes;
	}

	public void setFrameBytes(byte[] frameBytes) {
		this.frameBytes = frameBytes;
	}

	public byte getProcess() {
		return process;
	}

	public void setProcess(byte process) {
		this.process = process;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public OutputStream getOs() {
		return os;
	}

	public void setOs(OutputStream os) {
		this.os = os;
	}

}
