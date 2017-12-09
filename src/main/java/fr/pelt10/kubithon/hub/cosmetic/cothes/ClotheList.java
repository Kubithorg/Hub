package fr.pelt10.kubithon.hub.cosmetic.cothes;

import fr.pelt10.kubithon.hub.cosmetic.CosmeticList;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;

public enum ClotheList implements CosmeticList {
    ;

    private ClotheCosmetic.ArmorSlot armorSlot;
    private ItemStack itemStack;

    private ClotheList(ClotheCosmetic.ArmorSlot armorSlot, ItemStack itemStack) {
        this.armorSlot = armorSlot;
        this.itemStack = itemStack;
    }

    public ClotheCosmetic get(Player player) {
        return new ClotheCosmetic(player, armorSlot, itemStack);
    }
}
