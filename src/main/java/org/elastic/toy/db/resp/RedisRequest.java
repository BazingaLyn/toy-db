package org.elastic.toy.db.resp;

import org.elastic.toy.db.enums.RedisCommandEnums;

import java.io.Serializable;
import java.util.List;

/**
 * @author bazinga
 * 2022-4-10 09:52:49
 */
public class RedisRequest implements Serializable {


    private String command;


    private List<String> params;


    private RedisCommandEnums redisCommandEnums;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public List<String> getParams() {
        return params;
    }

    public void setParams(List<String> params) {
        this.params = params;
    }

    public RedisCommandEnums getRedisCommandEnums() {
        return redisCommandEnums;
    }

    public void setRedisCommandEnums(RedisCommandEnums redisCommandEnums) {
        this.redisCommandEnums = redisCommandEnums;
    }

    @Override
    public String toString() {
        return "RedisRequest{" +
                "command='" + command + '\'' +
                ", params=" + params +
                ", redisCommandEnums=" + redisCommandEnums +
                '}';
    }
}
