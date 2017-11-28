package fr.pelt10.kubithon.hub.dataregistry;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.function.Consumer;

public class JedisUtils {
    private JedisPool jedisPool;

    public JedisUtils(String ip, int port, String password, String clientName) {

        this.jedisPool = new JedisPool(new JedisPoolConfig(),
                ip, //IP Address
                port, //Port
                5000,  //Timeout
                password, //Password
                0, // Database
                clientName);
    }

    public void execute(Consumer<Jedis> consumer) {
        try(Jedis jedis = jedisPool.getResource()) {
            consumer.accept(jedis);
        }
    }

    public void destroy() {
        jedisPool.destroy();
    }
}
