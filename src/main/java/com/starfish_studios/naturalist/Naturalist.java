package com.starfish_studios.naturalist;

import com.starfish_studios.naturalist.core.registry.NaturalistBlockEntities;
import com.starfish_studios.naturalist.core.registry.NaturalistBrewingRecipes;
import com.starfish_studios.naturalist.core.registry.NaturalistCompostables;
import com.starfish_studios.naturalist.core.registry.NaturalistDispenserBehaviors;
import com.starfish_studios.naturalist.core.registry.NaturalistEntityTypes;
import com.starfish_studios.naturalist.core.registry.NaturalistPotions;
import com.starfish_studios.naturalist.core.registry.NaturalistRecipes;
import com.starfish_studios.naturalist.core.registry.NaturalistRegistry;
import com.starfish_studios.naturalist.core.registry.NaturalistSoundEvents;
import com.starfish_studios.naturalist.core.registry.NaturalistSpawnPlacements;

public class Naturalist {
    public static final String MOD_ID = "naturalist";

    public static void init() {
        NaturalistRegistry.init();
        NaturalistBlockEntities.init();
        NaturalistSoundEvents.init();
        NaturalistEntityTypes.init();
        NaturalistPotions.init();
        NaturalistRecipes.register();
    }

    public static void registerDispenserBehaviors() {
        NaturalistDispenserBehaviors.register();
    }

    public static void registerBrewingRecipes() {
        NaturalistBrewingRecipes.register();
    }

    public static void registerSpawnPlacements() {
        NaturalistSpawnPlacements.register();
    }

    public static void registerCompostables() {
        NaturalistCompostables.register();
    }
}
