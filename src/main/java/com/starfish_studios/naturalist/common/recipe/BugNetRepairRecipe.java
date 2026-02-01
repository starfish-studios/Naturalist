package com.starfish_studios.naturalist.common.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.CraftingInput;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class BugNetRepairRecipe implements CraftingRecipe {
    private final CraftingBookCategory category;

    public BugNetRepairRecipe(CraftingBookCategory category) {
        this.category = category;
    }

    @Override
    public boolean matches(CraftingInput input, @NotNull Level level) {
        // Hardcoded check: 1 Bug Net + 1 String
        boolean hasNet = false;
        boolean hasString = false;
        int otherItems = 0;

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.isEmpty())
                continue;

            if (stack.getItem() instanceof com.starfish_studios.naturalist.common.item.BugNetItem) {
                if (hasNet)
                    return false; // Only 1 net
                hasNet = true;
            } else if (stack.is(net.minecraft.world.item.Items.STRING)) {
                if (hasString)
                    return false; // Only 1 string
                hasString = true;
            } else {
                return false; // No other items
            }
        }
        return hasNet && hasString;
    }

    @Override
    public @NotNull ItemStack assemble(CraftingInput input, @NotNull HolderLookup.Provider provider) {
        ItemStack netStack = ItemStack.EMPTY;
        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.getItem() instanceof com.starfish_studios.naturalist.common.item.BugNetItem) {
                netStack = stack;
                break;
            }
        }

        if (netStack.isEmpty())
            return ItemStack.EMPTY;

        ItemStack result = netStack.copy();
        result.setDamageValue(0); // View file of BugNetItem to see if it uses damage or NBT
        return result;
    }

    // @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    // @Override
    public @NotNull ItemStack getResultItem(@NotNull HolderLookup.Provider provider) {
        // Return a generic Bug Net for display
        return new ItemStack(com.starfish_studios.naturalist.core.registry.NaturalistItems.BUG_NET.get());
    }

    // @Override
    public @NotNull CraftingBookCategory category() {
        return this.category;
    }

    @Override
    public @NotNull RecipeSerializer<? extends CraftingRecipe> getSerializer() {
        return com.starfish_studios.naturalist.core.registry.NaturalistRecipes.BUG_NET_REPAIR_SERIALIZER;
    }

    @Override
    public @NotNull RecipeType<CraftingRecipe> getType() {
        return RecipeType.CRAFTING;
    }

    // Explicitly implemented to avoid errors
    @Override
    public net.minecraft.world.item.crafting.PlacementInfo placementInfo() {
        return net.minecraft.world.item.crafting.PlacementInfo.NOT_PLACEABLE;
    }

    public static class Serializer implements RecipeSerializer<BugNetRepairRecipe> {
        public static final MapCodec<BugNetRepairRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC)
                        .forGetter(BugNetRepairRecipe::category))
                .apply(instance, BugNetRepairRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, BugNetRepairRecipe> STREAM_CODEC = StreamCodec
                .composite(
                        CraftingBookCategory.STREAM_CODEC, BugNetRepairRecipe::category,
                        BugNetRepairRecipe::new);

        @Override
        public @NotNull MapCodec<BugNetRepairRecipe> codec() {
            return CODEC;
        }

        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, BugNetRepairRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
