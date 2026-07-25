package com.starfish_studios.naturalist.common.item;

import com.starfish_studios.naturalist.common.recipe.BugNetInteractionRecipe;
import com.starfish_studios.naturalist.core.registry.NaturalistRecipes;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class BugNetItem extends Item {
    public BugNetItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult interactLivingEntity(@NotNull ItemStack stack, Player player, @NotNull LivingEntity interactionTarget, @NotNull InteractionHand usedHand) {

        var recipeManager = player.level().getRecipeManager();
        Optional<BugNetInteractionRecipe> allRecipes = recipeManager.getAllRecipesFor(NaturalistRecipes.BUG_NET)
                .stream()
                .map(recipe -> recipe.value())
                .filter(recipe -> recipe.entityType() == interactionTarget.getType())
                .findFirst();

        if (allRecipes.isPresent()) {
            var dropItem = allRecipes.get().dropStack().copy();
            Containers.dropItemStack(player.level(), interactionTarget.getX(), interactionTarget.getY(), interactionTarget.getZ(), dropItem);
            interactionTarget.discard();
            return InteractionResult.SUCCESS;
        }

        return super.interactLivingEntity(stack, player, interactionTarget, usedHand);
    }
}
