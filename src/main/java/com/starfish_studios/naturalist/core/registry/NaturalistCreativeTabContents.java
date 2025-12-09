package com.starfish_studios.naturalist.core.registry;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.ItemLike;

import java.util.function.Supplier;

public class NaturalistCreativeTabContents {

    private static void add(CreativeModeTab.Output output, Supplier<? extends ItemLike> entry) {
        output.accept(entry.get());
    }

    public static void addItems(CreativeModeTab.Output output) {
        add(output, NaturalistBlocks.ALLIGATOR_EGG);
        add(output, NaturalistItems.DUCK_EGG);
        add(output, NaturalistBlocks.TORTOISE_EGG);
        add(output, NaturalistItems.COOKED_EGG);


        add(output, NaturalistItems.BUG_NET);
        add(output, NaturalistItems.GLOW_GOOP);
        add(output, NaturalistItems.CATERPILLAR);
        add(output, NaturalistItems.BUTTERFLY);
        add(output, NaturalistItems.SNAIL_SHELL);
        add(output, NaturalistItems.ANTLER);
        add(output, NaturalistItems.FUR);
        add(output, NaturalistItems.CHRYSALIS);

        add(output, NaturalistItems.DUCK);
        add(output, NaturalistItems.COOKED_DUCK);
        add(output, NaturalistItems.BUSHMEAT);
        add(output, NaturalistItems.COOKED_BUSHMEAT);
        add(output, NaturalistItems.VENISON);
        add(output, NaturalistItems.COOKED_VENISON);
        add(output, NaturalistItems.LIZARD_TAIL);
        add(output, NaturalistItems.COOKED_LIZARD_TAIL);
        add(output, NaturalistItems.CATFISH);
        add(output, NaturalistItems.COOKED_CATFISH);
        add(output, NaturalistItems.BASS);
        add(output, NaturalistItems.COOKED_BASS);

        add(output, NaturalistItems.CATFISH_BUCKET);
        add(output, NaturalistItems.BASS_BUCKET);
        add(output, NaturalistItems.SNAIL_BUCKET);

        add(output, NaturalistBlocks.SNAIL_EGGS);
        add(output, NaturalistBlocks.TEDDY_BEAR);
        add(output, NaturalistBlocks.AZURE_FROGLASS);
        add(output, NaturalistBlocks.VERDANT_FROGLASS);
        add(output, NaturalistBlocks.CRIMSON_FROGLASS);
        add(output, NaturalistBlocks.AZURE_FROGLASS_PANE);
        add(output, NaturalistBlocks.VERDANT_FROGLASS_PANE);
        add(output, NaturalistBlocks.CRIMSON_FROGLASS_PANE);
        add(output, NaturalistBlocks.SHELLSTONE);
        add(output, NaturalistBlocks.SHELLSTONE_STAIRS);
        add(output, NaturalistBlocks.SHELLSTONE_SLAB);
        add(output, NaturalistBlocks.SHELLSTONE_WALL);
        add(output, NaturalistBlocks.SHELLSTONE_BRICKS);
        add(output, NaturalistBlocks.SHELLSTONE_BRICK_STAIRS);
        add(output, NaturalistBlocks.SHELLSTONE_BRICK_SLAB);
        add(output, NaturalistBlocks.SHELLSTONE_BRICK_WALL);
        add(output, NaturalistBlocks.CUT_SHELLSTONE);
        add(output, NaturalistBlocks.CUT_SHELLSTONE_STAIRS);
        add(output, NaturalistBlocks.CUT_SHELLSTONE_SLAB);
        add(output, NaturalistBlocks.CUT_SHELLSTONE_WALL);
        add(output, NaturalistBlocks.SMOOTH_SHELLSTONE);
        add(output, NaturalistBlocks.SMOOTH_SHELLSTONE_STAIRS);
        add(output, NaturalistBlocks.SMOOTH_SHELLSTONE_SLAB);
        add(output, NaturalistBlocks.SMOOTH_SHELLSTONE_WALL);

        add(output, NaturalistItems.ALLIGATOR_SPAWN_EGG);
        add(output, NaturalistItems.BASS_SPAWN_EGG);
        add(output, NaturalistItems.BEAR_SPAWN_EGG);
        add(output, NaturalistItems.BLUEJAY_SPAWN_EGG);
        add(output, NaturalistItems.BOAR_SPAWN_EGG);
        add(output, NaturalistItems.BUTTERFLY_SPAWN_EGG);
        add(output, NaturalistItems.CANARY_SPAWN_EGG);
        add(output, NaturalistItems.CARDINAL_SPAWN_EGG);
        add(output, NaturalistItems.CATFISH_SPAWN_EGG);
        add(output, NaturalistItems.CATERPILLAR_SPAWN_EGG);
        add(output, NaturalistItems.CORAL_SNAKE_SPAWN_EGG);
        add(output, NaturalistItems.DEER_SPAWN_EGG);
        add(output, NaturalistItems.DRAGONFLY_SPAWN_EGG);
        add(output, NaturalistItems.DUCK_SPAWN_EGG);
        add(output, NaturalistItems.ELEPHANT_SPAWN_EGG);
        add(output, NaturalistItems.FINCH_SPAWN_EGG);
        add(output, NaturalistItems.FIREFLY_SPAWN_EGG);
        add(output, NaturalistItems.GIRAFFE_SPAWN_EGG);
        add(output, NaturalistItems.HIPPO_SPAWN_EGG);
        add(output, NaturalistItems.LION_SPAWN_EGG);
        add(output, NaturalistItems.LIZARD_SPAWN_EGG);
        add(output, NaturalistItems.RATTLESNAKE_SPAWN_EGG);
        add(output, NaturalistItems.RHINO_SPAWN_EGG);
        add(output, NaturalistItems.ROBIN_SPAWN_EGG);
        add(output, NaturalistItems.SNAKE_SPAWN_EGG);
        add(output, NaturalistItems.SNAIL_SPAWN_EGG);
        add(output, NaturalistItems.SPARROW_SPAWN_EGG);
        add(output, NaturalistItems.TORTOISE_SPAWN_EGG);
        add(output, NaturalistItems.VULTURE_SPAWN_EGG);
        add(output, NaturalistItems.ZEBRA_SPAWN_EGG);
    }
}

