package fr.pelt10.kubithon.hub.cosmetic.pets;

import fr.pelt10.kubithon.hub.cosmetic.CosmeticList;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.monster.MagmaCube;
import org.spongepowered.api.entity.living.monster.Slime;
import org.spongepowered.api.entity.living.player.Player;

import java.util.function.BiConsumer;

public enum PetCosmeticList implements CosmeticList {
    HORSE_SKELETON(EntityTypes.SKELETON_HORSE),
    HORSE_ZOMBIE(EntityTypes.ZOMBIE_HORSE),
    HORSE_BROWN(EntityTypes.HORSE, (entity, player) -> setupHorse(entity, player, 1)),
    HORSE_WHITE(EntityTypes.HORSE, (entity, player) -> setupHorse(entity, player, 0)),
    SHEEP_ORANGE(EntityTypes.SHEEP, (entity, player) -> setupSheep(entity, player, EnumDyeColor.ORANGE)),
    SHEEP_PURPLE(EntityTypes.SHEEP, (entity, player) -> setupSheep(entity, player, EnumDyeColor.PURPLE)),
    SHEEP_PINK(EntityTypes.SHEEP, (entity, player) -> setupSheep(entity, player, EnumDyeColor.PINK)),
    SHEEP_RED(EntityTypes.SHEEP, (entity, player) -> setupSheep(entity, player, EnumDyeColor.RED)),
    SHEEP_BLACK(EntityTypes.SHEEP, (entity, player) -> setupSheep(entity, player, EnumDyeColor.BLACK)),
    SHEEP_YELLOW(EntityTypes.SHEEP, (entity, player) -> setupSheep(entity, player, EnumDyeColor.YELLOW)),
    MAGMACUBE(EntityTypes.MAGMA_CUBE, (entity, player) -> ((MagmaCube) entity).slimeSize().set(0)),
    SLIME(EntityTypes.SLIME, (entity, player) -> ((Slime) entity).slimeSize().set(0)),
    CHICKEN(EntityTypes.CHICKEN),
    WOLF(EntityTypes.WOLF),
    COW(EntityTypes.COW),
    COW_MUSHROOM(EntityTypes.MUSHROOM_COW),
    PIG(EntityTypes.PIG),
    RABBIT(EntityTypes.RABBIT),
    IRONGOLEM(EntityTypes.IRON_GOLEM),
    ENDERMAN(EntityTypes.ENDERMAN),
    VILLAGER(EntityTypes.VILLAGER);

    private EntityType entityType;
    private BiConsumer<Entity, Player>[] biConsumers;

    private PetCosmeticList(EntityType entityType, BiConsumer<Entity, Player>... biConsumer) {
        this.entityType = entityType;
        this.biConsumers = biConsumer;
    }

    private static void setupHorse(Entity entity, Player player, int color) {
        EntityHorse horse = (EntityHorse) entity;
        horse.setHorseVariant(color);
        horse.setTamedBy((EntityPlayer) player);
    }

    private static void setupSheep(Entity entity, Player player, EnumDyeColor color) {
        EntitySheep sheep = (EntitySheep) entity;
        sheep.setFleeceColor(color);
        sheep.setGrowingAge(Integer.MIN_VALUE);
    }

    public PetCosmetic get(Player player) {
        PetCosmetic petCosmetic = new PetCosmetic(player, entityType);

        for (BiConsumer biConsumer : biConsumers) {
            biConsumer.accept(petCosmetic.getEntity(), player);
        }

        return petCosmetic;
    }
}
