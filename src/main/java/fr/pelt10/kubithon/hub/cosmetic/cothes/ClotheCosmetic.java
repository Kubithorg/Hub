package fr.pelt10.kubithon.hub.cosmetic.cothes;

import fr.pelt10.kubithon.hub.cosmetic.AbstractCosmetic;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.InventoryProperty;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.SlotIndex;

import java.util.function.BiConsumer;

public class ClotheCosmetic extends AbstractCosmetic {
    private ArmorSlot armorSlot;
    private ItemStack itemStack;

    public ClotheCosmetic(Player player, ClotheCosmetic.ArmorSlot armorSlot, ItemStack itemStack) {
        super(player);
        this.armorSlot = armorSlot;
        this.itemStack = itemStack;
    }

    public void spawn() {
        armorSlot.equip(getPlayer(), itemStack);
    }

    public void dispawn() {
        armorSlot.equip(getPlayer(), ItemStack.builder().itemType(ItemTypes.AIR).build());
    }

    public static enum ArmorSlot {
        HELMET((itemStack, player) -> player.setHelmet(itemStack)),
        CHESTPLATE((itemStack, player) -> player.setChestplate(itemStack)),
        LEGGINGS((itemStack, player) -> player.setLeggings(itemStack)),
        BOOTS((itemStack, player) -> player.setBoots(itemStack));

        private BiConsumer<ItemStack, Player> consumer;

        private ArmorSlot(BiConsumer<ItemStack, Player> consumer) {
            this.consumer = consumer;
        }

        public void equip(Player player, ItemStack itemStack) {
            consumer.accept(itemStack, player);
        }
    }
}