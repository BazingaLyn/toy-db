package org.elastic.toy.db.resp;

import io.netty.util.Recycler;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * @author bazinga
 * 2022-4-17 08:49:38
 */
public class RedisSerializer {

    private static final Recycler<ByteBufferBuilder> RECYCLER = new Recycler<ByteBufferBuilder>() {
        @Override
        protected ByteBufferBuilder newObject(Handle<ByteBufferBuilder> handle) {
            return new ByteBufferBuilder(handle);
        }
    };

    public static byte[] encode(RedisResp redisResp) throws Exception {
        try (ByteBufferBuilder builder = RECYCLER.get()) {
            encode(builder, redisResp);
            return builder.toArray();
        }
    }


    private static void encode(ByteBufferBuilder builder, RedisResp redisResp) {
//        switch (redisResp.get)

    }


    private static class ByteBufferBuilder implements AutoCloseable {

        private static final int INITIAL_CAPACITY = 1024;

        private ByteBuffer buffer = ByteBuffer.allocate(INITIAL_CAPACITY);

        private final Recycler.Handle<ByteBufferBuilder> handle;


        private ByteBufferBuilder(Recycler.Handle<ByteBufferBuilder> handle) {
            this.handle = handle;
        }

        @Override
        public void close() throws Exception {
            if (buffer.capacity() > INITIAL_CAPACITY) {
                buffer = ByteBuffer.allocate(INITIAL_CAPACITY);
            } else {
                buffer.clear();
            }
            handle.recycle(this);
        }

        private ByteBufferBuilder append(int i) {
            append(String.valueOf(i));
            return this;
        }

        private ByteBufferBuilder append(String str) {
            append(str.getBytes(StandardCharsets.UTF_8));
            return this;
        }

        private ByteBufferBuilder append(byte[] buf) {
            ensureCapacity(buf.length);
            buffer.put(buf);
            return this;
        }

        private ByteBufferBuilder append(byte buf) {
            ensureCapacity(1);
            buffer.put(buf);
            return this;
        }


        private void ensureCapacity(int length) {
            if (buffer.remaining() < length) {
                growBuffer(length);
            }
        }

        private void growBuffer(int length) {
            int capacity = buffer.capacity() + Math.max(length, INITIAL_CAPACITY);
            buffer = ByteBuffer.allocate(capacity).put(toArray());
        }

        private byte[] toArray() {
            byte[] array = new byte[buffer.position()];
            buffer.rewind();
            buffer.get(array);
            return array;
        }

    }


}
