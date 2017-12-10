package fr.pelt10.kubithon.hub.cosmetic.pets;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import fr.pelt10.kubithon.hub.cosmetic.CosmeticList;
import lombok.Getter;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.RepresentedPlayerData;
import org.spongepowered.api.data.manipulator.mutable.SkullData;
import org.spongepowered.api.data.type.DyeColor;
import org.spongepowered.api.data.type.DyeColors;
import org.spongepowered.api.data.type.SkullTypes;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.entity.living.monster.MagmaCube;
import org.spongepowered.api.entity.living.monster.Slime;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.UUID;
import java.util.function.BiConsumer;

public enum PetCosmeticList implements CosmeticList {
    HORSE_SKELETON(EntityTypes.SKELETON_HORSE, "Cheval Squelette", customHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDdlZmZjZTM1MTMyYzg2ZmY3MmJjYWU3N2RmYmIxZDIyNTg3ZTk0ZGYzY2JjMjU3MGVkMTdjZjg5NzNhIn19fQ==")),
    HORSE_ZOMBIE(EntityTypes.ZOMBIE_HORSE, "Cheval Zombie", customHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDIyOTUwZjJkM2VmZGRiMThkZTg2ZjhmNTVhYzUxOGRjZTczZjEyYTZlMGY4NjM2ZDU1MWQ4ZWI0ODBjZWVjIn19fQ==")),
    HORSE_BROWN(EntityTypes.HORSE, "Cheval Marron", customHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmVkZjczZWExMmNlNmJkOTBhNGFlOWE4ZDE1MDk2NzQ5Y2ZlOTE4MjMwZGM4MjliMjU4MWQyMjNiMWEyYTgifX19"), (entity, player) -> setupHorse(entity, player, 1)),
    HORSE_WHITE(EntityTypes.HORSE, "Cheval Blanc", customHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjBhMmRiMmYxZWI5M2U1OTc4ZDJkYzkxYTc0ZGY0M2Q3Yjc1ZDllYzBlNjk0ZmQ3ZjJhNjUyZmJkMTUifX19"), (entity, player) -> setupHorse(entity, player, 0)),
    SHEEP_ORANGE(EntityTypes.SHEEP, "Mouton Orange", getWool(DyeColors.ORANGE), (entity, player) -> setupSheep(entity, EnumDyeColor.ORANGE)),
    SHEEP_PURPLE(EntityTypes.SHEEP, "Mouton Violet", getWool(DyeColors.PURPLE), (entity, player) -> setupSheep(entity, EnumDyeColor.PURPLE)),
    SHEEP_PINK(EntityTypes.SHEEP, "Mouton Rose", getWool(DyeColors.PINK), (entity, player) -> setupSheep(entity, EnumDyeColor.PINK)),
    SHEEP_RED(EntityTypes.SHEEP, "Mouton Rouge", getWool(DyeColors.RED), (entity, player) -> setupSheep(entity, EnumDyeColor.RED)),
    SHEEP_BLACK(EntityTypes.SHEEP, "Mouton Noir", getWool(DyeColors.BLACK), (entity, player) -> setupSheep(entity, EnumDyeColor.BLACK)),
    SHEEP_YELLOW(EntityTypes.SHEEP, "Mouton Jaune", getWool(DyeColors.YELLOW), (entity, player) -> setupSheep(entity, EnumDyeColor.YELLOW)),
    MAGMACUBE(EntityTypes.MAGMA_CUBE, "MagmaCube", customHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDE3MTU3ZjNkYzNlMWE1OTJiNDNiNGVmN2UxNjg3OTRlYzYyNzEzOGExMDY1MzcyM2FkMzJjOGQxNjAyNiJ9fX0="), (entity, player) -> ((MagmaCube) entity).slimeSize().set(0)),
    SLIME(EntityTypes.SLIME, "Slime", customHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODk1YWVlYzZiODQyYWRhODY2OWY4NDZkNjViYzQ5NzYyNTk3ODI0YWI5NDRmMjJmNDViZjNiYmI5NDFhYmU2YyJ9fX0="),(entity, player) -> ((Slime) entity).slimeSize().set(0)),
    CHICKEN(EntityTypes.CHICKEN, "Poulet", customHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWYxN2JkNGIyYThhMDE4ZjQ4ODVhYjMxYzk5MzliZjhjZDI4OWRhOWIyZTU4ZjRiYzMyZTkyZmNkNyJ9fX0=")),
    WOLF(EntityTypes.WOLF, "Chien", customHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWQ4MzczMWQ3N2Y1NGY1ZDRmOTNkZGQ5OWI5NDc2ZTRmMWZlNWI3ZTEzMThmMWUxNjI2ZjdkM2ZhM2FhODQ3In19fQ==")),
    COW(EntityTypes.COW, "Vache", customHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWQ2YzZlZGE5NDJmN2Y1ZjcxYzMxNjFjNzMwNmY0YWVkMzA3ZDgyODk1ZjlkMmIwN2FiNDUyNTcxOGVkYzUifX19")),
    COW_MUSHROOM(EntityTypes.MUSHROOM_COW, "Vache Champignon", customHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDBiYzYxYjk3NTdhN2I4M2UwM2NkMjUwN2EyMTU3OTEzYzJjZjAxNmU3YzA5NmE0ZDZjZjFmZTFiOGRiIn19fQ==")),
    PIG(EntityTypes.PIG, "Cochon", customHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjIxNjY4ZWY3Y2I3OWRkOWMyMmNlM2QxZjNmNGNiNmUyNTU5ODkzYjZkZjRhNDY5NTE0ZTY2N2MxNmFhNCJ9fX0=")),
    RABBIT(EntityTypes.RABBIT, "Lapin", customHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmZlY2M2YjVlNmVhNWNlZDc0YzQ2ZTc2MjdiZTNmMDgyNjMyN2ZiYTI2Mzg2YzZjYzc4NjMzNzJlOWJjIn19fQ==")),
    IRONGOLEM(EntityTypes.IRON_GOLEM, "Golem de fer", customHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODkwOTFkNzllYTBmNTllZjdlZjk0ZDdiYmE2ZTVmMTdmMmY3ZDQ1NzJjNDRmOTBmNzZjNDgxOWE3MTQifX19")),
    ENDERMAN(EntityTypes.ENDERMAN, "EnderMan", customHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2E1OWJiMGE3YTMyOTY1YjNkOTBkOGVhZmE4OTlkMTgzNWY0MjQ1MDllYWRkNGU2YjcwOWFkYTUwYjljZiJ9fX0=")),
    VILLAGER(EntityTypes.VILLAGER, "Villageois", customHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDFiODMwZWI0MDgyYWNlYzgzNmJjODM1ZTQwYTExMjgyYmI1MTE5MzMxNWY5MTE4NDMzN2U4ZDM1NTU1ODMifX19"));

    private EntityType entityType;
    private BiConsumer<Entity, Player>[] biConsumers;

    @Getter
    private String name;

    @Getter
    private ItemStack itemStack;

    private PetCosmeticList(EntityType entityType, String name, ItemStack itemStack, BiConsumer<Entity, Player>... biConsumer) {
        this.entityType = entityType;
        this.biConsumers = biConsumer;
        this.name = name;
        this.itemStack = itemStack;
    }

    private static ItemStack getWool(DyeColor color) {
        ItemStack itemStack = ItemStack.builder().itemType(ItemTypes.WOOL).build();
        itemStack.offer(Keys.DYE_COLOR, color);

        return itemStack;
    }

    private static ItemStack customHead(String base64) {
        //Generate Custom Player GameProfile
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), null);
        PropertyMap propertyMap = gameProfile.getProperties();

        if(propertyMap == null)
            throw new IllegalStateException("Profile doesn't contain a property map");

        //Set Skin Data
        byte[] encodedData = base64.getBytes();
        propertyMap.put("textures", new Property("textures", new String(encodedData)));

        //Give Custom Player GameProfile
        RepresentedPlayerData skinData = Sponge.getGame().getDataManager().getManipulatorBuilder(RepresentedPlayerData.class).get().create();
        skinData.set(Keys.REPRESENTED_PLAYER, (org.spongepowered.api.profile.GameProfile)gameProfile);

        //Generate SkullData
        SkullData skullData = Sponge.getGame().getDataManager().getManipulatorBuilder(SkullData.class).get().create();
        skullData.set(Keys.SKULL_TYPE, SkullTypes.PLAYER);

        //Create ItemStack with
        ItemStack itemStack = ItemStack.builder().itemType(ItemTypes.SKULL).itemData(skullData).build();
        itemStack.offer(skinData);

        return itemStack;
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
