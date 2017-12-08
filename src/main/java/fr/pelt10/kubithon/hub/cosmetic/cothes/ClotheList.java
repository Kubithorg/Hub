package fr.pelt10.kubithon.hub.cosmetic.cothes;

import fr.pelt10.kubithon.hub.cosmetic.AbstractCosmetic;
import fr.pelt10.kubithon.hub.cosmetic.CosmeticList;
import org.spongepowered.api.entity.living.player.Player;

public enum ClotheList implements CosmeticList {
    TEMP;

    private ClotheList() {
    }

    public <T extends AbstractCosmetic> T get(Player player) {
        return null;
    }
}
