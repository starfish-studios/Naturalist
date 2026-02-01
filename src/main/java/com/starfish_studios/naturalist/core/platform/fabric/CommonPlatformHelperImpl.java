package com.starfish_studios.naturalist.core.platform.fabric;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.common.item.NaturalistSpawnEggItem;
import com.starfish_studios.naturalist.common.item.fabric.*;

import com.starfish_studios.naturalist.mixin.fabric.SpawnPlacementsInvoker;
import com.starfish_studios.naturalist.core.registry.NaturalistMenus;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.SpawnPlacementType;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;
import java.util.List;
import java.util.ArrayList;
import net.minecraft.core.Holder;

public class CommonPlatformHelperImpl {

        public record BrewingRecipe(Holder<Potion> input, Item ingredient, Holder<Potion> output) {
        }

        public static final List<BrewingRecipe> RECIPES = new ArrayList<>();

        // MC 1.21 requires item ids to be set before creating items
        private static ResourceKey<Item> itemKey(String name) {
                return ResourceKey.create(Registries.ITEM,
                                ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, name));
        }

        public static <T extends Block> Supplier<T> registerBlock(@NotNull String name, Supplier<T> block) {
                T registry = Registry.register(BuiltInRegistries.BLOCK,
                                ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, name),
                                block.get());
                return () -> registry;
        }

        public static <T extends BlockEntity> Supplier<BlockEntityType<T>> registerBlockEntityType(String name,
                        @NotNull Supplier<BlockEntityType<T>> factory) {
                BlockEntityType<T> registry = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE,
                                ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, name), factory.get());
                return () -> registry;
        }

        public static <T extends Item> Supplier<T> registerItem(String name, @NotNull Supplier<T> item) {
                T registry = Registry.register((Registry<T>) BuiltInRegistries.ITEM,
                                ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, name),
                                item.get());
                return () -> registry;
        }

        @SuppressWarnings({ "unchecked", "rawtypes" })
        public static <T extends Mob> Supplier<Item> registerSpawnEggItem(@NotNull String name,
                        Supplier<EntityType<T>> entityType, int backgroundColor, int highlightColor) {
                Item.Properties props = new Item.Properties().setId(itemKey(name));
                return registerItem(name,
                                () -> new NaturalistSpawnEggItem((Supplier) entityType, backgroundColor, highlightColor,
                                                props));
        }

        @SuppressWarnings({ "unchecked", "rawtypes" })
        public static Supplier<Item> registerMobBucketItem(String name,
                        Supplier<? extends EntityType<?>> entitySupplier,
                        Supplier<? extends Fluid> fluidSupplier, Supplier<? extends SoundEvent> soundSupplier) {
                Item.Properties props = new Item.Properties().stacksTo(1).setId(itemKey(name));
                return registerItem(name, () -> {
                        Item item = new MobBucketItem(
                                        (EntityType) entitySupplier.get(),
                                        fluidSupplier.get(), soundSupplier.get(),
                                        props);
                        return item;
                });
        }

        @SuppressWarnings({ "unchecked", "rawtypes" })
        public static Supplier<Item> registerNoFluidMobBucketItem(String name,
                        Supplier<? extends EntityType<?>> entitySupplier,
                        Supplier<? extends Fluid> fluidSupplier,
                        Supplier<? extends SoundEvent> soundSupplier, int color) {
                Item.Properties props = new Item.Properties().stacksTo(1).setId(itemKey(name));
                return registerItem(name, () -> {
                        Item item = new NoFluidMobBucketWithVariantsItem(
                                        (Supplier) entitySupplier,
                                        fluidSupplier.get(),
                                        soundSupplier.get(), color, props);
                        return item;
                });
        }

        @SuppressWarnings({ "unchecked", "rawtypes" })
        public static @NotNull Supplier<Item> registerCaughtMobItem(String name,
                        Supplier<? extends EntityType<?>> entitySupplier, Supplier<? extends Fluid> fluidSupplier,
                        Supplier<? extends SoundEvent> soundSupplier) {
                Item.Properties props = new Item.Properties().stacksTo(1).setId(itemKey(name));
                return registerItem(name,
                                () -> new CaughtMobItem((EntityType) entitySupplier.get(),
                                                fluidSupplier.get(),
                                                soundSupplier.get(), props));
        }

        @SuppressWarnings({ "unchecked", "rawtypes" })
        public static Supplier<Item> registerCaughtMobItem(String name,
                        Supplier<? extends EntityType<?>> entitySupplier,
                        Supplier<? extends Fluid> fluidSupplier, Supplier<? extends SoundEvent> soundSupplier,
                        int variantAmount) {
                Item.Properties props = new Item.Properties().stacksTo(1).setId(itemKey(name));
                return registerItem(name,
                                () -> new CaughtMobWithVariantsItem(
                                                ((Supplier<? extends EntityType<? extends Mob>>) (Object) entitySupplier)
                                                                .get(),
                                                fluidSupplier.get(),
                                                soundSupplier.get(), variantAmount, props));
        }

        public static <T extends SoundEvent> Supplier<T> registerSoundEvent(String name, Supplier<T> soundEvent) {
                T registry = Registry.register(BuiltInRegistries.SOUND_EVENT,
                                ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, name),
                                soundEvent.get());
                return () -> registry;
        }

        public static <T extends Entity> Supplier<EntityType<T>> registerEntityType(String name,
                        EntityType.EntityFactory<T> factory, MobCategory category, float width, float height,
                        int clientTrackingRange) {
                ResourceLocation id = ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, name);
                net.minecraft.resources.ResourceKey<EntityType<?>> key = net.minecraft.resources.ResourceKey.create(
                                net.minecraft.core.registries.Registries.ENTITY_TYPE, id);
                EntityType<T> registry = Registry.register(BuiltInRegistries.ENTITY_TYPE, id,
                                EntityType.Builder.of(factory, category)
                                                .sized(width, height)
                                                .clientTrackingRange(clientTrackingRange)
                                                .build(key));
                return () -> registry;
        }

        public static <T extends AbstractContainerMenu> @NotNull Supplier<MenuType<T>> registerMenuType(String name,
                        @NotNull Supplier<MenuType<T>> menu) {
                var registry = Registry.register(BuiltInRegistries.MENU,
                                ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, name),
                                menu.get());
                return () -> registry;
        }

        @SuppressWarnings("unchecked")
        public static <T extends AbstractContainerMenu> MenuType<T> createMenuType(
                        NaturalistMenus.MenuFactory<T> factory) {
                // For Fabric 1.21, use basic MenuType with tri-function
                // ExtendedScreenHandlerType API changed - using simple approach
                return (MenuType<T>) new net.minecraft.world.inventory.MenuType<>(
                                (syncId, inventory) -> factory.create(syncId, inventory, null),
                                net.minecraft.world.flag.FeatureFlags.VANILLA_SET);
        }

        public static void openMenu(ServerPlayer player, MenuProvider provider) {
                player.openMenu(provider);
        }

        public static <T extends Potion> Supplier<T> registerPotion(String name, Supplier<T> potion) {
                T registry = Registry.register(BuiltInRegistries.POTION,
                                ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, name),
                                potion.get());
                return () -> registry;
        }

        public static void registerBrewingRecipe(Holder<Potion> input, Item potionIngredient, Holder<Potion> output) {
                RECIPES.add(new BrewingRecipe(input, potionIngredient, output));
        }

        public static <T extends Mob> void registerSpawnPlacement(EntityType<T> entityType,
                        SpawnPlacementType decoratorType, Heightmap.Types heightMapType,
                        SpawnPlacements.SpawnPredicate<T> decoratorPredicate) {
                SpawnPlacementsInvoker.invokeRegister(entityType, decoratorType, heightMapType, decoratorPredicate);
        }

        public static void registerCompostable(float chance, ItemLike item) {
                CompostingChanceRegistry.INSTANCE.add(item, chance);
        }

        /*
         * public static void registerRecipes(String name, Supplier<RecipeType<?>> type,
         * Supplier<RecipeSerializer<?>> serializer) {
         * Registry.register(BuiltInRegistries.RECIPE_TYPE, new
         * ResourceLocation("naturalist", name), type.get());
         * Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, new
         * ResourceLocation("naturalist", name),
         * serializer.get());
         * }
         */

        public static TagKey<Item> getShearsTag() {
                return ConventionalItemTags.SHEARS;
        }
}
