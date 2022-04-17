package org.elastic.toy.db.resp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author bazinga
 * 2022-4-17 08:11:27
 */
public class RedisEncoder extends MessageToByteEncoder<RedisResp> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, RedisResp redisResp, ByteBuf byteBuf) throws Exception {
        byteBuf.writeBytes(RedisSerializer.encode(redisResp));
    }
}
