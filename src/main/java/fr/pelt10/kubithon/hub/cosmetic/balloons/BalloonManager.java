package fr.pelt10.kubithon.hub.cosmetic.balloons;

import fr.pelt10.kubithon.hub.Hub;
import fr.pelt10.kubithon.hub.cosmetic.AbstractCosmeticManager;
import org.spongepowered.api.entity.living.player.Player;

import java.util.LinkedList;
import java.util.List;

public class BalloonManager extends AbstractCosmeticManager {
    private List<BalloonCosmetic> balloonCosmetics = new LinkedList();
    private Hub hub;

    public BalloonManager(Hub hub) {
        this.hub = hub;
    }

    protected void dispawnCosmeticFor(Player player) {
        this.balloonCosmetics.stream().filter(baloonCosmetic -> baloonCosmetic.getPlayer().equals(player)).forEach(BalloonCosmetic::dispawn);
    }
}
