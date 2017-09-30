package fr.pelt10.kubithon;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.gamerule.DefaultGameRules;

@Plugin(id = "kubithonhub", name = "KubithonHub", version = "1.0-SNAPSHOT", authors = "Pelt10", description = "Plugin de Gestion du Hub pour le projet Kubithon", url = "https://kubithon.org/")
public class Hub {
    private Game game;
    private Logger logger;

    @Inject
    public Hub(Game game, Logger logger) {
        this.game = game;
        this.logger = logger;
    }

    @Listener
    public void onServerStart(GameStartedServerEvent e) {
        //World setup
        game.getServer().getWorlds().stream().map(World::getProperties).forEach(properties -> {
            properties.setGameRule(DefaultGameRules.DO_DAYLIGHT_CYCLE, "false");
            properties.setWorldTime(6000);
        });

    }
}
