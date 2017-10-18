package fr.pelt10.kubithon;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import fr.pelt10.kubithon.dataregistry.DataManager;
import fr.pelt10.kubithon.gui.GuiManager;
import lombok.Getter;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.event.EventManager;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.gamerule.DefaultGameRules;

@Singleton
@Plugin(id = "kubithonhub", name = "KubithonHub", version = "1.0-SNAPSHOT", authors = "Pelt10", description = "Plugin de Gestion du Hub pour le projet Kubithon", url = "https://kubithon.org/")
public class Hub {
    @Getter
    private Game game;
    @Getter
    private Logger logger;

    @Getter
    private DataManager dataManager;

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
        dataManager = new DataManager(this);
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

        dataManager.setup();

    }

    @Listener
    public void onGameInitialization(GameInitializationEvent event) {
        logger.info("Register listeners");
        EventManager eventManager = game.getEventManager();
        eventManager.registerListeners(this, new GuiManager(this));
    }

    /**
     *  this event has finished executing, Minecraft will shut down.
     *  No further interaction with the game or other plugins should be attempted at this point.
     *
     * @param event
     */
    @Listener
    public void onGameStoppingServer(GameStoppedServerEvent event) {
        dataManager.unregister();
    }
}
