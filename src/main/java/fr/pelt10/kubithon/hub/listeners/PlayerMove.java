package fr.pelt10.kubithon.hub.listeners;

import fr.pelt10.kubithon.hub.Hub;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.MoveEntityEvent;

public class PlayerMove extends KubiListener {
    private Hub hub;

    public PlayerMove(Hub hub) {
        super(hub);
        this.hub = hub;
    }

    @Listener
    public void playerMoveEvent(MoveEntityEvent event) {
        Entity entity = event.getTargetEntity();
        if (entity instanceof Player && entity.getLocation().getY() <= 0) {
            ((Player)entity).setLocation(hub.getDataManager().getSpawnLocation());
        }
    }
}
