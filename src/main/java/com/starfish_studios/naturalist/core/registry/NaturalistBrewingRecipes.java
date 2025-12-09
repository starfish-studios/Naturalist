package com.starfish_studios.naturalist.core.registry;

import com.starfish_studios.naturalist.core.platform.CommonPlatformHelper;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;

public class NaturalistBrewingRecipes {
    public static void register() {
        CommonPlatformHelper.registerBrewingRecipe(Potions.AWKWARD, NaturalistItems.ANTLER.get(), NaturalistPotions.FOREST_DASHER.get());
        CommonPlatformHelper.registerBrewingRecipe(NaturalistPotions.FOREST_DASHER.get(), Items.REDSTONE, NaturalistPotions.LONG_FOREST_DASHER.get());
        CommonPlatformHelper.registerBrewingRecipe(NaturalistPotions.FOREST_DASHER.get(), Items.GLOWSTONE_DUST, NaturalistPotions.STRONG_FOREST_DASHER.get());
    }
}

