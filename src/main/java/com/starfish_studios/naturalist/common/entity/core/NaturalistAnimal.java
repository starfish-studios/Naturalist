package com.starfish_studios.naturalist.common.entity.core;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;

public abstract class NaturalistAnimal extends TamableAnimal {

    protected NaturalistAnimal(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
    }
}
