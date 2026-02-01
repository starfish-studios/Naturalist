package com.starfish_studios.naturalist.common.entity.core;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import org.jetbrains.annotations.NotNull;

public interface Catchable {
    boolean fromHand();

    void setFromHand(boolean fromHand);

    void saveToHandTag(ItemStack stack);

    void loadFromHandTag(ItemStack stack);

    @Deprecated
    static void saveDefaultDataToHandTag(@NotNull Mob mob, @NotNull ItemStack hand) {
        CustomData.update(DataComponents.BUCKET_ENTITY_DATA, hand, tag -> {
            if (mob.hasCustomName()) {
                // In 1.21, use set method on DataComponents for custom name
                hand.set(DataComponents.CUSTOM_NAME, mob.getCustomName());
            }

            if (mob.isNoAi()) {
                tag.putBoolean("NoAI", mob.isNoAi());
            }

            if (mob.isSilent()) {
                tag.putBoolean("Silent", mob.isSilent());
            }

            if (mob.isNoGravity()) {
                tag.putBoolean("NoGravity", mob.isNoGravity());
            }

            if (mob.hasGlowingTag()) {
                tag.putBoolean("Glowing", true);
            }

            if (mob.isInvulnerable()) {
                tag.putBoolean("Invulnerable", mob.isInvulnerable());
            }

            tag.putFloat("Health", mob.getHealth());
        });
    }

    @Deprecated
    static void loadDefaultDataFromHandTag(@NotNull Mob mob, ItemStack stack) {
        CustomData customData = stack.get(DataComponents.BUCKET_ENTITY_DATA);
        if (customData == null)
            return;
        CompoundTag tag = customData.copyTag();

        // In 1.21, CompoundTag.getBoolean/getInt/getFloat return Optional
        // Use orElse() to get primitive values
        if (tag.contains("NoAI")) {
            mob.setNoAi(tag.getBoolean("NoAI").orElse(false));
        }

        if (tag.contains("Silent")) {
            mob.setSilent(tag.getBoolean("Silent").orElse(false));
        }

        if (tag.contains("NoGravity")) {
            mob.setNoGravity(tag.getBoolean("NoGravity").orElse(false));
        }

        if (tag.contains("Glowing")) {
            mob.setGlowingTag(tag.getBoolean("Glowing").orElse(false));
        }

        if (tag.contains("Invulnerable")) {
            mob.setInvulnerable(tag.getBoolean("Invulnerable").orElse(false));
        }

        if (tag.contains("Health")) {
            mob.setHealth(tag.getFloat("Health").orElse(mob.getHealth()));
        }
    }
}
