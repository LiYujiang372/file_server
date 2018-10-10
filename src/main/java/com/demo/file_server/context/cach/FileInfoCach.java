package com.demo.file_server.context.cach;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.demo.file_server.pojo.FileInfo;

@Component
public class FileInfoCach {
	
	public static Map<Long, FileInfo> infoMap = new ConcurrentHashMap<>();
	
}
