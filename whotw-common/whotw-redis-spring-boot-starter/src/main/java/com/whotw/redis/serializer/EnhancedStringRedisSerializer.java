package com.whotw.redis.serializer;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author EdisonXu
 * @date 2020-03-23
 */
public class EnhancedStringRedisSerializer implements RedisSerializer<Object> {

    private final Charset charset;

    public EnhancedStringRedisSerializer() {
        this(StandardCharsets.UTF_8);
    }

    public EnhancedStringRedisSerializer(Charset charset) {
        Assert.notNull(charset, "Charset must not be null!");
        this.charset = charset;
    }

    @Override
    public byte[] serialize(@Nullable Object o) {
        return (o == null ? null : String.valueOf(o).getBytes(charset));
    }

    @Override
    public String deserialize(@Nullable byte[] bytes) {
        return (bytes == null ? null : new String(bytes, charset));
    }

}
