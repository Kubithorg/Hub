package fr.pelt10.kubithon;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.plugin.Plugin;

@Plugin(id="kubithonhub", name = "KubithonHub", version = "1.0-SNAPSHOT", authors = "Pelt10", description = "Plugin de Gestion du Hub pour le projet Kubithon", url = "https://kubithon.org/")
public class Hub {
    private Game game;
    private Logger logger;

    @Inject
    public Hub(Game game, Logger logger) {
        this.game = game;
        this.logger = logger;
    }
}
