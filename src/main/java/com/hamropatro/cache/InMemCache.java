package com.hamropatro.cache;

import jakarta.inject.Singleton;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Singleton
public class InMemCache<T> implements Cache<T> {
    private final ConcurrentHashMap<String, T> cache = new ConcurrentHashMap<>();

    @Override
    public boolean contains(String key) {
        return cache.containsKey(key);
    }

    @Override
    public boolean delete(String key) {
        return cache.remove(key) != null;
    }

    @Override
    public Optional<T> get(String key) {
        return Optional.ofNullable(cache.get(key));
    }

    @Override
    public boolean put(String key, T value) {
        cache.put(key, value);
        return true;
    }

    @Override
    public boolean put(String key, T value, long time, TimeUnit timeUnit) {
        cache.put(key, value);
        return true;
    }
}
