package fr.pelt10.kubithon.hub.cosmetic;

import lombok.Getter;
import org.spongepowered.api.entity.living.player.Player;

public abstract class AbstractCosmetic {
    @Getter
    private Player player;

    public AbstractCosmetic(Player player) {
        this.player = player;
    }

    public abstract void dispawn();

    public abstract void spawn();
}

