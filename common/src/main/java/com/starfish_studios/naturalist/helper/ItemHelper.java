package com.starfish_studios.naturalist.helper;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ItemHelper {

    //TODO: write a proper giveItemToPlayer function that only spawns an ItemEntity if the player has no room for the item
    public static void spawnItemOnEntity(LivingEntity entity, ItemStack stack) {
        Level level = entity.level;
        ItemEntity itemEntity = new ItemEntity(level, entity.getX(), entity.getY() + 0.5, entity.getZ(), stack);
        itemEntity.setPickUpDelay(0);
        itemEntity.setDeltaMovement(itemEntity.getDeltaMovement().multiply(0, 1, 0));
        level.addFreshEntity(itemEntity);
    }
}