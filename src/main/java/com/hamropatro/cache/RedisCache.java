package com.hamropatro.cache;

import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.SetArgs;
import com.lambdaworks.redis.api.StatefulRedisConnection;
import io.micronaut.context.annotation.Primary;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Singleton
@Primary
@Slf4j
class RedisCache<T> implements Cache<T> {

    private final StatefulRedisConnection<String, T> redisConnection;

    @Inject
    RedisCache() {
        var codec = new JavaSerializationRedisCodec<T>();
        redisConnection = RedisClient.create("redis://localhost:6379").connect(codec);
        redisConnection.setTimeout(10, TimeUnit.SECONDS);
    }

    public boolean contains(String key) {
        try {
            log.debug("contains :: key: {}", key);
            var keys = new String[]{key};
            var contains = redisConnection.sync().exists(keys) > 0;
            log.debug("contains :: return: {}", contains);
            return contains;
        } catch (Exception e) {
            log.warn("contains :: exception", e);
            return false;
        }
    }

    public boolean delete(String key) {
        try {
            log.debug("delete :: key: {}", key);
            var deleted = redisConnection.sync().del(key) > 0;
            log.debug("delete :: return: {}", deleted);
            return deleted;
        } catch (Exception e) {
            log.warn("delete :: exception", e);
            return false;
        }
    }

    public Optional<T> get(String key) {
        try {
            log.debug("get :: key: {}", key);
            var value = Optional.ofNullable(redisConnection.sync().get(key));
            log.debug("get :: return: {}", value);
            return value;
        } catch (Exception e) {
            log.warn("get :: exception", e);
            return Optional.empty();
        }
    }

    public boolean put(String key, T value) {
        try {
            log.debug("put :: key: {}, value: {}", key, value);
            redisConnection.sync().set(key, value);
            var put = Objects.equals(redisConnection.sync().get(key), value);
            log.debug("put :: return: {}", put);
            return put;
        } catch (Exception e) {
            log.warn("put :: exception", e);
            return false;
        }
    }

    public boolean put(String key, T value, long time, TimeUnit timeUnit) {
        try {
            log.debug("put :: key: {}, time: {}, timeUnit: {}", key, time, timeUnit);
            var timeout = timeUnit.toSeconds(time);
            redisConnection.sync().set(key, value, SetArgs.Builder.ex(timeout));
            return true;
        } catch (Exception e) {
            log.warn("put :: exception", e);
            return false;
        }
    }
}