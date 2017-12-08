package fr.pelt10.kubithon.hub.cosmetic.balloons;

import fr.pelt10.kubithon.hub.cosmetic.CosmeticList;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.player.Player;

public enum BallonList implements CosmeticList {
    COW(EntityTypes.COW),
    PIG(EntityTypes.PIG),
    SHEEP(EntityTypes.SHEEP),
    SHEEP_JEB(EntityTypes.SHEEP, "jeb_"),
    SHEEP_GRUMM(EntityTypes.SHEEP, "Grumm"),
    MUSHROOM_COW(EntityTypes.MUSHROOM_COW);

    private EntityType entityType;
    private String entityName = "";

    private BallonList(EntityType entityType) {
        this.entityType = entityType;
    }

    private BallonList(EntityType entityType, String entityName) {
        this(entityType);
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
