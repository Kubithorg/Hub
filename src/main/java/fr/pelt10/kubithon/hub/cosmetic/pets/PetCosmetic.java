package fr.pelt10.kubithon.hub.cosmetic.pets;

import fr.pelt10.kubithon.hub.cosmetic.AbstractCosmetic;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.ai.GoalTypes;
import org.spongepowered.api.entity.living.Agent;
import org.spongepowered.api.entity.living.player.Player;

public class PetCosmetic extends AbstractCosmetic {
    private EntityType entityType;
    private Entity entity;

    public PetCosmetic(Player player, EntityType entityType) {
        super(player);
        this.entityType = entityType;
    }

    public Entity getEntity() {
        return this.entity;
    }

    public void spawn() {
        this.entity = getPlayer().getLocation().createEntity(this.entityType);
        ((Agent) entity).getGoal(GoalTypes.NORMAL).get().clear();
        ((Agent) entity).getGoal(GoalTypes.TARGET).get().clear();
        ((Agent) entity).getGoal(GoalTypes.NORMAL).get().addTask(1, new CustomAITask(getPlayer()));
        getPlayer().getWorld().spawnEntity(this.entity);
    }

    public void dispawn() {
        this.entity.remove();
    }
}