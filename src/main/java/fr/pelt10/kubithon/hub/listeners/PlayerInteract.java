package fr.pelt10.kubithon.hub.listeners;

import fr.pelt10.kubithon.hub.Hub;
import fr.pelt10.kubithon.hub.gui.template.HubMenu;
import fr.pelt10.kubithon.hub.utils.HidePlayers;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.DyeColors;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.item.inventory.InteractItemEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.item.inventory.property.SlotIndex;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextStyles;

import java.util.concurrent.TimeUnit;

public class PlayerInteract extends KubiListener {
    private final ItemStack hidePlayerItemStack = ItemStack.builder().itemType(ItemTypes.DYE).build();
    private Hub hub;

    public PlayerInteract(Hub hub) {
        super(hub);

        this.hub = hub;
    }

    @Listener
    public void PlayerInteractEvent(InteractItemEvent.Secondary event) {

        Player player = event.getCause().first(Player.class).get();

        if (player.hasPermission("kubithon.hub.inventoryfree"))
            return;

        event.setCancelled(true);
        ItemStackSnapshot itemStack = event.getItemStack();

        if (itemStack.getType().equals(ItemTypes.END_PORTAL_FRAME)) {
            player.openInventory(hub.getGuiManager().getGUI(HubMenu.class).get().getInventory());
        } else if (itemStack.getType().equals(ItemTypes.DYE)) {
            HidePlayers hidePlayers = hub.getHidePlayers();
            if (hidePlayers.getPlayers().contains(player)) {
                hidePlayers.showPlayerFor(player);

                hidePlayerItemStack.offer(Keys.DISPLAY_NAME, Text.builder("Cacher les joueurs (OFF)").style(TextStyles.NONE).build());
                hidePlayerItemStack.offer(Keys.DYE_COLOR, DyeColors.GRAY);

                player.getInventory().query(new SlotIndex(5)).set(hidePlayerItemStack);
            } else {
                hidePlayers.hidePlayerFor(player);

                hidePlayerItemStack.offer(Keys.DISPLAY_NAME, Text.builder("Cacher les joueurs (ON)").style(TextStyles.NONE).build());
                hidePlayerItemStack.offer(Keys.DYE_COLOR, DyeColors.LIME);

                player.getInventory().query(new SlotIndex(5)).set(hidePlayerItemStack);
            }
        } else if (itemStack.getType().equals(ItemTypes.CHEST)) {
            player.sendMessage(Text.of("Not Implemented"));
        } else if (itemStack.getType().equals(ItemTypes.FIREWORKS)) {
            event.setCancelled(false);
            Task.builder().delay(1, TimeUnit.SECONDS).execute(() -> {
                ItemStack itemStackInst = itemStack.createStack();
                itemStackInst.setQuantity(1);
                player.getInventory().query(new SlotIndex(2)).offer(ItemStack.builder().itemType(ItemTypes.AIR).build());
                player.getInventory().query(new SlotIndex(2)).offer(itemStackInst);
            }).submit(hub);
        }
    }
}
