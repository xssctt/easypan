package com.example.easypan.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

//@EnableCaching
@Configuration
public class RedisConfig extends CachingConfigurerSupport {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        RedisSerializer<String> redisSerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        template.setConnectionFactory(factory);
//key序列化方式
        template.setKeySerializer(redisSerializer);
//value序列化
        template.setValueSerializer(jackson2JsonRedisSerializer);
//value hashmap序列化
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        return template;
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        RedisSerializer<String> redisSerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
//解决查询缓存转换异常的问题
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
// 配置序列化（解决乱码的问题）,过期时间600秒
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(600))
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer))
                .disableCachingNullValues();
        RedisCacheManager cacheManager = RedisCacheManager.builder(factory)
                .cacheDefaults(config)
                .build();
        return cacheManager;
    }


    /**
     * lettuce客户端配置
     *这段代码用来配置Spring Boot使用Lettuce连接Redis时的一些连接池参数。
     *
     * 具体来说:
     *
     * 1. 使用@Bean注解定义了一个Bean:lettuceClientConfigurationBuilderCustomizer。
     *
     * 2. 这个Bean的类型是LettuceClientConfigurationBuilderCustomizer,它可以用来自定义Lettuce的连接池配置。
     *
     * 3. 在该Bean的方法实现中,通过clientConfigurationBuilder对连接池进行了配置:
     *
     * - 调用clientOptions方法配置了客户端选项
     *
     * - 在clientOptions里又调用socketOptions方法配置了Socket选项
     *
     * - 在socketOptions里通过keepAlive方法配置了保持连接相关参数
     *
     * 4. 保持连接的具体参数包括:
     *
     * - enable - 是否开启保持连接,设为true
     *
     * - interval - 发送保持连接信号的时间间隔,10秒
     *
     * - idle - 连接空闲时间,30秒
     *
     * - count - 重新连接前发送保持连接信号的次数,3次
     *
     * 5. 这样就配置了Lettuce的连接池参数,主要是让连接池的连接启用保持连接功能,避免不活跃的连接被断开。
     *
     * 6. 后续通过Spring自动注入RedisTemplate时,这些连接池参数就会生效。
     *
     * 所以这段代码实现了自定义配置Lettuce连接池的作用,目的是保持Redis连接不被断开。
     * @return LettuceClientConfigurationBuilderCustomizer
     */
//    @Bean
//    public LettuceClientConfigurationBuilderCustomizer lettuceClientConfigurationBuilderCustomizer() {
//        return clientConfigurationBuilder -> {
//            clientConfigurationBuilder.clientOptions(ClientOptions.builder()
//                    .socketOptions(SocketOptions.builder()
//                            .keepAlive(SocketOptions.KeepAliveOptions.builder()
//                                    .enable(true)
//                                    .interval(Duration.ofSeconds(10))
//                                    .idle(Duration.ofSeconds(30))
//                                    .count(3)
//                                    .build())
//                            .build())
//                    .build()
//            ).build();
//        };
//    }






//    //CacheManager cacheManager()
//    //用来配置和返回CacheManager的Bean,CacheManager负责管理各种不同的缓存组件
//    @Bean
//    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
//        return RedisCacheManager.builder(redisConnectionFactory)
//                .cacheDefaults(defaultCacheConfig())
//                .transactionAware()
//                .build();
//    }
    //使用RedisCacheConfiguration的默认配置defaultCacheConfig()作为基础。
//通过prefixCacheNameWith方法为所有缓存key添加前缀,这里用的是常量REDIS_KEY_PREFIX。
//通过entryTtl方法设置缓存的默认过期时间为300秒。
//通过disableCachingNullValues方法禁止缓存null值。
//这样默认的缓存配置就设置好了:
//
//缓存key统一加前缀。
//默认过期时间300秒。
//不缓存null值
//    private RedisCacheConfiguration defaultCacheConfig() {
//        return RedisCacheConfiguration.defaultCacheConfig()
//                .prefixCacheNameWith(Constants.REDIS_KEY_PREFIX)
//                .entryTtl(Duration.ofSeconds(300))
//                .disableCachingNullValues();
//    }

    //用于生成缓存的key。
    @Bean(name = "cacheKeyGenerator")
    public KeyGenerator cacheKeyGenerator() {
        return (target, method, params) -> {
            StringBuilder sb = new StringBuilder();
            sb.append(target.getClass().getName());
            sb.append(method.getName());
            for (Object obj : params) {
                sb.append(obj.toString());
            }
            return sb.toString();
        };
    }



}

/*

//@EnableCaching注解启用缓存
//Spring的CachingConfigurer接口,用于配置Spring缓存的一些设置。
//实现CachingConfigurer接口需要实现以下方法:
//
//CacheManager cacheManager()
//用来配置和返回CacheManager的Bean,CacheManager负责管理各种不同的缓存组件。
//
//KeyGenerator keyGenerator()
//用来配置缓存Key的生成器,该生成器将会根据规则生成缓存的key。
//
//CacheResolver cacheResolver()
//用来配置CacheResolver的Bean,CacheResolver用于根据缓存的名称解析出对应的Cache实例。
//
//Config cacheConfig()
//用来设置一个全局的缓存配置。
@EnableCaching
@Configuration
public class CacheConfig implements CachingConfigurer {

    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(keySerializer());
        redisTemplate.setValueSerializer(valueSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
//定义了一个RedisTemplate bean,用于执行Redis操作,配置了key和value的序列化方式。

    private RedisSerializer<String> keySerializer() {
        return new StringRedisSerializer();
    }

    private RedisSerializer<Object> valueSerializer() {
        return new GenericFastJsonRedisSerializer();
    }



    /**
     * lettuce客户端配置
     *这段代码用来配置Spring Boot使用Lettuce连接Redis时的一些连接池参数。
     *
     * 具体来说:
     *
     * 1. 使用@Bean注解定义了一个Bean:lettuceClientConfigurationBuilderCustomizer。
     *
     * 2. 这个Bean的类型是LettuceClientConfigurationBuilderCustomizer,它可以用来自定义Lettuce的连接池配置。
     *
     * 3. 在该Bean的方法实现中,通过clientConfigurationBuilder对连接池进行了配置:
     *
     * - 调用clientOptions方法配置了客户端选项
     *
     * - 在clientOptions里又调用socketOptions方法配置了Socket选项
     *
     * - 在socketOptions里通过keepAlive方法配置了保持连接相关参数
     *
     * 4. 保持连接的具体参数包括:
     *
     * - enable - 是否开启保持连接,设为true
     *
     * - interval - 发送保持连接信号的时间间隔,10秒
     *
     * - idle - 连接空闲时间,30秒
     *
     * - count - 重新连接前发送保持连接信号的次数,3次
     *
     * 5. 这样就配置了Lettuce的连接池参数,主要是让连接池的连接启用保持连接功能,避免不活跃的连接被断开。
     *
     * 6. 后续通过Spring自动注入RedisTemplate时,这些连接池参数就会生效。
     *
     * 所以这段代码实现了自定义配置Lettuce连接池的作用,目的是保持Redis连接不被断开。
     * @return LettuceClientConfigurationBuilderCustomizer
  */
    /*
@Bean
public LettuceClientConfigurationBuilderCustomizer lettuceClientConfigurationBuilderCustomizer() {
    return clientConfigurationBuilder -> {
        clientConfigurationBuilder.clientOptions(ClientOptions.builder()
                .socketOptions(SocketOptions.builder()
                        .keepAlive(SocketOptions.KeepAliveOptions.builder()
                                .enable(true)
                                .interval(Duration.ofSeconds(10))
                                .idle(Duration.ofSeconds(30))
                                .count(3)
                                .build())
                        .build())
                .build()
        ).build();
    };
}






    //CacheManager cacheManager()
    //用来配置和返回CacheManager的Bean,CacheManager负责管理各种不同的缓存组件
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(defaultCacheConfig())
                .transactionAware()
                .build();
    }
    //使用RedisCacheConfiguration的默认配置defaultCacheConfig()作为基础。
//通过prefixCacheNameWith方法为所有缓存key添加前缀,这里用的是常量REDIS_KEY_PREFIX。
//通过entryTtl方法设置缓存的默认过期时间为300秒。
//通过disableCachingNullValues方法禁止缓存null值。
//这样默认的缓存配置就设置好了:
//
//缓存key统一加前缀。
//默认过期时间300秒。
//不缓存null值
    private RedisCacheConfiguration defaultCacheConfig() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .prefixCacheNameWith(Constants.REDIS_KEY_PREFIX)
                .entryTtl(Duration.ofSeconds(300))
                .disableCachingNullValues();
    }

    //用于生成缓存的key。
    @Bean(name = "cacheKeyGenerator")
    public KeyGenerator cacheKeyGenerator() {
        return (target, method, params) -> {
            StringBuilder sb = new StringBuilder();
            sb.append(target.getClass().getName());
            sb.append(method.getName());
            for (Object obj : params) {
                sb.append(obj.toString());
            }
            return sb.toString();
        };
    }

}

*/

