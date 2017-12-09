package fr.pelt10.kubithon.hub.cosmetic.balloons;

import fr.pelt10.kubithon.hub.cosmetic.CosmeticList;
import lombok.Getter;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.player.Player;

public enum BallonList implements CosmeticList {
    COW(EntityTypes.COW, "Vache"),
    PIG(EntityTypes.PIG, "Cochon"),
    SHEEP(EntityTypes.SHEEP, "Mouton"),
    SHEEP_JEB(EntityTypes.SHEEP, "jeb_", "Mouton Retourné"),
    SHEEP_GRUMM(EntityTypes.SHEEP, "Grumm", "Mouton Multicolore"),
    MUSHROOM_COW(EntityTypes.MUSHROOM_COW, "Vache Champignon");

    private EntityType entityType;
    private String entityName = "";

    @Getter
    private String name;

    private BallonList(EntityType entityType, String name) {
        this.entityType = entityType;
        this.name = "Ballon " + name;
    }

    private BallonList(EntityType entityType, String entityName, String name) {
        this(entityType, name);
        this.entityName = entityName;
    }


    public BalloonCosmetic get(Player player) {
        BalloonCosmetic balloon = new BalloonCosmetic(player, entityType);
        if (!entityName.isEmpty()) {
            balloon.rename(entityName);
        }
        return balloon;
    }
}
