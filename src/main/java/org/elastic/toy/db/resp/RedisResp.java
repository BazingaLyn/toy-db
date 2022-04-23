package org.elastic.toy.db.resp;

import java.io.Serializable;
import java.util.List;

/**
 * @author bazinga
 * 2022-4-9 22:45:18
 */
public class RedisResp implements Serializable {

    private RedisTokenType type;

    private Object o;

    private static final String OK = "OK";


    public static RedisResp writeOK(){
        RedisResp redisResp = new RedisResp();
        redisResp.setType(RedisTokenType.SIMPLE_STRING);
        redisResp.setO(OK);
        return redisResp;
    }


    public static RedisResp writeError(String msg){
        RedisResp redisResp = new RedisResp();
        redisResp.setType(RedisTokenType.ERROR);
        redisResp.setO(msg);
        return redisResp;
    }


    public static RedisResp writeString(String msg){
        RedisResp redisResp = new RedisResp();
        redisResp.setType(RedisTokenType.STRING);
        redisResp.setO(msg);
        return redisResp;
    }


    public static RedisResp writeBulk(List<String> msg){
        RedisResp redisResp = new RedisResp();
        redisResp.setType(RedisTokenType.STRING);
        redisResp.setO(msg);
        return redisResp;
    }


    public static RedisResp writeInt(int value){
        RedisResp redisResp = new RedisResp();
        redisResp.setType(RedisTokenType.INTEGER);
        redisResp.setO(value);
        return redisResp;
    }

    public static RedisResp writeNull(){
        RedisResp redisResp = new RedisResp();
        redisResp.setType(RedisTokenType.STRING);
        return redisResp;
    }




    public RedisTokenType getType() {
        return type;
    }

    public void setType(RedisTokenType type) {
        this.type = type;
    }

    public Object getO() {
        return o;
    }

    public void setO(Object o) {
        this.o = o;
    }

    @Override
    public String toString() {
        return "RedisResp{" +
                "type=" + type +
                ", o=" + o +
                '}';
    }

    public enum RedisTokenType {
        INTEGER,
        SIMPLE_STRING,
        STRING,
        ARRAY,
        ERROR,
        UNKNOWN
    }


}
