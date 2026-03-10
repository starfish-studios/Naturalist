package com.starfish_studios.naturalist.core.platform;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.common.item.fabric.*;
import com.starfish_studios.naturalist.core.registry.NaturalistMenus;
// FabricEntityTypeBuilder removed in newer Fabric API - using EntityType.Builder
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
// ExtendedScreenHandlerType API changed in 1.21
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
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
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class CommonPlatformHelper {

    public static <T extends Block> Supplier<T> registerBlock(@NotNull String name, Supplier<T> block) {
        T registry = Registry.register(BuiltInRegistries.BLOCK, ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, name), block.get());
        return () -> registry;
    }

    public static <T extends BlockEntity> Supplier<BlockEntityType<T>> registerBlockEntityType(String name, @NotNull Supplier<BlockEntityType<T>> factory) {
        BlockEntityType<T> registry = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, name), factory.get());
        return () -> registry;
    }

    public static <T extends Item> Supplier<T> registerItem(String name, @NotNull Supplier<T> item) {
        T registry = Registry.register(BuiltInRegistries.ITEM, ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, name), item.get());
        return () -> registry;
    }

    public static <T extends Mob> Supplier<SpawnEggItem> registerSpawnEggItem(@NotNull String name, Supplier<EntityType<T>> entityType, int backgroundColor, int highlightColor) {
        return registerItem(name, () -> new SpawnEggItem(entityType.get(), backgroundColor, highlightColor, new Item.Properties()));
    }

    public static Supplier<Item> registerNoFluidMobBucketItem(String name, Supplier<? extends EntityType<?>> entitySupplier, @NotNull Supplier<? extends Fluid> fluidSupplier, Supplier<? extends SoundEvent> soundSupplier) {
        return registerItem(name, () -> new NoFluidMobBucketItem(entitySupplier.get(), fluidSupplier.get(), soundSupplier.get(), new Item.Properties().stacksTo(1)/* .craftRemainder(Items.BUCKET) -- removed in 1.21 */));
    }

    public static Supplier<Item> registerNoFluidMobBucketItem(String name, @NotNull Supplier<? extends EntityType<?>> entitySupplier, Supplier<? extends Fluid> fluidSupplier, @NotNull Supplier<? extends SoundEvent> soundSupplier, int color) {
        return registerItem(name, () -> new NoFluidMobBucketWithVariantsItem(entitySupplier, fluidSupplier.get(), soundSupplier.get(), color, new Item.Properties().stacksTo(1)/* .craftRemainder(Items.BUCKET) -- removed in 1.21 */));
    }

    public static @NotNull Supplier<Item> registerMobBucketItem(@NotNull String name, Supplier<? extends EntityType<?>> entitySupplier, @NotNull Supplier<? extends Fluid> fluidSupplier, Supplier<? extends SoundEvent> soundSupplier) {
        return registerItem(name, () -> new MobBucketItem(entitySupplier.get(), fluidSupplier.get(), soundSupplier.get(), new Item.Properties().stacksTo(1)/* .craftRemainder(Items.BUCKET) -- removed in 1.21 */));
    }

    public static @NotNull Supplier<Item> registerCaughtMobItem(String name, Supplier<? extends EntityType<?>> entitySupplier, Supplier<? extends Fluid> fluidSupplier, Supplier<? extends SoundEvent> soundSupplier) {
        return registerItem(name, () -> new CaughtMobItem(entitySupplier.get(), fluidSupplier.get(), soundSupplier.get(), new Item.Properties().stacksTo(1)));
    }

    public static Supplier<Item> registerCaughtMobItem(String name, Supplier<? extends EntityType<?>> entitySupplier, Supplier<? extends Fluid> fluidSupplier, Supplier<? extends SoundEvent> soundSupplier, int variantAmount) {
        return registerItem(name, () -> new CaughtMobWithVariantsItem(entitySupplier.get(), fluidSupplier.get(), soundSupplier.get(), variantAmount, new Item.Properties().stacksTo(1)));
    }

    public static <T extends SoundEvent> Supplier<T> registerSoundEvent(String name, Supplier<T> soundEvent) {
        T registry = Registry.register(BuiltInRegistries.SOUND_EVENT, ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, name), soundEvent.get());
        return () -> registry;
    }

    public static <T extends Entity> @NotNull Supplier<EntityType<T>> registerEntityType(@NotNull String name, EntityType.EntityFactory<T> factory, MobCategory category, float width, float height, int clientTrackingRange) {
        EntityType<T> registry = Registry.register(BuiltInRegistries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, name), EntityType.Builder.of(factory, category).sized(width, height).clientTrackingRange(clientTrackingRange).build(ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, name).toString()));
        return () -> registry;
    }

    public static <T extends AbstractContainerMenu> @NotNull Supplier<MenuType<T>> registerMenuType(String name, @NotNull Supplier<MenuType<T>> menu) {
        var registry = Registry.register(BuiltInRegistries.MENU, ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, name), menu.get());
        return () -> registry;
    }

    // TODO: ExtendedScreenHandlerType API changed in 1.21 - needs StreamCodec parameter
    // public static <T extends AbstractContainerMenu> MenuType<T> createMenuType(NaturalistMenus.MenuFactory<T> factory) {
    //     return new ExtendedScreenHandlerType<>(factory::create, streamCodec);
    // }

    public static void openMenu(ServerPlayer player, MenuProvider provider) {
        player.openMenu(provider);
    }

    public static <T extends Potion> Supplier<T> registerPotion(String name, Supplier<T> potion) {
        T registry = Registry.register(BuiltInRegistries.POTION, ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, name), potion.get());
        return () -> registry;
    }

    public static <T extends Mob> void registerSpawnPlacement(EntityType<T> entityType, SpawnPlacementType placementType, Heightmap.Types heightMapType, SpawnPlacements.SpawnPredicate<T> decoratorPredicate) {
        SpawnPlacements.register(entityType, placementType, heightMapType, decoratorPredicate);
    }

    public static void registerCompostable(float chance, ItemLike item) {
        CompostingChanceRegistry.INSTANCE.add(item, chance);
    }

    public static void registerRecipes(String name, Supplier<RecipeType<?>> type, Supplier<RecipeSerializer<?>> serializer) {
        Registry.register(BuiltInRegistries.RECIPE_TYPE, ResourceLocation.fromNamespaceAndPath("naturalist", name), type.get());
        Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, ResourceLocation.fromNamespaceAndPath("naturalist", name), serializer.get());
    }

    public static TagKey<Item> getShearsTag() {
        return ConventionalItemTags.SHEARS_TOOLS;
    }
}
