package com.ix.framework.redis.service.configure;

import io.lettuce.core.resource.ClientResources;
import io.lettuce.core.resource.NettyCustomizer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.Data;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;



/**
 * @author Zeng IX
 * @email zeng_kiku@qq.com
 * @Description: Redis 配置类
 */
@Data
@EnableCaching
@AutoConfiguration
@AutoConfigureBefore(RedisAutoConfiguration.class)
@ConfigurationProperties(prefix = "spring.redis")
public class RedisTemplateConfiguration {

	private String host;

	private Integer port;

	private Integer database;

	private Duration timeout;


	@Bean
	@Primary
	@ConfigurationProperties(prefix = "spring.redis.lettuce.pool")
	public GenericObjectPoolConfig<LettucePoolingClientConfiguration> genericObjectPoolConfig() {
		return new GenericObjectPoolConfig<>();
	}

	@Bean
	@Primary
	@ConditionalOnClass(RedisOperations.class)
	public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory factory) {

		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

		redisTemplate.setConnectionFactory(factory);

		redisTemplate.setKeySerializer(RedisSerializer.string());
		redisTemplate.setHashKeySerializer(RedisSerializer.string());
		redisTemplate.setValueSerializer(RedisSerializer.string());
		redisTemplate.setHashValueSerializer(RedisSerializer.string());
		return redisTemplate;
	}

	/**
	 * 加入心跳检测防止连接超时
	 * @return
	 */
	@Bean
	public ClientResources clientResources(){
		NettyCustomizer nettyCustomizer = new NettyCustomizer() {
			@Override
			public void afterChannelInitialized(Channel channel) {
				channel.pipeline().addLast(
						new IdleStateHandler(0, 0, 60));
				channel.pipeline().addLast(new ChannelDuplexHandler() {
					@Override
					public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
						if (evt instanceof IdleStateEvent) {
							ctx.disconnect();
						}
					}
				});
			}
			@Override
			public void afterBootstrapInitialized(Bootstrap bootstrap) {
			}
		};

		return ClientResources.builder().nettyCustomizer(nettyCustomizer ).build();
	}

	@Bean
	@Primary
	public LettuceConnectionFactory lettuceConnectionFactory(ClientResources clientResources,
															 GenericObjectPoolConfig<LettucePoolingClientConfiguration> genericObjectPoolConfig) {

		LettuceClientConfiguration clientConfig = LettucePoolingClientConfiguration.builder()
				.clientResources(clientResources)
				.commandTimeout(timeout)
				.poolConfig(genericObjectPoolConfig)
				.build();

			RedisStandaloneConfiguration redisConfiguration = new RedisStandaloneConfiguration(host, port);
			redisConfiguration.setDatabase(database);

			LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisConfiguration, clientConfig);
			lettuceConnectionFactory.afterPropertiesSet();
			lettuceConnectionFactory.setValidateConnection(false);

			return lettuceConnectionFactory;
	}


	@Bean
	public HashOperations<String, String, Object> hashOperations(RedisTemplate<String, Object> redisTemplate) {
		return redisTemplate.opsForHash();
	}

	@Bean
	public ValueOperations<String, String> valueOperations(RedisTemplate<String, String> redisTemplate) {
		return redisTemplate.opsForValue();
	}

	@Bean
	public ListOperations<String, Object> listOperations(RedisTemplate<String, Object> redisTemplate) {
		return redisTemplate.opsForList();
	}

	@Bean
	public SetOperations<String, Object> setOperations(RedisTemplate<String, Object> redisTemplate) {
		return redisTemplate.opsForSet();
	}

	@Bean
	public ZSetOperations<String, Object> zSetOperations(RedisTemplate<String, Object> redisTemplate) {
		return redisTemplate.opsForZSet();
	}

}