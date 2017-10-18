package fr.pelt10.kubithon.gui;

import com.google.inject.Inject;
import fr.pelt10.kubithon.Hub;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.InventoryArchetype;
import org.spongepowered.api.item.inventory.property.InventoryDimension;
import org.spongepowered.api.text.Text;

public abstract class InventoryGUI {
    private Hub hub;

    public abstract void onAction(ClickInventoryEvent event);
	public abstract Inventory getInventory();
	public abstract String getName();
	public abstract String getDisplayName();

	@Inject
    public InventoryGUI(Hub hub) {
        this.hub = hub;
    }

    protected Inventory getDefaultInventory(String title, int width, int height) {
        return Inventory.builder().of(InventoryArchetype.builder().title(Text.of(title)).property(new InventoryDimension(width, height)).build(title.toLowerCase(), title)).build(hub);
    }
}
