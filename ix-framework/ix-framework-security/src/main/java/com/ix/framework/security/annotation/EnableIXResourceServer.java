package com.ix.framework.security.annotation;

import com.ix.framework.security.config.IXResourceServerAutoConfiguration;
import com.ix.framework.security.config.IXResourceServerConfiguration;
import com.ix.framework.security.feign.FeignConfig;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import java.lang.annotation.*;

/**
 * @author Zeng IX
 * @email zeng_kiku@qq.com
 * @Description: 资源服务注解
 */
@Documented
@Inherited
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@EnableMethodSecurity(prePostEnabled = true)
@Import({ IXResourceServerAutoConfiguration.class, IXResourceServerConfiguration.class, FeignConfig.class })
public @interface EnableIXResourceServer {

}