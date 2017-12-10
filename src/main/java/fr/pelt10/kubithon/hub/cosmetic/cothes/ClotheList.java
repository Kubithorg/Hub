package fr.pelt10.kubithon.hub.cosmetic.cothes;

import fr.pelt10.kubithon.hub.cosmetic.CosmeticList;
import lombok.Getter;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;

public enum ClotheList implements CosmeticList {
    TNT(ClotheCosmetic.ArmorSlot.HELMET, toItemStack(ItemTypes.TNT), "TNT"),
    ASTRONAUT(ClotheCosmetic.ArmorSlot.HELMET, toItemStack(ItemTypes.GLASS), "Astronaute"),
    BANANA(ClotheCosmetic.ArmorSlot.HELMET, toItemStack(ItemTypes.TOTEM_OF_UNDYING), "Banana ?");

    private ClotheCosmetic.ArmorSlot armorSlot;
    private ItemStack itemStack;

    @Getter
    private String name;

    private ClotheList(ClotheCosmetic.ArmorSlot armorSlot, ItemStack itemStack, String name) {
        this.armorSlot = armorSlot;
        this.itemStack = itemStack;
        this.name = name;
    }

    public ClotheCosmetic get(Player player) {
        ClotheCosmetic clotheCosmetic = new ClotheCosmetic(player, armorSlot, itemStack);
        clotheCosmetic.spawn();
        return clotheCosmetic;
    }

    static private ItemStack toItemStack(ItemType type) {
        return ItemStack.builder().itemType(type).build();
    }

}
