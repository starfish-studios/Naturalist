package com.starfish_studios.naturalist.core.registry;

import com.starfish_studios.naturalist.core.platform.CommonPlatformHelper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;

public class NaturalistBrewingRecipes {
        public static void register() {
                CommonPlatformHelper.registerBrewingRecipe(Potions.AWKWARD,
                                NaturalistItems.ANTLER.get(),
                                BuiltInRegistries.POTION.wrapAsHolder(NaturalistPotions.FOREST_DASHER.get()));
                CommonPlatformHelper.registerBrewingRecipe(
                                BuiltInRegistries.POTION.wrapAsHolder(NaturalistPotions.FOREST_DASHER.get()),
                                Items.REDSTONE,
                                BuiltInRegistries.POTION.wrapAsHolder(NaturalistPotions.LONG_FOREST_DASHER.get()));
                CommonPlatformHelper.registerBrewingRecipe(
                                BuiltInRegistries.POTION.wrapAsHolder(NaturalistPotions.FOREST_DASHER.get()),
                                Items.GLOWSTONE_DUST,
                                BuiltInRegistries.POTION.wrapAsHolder(NaturalistPotions.STRONG_FOREST_DASHER.get()));
        }
}
