package com.demo.file_server.context.cach;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.demo.file_server.dao.entity.FileStorage;

@Component
public class FileInfoCach {
	
	public static Map<Long, FileStorage> infoMap = new ConcurrentHashMap<>();
	
}
