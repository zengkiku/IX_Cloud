package com.ix.auth;

import com.ix.framework.core.annotation.EnableIXFeignClients;
import com.ix.framework.core.annotation.EnableIXConfig;
import com.ix.framework.swagger.annotation.EnableIXSwagger2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Zeng IX
 * @email zeng_kiku@qq.com
 * @Description: 认证中心启动器
 */
@EnableIXSwagger2
@EnableIXConfig
@EnableIXFeignClients
@SpringBootApplication
public class AuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthApplication.class, args);
	}

}
