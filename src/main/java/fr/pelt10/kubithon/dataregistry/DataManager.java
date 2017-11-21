package fr.pelt10.kubithon.dataregistry;

import fr.pelt10.kubithon.Hub;
import lombok.Getter;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.slf4j.Logger;
import org.spongepowered.api.scheduler.Task;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DataManager {
    @Getter
    private HubInstance hubInstance;

    private ConfigurationLoader<CommentedConfigurationNode> loader;
    private ConfigurationNode rootNode;

    private Logger logger;
    private Hub hub;

    private JedisPool jedisPool;
    private HubPubSub hubPubSub;

    private List<HubInstance> hubInstanceList = new ArrayList<>();


    public DataManager(Hub hub, Path config) {
        this.hub = hub;
        logger = hub.getLogger();

        logger.info("Loading data");

        InetSocketAddress ip = hub.getGame().getServer().getBoundAddress().get();
        hubInstance = new HubInstance("HUB_" + UUID.randomUUID(), ip.getAddress().getHostAddress(), ip.getPort());

        logger.info("Hub ID : {}", hubInstance.getHubID());

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
                    hubInstance.getHubID());

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
            String serialize = HubInstance.serialize(hubInstance);
            try(Jedis jedis = jedisPool.getResource()) {
                jedis.select(RedisKeys.getHUB_DB_ID());
                jedis.hset(RedisKeys.getHUB_KEY_NAME(), hubInstance.getHubID(), serialize);
                jedis.publish(RedisKeys.getHUB_PUBSUB_CHANNEL(), RedisKeys.getPUBSUB_CMD_NEW_HUB() + serialize);
            }
        }).submit(hub);

        hubPubSub = new HubPubSub(jedisPool, hubInstanceList);
        new Thread(hubPubSub).start();
    }

    public void unregister() {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.select(RedisKeys.getHUB_DB_ID());
            jedis.hdel(RedisKeys.getHUB_KEY_NAME(), hubInstance.getHubID());
            jedis.publish(RedisKeys.getHUB_PUBSUB_CHANNEL(), RedisKeys.getPUBSUB_CMD_DELETE_HUB() + hubInstance.getHubID());
        }
        hubPubSub.unsubscribe();
        jedisPool.destroy();
    }
}