package com.ix.server.dfs;

import com.ix.framework.core.annotation.EnableIXFeignClients;
import com.ix.framework.core.annotation.EnableIXConfig;
import com.ix.framework.security.annotation.EnableIXResourceServer;
import com.ix.framework.swagger.annotation.EnableIXSwagger2;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author Zeng IX
 * @email zeng_kiku@qq.com
 * @Description: 启动程序
 * @EnableFeignClients 开启服务扫描
 */
@EnableIXSwagger2
@EnableIXResourceServer
@MapperScan("com.ix.**.mapper")
@EnableIXConfig
@EnableIXFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class DFSApplication {

	public static void main(String[] args) {
		SpringApplication.run(DFSApplication.class, args);
	}

}
