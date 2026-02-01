package com.starfish_studios.naturalist.common.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.starfish_studios.naturalist.core.registry.NaturalistRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

/**
 * A custom recipe type for Bug Net interactions with entities.
 * In 1.21, Recipe now extends RecipeInput instead of Container.
 */
public record BugNetInteractionRecipe(EntityType<?> entityType, ItemStack dropStack,
        Optional<List<ItemStack>> inputItems, int priority, boolean shouldSwing, Optional<String> playSound)
        implements Recipe<BugNetInteractionRecipe.BugNetInput> {

    /**
     * Simple RecipeInput implementation for Bug Net interactions
     */
    public static class BugNetInput implements RecipeInput {
        private final ItemStack heldItem;

        public BugNetInput(ItemStack heldItem) {
            this.heldItem = heldItem;
        }

        @Override
        public @NotNull ItemStack getItem(int slot) {
            return slot == 0 ? heldItem : ItemStack.EMPTY;
        }

        @Override
        public int size() {
            return 1;
        }
    }

    @Override
    public boolean matches(@NotNull BugNetInput input, @NotNull Level level) {
        // Bug net recipes match by entity type, not by container contents
        return false;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull BugNetInput input, @NotNull HolderLookup.Provider provider) {
        return dropStack.copy();
    }

    // 1.21 API: placementInfo() tells the game how to display this recipe
    // Returning null indicates this recipe shouldn't be placed in the recipe book
    // grid
    @Override
    @Nullable
    public PlacementInfo placementInfo() {
        return null; // Custom recipes don't need placement info
    }

    // 1.21 API: recipeBookCategory() determines where this recipe appears in recipe
    // book
    @Override
    public @NotNull RecipeBookCategory recipeBookCategory() {
        // Return MISC category - this is a special recipe that doesn't fit standard
        // categories
        return RecipeBookCategories.CRAFTING_MISC;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public @NotNull RecipeSerializer<BugNetInteractionRecipe> getSerializer() {
        return (RecipeSerializer<BugNetInteractionRecipe>) NaturalistRecipes.BUG_NET_SERIALIZER;
    }

    @Override
    public @NotNull RecipeType<BugNetInteractionRecipe> getType() {
        return NaturalistRecipes.BUG_NET;
    }

    public static class Serializer implements RecipeSerializer<BugNetInteractionRecipe> {
        public static final MapCodec<BugNetInteractionRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
                .group(
                        BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("entity_type")
                                .forGetter(BugNetInteractionRecipe::entityType),
                        ItemStack.CODEC.fieldOf("result").forGetter(BugNetInteractionRecipe::dropStack),
                        ItemStack.CODEC.listOf().optionalFieldOf("ingredients")
                                .forGetter(BugNetInteractionRecipe::inputItems),
                        Codec.INT.optionalFieldOf("priority", 0).forGetter(BugNetInteractionRecipe::priority),
                        Codec.BOOL.optionalFieldOf("should_swing", true)
                                .forGetter(BugNetInteractionRecipe::shouldSwing),
                        Codec.STRING.optionalFieldOf("play_sound").forGetter(BugNetInteractionRecipe::playSound))
                .apply(instance, BugNetInteractionRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, BugNetInteractionRecipe> STREAM_CODEC = StreamCodec
                .composite(
                        ByteBufCodecs.registry(BuiltInRegistries.ENTITY_TYPE.key()),
                        BugNetInteractionRecipe::entityType,
                        ItemStack.STREAM_CODEC, BugNetInteractionRecipe::dropStack,
                        ItemStack.STREAM_CODEC.apply(ByteBufCodecs.list()).apply(ByteBufCodecs::optional),
                        BugNetInteractionRecipe::inputItems,
                        ByteBufCodecs.INT, BugNetInteractionRecipe::priority,
                        ByteBufCodecs.BOOL, BugNetInteractionRecipe::shouldSwing,
                        ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs::optional), BugNetInteractionRecipe::playSound,
                        BugNetInteractionRecipe::new);

        @Override
        public @NotNull MapCodec<BugNetInteractionRecipe> codec() {
            return CODEC;
        }

        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, BugNetInteractionRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}