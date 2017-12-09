package fr.pelt10.kubithon.hub.gui.template;

import fr.pelt10.kubithon.hub.Hub;
import fr.pelt10.kubithon.hub.cosmetic.balloons.BallonList;
import fr.pelt10.kubithon.hub.gui.InventoryGUI;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import java.util.Arrays;

public class BallonMenu extends InventoryGUI {
    private Hub hub;
    public BallonMenu(Hub hub) {
        super(hub);
        this.hub = hub;
    }

    @Override
    public void onAction(ClickInventoryEvent event) {
        Player player = event.getCause().first(Player.class).get();
        event.filter(itemStack -> itemStack.getType().equals(ItemTypes.WOOL)).forEach(slotTransaction ->
                slotTransaction.getOriginal().createStack().get(Keys.DISPLAY_NAME).ifPresent(text ->
                        Arrays.stream(BallonList.values()).filter(ballon -> ballon.getName().equals(text.toPlainSingle())).forEach(ballon -> {
                            if (player.hasPermission("kubithon.cosmetic." + ballon.name().toLowerCase())) {
                                ballon.get(event.getCause().first(Player.class).get());
                            } else {
                                player.sendMessage(Text.builder("Vous n'avez pas encore acheté : " + ballon.getName()).color(TextColors.RED).build());
                            }
                        })
                )
        );
    }

    @Override
    public Inventory getInventory() {
        double heightD = BallonList.values().length / 9.0f;
        int height = (int) heightD;

        if (heightD - height > 0)
            height++;

        Inventory inv = getDefaultInventory(9, height);

        Arrays.stream(BallonList.values()).forEach(balloon -> {
            ItemStack itemStack = ItemStack.builder().itemType(ItemTypes.WOOL).build();
            itemStack.offer(Keys.DISPLAY_NAME, Text.builder(balloon.getName()).style(TextStyles.NONE).build());
            inv.offer(itemStack);
        });

        return inv;
    }

    @Override
    public String getDisplayName() {
        return "Menu des cosmétiques > Ballons";
    }
}
