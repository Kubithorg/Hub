package fr.pelt10.kubithon.hub.gui.template;

import fr.pelt10.kubithon.hub.Hub;
import fr.pelt10.kubithon.hub.cosmetic.pets.PetCosmeticList;
import fr.pelt10.kubithon.hub.gui.InventoryGUI;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextStyles;

import java.util.Arrays;

public class PetMenu extends InventoryGUI {
    public PetMenu(Hub hub) {
        super(hub);
    }

    @Override
    public void onAction(ClickInventoryEvent event) {

    }

    @Override
    public Inventory getInventory() {
        double heightD = PetCosmeticList.values().length / 9.0f;
        int height = (int) heightD;

        if (heightD - height > 0)
            height++;

        Inventory inv = getDefaultInventory(9, height);

        Arrays.stream(PetCosmeticList.values()).forEach(pet -> {
            ItemStack itemStack = ItemStack.builder().itemType(ItemTypes.WOOL).build();
            itemStack.offer(Keys.DISPLAY_NAME, Text.builder(pet.getName()).style(TextStyles.NONE).build());
            inv.offer(itemStack);
        });

        return inv;
    }

    @Override
    public String getDisplayName() {
        return "Menu des cosmétiques > Animaux";
    }
}
