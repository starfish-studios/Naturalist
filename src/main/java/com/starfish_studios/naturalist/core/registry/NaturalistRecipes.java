package com.starfish_studios.naturalist.core.registry;

import com.starfish_studios.naturalist.common.recipe.BugNetInteractionRecipe;
import com.starfish_studios.naturalist.common.recipe.BugNetRepairRecipe;
import com.starfish_studios.naturalist.core.platform.CommonPlatformHelper;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.CraftingRecipe;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

public class NaturalistRecipes {

    public static final RecipeType<BugNetInteractionRecipe> BUG_NET = new RecipeType<>() {
        @Override
        public String toString() {
            return "naturalist:net";
        }
    };

    public static final RecipeSerializer<?> BUG_NET_SERIALIZER = new BugNetInteractionRecipe.Serializer();

    public static final RecipeType<CraftingRecipe> BUG_NET_REPAIR = new RecipeType<>() {
        @Override
        public String toString() {
            return "naturalist:bug_net_repair";
        }
    };

    public static final RecipeSerializer<BugNetRepairRecipe> BUG_NET_REPAIR_SERIALIZER = new BugNetRepairRecipe.Serializer();

    public static void register() {
        Registry.register(BuiltInRegistries.RECIPE_SERIALIZER,
                ResourceLocation.fromNamespaceAndPath("naturalist", "net"), BUG_NET_SERIALIZER);
        Registry.register(BuiltInRegistries.RECIPE_TYPE, ResourceLocation.fromNamespaceAndPath("naturalist", "net"),
                BUG_NET);

        Registry.register(BuiltInRegistries.RECIPE_SERIALIZER,
                ResourceLocation.fromNamespaceAndPath("naturalist", "bug_net_repair"), BUG_NET_REPAIR_SERIALIZER);
        Registry.register(BuiltInRegistries.RECIPE_TYPE,
                ResourceLocation.fromNamespaceAndPath("naturalist", "bug_net_repair"), BUG_NET_REPAIR);
    }
}