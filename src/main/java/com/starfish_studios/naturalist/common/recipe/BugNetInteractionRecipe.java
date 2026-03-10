package com.starfish_studios.naturalist.common.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.starfish_studios.naturalist.core.registry.NaturalistRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public record BugNetInteractionRecipe(EntityType<?> entityType, ItemStack dropStack) implements Recipe<RecipeInput> {

    @Override
    public boolean matches(RecipeInput input, Level level) {
        return false;
    }

    @Override
    public @NotNull ItemStack assemble(RecipeInput input, HolderLookup.Provider registries) {
        return dropStack.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return false;
    }

    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.Provider registries) {
        return dropStack;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return NaturalistRecipes.BUG_NET_SERIALIZER;
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return NaturalistRecipes.BUG_NET;
    }

    public static class Serializer implements RecipeSerializer<BugNetInteractionRecipe> {
        public static final MapCodec<BugNetInteractionRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("entity_type").forGetter(BugNetInteractionRecipe::entityType),
                ItemStack.CODEC.fieldOf("result").forGetter(BugNetInteractionRecipe::dropStack)
            ).apply(instance, BugNetInteractionRecipe::new)
        );

        public static final StreamCodec<RegistryFriendlyByteBuf, BugNetInteractionRecipe> STREAM_CODEC =
            StreamCodec.of(
                (buf, recipe) -> {
                    buf.writeResourceLocation(BuiltInRegistries.ENTITY_TYPE.getKey(recipe.entityType()));
                    ItemStack.STREAM_CODEC.encode(buf, recipe.dropStack());
                },
                buf -> {
                    EntityType<?> entityType = BuiltInRegistries.ENTITY_TYPE.get(buf.readResourceLocation());
                    ItemStack dropStack = ItemStack.STREAM_CODEC.decode(buf);
                    return new BugNetInteractionRecipe(entityType, dropStack);
                }
            );

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
