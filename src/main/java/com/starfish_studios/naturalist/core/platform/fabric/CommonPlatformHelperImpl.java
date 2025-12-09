//? if fabric {
package com.starfish_studios.naturalist.core.platform.fabric;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.common.item.fabric.CaughtMobItem;
import com.starfish_studios.naturalist.common.item.fabric.CaughtMobWithVariantsItem;
import com.starfish_studios.naturalist.common.item.fabric.NoFluidMobBucketWithVariantsItem;
import com.starfish_studios.naturalist.core.registry.NaturalistMenus;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.*;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class CommonPlatformHelperImpl {
    public static <T extends Block> Supplier<T> registerBlock(String name, Supplier<T> block) {
        T registered = Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(Naturalist.MOD_ID, name), block.get());
        return () -> registered;
    }

    public static <T extends BlockEntity> Supplier<BlockEntityType<T>> registerBlockEntityType(String name, Supplier<BlockEntityType<T>> factory) {
        BlockEntityType<T> registered = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, new ResourceLocation(Naturalist.MOD_ID, name), factory.get());
        return () -> registered;
    }

    public static <T extends Item> Supplier<T> registerItem(String name, Supplier<T> item) {
        T registered = Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(Naturalist.MOD_ID, name), item.get());
        return () -> registered;
    }

    public static <T extends Mob> Supplier<SpawnEggItem> registerSpawnEggItem(String name, Supplier<EntityType<T>> entityType, int backgroundColor, int highlightColor) {
        SpawnEggItem egg = new SpawnEggItem(entityType.get(), backgroundColor, highlightColor, new Item.Properties());
        SpawnEggItem registered = Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(Naturalist.MOD_ID, name), egg);
        return () -> registered;
    }

    public static Supplier<Item> registerNoFluidMobBucketItem(String name, Supplier<? extends EntityType<?>> entitySupplier, Supplier<? extends Fluid> fluidSupplier, @NotNull Supplier<? extends SoundEvent> soundSupplier, int color) {
        Item item = new NoFluidMobBucketWithVariantsItem(entitySupplier, fluidSupplier.get(), soundSupplier.get(), color, new Item.Properties().stacksTo(1).craftRemainder(Items.BUCKET));
        Item registered = Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(Naturalist.MOD_ID, name), item);
        return () -> registered;
    }

    public static Supplier<Item> registerMobBucketItem(String name, Supplier<? extends EntityType<?>> entitySupplier, Supplier<? extends Fluid> fluidSupplier, Supplier<? extends SoundEvent> soundSupplier) {
        Item item = new MobBucketItem(entitySupplier.get(), fluidSupplier.get(), soundSupplier.get(), new Item.Properties().stacksTo(1).craftRemainder(Items.BUCKET));
        Item registered = Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(Naturalist.MOD_ID, name), item);
        return () -> registered;
    }

    public static Supplier<Item> registerCaughtMobItem(String name, Supplier<? extends EntityType<?>> entitySupplier, Supplier<? extends Fluid> fluidSupplier, Supplier<? extends SoundEvent> soundSupplier) {
        Item item = new CaughtMobItem(entitySupplier.get(), fluidSupplier.get(), soundSupplier.get(), new Item.Properties().stacksTo(1));
        Item registered = Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(Naturalist.MOD_ID, name), item);
        return () -> registered;
    }

    public static Supplier<Item> registerCaughtMobItem(String name, Supplier<? extends EntityType<?>> entitySupplier, Supplier<? extends Fluid> fluidSupplier, Supplier<? extends SoundEvent> soundSupplier, int variantAmount) {
        Item item = new CaughtMobWithVariantsItem(entitySupplier.get(), fluidSupplier.get(), soundSupplier.get(), new Item.Properties().stacksTo(1));
        Item registered = Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(Naturalist.MOD_ID, name), item);
        return () -> registered;
    }

    public static <T extends SoundEvent> Supplier<T> registerSoundEvent(String name, Supplier<T> soundEvent) {
        T registered = Registry.register(BuiltInRegistries.SOUND_EVENT, new ResourceLocation(Naturalist.MOD_ID, name), soundEvent.get());
        return () -> registered;
    }

    public static <T extends Entity> Supplier<EntityType<T>> registerEntityType(String name, EntityType.EntityFactory<T> factory, MobCategory category, float width, float height, int clientTrackingRange) {
        EntityType<T> type = FabricEntityTypeBuilder.create(category, factory)
                .dimensions(EntityDimensions.scalable(width, height))
                .trackRangeChunks(clientTrackingRange)
                .build();
        EntityType<T> registered = Registry.register(BuiltInRegistries.ENTITY_TYPE, new ResourceLocation(Naturalist.MOD_ID, name), type);
        return () -> registered;
    }

    public static <T extends AbstractContainerMenu> Supplier<MenuType<T>> registerMenuType(String name, Supplier<MenuType<T>> supplier) {
        MenuType<T> registered = Registry.register(BuiltInRegistries.MENU, new ResourceLocation(Naturalist.MOD_ID, name), supplier.get());
        return () -> registered;
    }

    public static <T extends AbstractContainerMenu> MenuType<T> createMenuType(NaturalistMenus.MenuFactory<T> factory) {
        return new ExtendedScreenHandlerType<>(factory::create);
    }

    public static void openMenu(ServerPlayer player, MenuProvider provider) {
        player.openMenu(provider);
    }

    public static <T extends Potion> Supplier<T> registerPotion(String name, Supplier<T> potion) {
        T registered = Registry.register(BuiltInRegistries.POTION, new ResourceLocation(Naturalist.MOD_ID, name), potion.get());
        return () -> registered;
    }

    public static void registerBrewingRecipe(Potion input, Item ingredient, Potion output) {
        PotionBrewing.addMix(input, ingredient, output);
    }

    public static <T extends Mob> void registerSpawnPlacement(EntityType<T> entityType, SpawnPlacements.Type decoratorType, Heightmap.Types heightMapType, SpawnPlacements.@NotNull SpawnPredicate<T> decoratorPredicate) {
        SpawnPlacements.register(entityType, decoratorType, heightMapType, decoratorPredicate);
    }

    public static void registerCompostable(float chance, ItemLike item) {
        ComposterBlock.COMPOSTABLES.put(item.asItem(), chance);
    }

    public static void registerRecipes(String name, Supplier<RecipeType<?>> type, Supplier<RecipeSerializer<?>> serializer) {
        Registry.register(BuiltInRegistries.RECIPE_TYPE, new ResourceLocation(Naturalist.MOD_ID, name), type.get());
        Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, new ResourceLocation(Naturalist.MOD_ID, name), serializer.get());
    }

    public static TagKey<Item> getShearsTag() {
        return ConventionalItemTags.SHEARS;
    }
}
//?}

