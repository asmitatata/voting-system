package com.hamropatro.cache;

import com.lambdaworks.redis.codec.RedisCodec;

import java.io.*;
import java.nio.ByteBuffer;


class JavaSerializationRedisCodec<T> implements RedisCodec<String, T> {

    @Override
    public String decodeKey(ByteBuffer bytes) {
        try (var objectInputStream = toObjectInputStream(bytes)) {
            return objectInputStream.readUTF();
        } catch (IOException e) {
            throw new RuntimeException("Unable to deserialize object.", e);
        }
    }

    @Override
    public T decodeValue(ByteBuffer bytes) {
        try (var objectInputStream = toObjectInputStream(bytes)) {
            return (T) objectInputStream.readObject();
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException("Unable to deserialize object.", e);
        }
    }

    private ObjectInputStream toObjectInputStream(ByteBuffer byteBuffer) throws IOException {
        var bytes = new byte[byteBuffer.remaining()];
        byteBuffer.get(bytes);
        var byteArrayInputStream = new ByteArrayInputStream(bytes);
        return new ObjectInputStream(byteArrayInputStream);
    }

    @Override
    public ByteBuffer encodeKey(String key) {
        return toByteBuffer(key);
    }

    @Override
    public ByteBuffer encodeValue(Object value) {
        return toByteBuffer(value);
    }

    private ByteBuffer toByteBuffer(Object object) {
        try {
            var byteArrayOutputStream = new ByteArrayOutputStream();
            var objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(object);
            return ByteBuffer.wrap(byteArrayOutputStream.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Unable to serialize object.", e);
        }
    }
}