package fr.pelt10.kubithon.hub;


import com.google.inject.Inject;
import com.google.inject.Singleton;
import fr.pelt10.kubithon.hub.com.CommunicationManager;
import fr.pelt10.kubithon.hub.com.messages.PlayerTeleportMessage;
import fr.pelt10.kubithon.hub.dataregistry.DataManager;
import fr.pelt10.kubithon.hub.gui.GuiManager;
import fr.pelt10.kubithon.hub.gui.template.HubMenu;
import fr.pelt10.kubithon.hub.gui.template.MainMenu;
import fr.pelt10.kubithon.hub.listeners.CancelAction;
import fr.pelt10.kubithon.hub.listeners.PlayerInteract;
import fr.pelt10.kubithon.hub.listeners.PlayerJoin;
import fr.pelt10.kubithon.hub.listeners.PlayerMove;
import fr.pelt10.kubithon.hub.utils.HidePlayers;
import lombok.Getter;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.EventManager;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.gamerule.DefaultGameRules;
import org.spongepowered.api.world.storage.WorldProperties;
import org.spongepowered.api.world.weather.Weathers;

import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Singleton
@Plugin(id = "kubithonhub", name = "KubithonHub", version = "1.0-SNAPSHOT", authors = "Pelt10", description = "Plugin de Gestion du Hub pour le projet Kubithon", url = "https://kubithon.org/")
public class Hub {
    @Getter
    private Game game;
    @Getter
    private Logger logger;

    @Getter
    private DataManager dataManager;

    @Getter
    private GuiManager guiManager;

    @Inject
    @DefaultConfig(sharedRoot = true)
    private Path config;

    @Getter
    private CommunicationManager communicationManager;

    @Getter
    private HidePlayers hidePlayers;

    @Inject
    public Hub(Game game, Logger logger) {
        this.game = game;
        this.logger = logger;
    }

    /**
     * During this state, the plugin gets setup for initialization.
     * Access to a default logger instance and access to information regarding preferred configuration file locations is available.
     *
     * @param event
     */
    @Listener
    public void onGamePreInitialization(GamePreInitializationEvent event) {

    }

    /**
     * The server instance exists, and worlds are loaded.
     * It's time to setup world!
     *
     * @param event
     */
    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        //Dirty hack to remove "org.apache." from blacklist, thx Forge
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

            Class clazz = classLoader.getClass();
            Field field = clazz.getDeclaredField("classLoaderExceptions");
            field.setAccessible(true);

            Set<String> classLoaderExceptions = (Set<String>) field.get(classLoader);
            classLoaderExceptions.remove("org.apache.");

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        //World setup
        game.getServer().getWorlds().stream().forEach(world -> {
            WorldProperties worldProperties = world.getProperties();
            worldProperties.setGameRule(DefaultGameRules.DO_DAYLIGHT_CYCLE, "false");
            worldProperties.setWorldTime(6000);

            world.setWeather(Weathers.CLEAR, Long.MAX_VALUE);
        });

        dataManager = new DataManager(this, config);

        communicationManager = new CommunicationManager();
        communicationManager.registerMessage(new PlayerTeleportMessage(dataManager.getJedisUtils()));

        new PlayerJoin(this);
        new PlayerInteract(this);
        new CancelAction(this);
        new PlayerMove(this);

        Task.builder().interval(10, TimeUnit.SECONDS).execute(() -> {
            getGame().getServer().getOnlinePlayers().forEach(player -> {
                player.getFoodData().foodLevel().set(20);
                player.getFoodData().saturation().set(20.0D);
            });
        }).submit(this);
    }

    @Listener
    public void onGameInitialization(GameInitializationEvent event) {
        logger.info("Register listeners");
        EventManager eventManager = game.getEventManager();
        eventManager.registerListeners(this, new GuiManager(this));

        guiManager = new GuiManager(this);
        new MainMenu(this);
        new HubMenu(this);

        hidePlayers = new HidePlayers(this);
    }

    /**
     * this event has finished executing, Minecraft will shut down.
     * No further interaction with the game or other plugins should be attempted at this point.
     *
     * @param event
     */
    @Listener
    public void onGameStoppingServer(GameStoppedServerEvent event) {
        dataManager.unregister();
    }
}
