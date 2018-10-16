package com.demo.file_server.dao.entity;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;


/**
 * 文件数据帧
 * @author tyjw
 *
 */
@Entity
@Table(name = "file_storage")
public class FileStorage {
	
	/**数据库映射字段**/
	
	//主键
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	//项目id
	private long project_id;
	
	//任务id
	private long task_id;
	
	//文件id
	private long file_id;
	
	//文件名
	private String file_name;
	
	//文件类型
	private int file_type;
	
	//文件总大小
	private int file_size;
	
	//已经存储的文件大小
	private int save_size = 0;
	
	//文件状态  0:存储未完成   1:文件校验失败  2:文件校验成功
	private int finish = 0;
	
	//保存类型,这里全部是用户上传
	private int save_type = 1;
	
	
	/**自定义业务逻辑字段**/
	
	//临时文件内容
	@Transient
	private byte[] frameBytes;
	
	//文件对应的输出流
	@Transient
	private OutputStream os;
	
	//MD5校验码
	@Transient
	private byte[] oldMD5;
	
	//服务端计算的校验码
	@Transient
	private byte[] newMD5;
	
	//MD5校验器
	@Transient
	private MessageDigest md5;
	
	/**
	 * 初始化
	 * 创建输出流,该流在文件写完时关闭
	 * 创建MD5校验器
	 */
	public void init() {
		try {
			//建立传输流
			Path path = Paths.get("E:/file_server/" + file_name);
			this.os = Files.newOutputStream(path, StandardOpenOption.CREATE);
			//初始化Md5校验器
			md5 = MessageDigest.getInstance("md5");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 写入数据
	 */
	public void wirteBytes() {
		try {
			os.write(frameBytes);
			setSaveSize(frameBytes.length + getSave_size());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 数据存储成功后调用
	 * 更新已经存储大小的同时更新进度和状态
	 */
	public void setSaveSize(int saveSize) {
		//更新saveSize
		this.save_size = saveSize;
		
		//更新md5
		updateMD5();
		
		/*
		 * 判断文件流末尾,如果写到文件末尾
		 * 1.对文件进行md5校验, 根据校验结果更新状态值 status
		 * 2.对应的关闭流
		 */
		if (saveSize == file_size) {
			
			//MD5校验
			finish = checkMD5() ? 2 : 1;
			
			//关闭流
			close();
		}
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public byte[] getFrameBytes() {
		return frameBytes;
	}

	public void setFrameBytes(byte[] frameBytes) {
		this.frameBytes = frameBytes;
	}

	public OutputStream getOs() {
		return os;
	}

	public void setOs(OutputStream os) {
		this.os = os;
	}

	public byte[] getOldMD5() {
		return oldMD5;
	}

	public void setOldMD5(byte[] oldMD5) {
		this.oldMD5 = oldMD5;
	}

	public byte[] getNewMD5() {
		return newMD5;
	}

	public void setNewMD5(byte[] newMD5) {
		this.newMD5 = newMD5;
	}
	
	public long getFile_id() {
		return file_id;
	}

	public void setFile_id(long file_id) {
		this.file_id = file_id;
	}

	public String getFile_name() {
		return file_name;
	}

	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}

	public int getFile_type() {
		return file_type;
	}

	public void setFile_type(int file_type) {
		this.file_type = file_type;
	}

	public int getFile_size() {
		return file_size;
	}

	public void setFile_size(int file_size) {
		this.file_size = file_size;
	}

	public int getSave_size() {
		return save_size;
	}

	public void setSave_size(int save_size) {
		this.save_size = save_size;
	}

	public int getFinish() {
		return finish;
	}

	public void setFinish(int finish) {
		this.finish = finish;
	}

	public int getSave_type() {
		return save_type;
	}

	public void setSave_type(int save_type) {
		this.save_type = save_type;
	}

	public MessageDigest getMd5() {
		return md5;
	}

	public void setMd5(MessageDigest md5) {
		this.md5 = md5;
	}
	
	public long getProject_id() {
		return project_id;
	}

	public void setProject_id(long project_id) {
		this.project_id = project_id;
	}

	public long getTask_id() {
		return task_id;
	}

	public void setTask_id(long task_id) {
		this.task_id = task_id;
	}

	/**
	 * MD5校验
	 */
	private boolean checkMD5() {
		newMD5 = md5.digest();
		return Arrays.equals(newMD5, oldMD5);
	}
	
	/**
	 * 更新MD5
	 */
	private void updateMD5() {
		md5.update(frameBytes);
	}
	
	private void close() {
		try {
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 测试
	 * @throws NoSuchAlgorithmException 
	 */
	public static void main(String[] args) throws NoSuchAlgorithmException {
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		String str1 = "dfdsfdsfsdf";
		String str2 = "9diojf34e329";
		String str = str1 + str2;
		
		md5.update(str.getBytes());
		byte[] bytes1 = md5.digest();
		for (byte b : bytes1) {
			System.err.print(b);
		}
		System.err.println("\n------------------");
		md5.reset();
		md5.update(str1.getBytes());
		byte[] bytes2 = md5.digest();
		for (byte b : bytes2) {
			System.err.print(b);
		}
		System.err.println("\n------------------");
		md5.update(str2.getBytes());
		byte[] bytes3 = md5.digest();
		for (byte b : bytes3) {
			System.err.print(b);
		}
		
		System.err.println("\n" + (Arrays.equals(bytes1, bytes3) ? "true" : "false"));
	}

	@Override
	public String toString() {
		return "FileStorage [id=" + id + ", project_id=" + project_id + ", task_id=" + task_id + ", file_id=" + file_id
				+ ", file_name=" + file_name + ", file_type=" + file_type + ", file_size=" + file_size + ", save_size="
				+ save_size + ", finish=" + finish + ", save_type=" + save_type + "]";
	}
	
}
