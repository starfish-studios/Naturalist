package com.crispytwig.naturalist.server.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.crispytwig.naturalist.registry.NaturalistRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

@SuppressWarnings("unused")
public record BugNetInteractionRecipe(EntityType<?> entityType, Optional<Ingredient> ingredient, ItemStack dropStack) implements Recipe<RecipeInput> {
    public int findIngredientSlot(@NotNull Player player) {
        if (ingredient.isEmpty()) {
            return -1;
        }
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            if (ingredient.get().test(player.getInventory().getItem(i))) {
                return i;
            }
        }
        return -1;
    }

    public boolean hasIngredient(@NotNull Player player) {
        return ingredient.isEmpty() || findIngredientSlot(player) >= 0;
    }

    public void consumeIngredient(@NotNull Player player) {
        if (ingredient.isEmpty() || player.getAbilities().instabuild) {
            return;
        }
        int slot = findIngredientSlot(player);
        if (slot >= 0) {
            player.getInventory().removeItem(slot, 1);
        }
    }

    @Override
    public boolean matches(@NotNull RecipeInput input, @NotNull Level level) {
        return false;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull RecipeInput input, HolderLookup.@NotNull Provider registries) {
        return dropStack;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return false;
    }

    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.@NotNull Provider registries) {
        return dropStack;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return NaturalistRecipes.BUG_NET_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return NaturalistRecipes.BUG_NET.get();
    }

    public static class Serializer implements RecipeSerializer<BugNetInteractionRecipe> {
        public static final MapCodec<BugNetInteractionRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
                instance.group(
                        BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("entity_type").forGetter(BugNetInteractionRecipe::entityType),
                        Ingredient.CODEC.optionalFieldOf("ingredient").forGetter(BugNetInteractionRecipe::ingredient),
                        ItemStack.CODEC.fieldOf("result").forGetter(BugNetInteractionRecipe::dropStack)
                ).apply(instance, BugNetInteractionRecipe::new)
        );

        public static final StreamCodec<RegistryFriendlyByteBuf, BugNetInteractionRecipe> STREAM_CODEC = StreamCodec.of(
                (buf, recipe) -> {
                    buf.writeResourceLocation(BuiltInRegistries.ENTITY_TYPE.getKey(recipe.entityType));
                    ByteBufCodecs.optional(Ingredient.CONTENTS_STREAM_CODEC).encode(buf, recipe.ingredient);
                    ItemStack.STREAM_CODEC.encode(buf, recipe.dropStack);
                },
                buf -> {
                    EntityType<?> entityType = BuiltInRegistries.ENTITY_TYPE.get(buf.readResourceLocation());
                    Optional<Ingredient> ingredient = ByteBufCodecs.optional(Ingredient.CONTENTS_STREAM_CODEC).decode(buf);
                    return new BugNetInteractionRecipe(entityType, ingredient, ItemStack.STREAM_CODEC.decode(buf));
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
