package fr.pelt10.kubithon.hub.gui.template;

import fr.pelt10.kubithon.hub.Hub;
import fr.pelt10.kubithon.hub.gui.InventoryGUI;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.SlotIndex;

public class CosmeticsMenu extends InventoryGUI {
    public CosmeticsMenu(Hub hub) {
        super(hub);
    }

    @Override
    public void onAction(ClickInventoryEvent event) {

    }

    @Override
    public Inventory getInventory() {
        Inventory inv = getDefaultInventory(9, 1);

        ItemStack balloon = ItemStack.builder().itemType(ItemTypes.WOOL).build();

        inv.query(new SlotIndex(5)).offer(balloon);

        return inv;
    }

    @Override
    public String getDisplayName() {
        return "Menu des cosmétiques";
    }
}
