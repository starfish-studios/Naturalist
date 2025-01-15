package com.starfish_studios.naturalist.platform.fabric;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.common.item.fabric.*;
import com.starfish_studios.naturalist.mixin.fabric.SpawnPlacementsInvoker;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.*;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.Fluid;

import java.util.function.Supplier;

public class CommonPlatformHelperImpl {

    public static <T extends Block> Supplier<T> registerBlock(String name, Supplier<T> block) {
        T registry = Registry.register(BuiltInRegistries.BLOCK, ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, name), block.get());
        return () -> registry;
    }

    public static <T extends BlockEntity> Supplier<BlockEntityType<T>> registerBlockEntityType(String name, Supplier<BlockEntityType<T>> factory) {
        BlockEntityType<T> registry = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, name), factory.get());
        return () -> registry;
    }

    public static <T extends Item> Supplier<T> registerItem(String name, Supplier<T> item) {
        T registry = Registry.register(BuiltInRegistries.ITEM, ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, name), item.get());
        /*
        if (shouldPutInCreativeTab) {
            ItemGroupEvents.modifyEntriesEvent(Naturalist.TAB_KEY).register(content -> {
                content.accept(item.get());
            });
        }
         */
        return () -> registry;
    }

    public static <T extends Mob> Supplier<SpawnEggItem> registerSpawnEggItem(String name, Supplier<EntityType<T>> entityType, int backgroundColor, int highlightColor) {
        return registerItem(name, () -> new SpawnEggItem(entityType.get(), new Item.Properties()));
    }

    public static Supplier<Item> registerNoFluidMobBucketItem(String name, Supplier<? extends EntityType<? extends Mob>> entitySupplier, Supplier<? extends Fluid> fluidSupplier, Supplier<? extends SoundEvent> soundSupplier) {
        return registerItem(name, () ->  new NoFluidMobBucketItem(entitySupplier.get(), fluidSupplier.get(), soundSupplier.get(), new Item.Properties().stacksTo(1)));
    }

    public static Supplier<Item> registerNoFluidMobBucketItem(String name, Supplier<? extends EntityType<? extends Mob>> entitySupplier, Supplier<? extends Fluid> fluidSupplier, Supplier<? extends SoundEvent> soundSupplier, int color) {
        return registerItem(name, () ->  new NoFluidMobBucketWithVariantsItem(entitySupplier, fluidSupplier.get(), soundSupplier.get(), color, new Item.Properties().stacksTo(1)));
    }

    public static Supplier<Item> registerMobBucketItem(String name, Supplier<? extends EntityType<? extends Mob>> entitySupplier, Supplier<? extends Fluid> fluidSupplier, Supplier<? extends SoundEvent> soundSupplier) {
        return registerItem(name, () ->  new MobBucketItem(entitySupplier.get(), fluidSupplier.get(), soundSupplier.get(), new Item.Properties().stacksTo(1)));
    }

    public static Supplier<Item> registerCaughtMobItem(String name, Supplier<? extends EntityType<? extends Mob>> entitySupplier, Supplier<? extends Fluid> fluidSupplier, Supplier<? extends SoundEvent> soundSupplier) {
        return registerItem(name, () ->  new CaughtMobItem(entitySupplier.get(), fluidSupplier.get(), soundSupplier.get(), new Item.Properties().stacksTo(1)));
    }

    public static Supplier<Item> registerCaughtMobItem(String name, Supplier<? extends EntityType<? extends Mob>> entitySupplier, Supplier<? extends Fluid> fluidSupplier, Supplier<? extends SoundEvent> soundSupplier, int variantAmount) {
        return registerItem(name, () ->  new CaughtMobWithVariantsItem(entitySupplier.get(), fluidSupplier.get(), soundSupplier.get(), variantAmount, new Item.Properties().stacksTo(1)));
    }

    public static <T extends SoundEvent> Supplier<T> registerSoundEvent(String name, Supplier<T> soundEvent) {
        T registry = Registry.register(BuiltInRegistries.SOUND_EVENT, ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, name), soundEvent.get());
        return () -> registry;
    }

    private static ResourceKey<EntityType<?>> naturalistEntityId(String $$0) {
        return ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, $$0));
    }

    public static <T extends AbstractContainerMenu> Supplier<MenuType<T>> registerMenuType(String name, Supplier<MenuType<T>> menu) {
        var registry = Registry.register(BuiltInRegistries.MENU, ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, name), menu.get());
        return () -> registry;
    }

    public static void openMenu(ServerPlayer player, MenuProvider provider) {
        player.openMenu(provider);
    }

    public static Holder<Potion> registerPotion(String name, Potion potion) {
        Holder.Reference<Potion> registry = Registry.registerForHolder(BuiltInRegistries.POTION, ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, name), potion);
        return registry;
    }

    public static <T extends Mob> void registerSpawnPlacement(EntityType<T> entityType, SpawnPlacementType decoratorType, Heightmap.Types heightMapType, SpawnPlacements.SpawnPredicate<T> decoratorPredicate) {
        SpawnPlacementsInvoker.invokeRegister(entityType, decoratorType, heightMapType, decoratorPredicate);
    }

    public static void registerCompostable(float chance, ItemLike item) {
        CompostingChanceRegistry.INSTANCE.add(item, chance);
    }
}
