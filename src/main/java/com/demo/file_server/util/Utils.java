package com.demo.file_server.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Utils {
	
	private static Logger logger = LoggerFactory.getLogger(Utils.class);
	
	/**
	 * 线程睡眠
	 * @param s 睡眠时间
	 */
	public static void sleep(int ms) {
		try {
			logger.info("{}---正在睡眠", Thread.currentThread().getName());
			Thread.sleep(ms);
		} catch (InterruptedException e) {}
	}

}
