package fr.pelt10.kubithon.hub.listeners;

import fr.pelt10.kubithon.hub.Hub;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.DamageEntityEvent;
import org.spongepowered.api.event.item.inventory.DropItemEvent;
import org.spongepowered.api.event.world.UnloadWorldEvent;

public class CancelAction extends KubiListener {
    public CancelAction(Hub hub) {
        super(hub);
    }

    @Listener
    public void onPlayerDropItem(DropItemEvent.Dispense event) {
        event.setCancelled(true);
    }

    @Listener
    public void playerTakeDamageEvent(DamageEntityEvent event) {
        event.setCancelled(true);
    }

    @Listener
    public void WorldUnload(UnloadWorldEvent event) {
        event.setCancelled(true);
    }
}
