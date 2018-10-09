package com.demo.file_server.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

/**
 * 文件完整性检查
 * @author liyujiang
 *
 */
@Component
public class FileChecker {
	
	private static Map<Long, Integer> sizeMap = new ConcurrentHashMap<>();
	
	/**
	 * 记录已经保存的文件的大小
	 * @param fileid 文件Id
	 * @param tempSize 当前包文件数据的大小
	 * @return
	 */
	public void setSize(long fileid, int tempSize) {
		Integer oldSize = sizeMap.get(fileid);
		if (oldSize == null) {
			oldSize = 0;
		}
		int newSize = oldSize + tempSize;
		sizeMap.put(fileid, newSize);
	}
	
	/**
	 * 获取已经保存的文件的总大小
	 * @param fileid
	 * @return
	 */
	public int getSize(long fileid) {
		return sizeMap.get(fileid);
	}

}
