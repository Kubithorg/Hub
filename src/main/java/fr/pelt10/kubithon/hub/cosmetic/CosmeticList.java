package fr.pelt10.kubithon.hub.cosmetic;

import org.spongepowered.api.entity.living.player.Player;

public interface CosmeticList {
    <T extends AbstractCosmetic> T get(Player player);
}
