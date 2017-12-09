package fr.pelt10.kubithon.hub.cosmetic.pets;

import fr.pelt10.kubithon.hub.cosmetic.CosmeticList;
import lombok.Getter;
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
    HORSE_SKELETON(EntityTypes.SKELETON_HORSE, "Cheval Squelette"),
    HORSE_ZOMBIE(EntityTypes.ZOMBIE_HORSE, "Cheval Zombie"),
    HORSE_BROWN(EntityTypes.HORSE,"Cheval Marron", (entity, player) -> setupHorse(entity, player, 1)),
    HORSE_WHITE(EntityTypes.HORSE,"Cheval Blanc", (entity, player) -> setupHorse(entity, player, 0)),
    SHEEP_ORANGE(EntityTypes.SHEEP,"Mouton Orange", (entity, player) -> setupSheep(entity, EnumDyeColor.ORANGE)),
    SHEEP_PURPLE(EntityTypes.SHEEP,"Mouton Violet", (entity, player) -> setupSheep(entity, EnumDyeColor.PURPLE)),
    SHEEP_PINK(EntityTypes.SHEEP,"Mouton Rose", (entity, player) -> setupSheep(entity, EnumDyeColor.PINK)),
    SHEEP_RED(EntityTypes.SHEEP,"Mouton Rouge", (entity, player) -> setupSheep(entity, EnumDyeColor.RED)),
    SHEEP_BLACK(EntityTypes.SHEEP,"Mouton Noir", (entity, player) -> setupSheep(entity, EnumDyeColor.BLACK)),
    SHEEP_YELLOW(EntityTypes.SHEEP,"Mouton Jaune", (entity, player) -> setupSheep(entity, EnumDyeColor.YELLOW)),
    MAGMACUBE(EntityTypes.MAGMA_CUBE,"MagmaCube", (entity, player) -> ((MagmaCube) entity).slimeSize().set(0)),
    SLIME(EntityTypes.SLIME,"Slime", (entity, player) -> ((Slime) entity).slimeSize().set(0)),
    CHICKEN(EntityTypes.CHICKEN,"Poulet"),
    WOLF(EntityTypes.WOLF, "Chien"),
    COW(EntityTypes.COW, "Vache"),
    COW_MUSHROOM(EntityTypes.MUSHROOM_COW, "Vache Champignon"),
    PIG(EntityTypes.PIG, "Cochon"),
    RABBIT(EntityTypes.RABBIT, "Lapin"),
    IRONGOLEM(EntityTypes.IRON_GOLEM, "Golem de fer"),
    ENDERMAN(EntityTypes.ENDERMAN, "EnderMan"),
    VILLAGER(EntityTypes.VILLAGER, "Villageois");

    private EntityType entityType;
    private BiConsumer<Entity, Player>[] biConsumers;

    @Getter
    private String name;

    private PetCosmeticList(EntityType entityType, String name, BiConsumer<Entity, Player>... biConsumer) {
        this.entityType = entityType;
        this.biConsumers = biConsumer;
        this.name = name;
    }

    private static void setupHorse(Entity entity, Player player, int color) {
        EntityHorse horse = (EntityHorse) entity;
        horse.setHorseVariant(color);
        horse.setTamedBy((EntityPlayer) player);
    }

    private static void setupSheep(Entity entity, EnumDyeColor color) {
        EntitySheep sheep = (EntitySheep) entity;
        sheep.setFleeceColor(color);
        sheep.setGrowingAge(Integer.MIN_VALUE);
    }

    public PetCosmetic get(Player player) {
        PetCosmetic petCosmetic = new PetCosmetic(player, entityType);

        for (BiConsumer biConsumer : biConsumers) {
            biConsumer.accept(petCosmetic.getEntity(), player);
        }

        petCosmetic.spawn();
        return petCosmetic;
    }
}
