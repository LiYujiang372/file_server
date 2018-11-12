package com.demo.file_server.dao.entity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.ceph.rados.IoCTX;
import com.ceph.rados.exceptions.RadosException;
import com.ceph.rados.jna.RadosObjectInfo;
import com.demo.file_server.context.AppBeans;



/**
 * 文件数据帧
 * @author tyjw
 *
 */
@Entity
@Table(name = "wotianyu_file_storage")
public class FileStorage {
	
	/**数据库映射字段**/
	//主键
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	//项目id
	private int project_id;
	
	//任务id
	private int task_id;
	
	//文件创建时间
	private Date create_date;
	
	//文件名
	private String file_name;
	
	//文件类型
	private int file_type;
	
	//文件总大小
	private int file_size;
	
	//已经存储的文件大小
	private int save_size = 0;
	
	//文件状态  0:存储未完成   1:文件校验失败 | 文件存储失败   2:文件存储成功,校验成功
	private int finish = 0;
	
	//保存类型,这里全部是用户上传
	private int save_type = 1;
	
	//文件访问路劲
	private String file_path;
	
	//图片缩略图访问路径
	private String small_path;
	
	
	/**自定义业务逻辑字段**/
	
	//文件本地id
	@Transient
	private int localId;
	
	//临时文件内容
	@Transient
	private byte[] frameBytes;
	
	//文件对应的输出流
	@Transient
	private IoCTX io = AppBeans.getIO();
	
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
		//初始化Md5校验器
		try {
			md5 = MessageDigest.getInstance("md5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 写入数据
	 */
	public void wirteBytes() {
		try {
			io.write(id + "", frameBytes, this.save_size);
			setSaveSize(frameBytes.length + this.save_size);
		} catch (RadosException | IllegalArgumentException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 数据存储成功后调用
	 * 更新已经存储大小的同时更新进度和状态
	 * @throws RadosException 
	 */
	public void setSaveSize(int saveSize) throws RadosException {
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
			//文件存储完整性校验
			RadosObjectInfo objectInfo = io.stat(id + "");
			if (objectInfo.getSize() == file_size) {
				//MD5校验
				finish = checkMD5() ? 2 : 1;
			}else {
				finish = 1;
			}
		}
	}
	
	public byte[] getFrameBytes() {
		return frameBytes;
	}

	public void setFrameBytes(byte[] frameBytes) {
		this.frameBytes = frameBytes;
	}

	public IoCTX getIo() {
		return io;
	}

	public void setIo(IoCTX io) {
		this.io = io;
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
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getProject_id() {
		return project_id;
	}

	public void setProject_id(int project_id) {
		this.project_id = project_id;
	}

	public int getTask_id() {
		return task_id;
	}

	public void setTask_id(int task_id) {
		this.task_id = task_id;
	}

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}

	public int getLocalId() {
		return localId;
	}

	public void setLocalId(int localId) {
		this.localId = localId;
	}
	
	public String getFile_path() {
		return file_path;
	}

	public void setFile_path(String file_path) {
		this.file_path = file_path;
	}

	public String getSmall_path() {
		return small_path;
	}

	public void setSmall_path(String small_path) {
		this.small_path = small_path;
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

	@Override
	public String toString() {
		return "FileStorage [id=" + id + ", project_id=" + project_id + ", task_id=" + task_id + ", create_date="
				+ create_date + ", file_name=" + file_name + ", file_type=" + file_type + ", file_size=" + file_size
				+ ", save_size=" + save_size + ", finish=" + finish + ", save_type=" + save_type + ", localId="
				+ localId + "]";
	}
	
}
