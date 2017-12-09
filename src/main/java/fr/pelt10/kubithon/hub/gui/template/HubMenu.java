package fr.pelt10.kubithon.hub.gui.template;


import fr.pelt10.kubithon.hub.Hub;
import fr.pelt10.kubithon.hub.com.messages.PlayerTeleportMessage;
import fr.pelt10.kubithon.hub.gui.InventoryGUI;
import fr.pelt10.kubithon.hub.utils.ServerInstance;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.item.EnchantmentData;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.enchantment.Enchantment;
import org.spongepowered.api.item.enchantment.EnchantmentTypes;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import java.util.Arrays;
import java.util.UUID;

public class HubMenu extends InventoryGUI {
    private Hub hubPlugin;

    public HubMenu(Hub hub) {
        super(hub);
        this.hubPlugin = hub;
    }

    @Override
    public void onAction(ClickInventoryEvent event) {
        event.setCancelled(true);

        event.getTransactions().stream().filter(slotTransaction -> slotTransaction.getOriginal().getType().equals(ItemTypes.BEACON)).forEach(slotTransaction -> {
            UUID uuid = event.getCause().first(Player.class).get().getUniqueId();
            ServerInstance server = hubPlugin.getDataManager().getHub(slotTransaction.getOriginal().get(Keys.ITEM_LORE).get().get(0).toPlainSingle()).get();
            if (!hubPlugin.getDataManager().getHubInstance().getHubID().equals(server.getHubID())) {

                Object[] data = {uuid, server};
                hubPlugin.getCommunicationManager().sendMessage(PlayerTeleportMessage.class, data);
            }
        });
    }

    @Override
    public Inventory getInventory() {
        double heightD = hubPlugin.getDataManager().getHubList().size() / 9.0f;
        int height = (int) heightD;

        if (heightD - height > 0)
            height++;

        Inventory inv = getDefaultInventory(9, height);

        int i = 1;
        for (ServerInstance hub : hubPlugin.getDataManager().getHubList()) {
            ItemStack itemStack = ItemStack.builder().itemType(ItemTypes.BEACON).build();
            itemStack.offer(Keys.ITEM_LORE, Arrays.asList(Text.builder(hub.getHubID()).color(TextColors.GRAY).style(TextStyles.ITALIC).build()));
            itemStack.offer(Keys.DISPLAY_NAME, Text.builder("Hub " + i).style(TextStyles.NONE).build());

            if (this.hubPlugin.getDataManager().getHubInstance().getHubID().equals(hub.getHubID())) {
                EnchantmentData enchantmentData = itemStack.getOrCreate(EnchantmentData.class).get();
                enchantmentData.set(enchantmentData.enchantments().add(Enchantment.builder().type(EnchantmentTypes.SHARPNESS).level(1).build()));
                itemStack.offer(enchantmentData);
                itemStack.offer(Keys.HIDE_ENCHANTMENTS, true);
            }

            inv.offer(itemStack);
            i++;
        }

        return inv;
    }

    @Override
    public String getDisplayName() {
        return "Liste des Hubs";
    }
}
