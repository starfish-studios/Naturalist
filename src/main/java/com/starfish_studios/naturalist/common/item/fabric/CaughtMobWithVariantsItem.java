package com.starfish_studios.naturalist.common.item.fabric;

import net.minecraft.sounds.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.material.*;
import org.jetbrains.annotations.NotNull;

public class CaughtMobWithVariantsItem extends CaughtMobItem {
    private final int variantAmount;

    public CaughtMobWithVariantsItem(EntityType<? extends Mob> entitySupplier, Fluid fluid, SoundEvent emptyingSound,
            int variantAmount, Properties settings) {
        super(entitySupplier, fluid, emptyingSound, settings);
        this.variantAmount = variantAmount;
    }

    public int getVariantAmount() {
        return variantAmount;
    }
}
