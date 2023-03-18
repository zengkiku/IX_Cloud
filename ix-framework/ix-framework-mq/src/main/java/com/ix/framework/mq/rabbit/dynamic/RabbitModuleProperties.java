package com.ix.framework.mq.rabbit.dynamic;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author Zeng IX
 * @email zeng_kiku@qq.com
 * @Description: 配置类
 */
@ConfigurationProperties(prefix = "spring.rabbitmq")
@Data
public class RabbitModuleProperties {

	private List<RabbitModuleInfo> modules;

}
