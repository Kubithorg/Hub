package fr.pelt10.kubithon.gui.template;

import fr.pelt10.kubithon.Hub;
import fr.pelt10.kubithon.gui.InventoryGUI;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;

public class HubMenu extends InventoryGUI {
    private Hub hub;

    public HubMenu(Hub hub) {
        super(hub);
        this.hub = hub;
    }

    @Override
    public void onAction(ClickInventoryEvent event) {

    }

    @Override
    public Inventory getInventory() {
        Inventory inv = getDefaultInventory(9, 6);
        ItemStack itemStack = ItemStack.builder().itemType(ItemTypes.BEACON).build();
        hub.getDataManager().getHubList().forEach(hub -> {
            itemStack.offer(Keys.DISPLAY_NAME, Text.of(hub.getHubID()));
            inv.offer(itemStack);
        });

        return inv;
    }

    @Override
    public String getDisplayName() {
        return "Liste des Hubs";
    }
}
