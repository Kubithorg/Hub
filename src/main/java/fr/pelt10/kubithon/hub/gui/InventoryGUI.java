package fr.pelt10.kubithon.hub.gui;

import com.google.inject.Inject;
import fr.pelt10.kubithon.hub.Hub;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.InventoryArchetype;
import org.spongepowered.api.item.inventory.property.InventoryDimension;
import org.spongepowered.api.text.Text;

public abstract class InventoryGUI {
    private Hub hub;

    @Inject
    public InventoryGUI(Hub hub) {
        this.hub = hub;
        hub.getGuiManager().registerGUI(this);
    }

    public abstract void onAction(ClickInventoryEvent event);

    public abstract Inventory getInventory();

    public abstract String getDisplayName();

    protected Inventory getDefaultInventory(int width, int height) {
        return Inventory.builder().of(
                InventoryArchetype.builder().title(Text.of(getDisplayName())).property(new InventoryDimension(width, height)).build(getDisplayName().toLowerCase(), getDisplayName())
        ).build(hub);
    }
}
