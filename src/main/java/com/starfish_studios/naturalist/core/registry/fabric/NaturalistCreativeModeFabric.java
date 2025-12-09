package com.starfish_studios.naturalist.core.registry.fabric;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.core.registry.NaturalistCreativeTabContents;
import com.starfish_studios.naturalist.core.registry.NaturalistItems;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;

public class NaturalistCreativeModeFabric {

    @SuppressWarnings("unused")
    public static final CreativeModeTab ITEM_GROUP = register("item_group", FabricItemGroup.builder().icon(NaturalistItems.BUG_NET.get()::getDefaultInstance).title(Component.translatable("itemGroup.naturalist.tab")).displayItems((featureFlagSet, output) -> {
        NaturalistCreativeTabContents.addItems(output);
            }).build()
    );

    @SuppressWarnings("all")
    private static CreativeModeTab register(String id, CreativeModeTab tab) {
        return Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, new ResourceLocation(Naturalist.MOD_ID, id), tab);
    }

    public static void init() {
    }
}