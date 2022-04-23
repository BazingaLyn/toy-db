package org.elastic.toy.db.resp;

import io.netty.util.Recycler;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;

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

    private static final byte ARRAY = '*';
    private static final byte ERROR = '-';
    private static final byte INTEGER = ':';
    private static final byte SIMPLE_STRING = '+';
    private static final byte BULK_STRING = '$';

    private static final byte[] DELIMITER = new byte[]{'\r', '\n'};


    public static byte[] encode(RedisResp redisResp) throws Exception {
        try (ByteBufferBuilder builder = RECYCLER.get()) {
            encode(builder, redisResp);
            return builder.toArray();
        }
    }


    private static void encode(ByteBufferBuilder builder, RedisResp redisResp) {
        switch (redisResp.getType()) {
            case SIMPLE_STRING:
                String status = (String) redisResp.getO();
                builder.append(SIMPLE_STRING).append(status).append(DELIMITER);
                break;
            case STRING:
                Object msgInfo = redisResp.getO();
                if (null == msgInfo) {
                    builder.append(BULK_STRING).append(-1);
                } else {
                    String msgStr = (String) msgInfo;
                    builder.append(BULK_STRING).append(msgStr.length()).append(DELIMITER)
                            .append(msgStr);
                }
                builder.append(DELIMITER);
                break;
            case ARRAY:
                List<String> arrays = (List<String>) redisResp.getO();
                if (arrays != null) {
                    builder.append(ARRAY).append(arrays.size()).append(DELIMITER);
                    for (String eachLine : arrays) {
                        if (null == eachLine) {
                            builder.append(BULK_STRING).append(-1);
                        } else {
                            builder.append(BULK_STRING).append(eachLine.length()).append(DELIMITER).append(eachLine);
                        }
                        builder.append(DELIMITER);
                    }
                } else {
                    builder.append(ARRAY).append(0).append(DELIMITER);
                }
                break;
            case INTEGER:
                Integer count = (Integer) redisResp.getO();
                builder.append(INTEGER).append(count).append(DELIMITER);
                break;
            case ERROR:
                String error = (String) redisResp.getO();
                builder.append(ERROR).append(error).append(DELIMITER);
                break;
            case UNKNOWN:
                throw new IllegalArgumentException(redisResp.toString());
        }
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
