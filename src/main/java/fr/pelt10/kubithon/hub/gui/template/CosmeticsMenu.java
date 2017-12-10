package fr.pelt10.kubithon.hub.gui.template;

import fr.pelt10.kubithon.hub.Hub;
import fr.pelt10.kubithon.hub.gui.InventoryGUI;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.SkullTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.SlotIndex;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextStyles;

public class CosmeticsMenu extends InventoryGUI {
    private Hub hub;

    public CosmeticsMenu(Hub hub) {
        super(hub);
        this.hub = hub;
    }

    @Override
    public void onAction(ClickInventoryEvent event) {
        event.getTransactions().stream().forEach(slotTransaction ->
            //Another Dirty Hack...
            Task.builder().execute(() -> {
                Player player = event.getCause().first(Player.class).get();

                ItemType itemType = slotTransaction.getOriginal().getType();
                if (itemType.equals(ItemTypes.WOOL)) {
                    player.openInventory(hub.getGuiManager().getGUI(BallonMenu.class).get().getInventory());
                } else if (itemType.equals(ItemTypes.SKULL)) {
                    player.openInventory(hub.getGuiManager().getGUI(PetMenu.class).get().getInventory());
                }
            }).submit(hub)
        );
        event.setCancelled(true);
    }

    @Override
    public Inventory getInventory() {
        Inventory inv = getDefaultInventory(9, 1);

        ItemStack balloon = ItemStack.builder().itemType(ItemTypes.WOOL).build();
        balloon.offer(Keys.DISPLAY_NAME, Text.builder("Ballons").style(TextStyles.NONE).build());

        ItemStack pet = ItemStack.builder().itemType(ItemTypes.SKULL).build();
        pet.offer(Keys.DISPLAY_NAME, Text.builder("Annimaux").style(TextStyles.NONE).build());
        pet.offer(Keys.SKULL_TYPE, SkullTypes.SKELETON);

        inv.query(new SlotIndex(3)).offer(balloon);
        inv.query(new SlotIndex(5)).offer(pet);

        return inv;
    }

    @Override
    public String getDisplayName() {
        return "Menu des cosmétiques";
    }
}
