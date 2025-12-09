package com.starfish_studios.naturalist.common.item;

import com.starfish_studios.naturalist.common.entity.core.Catchable;
import com.starfish_studios.naturalist.common.recipe.BugNetInteractionRecipe;
import com.starfish_studios.naturalist.core.registry.NaturalistRecipes;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;

public class BugNetItem extends Item {
    public BugNetItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult interactLivingEntity(@NotNull ItemStack stack, Player player, @NotNull LivingEntity interactionTarget, @NotNull InteractionHand usedHand) {
        if (player.getCooldowns().isOnCooldown(this)) {
            return InteractionResult.PASS;
        }
        
        if (tryCatchEntity(stack, player, interactionTarget)) {
            return InteractionResult.SUCCESS;
        }
        return super.interactLivingEntity(stack, player, interactionTarget, usedHand);
    }

    @Override
    public boolean hurtEnemy(@NotNull ItemStack stack, @NotNull LivingEntity target, @NotNull LivingEntity attacker) {
        if (attacker instanceof Player player) {
            if (player.getCooldowns().isOnCooldown(this)) {
                return super.hurtEnemy(stack, target, attacker);
            }
            
            if (tryCatchEntity(stack, player, target)) {
                return true;
            }
        }
        return super.hurtEnemy(stack, target, attacker);
    }

    @SuppressWarnings("deprecation")
    private boolean tryCatchEntity(@NotNull ItemStack stack, Player player, @NotNull LivingEntity target) {
        var recipeManager = player.level().getRecipeManager();
        List<BugNetInteractionRecipe> recipes = recipeManager.getAllRecipesFor(NaturalistRecipes.BUG_NET)
                .stream()
                .filter(r -> r.entityType() == target.getType())
                .sorted(Comparator.comparingInt(BugNetInteractionRecipe::priority))
                .toList();

        for (BugNetInteractionRecipe bugNetRecipe : recipes) {
            if (bugNetRecipe.inputItems() != null && !bugNetRecipe.inputItems().isEmpty()) {
                boolean hasInput = false;
                ItemStack matchedInput = null;
                int matchedSlot = -1;
                int requiredCount = 1;
                
                for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                    ItemStack inventoryStack = player.getInventory().getItem(i);
                    if (inventoryStack.isEmpty()) {
                        continue;
                    }
                    
                    for (ItemStack requiredInput : bugNetRecipe.inputItems()) {
                        if (ItemStack.isSameItem(inventoryStack, requiredInput) && 
                            ItemStack.isSameItemSameTags(inventoryStack, requiredInput)) {
                            int neededCount = requiredInput.getCount();
                            if (inventoryStack.getCount() >= neededCount) {
                                hasInput = true;
                                matchedInput = inventoryStack;
                                matchedSlot = i;
                                requiredCount = neededCount;
                                break;
                            }
                        }
                    }
                    
                    if (hasInput) {
                        break;
                    }
                }
                
                if (!hasInput) {
                    continue;
                }
                
                if (!player.getAbilities().instabuild) {
                    matchedInput.shrink(requiredCount);
                    if (matchedInput.isEmpty()) {
                        player.getInventory().setItem(matchedSlot, ItemStack.EMPTY);
                    }
                }
            }
            
            var caughtItem = bugNetRecipe.dropStack().copy();
            
            if (target instanceof Catchable catchable) {
                catchable.saveToHandTag(caughtItem);
            }
            
            if (!player.getInventory().add(caughtItem)) {
                Containers.dropItemStack(player.level(), target.getX(), target.getY(), target.getZ(), caughtItem);
            }
            
            String playSound = bugNetRecipe.playSound();
            if (playSound == null) {
                var random = player.level().getRandom();
                player.playSound(SoundEvents.PLAYER_ATTACK_SWEEP, 1.0F, 1.0F + (random.nextFloat() - random.nextFloat()) * 0.2F);
            } else if (!playSound.equals("false")) {
                SoundEvent soundEvent = BuiltInRegistries.SOUND_EVENT.get(new ResourceLocation(playSound));
                if (soundEvent != null) {
                    var random = player.level().getRandom();
                    player.playSound(soundEvent, 1.0F, 1.0F + (random.nextFloat() - random.nextFloat()) * 0.2F);
                }
            }
            
            if (bugNetRecipe.shouldSwing() && player.level() instanceof ServerLevel serverLevel) {
                serverLevel.broadcastEntityEvent(player, (byte) 4);
                
                double d0 = -Mth.sin(player.getYRot() * ((float)Math.PI / 180F));
                double d1 = Mth.cos(player.getYRot() * ((float)Math.PI / 180F));
                serverLevel.sendParticles(ParticleTypes.SWEEP_ATTACK, player.getX() + d0, player.getY(0.5D), player.getZ() + d1, 0, d0, 0.0D, d1, 0.0D);
            }
            
            target.discard();
            
            if (!player.getAbilities().instabuild) {
                InteractionHand hand = player.getMainHandItem() == stack ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
                stack.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(hand));
            }
            
            player.getCooldowns().addCooldown(this, 40);
            
            return true;
        }
        
        return false;
    }
    
    @Override
    public boolean isValidRepairItem(@NotNull ItemStack stack, ItemStack repairItem) {
        return repairItem.is(Items.STRING);
    }
}