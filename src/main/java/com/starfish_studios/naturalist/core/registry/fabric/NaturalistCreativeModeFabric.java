package com.starfish_studios.naturalist.core.registry.fabric;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.core.registry.NaturalistRegistry;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class NaturalistCreativeModeFabric {

    @SuppressWarnings("unused")
    public static final CreativeModeTab ITEM_GROUP = register("item_group", FabricItemGroup.builder().icon(NaturalistRegistry.BUG_NET.get()::getDefaultInstance).title(Component.translatable("itemGroup.naturalist.tab")).displayItems((featureFlagSet, output) -> {
        for (ItemStack stack : NaturalistRegistry.collectAllItemStacks()) {
            output.accept(stack);
        }
            }).build()
    );

    @SuppressWarnings("all")
    private static CreativeModeTab register(String id, CreativeModeTab tab) {
        return Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, id), tab);
    }

    public static void init() {
    }
}