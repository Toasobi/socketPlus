package cn.omertacoding.redisConfig;


import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.List;

/**
 * @author: Omerta
 * @create-date: 2023/5/30 8:10
 *
 */


public class MyRedisPool  {
    private JedisPool jedisPool;
    //实例化连接池
    public MyRedisPool() {
        //创建一个JedisPoolConfig对象
        JedisPoolConfig config = new JedisPoolConfig();
        //设置Redis的ip和端口
        //设置其他的一些连接池参数，如最大连接数，最大空闲数等，根据需要自行调整
        config.setMaxTotal(100);
        config.setMaxIdle(10);
        //使用JedisPoolConfig对象创建JedisPool
        this.jedisPool = new JedisPool(config,"43.138.199.12",6379);
    }
    //获取Redis连接资源，并确保在使用后归还
    public void execute(CallJedis caller){
        try(Jedis jedis = jedisPool.getResource()){
            caller.call(jedis);
        }
    }
}
