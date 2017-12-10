package fr.pelt10.kubithon.hub.gui.template;

import fr.pelt10.kubithon.hub.Hub;
import fr.pelt10.kubithon.hub.cosmetic.balloons.BallonList;
import fr.pelt10.kubithon.hub.cosmetic.pets.PetCosmeticList;
import fr.pelt10.kubithon.hub.gui.InventoryGUI;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import java.util.Arrays;

public class PetMenu extends InventoryGUI {
    private Hub hub;

    public PetMenu(Hub hub) {
        super(hub);
        this.hub = hub;
    }

    @Override
    public void onAction(ClickInventoryEvent event) {
        //Another Dirty Hack...
        Task.builder().execute(() -> {
            Player player = event.getCause().first(Player.class).get();
            event.filter(itemStack -> !itemStack.getType().equals(ItemTypes.AIR)).forEach(slotTransaction ->
                    slotTransaction.getOriginal().createStack().get(Keys.DISPLAY_NAME).ifPresent(text ->
                            Arrays.stream(PetCosmeticList.values()).filter(pet -> pet.getName().equals(text.toPlainSingle())).forEach(pet -> {
                                if (player.hasPermission("kubithon.cosmetic.pet." + pet.name().toLowerCase())) {
                                    pet.get(player);
                                    player.closeInventory();
                                } else {
                                    player.sendMessage(Text.builder("Vous n'avez pas encore acheté : " + pet.getName()).color(TextColors.RED).build());
                                }
                            })
                    )
            );
        }).submit(hub);

        event.setCancelled(true);
    }

    @Override
    public Inventory getInventory() {
        double heightD = PetCosmeticList.values().length / 9.0f;
        int height = (int) heightD;

        if (heightD - height > 0)
            height++;

        Inventory inv = getDefaultInventory(9, height);

        Arrays.stream(PetCosmeticList.values()).forEach(pet -> {
            ItemStack itemStack = pet.getItemStack();
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
