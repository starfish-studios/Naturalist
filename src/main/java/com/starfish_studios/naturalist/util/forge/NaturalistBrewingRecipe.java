package com.starfish_studios.naturalist.util.forge;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraftforge.common.brewing.IBrewingRecipe;
import org.jetbrains.annotations.NotNull;

public record NaturalistBrewingRecipe(Potion input, Item ingredient, Potion output) implements IBrewingRecipe {
    @Override
    public boolean isInput(@NotNull ItemStack stack) {
        return PotionUtils.getPotion(stack).equals(this.input);
    }

    @Override
    public boolean isIngredient(ItemStack ingredientStack) {
        return ingredientStack.getItem().equals(this.ingredient);
    }

    @Override
    public @NotNull ItemStack getOutput(@NotNull ItemStack inputStack, @NotNull ItemStack ingredientStack) {
        if (!isInput(inputStack) || !isIngredient(ingredientStack)) {
            return ItemStack.EMPTY;
        }

        ItemStack stack = new ItemStack(inputStack.getItem());
        stack.setTag(new CompoundTag());
        PotionUtils.setPotion(stack, this.output);
        return stack;
    }
}

