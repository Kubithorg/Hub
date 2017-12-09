package fr.pelt10.kubithon.hub.gui.template;

import fr.pelt10.kubithon.hub.Hub;
import fr.pelt10.kubithon.hub.cosmetic.cothes.ClotheList;
import fr.pelt10.kubithon.hub.gui.InventoryGUI;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextStyles;

import java.util.Arrays;

public class ClotheMenu extends InventoryGUI {
    public ClotheMenu(Hub hub) {
        super(hub);
    }

    @Override
    public void onAction(ClickInventoryEvent event) {

    }

    @Override
    public Inventory getInventory() {
        double heightD = ClotheList.values().length / 9.0f;
        int height = (int) heightD;

        if (heightD - height > 0)
            height++;

        Inventory inv = getDefaultInventory(9, height);

        Arrays.stream(ClotheList.values()).forEach(clothe -> {
            ItemStack itemStack = ItemStack.builder().itemType(ItemTypes.LEATHER_CHESTPLATE).build();
            itemStack.offer(Keys.DISPLAY_NAME, Text.builder(clothe.getName()).style(TextStyles.NONE).build());
            inv.offer(itemStack);
        });

        return inv;
    }

    @Override
    public String getDisplayName() {
        return "Menu des cosmétiques > Vêtements";
    }
}
