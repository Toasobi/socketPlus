package cn.omertacoding.redisConfig;

import cn.omertacoding.redisConfig.MyRedisPool;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Omerta
 * @create-date: 2023/5/30 8:28
 */
public class RedisAct {
    public static void setNx(MyRedisPool myRedisPool, String key, String value){
        myRedisPool.execute(jedis -> {
            //Redis相关业务...
            jedis.lpush(key,value);
        });
    }

    public static List<String> getNx(MyRedisPool myRedisPool, String key){
        List<String> result = new ArrayList<>();
        myRedisPool.execute(jedis -> {
            //Redis相关业务...
            List<String> lrange = jedis.lrange(key, 0, -1);
            result.addAll(lrange); // 把lrange中的所有元素添加到result中
        });
        return result;
    }
}
