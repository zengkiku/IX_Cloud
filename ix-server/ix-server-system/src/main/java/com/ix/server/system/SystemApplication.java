package com.ix.server.system;

import com.ix.framework.core.annotation.EnableIXConfig;
import com.ix.framework.core.annotation.EnableIXFeignClients;
import com.ix.framework.core.annotation.EnableIXConfig;
import com.ix.framework.security.annotation.EnableIXResourceServer;
import com.ix.framework.swagger.annotation.EnableIXSwagger2;
import com.ix.framework.swagger.annotation.EnableIXSwagger2;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Zeng IX
 * @email zeng_kiku@qq.com
 * @Description: 启动程序
 */
@EnableIXSwagger2
@EnableIXResourceServer
@MapperScan("com.ix.**.mapper")
@EnableIXConfig
@EnableIXFeignClients
@SpringBootApplication
public class SystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(SystemApplication.class, args);
	}

}
