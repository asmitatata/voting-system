package com.hamropatro.cache;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public interface Cache<T> {
    boolean contains(String key);

    boolean delete(String key);

    Optional<T> get(String key);

    boolean put(String key, T value);

    boolean put(String key, T value, long time, TimeUnit timeUnit);
}