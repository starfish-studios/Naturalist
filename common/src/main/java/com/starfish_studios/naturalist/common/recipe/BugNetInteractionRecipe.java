package com.starfish_studios.naturalist.common.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.starfish_studios.naturalist.core.registry.NaturalistRecipes;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public record BugNetInteractionRecipe(EntityType<?> entityType, ItemStack dropStack) implements Recipe<Container> {

    @Override
    public boolean matches(Container container, Level level) {
        return false;
    }

    @Override
    public ItemStack assemble(Container container, RegistryAccess registryAccess) {
        return dropStack;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return false;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return dropStack;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return NaturalistRecipes.BUG_NET_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return NaturalistRecipes.BUG_NET;
    }

    public static class Serializer implements RecipeSerializer<BugNetInteractionRecipe> {
        private static final Codec<BugNetInteractionRecipe> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("entity_type").forGetter(BugNetInteractionRecipe::entityType),
            ItemStack.ITEM_WITH_COUNT_CODEC.fieldOf("result").forGetter(BugNetInteractionRecipe::dropStack)
        ).apply(instance, BugNetInteractionRecipe::new));

        @Override
        public Codec<BugNetInteractionRecipe> codec() {
            return CODEC;
        }

        @Override
        public BugNetInteractionRecipe fromNetwork(FriendlyByteBuf buffer) {
            ItemStack output = buffer.readItem();
            EntityType<?> entityType = BuiltInRegistries.ENTITY_TYPE.get(buffer.readResourceLocation());

            return new BugNetInteractionRecipe(entityType, output);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, BugNetInteractionRecipe recipe) {
            buffer.writeItem(recipe.dropStack);
            buffer.writeResourceLocation(BuiltInRegistries.ENTITY_TYPE.getKey(recipe.entityType));
        }
    }
}
