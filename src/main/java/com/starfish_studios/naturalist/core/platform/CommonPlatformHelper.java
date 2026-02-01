package com.starfish_studios.naturalist.core.platform;

import com.starfish_studios.naturalist.core.platform.fabric.CommonPlatformHelperImpl;
import com.starfish_studios.naturalist.core.registry.NaturalistMenus;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.*;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.entity.SpawnPlacementType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.SpawnPlacementType;
import net.minecraft.world.entity.SpawnPlacements;

import java.util.function.Supplier;
import net.minecraft.core.Holder;

@SuppressWarnings("unused")
public class CommonPlatformHelper {

    public static <T extends Block> Supplier<T> registerBlock(String name, Supplier<T> block) {
        return CommonPlatformHelperImpl.registerBlock(name, block);
    }

    public static <T extends BlockEntity> Supplier<BlockEntityType<T>> registerBlockEntityType(String name,
            Supplier<BlockEntityType<T>> factory) {
        return CommonPlatformHelperImpl.registerBlockEntityType(name, factory);
    }

    public static <T extends Item> Supplier<T> registerItem(String name, Supplier<T> item) {
        return CommonPlatformHelperImpl.registerItem(name, item);
    }

    public static <T extends Mob> Supplier<Item> registerSpawnEggItem(String name,
            Supplier<EntityType<T>> entityType, int backgroundColor, int highlightColor) {
        return CommonPlatformHelperImpl.registerSpawnEggItem(name, entityType, backgroundColor, highlightColor);
    }

    public static Supplier<Item> registerNoFluidMobBucketItem(String name,
            Supplier<? extends EntityType<?>> entitySupplier, Supplier<? extends Fluid> fluidSupplier,
            Supplier<? extends SoundEvent> soundSupplier, int color) {
        return CommonPlatformHelperImpl.registerNoFluidMobBucketItem(name, entitySupplier, fluidSupplier, soundSupplier,
                color);
    }

    public static Supplier<Item> registerMobBucketItem(String name, Supplier<? extends EntityType<?>> entitySupplier,
            Supplier<? extends Fluid> fluidSupplier, Supplier<? extends SoundEvent> soundSupplier) {
        return CommonPlatformHelperImpl.registerMobBucketItem(name, entitySupplier, fluidSupplier, soundSupplier);
    }

    public static Supplier<Item> registerCaughtMobItem(String name, Supplier<? extends EntityType<?>> entitySupplier,
            Supplier<? extends Fluid> fluidSupplier, Supplier<? extends SoundEvent> soundSupplier) {
        return CommonPlatformHelperImpl.registerCaughtMobItem(name, entitySupplier, fluidSupplier, soundSupplier);
    }

    public static Supplier<Item> registerCaughtMobItem(String name, Supplier<? extends EntityType<?>> entitySupplier,
            Supplier<? extends Fluid> fluidSupplier, Supplier<? extends SoundEvent> soundSupplier, int variantAmount) {
        return CommonPlatformHelperImpl.registerCaughtMobItem(name, entitySupplier, fluidSupplier, soundSupplier,
                variantAmount);
    }

    public static <T extends SoundEvent> Supplier<T> registerSoundEvent(String name, Supplier<T> soundEvent) {
        return CommonPlatformHelperImpl.registerSoundEvent(name, soundEvent);
    }

    public static <T extends Entity> Supplier<EntityType<T>> registerEntityType(String name,
            EntityType.EntityFactory<T> factory, MobCategory category, float width, float height,
            int clientTrackingRange) {
        return CommonPlatformHelperImpl.registerEntityType(name, factory, category, width, height, clientTrackingRange);
    }

    public static <T extends AbstractContainerMenu> MenuType<T> registerMenuType(String name,
            Supplier<MenuType<T>> supplier) {
        return CommonPlatformHelperImpl.registerMenuType(name, supplier).get();
    }

    public static <T extends AbstractContainerMenu> MenuType<T> createMenuType(NaturalistMenus.MenuFactory<T> factory) {
        return CommonPlatformHelperImpl.createMenuType(factory);
    }

    public static void openMenu(ServerPlayer player, MenuProvider provider) {
        CommonPlatformHelperImpl.openMenu(player, provider);
    }

    public static <T extends Potion> Supplier<T> registerPotion(String name, Supplier<T> potion) {
        return CommonPlatformHelperImpl.registerPotion(name, potion);
    }

    public static void registerBrewingRecipe(Holder<Potion> input, Item ingredient, Holder<Potion> output) {
        CommonPlatformHelperImpl.registerBrewingRecipe(input, ingredient, output);
    }

    public static <T extends Mob> void registerSpawnPlacement(EntityType<T> entityType,
            SpawnPlacementType decoratorType, Heightmap.Types heightMapType,
            SpawnPlacements.SpawnPredicate<T> decoratorPredicate) {
        CommonPlatformHelperImpl.registerSpawnPlacement(entityType, decoratorType, heightMapType, decoratorPredicate);
    }

    public static void registerCompostable(float chance, ItemLike item) {
        CommonPlatformHelperImpl.registerCompostable(chance, item);
    }

    // TODO: Re-implement registerRecipes for Fabric
    // This method is commented out in CommonPlatformHelperImpl
    // public static void registerRecipes(String name, Supplier<RecipeType<?>> type,
    // Supplier<RecipeSerializer<?>> serializer) {
    // CommonPlatformHelperImpl.registerRecipes(name, type, serializer);
    // }

    public static TagKey<Item> getShearsTag() {
        return CommonPlatformHelperImpl.getShearsTag();
    }
}
