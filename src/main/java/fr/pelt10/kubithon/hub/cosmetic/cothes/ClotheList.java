package fr.pelt10.kubithon.hub.cosmetic.cothes;

import fr.pelt10.kubithon.hub.cosmetic.CosmeticList;
import lombok.Getter;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;

public enum ClotheList implements CosmeticList {
    ;

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

}
