//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.starfish_studios.naturalist.common.entity.core;

import java.util.Optional;

import com.starfish_studios.naturalist.common.entity.*;
import com.starfish_studios.naturalist.common.helper.*;
import com.starfish_studios.naturalist.core.registry.*;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public interface Catchable {
    boolean fromHand();

    void setFromHand(boolean fromHand);

    void saveToHandTag(ItemStack stack);

    void loadFromHandTag(CompoundTag tag);

    ItemStack getCaughtItemStack();

    SoundEvent getPickupSound();

    /** @deprecated */
    @Deprecated
    static void saveDefaultDataToHandTag(Mob mob, ItemStack hand) {
        CompoundTag compoundTag = hand.getOrCreateTag();
        if (mob.hasCustomName()) {
            hand.setHoverName(mob.getCustomName());
        }

        if (mob.isNoAi()) {
            compoundTag.putBoolean("NoAI", mob.isNoAi());
        }

        if (mob.isSilent()) {
            compoundTag.putBoolean("Silent", mob.isSilent());
        }

        if (mob.isNoGravity()) {
            compoundTag.putBoolean("NoGravity", mob.isNoGravity());
        }

        if (mob.hasGlowingTag()) {
            compoundTag.putBoolean("Glowing", true);
        }

        if (mob.isInvulnerable()) {
            compoundTag.putBoolean("Invulnerable", mob.isInvulnerable());
        }

        compoundTag.putFloat("Health", mob.getHealth());
    }

    /** @deprecated */

    @Deprecated
    static void loadDefaultDataFromHandTag(Mob mob, CompoundTag tag) {
        if (tag.contains("NoAI")) {
            mob.setNoAi(tag.getBoolean("NoAI"));
        }

        if (tag.contains("Silent")) {
            mob.setSilent(tag.getBoolean("Silent"));
        }

        if (tag.contains("NoGravity")) {
            mob.setNoGravity(tag.getBoolean("NoGravity"));
        }

        if (tag.contains("Glowing")) {
            mob.setGlowingTag(tag.getBoolean("Glowing"));
        }

        if (tag.contains("Invulnerable")) {
            mob.setInvulnerable(tag.getBoolean("Invulnerable"));
        }

        if (tag.contains("Health", 99)) {
            mob.setHealth(tag.getFloat("Health"));
        }

    }

    static <T extends LivingEntity & Catchable> Optional<InteractionResult> catchAnimal(Player player, InteractionHand hand, T entity, boolean needsNet) {
        ItemStack itemStack = player.getItemInHand(hand);
        if ((needsNet ? itemStack.getItem().equals(NaturalistItems.BUG_NET.get()) : itemStack.isEmpty()) && entity.isAlive()) {
            ItemStack caughtItemStack = entity.getCaughtItemStack();
            entity.saveToHandTag(caughtItemStack);
            if (needsNet) {
                itemStack.hurtAndBreak(1, player, (playerEntity) -> {
                    playerEntity.broadcastBreakEvent(hand);
                });
            }
            if (player.getInventory().add(caughtItemStack)) {
                entity.discard();
                return Optional.of(InteractionResult.SUCCESS);
            }
            else {
                ItemHelper.spawnItemOnEntity(player, caughtItemStack);
            }
            player.playSound(SoundEvents.ITEM_PICKUP, 0.3F, 1.0F);
            if (!entity.level().isClientSide) {
                CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer)player, caughtItemStack);
            }
            entity.discard();
            return Optional.of(InteractionResult.SUCCESS);
        } else {
            return Optional.empty();
        }
    }
}
