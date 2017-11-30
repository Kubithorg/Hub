package fr.pelt10.kubithon.hub.gui.template;

import fr.pelt10.kubithon.hub.Hub;
import fr.pelt10.kubithon.hub.com.messages.PlayerTeleportMessage;
import fr.pelt10.kubithon.hub.gui.InventoryGUI;
import fr.pelt10.kubithon.hub.utils.ServerInstance;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;

import java.util.Arrays;
import java.util.UUID;

public class HubMenu extends InventoryGUI {
    private Hub hub;

    public HubMenu(Hub hub) {
        super(hub);
        this.hub = hub;
    }

    @Override
    public void onAction(ClickInventoryEvent event) {
        event.getTransactions().stream().filter(slotTransaction -> slotTransaction.getOriginal().getType().equals(ItemTypes.BEACON)).forEach(slotTransaction -> {
            UUID uuid = event.getCause().first(Player.class).get().getUniqueId();
            ServerInstance server = hub.getDataManager().getHub(slotTransaction.getOriginal().get(Keys.DISPLAY_NAME).orElse(Text.EMPTY).toPlainSingle()).get();
            Object[] data = {uuid, server};
            hub.getCommunicationManager().sendMessage(PlayerTeleportMessage.class, data);
        });
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
