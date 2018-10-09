package com.demo.file_server.context;

import java.nio.ByteOrder;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.demo.file_server.hanlder.FirstHandler;

@Component
public class AppBeans {
	
	
	@Bean
	public FirstHandler firstHandler() {
		/*
		 * ByteOrder.BIG_ENDIAN 表示字节序使用大端
		 * 0 4   表示长度字段位置 0,4
		 * -4 表示开始计算长度的位置 -4 表示最开始位置
		 * 0 表示除去字段为0,保留完整原始字段
		 */
		return new FirstHandler(ByteOrder.BIG_ENDIAN, Integer.MAX_VALUE, 0, 4, -4, 0, true);
	}

}
