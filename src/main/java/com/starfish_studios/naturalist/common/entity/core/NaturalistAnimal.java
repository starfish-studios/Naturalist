package com.starfish_studios.naturalist.common.entity.core;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;

public abstract class NaturalistAnimal extends Animal {

    protected NaturalistAnimal(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }
}
