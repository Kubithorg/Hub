package fr.pelt10.kubithon.hub.cosmetic.balloons;

import fr.pelt10.kubithon.hub.cosmetic.AbstractCosmetic;
import net.minecraft.entity.EntityCreature;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.DisplayNameData;
import org.spongepowered.api.data.manipulator.mutable.PotionEffectData;
import org.spongepowered.api.data.manipulator.mutable.entity.InvulnerabilityData;
import org.spongepowered.api.effect.potion.PotionEffect;
import org.spongepowered.api.effect.potion.PotionEffectTypes;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

public class BalloonCosmetic extends AbstractCosmetic {
    private Entity baloon;
    private EntityType entityType;

    public BalloonCosmetic(Player player, EntityType entityType) {
        super(player);
        this.entityType = entityType;
    }

    public void rename(String name) {
        baloon.offer(baloon.getOrCreate(DisplayNameData.class).get().displayName().set(Text.of(name)));
    }

    public void spawn() {
        baloon = getPlayer().getLocation().createEntity(entityType);
        EntityCreature theBaloon = (EntityCreature) baloon;
        theBaloon.setLeashHolder((net.minecraft.entity.Entity) getPlayer(), true);

        InvulnerabilityData invulnerabilityData = baloon.getOrCreate(InvulnerabilityData.class).get();
        invulnerabilityData.set(Keys.INVULNERABLE, Boolean.valueOf(true));
        baloon.offer(invulnerabilityData);

        PotionEffectData effects = baloon.getOrCreate(PotionEffectData.class).get();
        effects.addElement(PotionEffect.builder().potionType(PotionEffectTypes.LEVITATION).duration(Integer.MAX_VALUE).build());
        baloon.offer(effects);
        getPlayer().getWorld().spawnEntity(baloon);
    }

    public void dispawn() {
        baloon.remove();
    }
}
