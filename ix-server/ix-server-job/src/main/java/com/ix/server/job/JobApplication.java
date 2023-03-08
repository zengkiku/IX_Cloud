package com.ix.server.job;

import com.ix.framework.core.annotation.EnableIXFeignClients;
import com.ix.framework.core.annotation.EnableixConfig;
import com.ix.framework.security.annotation.EnableIXResourceServer;
import com.ix.framework.swagger.annotation.EnableixSwagger2;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Zeng IX
 * @email zeng_kiku@qq.com
 * @Description: 启动程序
 * @EnableFeignClients 开启服务扫描
 */
@EnableixSwagger2
@EnableIXResourceServer
@MapperScan("com.ix.**.mapper")
@EnableixConfig
@EnableIXFeignClients
@SpringBootApplication
public class JobApplication {

	public static void main(String[] args) {
		SpringApplication.run(JobApplication.class, args);
	}

}
