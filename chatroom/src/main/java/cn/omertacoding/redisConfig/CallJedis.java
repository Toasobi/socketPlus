package cn.omertacoding.redisConfig;

import redis.clients.jedis.Jedis;

public interface CallJedis {
    void call(Jedis jedis);
}
