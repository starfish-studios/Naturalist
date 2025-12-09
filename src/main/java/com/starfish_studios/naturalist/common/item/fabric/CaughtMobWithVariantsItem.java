package com.starfish_studios.naturalist.common.item.fabric;

import net.minecraft.sounds.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.material.*;
import org.jetbrains.annotations.NotNull;

public class CaughtMobWithVariantsItem extends CaughtMobItem {
    public CaughtMobWithVariantsItem(@NotNull EntityType<?> entitySupplier, @NotNull Fluid fluid, SoundEvent emptyingSound, @NotNull Properties settings) {
        super(entitySupplier, fluid, emptyingSound, settings);
    }
}
