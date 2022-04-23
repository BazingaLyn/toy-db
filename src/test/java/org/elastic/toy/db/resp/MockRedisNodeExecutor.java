package org.elastic.toy.db.resp;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;
import org.elastic.toy.db.core.RedisNodeExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

/**
 * @author bazinga
 * 2022-4-23 09:28:38
 */
public class MockRedisNodeExecutor implements RedisNodeExecutor {

    protected static final Logger log = LoggerFactory.getLogger(MockRedisNodeExecutor.class);

    @Override
    public void registerCommandProcessor() {
        //IGNORE
    }

    @Override
    public RedisResp execute(RedisRequest redisRequest) {
        log.info("receive redisCommand is {}", JSON.toJSONString(redisRequest));

        String command = redisRequest.getCommand();

        if (command.equals("quit")) {
            return RedisResp.writeOK();
        }

        if (Strings.isNullOrEmpty(command) || !command.equals("get")) {
            return RedisResp.writeOK();
        }

        if (redisRequest.getParams() == null || redisRequest.getParams().size() != 1) {
            return RedisResp.writeError("param is empty");
        }


        String params = redisRequest.getParams().get(0);

        if (params.equals("0")) {
            return RedisResp.writeOK();
        }

        if (params.equals("1")) {
            return RedisResp.writeString("hello");
        }

        if (params.equals("2")) {
            return RedisResp.writeInt(3);
        }

        if (params.equals("3")) {
            List<String> result = Arrays.asList("hello", "world", "lyn");
            return RedisResp.writeBulk(result);
        }

        if (params.equals("4")) {
            return RedisResp.writeError("can't support command");
        }

        return RedisResp.writeNull();
    }
}
