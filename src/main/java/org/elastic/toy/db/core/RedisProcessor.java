package org.elastic.toy.db.core;

import org.elastic.toy.db.resp.RedisRequest;
import org.elastic.toy.db.resp.RedisResp;

/**
 * @author bazinga
 * 2022-4-23 09:42:44
 */
public interface RedisProcessor {


    /**
     * redis的处理入口
     *
     * @param redisRequest
     * @return
     * @throws Exception
     */
    RedisResp processor(RedisRequest redisRequest) throws Exception;
}
