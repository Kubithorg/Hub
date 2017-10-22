package fr.pelt10.kubithon.dataregistry;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.inject.Inject;
import fr.pelt10.kubithon.Hub;
import lombok.Getter;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.slf4j.Logger;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.scheduler.Task;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Path;
import java.util.UUID;

public class DataManager {
    @Getter
    private final String hubID = "HUB_" + UUID.randomUUID();

    private ConfigurationLoader<CommentedConfigurationNode> loader;
    private ConfigurationNode rootNode;

    private Logger logger;
    private Hub hub;
    private JedisPool jedisPool;


    public DataManager(Hub hub, Path config) {
        this.hub = hub;
        logger = hub.getLogger();

        logger.info("Loading data");
        logger.info("Hub ID : {}", hubID);

        logger.info("Loading ConfigFile");
        loader = HoconConfigurationLoader.builder().setPath(config).build();
        try {
            rootNode = loader.load(ConfigurationOptions.defaults());

            logger.info("Starting RedisPool System");
            ConfigurationNode redisNode = rootNode.getNode("redis");
            jedisPool = new JedisPool(new JedisPoolConfig(),
                    redisNode.getNode("ip").getString(), //IP Address
                    redisNode.getNode("port").getInt(), //Port
                    5000,  //Timeout
                    redisNode.getNode("password").getString(), //Password
                    0, // Database
                    hubID);

            try (Jedis jedis = jedisPool.getResource()) {
                jedis.ping();
            }
        } catch (IOException e) {
            logger.trace(e.getMessage(), e);
            hub.getGame().getServer().shutdown();
        }
    }

    public void setup() {
        Task.builder().execute(task -> {
            JsonParser parser = new JsonParser();
            JsonObject serverInfo = new JsonObject();
            InetSocketAddress ip = hub.getGame().getServer().getBoundAddress().get();
            serverInfo.add("name", parser.parse(hubID));
            serverInfo.add("ip", parser.parse(ip.getAddress().getHostAddress()));
            serverInfo.add("port", parser.parse(Integer.toString(ip.getPort())));
            try(Jedis jedis = jedisPool.getResource()) {
                jedis.select(RedisKeys.getHUB_DB_ID());
                jedis.hset(RedisKeys.getHUB_KEY_NAME(), hubID, serverInfo.toString());
                jedis.publish(RedisKeys.getHUB_PUBSUB_CHANNEL(), "NEW " + serverInfo.toString());
            }
        }).submit(hub);
    }

    public void unregister() {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.select(RedisKeys.getHUB_DB_ID());
            jedis.hdel(RedisKeys.getHUB_KEY_NAME(), hubID);
            jedis.publish(RedisKeys.getHUB_PUBSUB_CHANNEL(), "DELETE " + hubID);
        }
        jedisPool.destroy();
    }
}
