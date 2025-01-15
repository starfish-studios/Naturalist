package com.starfish_studios.naturalist.registry;

import com.starfish_studios.naturalist.Naturalist;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class NaturalistCreativeModeTabs {
    public static final ResourceKey<CreativeModeTab> TAB_KEY = ResourceKey.create(Registries.CREATIVE_MODE_TAB, ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "tab"));

    public static void registerCreativeModeTabs() {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, TAB_KEY, CreativeModeTab.builder(null, -1)
                .title(Component.translatable("itemGroup.naturalist.tab"))
                .icon(() -> new ItemStack(NaturalistRegistry.BUG_NET))
                .displayItems((itemDisplayParameters, output) -> {
                    output.accept(NaturalistRegistry.CHRYSALIS);
                    output.accept(NaturalistRegistry.DUCKWEED);
                    output.accept(NaturalistRegistry.GLOW_GOOP);
                    output.accept(NaturalistRegistry.AZURE_FROGLASS);
                    output.accept(NaturalistRegistry.VERDANT_FROGLASS);
                    output.accept(NaturalistRegistry.CRIMSON_FROGLASS);
                    output.accept(NaturalistRegistry.AZURE_FROGLASS_PANE);
                    output.accept(NaturalistRegistry.VERDANT_FROGLASS_PANE);
                    output.accept(NaturalistRegistry.CRIMSON_FROGLASS_PANE);
                    output.accept(NaturalistRegistry.CATTAIL);
                    output.accept(NaturalistRegistry.TORTOISE_EGG);
                    output.accept(NaturalistRegistry.ALLIGATOR_EGG);
                    output.accept(NaturalistRegistry.SNAIL_EGGS);
                    output.accept(NaturalistRegistry.TEDDY_BEAR);
                    output.accept(NaturalistRegistry.SHELLSTONE);
                    output.accept(NaturalistRegistry.SHELLSTONE_STAIRS);
                    output.accept(NaturalistRegistry.SHELLSTONE_SLAB);
                    output.accept(NaturalistRegistry.SHELLSTONE_WALL);
                    output.accept(NaturalistRegistry.SHELLSTONE_BRICKS);
                    output.accept(NaturalistRegistry.SHELLSTONE_BRICK_STAIRS);
                    output.accept(NaturalistRegistry.SHELLSTONE_BRICK_SLAB);
                    output.accept(NaturalistRegistry.SHELLSTONE_BRICK_WALL);
                    output.accept(NaturalistRegistry.CUT_SHELLSTONE);
                    output.accept(NaturalistRegistry.CUT_SHELLSTONE_STAIRS);
                    output.accept(NaturalistRegistry.CUT_SHELLSTONE_SLAB);
                    output.accept(NaturalistRegistry.CUT_SHELLSTONE_WALL);
                    output.accept(NaturalistRegistry.SMOOTH_SHELLSTONE);
                    output.accept(NaturalistRegistry.SMOOTH_SHELLSTONE_STAIRS);
                    output.accept(NaturalistRegistry.SMOOTH_SHELLSTONE_SLAB);
                    output.accept(NaturalistRegistry.SMOOTH_SHELLSTONE_WALL);
                    output.accept(NaturalistRegistry.CATTAIL_FLUFF);
                    output.accept(NaturalistRegistry.DUCK_EGG);
                    output.accept(NaturalistRegistry.COOKED_EGG);
                    output.accept(NaturalistRegistry.ANTLER);
                    output.accept(NaturalistRegistry.DUCK);
                    output.accept(NaturalistRegistry.COOKED_DUCK);
                    output.accept(NaturalistRegistry.VENISON);
                    output.accept(NaturalistRegistry.COOKED_VENISON);
                    output.accept(NaturalistRegistry.LIZARD_TAIL);
                    output.accept(NaturalistRegistry.COOKED_LIZARD_TAIL);
                    output.accept(NaturalistRegistry.BEAR_FUR);
                    output.accept(NaturalistRegistry.BUTTERFLY);
                    output.accept(NaturalistRegistry.CATERPILLAR);
                    output.accept(NaturalistRegistry.BUG_NET);
                    output.accept(NaturalistRegistry.SNAIL_BUCKET);
                    output.accept(NaturalistRegistry.SNAIL_SHELL);
                    output.accept(NaturalistRegistry.CATFISH_BUCKET);
                    output.accept(NaturalistRegistry.CATFISH);
                    output.accept(NaturalistRegistry.COOKED_CATFISH);
                    output.accept(NaturalistRegistry.BASS_BUCKET);
                    output.accept(NaturalistRegistry.BASS);
                    output.accept(NaturalistRegistry.COOKED_BASS);
                    output.accept(NaturalistRegistry.SNAIL_SPAWN_EGG);
                    output.accept(NaturalistRegistry.BEAR_SPAWN_EGG);
                    output.accept(NaturalistRegistry.BUTTERFLY_SPAWN_EGG);
                    output.accept(NaturalistRegistry.FIREFLY_SPAWN_EGG);
                    output.accept(NaturalistRegistry.SNAKE_SPAWN_EGG);
                    output.accept(NaturalistRegistry.CORAL_SNAKE_SPAWN_EGG);
                    output.accept(NaturalistRegistry.RATTLESNAKE_SPAWN_EGG);
                    output.accept(NaturalistRegistry.DEER_SPAWN_EGG);
                }).build());
    }
}
