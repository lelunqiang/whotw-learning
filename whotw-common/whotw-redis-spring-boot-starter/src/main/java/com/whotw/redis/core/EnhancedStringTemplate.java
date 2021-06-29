package com.whotw.redis.core;

import com.whotw.redis.serializer.EnhancedStringRedisSerializer;
import org.springframework.data.redis.connection.DefaultStringRedisConnection;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author EdisonXu
 * @date 2020-03-23
 */
public class EnhancedStringTemplate extends StringRedisTemplate {

    /**
     * Constructs a new <code>StringRedisTemplate</code> instance. {@link #setConnectionFactory(RedisConnectionFactory)}
     * and {@link #afterPropertiesSet()} still need to be called.
     */
    public EnhancedStringTemplate() {
        setKeySerializer(new EnhancedStringRedisSerializer());
        setValueSerializer(new EnhancedStringRedisSerializer());
        setHashKeySerializer(new EnhancedStringRedisSerializer());
        setHashValueSerializer(new EnhancedStringRedisSerializer());
    }

    /**
     * Constructs a new <code>StringRedisTemplate</code> instance ready to be used.
     *
     * @param connectionFactory connection factory for creating new connections
     */
    public EnhancedStringTemplate(RedisConnectionFactory connectionFactory) {
        this();
        setConnectionFactory(connectionFactory);
        afterPropertiesSet();
    }

    protected RedisConnection preProcessConnection(RedisConnection connection, boolean existingConnection) {
        return new DefaultStringRedisConnection(connection);
    }

}
