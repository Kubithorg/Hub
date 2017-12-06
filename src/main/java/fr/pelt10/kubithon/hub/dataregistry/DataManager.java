package fr.pelt10.kubithon.hub.dataregistry;

import fr.pelt10.kubithon.hub.Hub;
import fr.pelt10.kubithon.hub.utils.ServerInstance;
import lombok.Getter;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.slf4j.Logger;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class DataManager {
    @Getter
    private ServerInstance hubInstance;

    private ConfigurationLoader<CommentedConfigurationNode> loader;
    private ConfigurationNode rootNode;

    private Logger logger;
    private Hub hub;

    @Getter
    private JedisUtils jedisUtils;
    private HubPubSub hubPubSub;

    @Getter
    private List<ServerInstance> hubList = new ArrayList<>();

    @Getter
    private Location<World> spawnLocation;

    public DataManager(Hub hub, Path config) {
        this.hub = hub;
        logger = hub.getLogger();

        logger.info("Loading data");

        InetSocketAddress ip = hub.getGame().getServer().getBoundAddress().get();
        hubInstance = new ServerInstance("HUB_" + UUID.randomUUID(), ip.getAddress().getHostAddress(), ip.getPort());

        logger.info("Hub ID : {}", hubInstance.getHubID());

        logger.info("Loading ConfigFile");
        loader = HoconConfigurationLoader.builder().setPath(config).build();
        try {
            rootNode = loader.load(ConfigurationOptions.defaults());

            logger.info("Get Spawn Location");

            ConfigurationNode spawnLocationNode = rootNode.getNode("spawnLocation");
            spawnLocation = new Location<>(hub.getGame().getServer().getWorld(spawnLocationNode.getNode("world").getString()).get(),
                    spawnLocationNode.getNode("x").getInt(),
                    spawnLocationNode.getNode("y").getInt(),
                    spawnLocationNode.getNode("z").getInt());

            logger.info("Starting RedisPool System");
            ConfigurationNode redisNode = rootNode.getNode("redis");
            jedisUtils = new JedisUtils(redisNode.getNode("ip").getString(),
                    redisNode.getNode("port").getInt(),
                    redisNode.getNode("password").getString(),
                    hubInstance.getHubID());

            jedisUtils.execute(jedis -> {
                jedis.select(RedisKeys.HUB_DB_ID);
                jedis.ping();
                logger.info("Loading Started Hub...");
                jedis.hgetAll(RedisKeys.HUB_KEY_NAME).values().stream().map(ServerInstance::deserialize).forEach(hubInstance -> {
                    hubList.add(hubInstance);
                    logger.info(" - " + hubInstance.getHubID() + " load.");
                });
            });
        } catch (IOException e) {
            logger.trace(e.getMessage(), e);
            hub.getGame().getServer().shutdown();
        }

        hubPubSub = new HubPubSub(jedisUtils, this, hub.getLogger());
        new Thread(hubPubSub).start();

        Task.builder().execute(() -> {
            String serialize = ServerInstance.serialize(hubInstance);
            jedisUtils.execute(jedis -> {
                jedis.select(RedisKeys.HUB_DB_ID);
                jedis.hset(RedisKeys.HUB_KEY_NAME, hubInstance.getHubID(), serialize);
                jedis.publish(RedisKeys.HUB_PUBSUB_CHANNEL, RedisKeys.PUBSUB_CMD_NEW_HUB + " " + serialize);
            });
        }).submit(hub);

    }

    public void unregister() {
        jedisUtils.execute(jedis -> {
            jedis.select(RedisKeys.HUB_DB_ID);
            jedis.hdel(RedisKeys.HUB_KEY_NAME, hubInstance.getHubID());
            jedis.publish(RedisKeys.HUB_PUBSUB_CHANNEL, RedisKeys.PUBSUB_CMD_DELETE_HUB + " " + hubInstance.getHubID());
        });
        hubPubSub.unsubscribe();
        jedisUtils.destroy();
    }

    public Optional<ServerInstance> getHub(String name) {
        return hubList.stream().filter(hub -> hub.getHubID().equals(name)).findFirst();
    }
}