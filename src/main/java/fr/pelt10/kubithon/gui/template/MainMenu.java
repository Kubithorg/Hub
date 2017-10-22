package fr.pelt10.kubithon.gui.template;

import fr.pelt10.kubithon.Hub;
import fr.pelt10.kubithon.gui.InventoryGUI;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.item.inventory.Inventory;

public class Main extends InventoryGUI {

    public Main(Hub hub) {
        super(hub);
    }

    @Override
    public void onAction(ClickInventoryEvent event) {
    }

    @Override
    public Inventory getInventory() {
        return getDefaultInventory(9, 6);
    }

    @Override
    public String getDisplayName() {
        return "Menu principal";
    }
}
