package fr.pelt10.kubithon.hub.listeners;

import fr.pelt10.kubithon.hub.Hub;
import net.minecraft.inventory.IInventoryChangedListener;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.item.inventory.ChangeInventoryEvent;
import org.spongepowered.api.event.item.inventory.DropItemEvent;
import org.spongepowered.api.event.item.inventory.InteractInventoryEvent;

public class CancelAction extends KubiListener {
    public CancelAction(Hub hub) {
        super(hub);
    }

    @Listener
    public void onPlayerSwapItem(ChangeInventoryEvent.Transfer.Pre event) {
        System.out.println(event.getSource());
    }

    @Listener
    public void onPlayerDropItem(DropItemEvent.Dispense event) {
        event.setCancelled(true);
    }
}
