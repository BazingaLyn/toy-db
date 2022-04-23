package org.elastic.toy.db.resp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import io.netty.util.ByteProcessor;
import org.elastic.toy.db.enums.RedisCommandEnums;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author bazinga
 * 2022-4-17 08:10:44
 */
public class RedisDecoder extends ReplayingDecoder<RedisDecoder.State> {


    public RedisDecoder() {
        // 默认先check一下基于RESP协议一共有多少个数据行需要解析
        super(State.COUNT);
    }


    private InnerRedisData innerRedisData = new InnerRedisData();

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        byte[] data = getLine(byteBuf);

        if (data == null) {
            return;
        }

        byte[] bytes = null;
        // 接下来对这行RESP协议的数据进行解析
        switch (state()) {
            //先解析有多少行基于RESP协议的数据
            case COUNT:
                checkCountHeader(data[0]);
                //跳过第一个标记位字节(*)
                bytes = Arrays.copyOfRange(data, 1, data.length);
                innerRedisData.setCount(Integer.parseInt(new String(bytes)));
                checkpoint(State.LENGTH);
                return;
            // 接下来解析每一行有多少字节的数据
            case LENGTH:
                checkLengthHeader(data[0]);
                bytes = Arrays.copyOfRange(data, 1, data.length);
                innerRedisData.setLength(Integer.parseInt(new String(bytes)));
                checkpoint(State.BODY);
                return;
            case BODY:
                //根据上一行对每一行字数的解析 来真实解析每一行
                bytes = Arrays.copyOfRange(data, 0, innerRedisData.getLength());
                innerRedisData.getParams().add(new String(bytes));
                // 如果这个时候已经解析出规定的行数，说明这个时候一个完整的基于RESP协议的已经解析完成了
                if (innerRedisData.getParams().size() == innerRedisData.getCount()) {
                    RedisRequest redisRequest = buildRequest(innerRedisData);
                    list.add(redisRequest);
                    // 重新初始化
                    innerRedisData = new InnerRedisData();
                    checkpoint(State.COUNT);
                } else {
                    // 没有完全解析完成 继续解析下一行数据
                    checkpoint(State.LENGTH);
                }


        }
    }

    private RedisRequest buildRequest(InnerRedisData innerRedisData) {
        RedisRequest redisRequest = new RedisRequest();
        redisRequest.setCommand(innerRedisData.getParams().get(0));
        redisRequest.setRedisCommandEnums(getRedisCommand(redisRequest.getCommand().toLowerCase()));
        redisRequest.setParams(innerRedisData.getParams().subList(1, innerRedisData.getParams().size()));
        return redisRequest;
    }

    private RedisCommandEnums getRedisCommand(String command) {

        switch (command) {
            case "set":
            case "get":
                return RedisCommandEnums.STRING;
            case "lpop":
                return RedisCommandEnums.LIST;
        }
        return null;
    }

    /**
     * RESP协议标记接下来的数据是记录行数的使用‘*’+数字来表示
     *
     * @param header
     */
    private void checkCountHeader(byte header) {
        if (header != '*') {
            throw new IllegalArgumentException("unknown argument exception");
        }
    }


    private void checkLengthHeader(byte header) {
        if (header != '$') {
            throw new IllegalArgumentException("unknown argument exception");
        }
    }

    private byte[] getLine(ByteBuf byteBuf) {
        byte[] data = null;
        int currentIndex = byteBuf.forEachByte(ByteProcessor.FIND_CRLF);
        if (currentIndex > 0 && byteBuf.getByte(currentIndex - 1) == '\r') {
            currentIndex--;
        }

        if (currentIndex > 0) {
            if (currentIndex - byteBuf.readerIndex() <= 0) {
                return null;
            }

            // 读取一行新的信息
            byte[] msg = new byte[currentIndex - byteBuf.readerIndex()];
            byteBuf.readBytes(msg);

            data = msg;

            //如果后续的字段超过2个字节 则直接跳过\r\n
            if (byteBuf.readableBytes() >= 2) {
                byteBuf.skipBytes(2);
            }
        }

        return data;
    }


    enum State {
        COUNT, LENGTH, BODY
    }


    public static class InnerRedisData implements Serializable {

        private int count;

        private List<String> params = new ArrayList<>();

        private int length;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public List<String> getParams() {
            return params;
        }

        public void setParams(List<String> params) {
            this.params = params;
        }

        public int getLength() {
            return length;
        }

        public void setLength(int length) {
            this.length = length;
        }
    }
}
