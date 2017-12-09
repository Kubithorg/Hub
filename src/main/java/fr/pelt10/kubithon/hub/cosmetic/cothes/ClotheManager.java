package fr.pelt10.kubithon.hub.cosmetic.cothes;

import fr.pelt10.kubithon.hub.cosmetic.AbstractCosmeticManager;
import org.spongepowered.api.entity.living.player.Player;

import java.util.LinkedList;
import java.util.List;

public class ClotheManager extends AbstractCosmeticManager {
    private List<ClotheCosmetic> clotheCosmetics = new LinkedList();

    protected void dispawnCosmeticFor(Player player) {
        this.clotheCosmetics.stream().filter(baloonCosmetic -> baloonCosmetic.getPlayer().equals(player)).forEach(ClotheCosmetic::dispawn);
    }
}
