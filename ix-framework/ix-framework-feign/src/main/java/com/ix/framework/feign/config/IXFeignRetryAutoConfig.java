/*
 * COPYRIGHT (C) 2023 Art AUTHORS(fxzcloud@gmail.com). ALL RIGHTS RESERVED.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ix.framework.feign.config;

import com.ix.framework.feign.aspect.IXFeignRetryAspect;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * @author Zeng IX
 * @email zeng_kiku@qq.com
 * @Description: 重试配置
 */
@AutoConfiguration
public class IXFeignRetryAutoConfig {

	@Bean
	public IXFeignRetryAspect feignRetryAspect() {
		return new IXFeignRetryAspect();
	}

}
