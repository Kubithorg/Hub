package fr.pelt10.kubithon;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.inject.Inject;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.gamerule.DefaultGameRules;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Path;
import java.util.UUID;

@Plugin(id = "kubithonhub", name = "KubithonHub", version = "1.0-SNAPSHOT", authors = "Pelt10", description = "Plugin de Gestion du Hub pour le projet Kubithon", url = "https://kubithon.org/")
public class Hub {
    private Game game;
    private Logger logger;
    private final String hubID = "HUB_" + UUID.randomUUID();
    private JedisPool jedisPool;

    @Inject
    @DefaultConfig(sharedRoot = true)
    private Path config;

    private ConfigurationLoader<CommentedConfigurationNode> loader;
    private ConfigurationNode rootNode;

    @Inject
    public Hub(Game game, Logger logger) {
        this.game = game;
        this.logger = logger;
    }

    /**
     * During this state, the plugin gets ready for initialization.
     * Access to a default logger instance and access to information regarding preferred configuration file locations is available.
     *
     * @param event
     */
    @Listener
    public void onGamePreInitialization(GamePreInitializationEvent event) {
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
            game.getServer().shutdown();
        }
    }

    /**
     * The server instance exists, and worlds are loaded.
     * It's time to setup world!
     *
     * @param event
     */
    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        //World setup
        game.getServer().getWorlds().stream().map(World::getProperties).forEach(properties -> {
            properties.setGameRule(DefaultGameRules.DO_DAYLIGHT_CYCLE, "false");
            properties.setWorldTime(6000);
        });

        Task.builder().execute(task -> {
            JsonParser parser = new JsonParser();
            JsonObject serverInfo = new JsonObject();
            InetSocketAddress ip = game.getServer().getBoundAddress().get();
            serverInfo.add("name", parser.parse(hubID));
            serverInfo.add("ip", parser.parse(ip.getAddress().getHostAddress()));
            serverInfo.add("port", parser.parse(Integer.toString(ip.getPort())));
            try(Jedis jedis = jedisPool.getResource()) {
                jedis.select(1);
                jedis.hset("HUB", hubID, serverInfo.toString());
                jedis.publish("HUB_PUBSUB", "NEW " + serverInfo.toString());
            }
        }).submit(this);

    }

    /**
     *  this event has finished executing, Minecraft will shut down.
     *  No further interaction with the game or other plugins should be attempted at this point.
     *
     * @param event
     */
    @Listener
    public void onGameStoppingServer(GameStoppedServerEvent event) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.select(1);
            jedis.hdel("HUB", hubID);
            jedis.publish("HUB_PUBSUB", "DELETE " + hubID);
        }
        jedisPool.destroy();
    }
}
