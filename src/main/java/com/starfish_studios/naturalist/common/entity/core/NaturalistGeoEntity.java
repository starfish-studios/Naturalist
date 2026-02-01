package com.starfish_studios.naturalist.common.entity.core;

import software.bernie.geckolib.animatable.GeoAnimatable;

public interface NaturalistGeoEntity extends GeoAnimatable {

    default double getTick(Object object) {
        if (object instanceof net.minecraft.world.entity.Entity entity) {
            return entity.tickCount;
        }
        return 0;
    }

    default double getBoneResetTime() {
        return 5;
    }
}
