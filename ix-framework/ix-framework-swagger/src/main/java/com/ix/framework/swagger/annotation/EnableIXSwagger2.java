package com.ix.framework.swagger.annotation;

import com.ix.framework.swagger.config.SwaggerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.lang.annotation.*;

/**
 * @author Zeng IX
 * @email zeng_kiku@qq.com
 * @Description: 开启 swagger
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@EnableConfigurationProperties(SwaggerProperties.class)
// @Import({ SwaggerAutoConfiguration.class })
public @interface EnableIXSwagger2 {

}
