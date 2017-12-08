package fr.pelt10.kubithon.hub.listeners;

import fr.pelt10.kubithon.hub.Hub;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketHeldItemChange;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.entity.GameModeData;
import org.spongepowered.api.data.type.DyeColors;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameMode;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.SlotIndex;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextStyles;

public class PlayerJoin extends KubiListener {
    private Hub hub;

    public PlayerJoin(Hub hub) {
        super(hub);
        this.hub = hub;
    }

    @Listener
    public void PlayerJoinEvent(ClientConnectionEvent.Join event) {
        event.setMessageCancelled(true);
        Player player = event.getTargetEntity();

        player.offer(Keys.GAME_MODE, GameModes.ADVENTURE);

        player.getFoodData().saturation().set(20.0D);
        player.getFoodData().foodLevel().set(20);
        player.setLocation(hub.getDataManager().getSpawnLocation());

        Inventory inv = player.getInventory();
        inv.clear();
        setupPlayerInv(inv);

        ItemStack elytra = ItemStack.builder().itemType(ItemTypes.ELYTRA).build();
        elytra.offer(Keys.UNBREAKABLE, true);

        player.setChestplate(elytra);

        //Change Held ItemSlot
        EntityPlayerMP thePlayer = (EntityPlayerMP) player;
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
        cosmeticItemStack.offer(Keys.DISPLAY_NAME, Text.builder("Cosmétiques").style(TextStyles.NONE).build());

        //Firework
        ItemStack fireworkItemStack = ItemStack.builder().itemType(ItemTypes.FIREWORKS).build();
        fireworkItemStack.offer(Keys.DISPLAY_NAME, Text.builder("Décollage immédiat !").style(TextStyles.NONE).build());

        inv.query(new SlotIndex(2)).offer(fireworkItemStack);
        inv.query(new SlotIndex(3)).offer(hubItemStack);
        inv.query(new SlotIndex(5)).offer(hidePlayerItemStack);
        inv.query(new SlotIndex(6)).offer(cosmeticItemStack);

    }
}
