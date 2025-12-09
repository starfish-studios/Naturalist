package com.starfish_studios.naturalist.common.item.forge;

import net.minecraft.world.item.Item;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.sounds.SoundEvent;

import java.util.function.Supplier;

public class CaughtMobWithVariantsItem extends CaughtMobItem {

    @SuppressWarnings("unused")
    public CaughtMobWithVariantsItem(Supplier<? extends EntityType<?>> entitySupplier, Supplier<? extends Fluid> fluidSupplier, Supplier<? extends SoundEvent> soundSupplier, int variantCount, Item.Properties properties) {
        super(entitySupplier, fluidSupplier, soundSupplier, properties);
    }
}

