package fr.pelt10.kubithon.hub.listeners;

import fr.pelt10.kubithon.hub.Hub;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketHeldItemChange;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.DyeColors;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.SlotIndex;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextStyles;

public class PlayerJoin extends KubiListener {

    public PlayerJoin(Hub hub) {
        super(hub);
    }

    @Listener
    public void PlayerJoinEvent(ClientConnectionEvent.Join event) {
        Player player = event.getTargetEntity();

        Inventory inv = player.getInventory();
        inv.clear();
        setupPlayerInv(inv);

        //Change Held ItemSlot
        EntityPlayerMP thePlayer = (EntityPlayerMP)player;
        thePlayer.connection.sendPacket(new SPacketHeldItemChange(4));
    }

    private void setupPlayerInv(Inventory inv) {

        //HUB ItemStack
        ItemStack hubItemStack = ItemStack.builder().itemType(ItemTypes.END_PORTAL_FRAME).build();
        hubItemStack.offer(Keys.DISPLAY_NAME, Text.builder("Changer de Hub").style(TextStyles.NONE).build());

        //HidePlayer ItemStack
        ItemStack hidePlayerItemStack = ItemStack.builder().itemType(ItemTypes.DYE).build();
        hidePlayerItemStack.offer(Keys.DISPLAY_NAME, Text.builder("Cacher les joueurs (OFF)").style(TextStyles.NONE).build());
        hidePlayerItemStack.offer(Keys.DYE_COLOR, DyeColors.GRAY);

        //Cosmetic ItemStack
        ItemStack cosmeticItemStack = ItemStack.builder().itemType(ItemTypes.CHEST).build();
        cosmeticItemStack.offer(Keys.DISPLAY_NAME, Text.builder("Cosmetiques").style(TextStyles.NONE).build());

        inv.query(new SlotIndex(3)).offer(hubItemStack);
        inv.query(new SlotIndex(5)).offer(hidePlayerItemStack);
        inv.query(new SlotIndex(6)).offer(cosmeticItemStack);

    }
}
