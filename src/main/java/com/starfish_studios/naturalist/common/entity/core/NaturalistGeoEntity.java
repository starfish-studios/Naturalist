package com.starfish_studios.naturalist.common.entity.core;

import software.bernie.geckolib.animatable.GeoEntity;

public interface NaturalistGeoEntity extends GeoEntity {

    @Override
    default double getBoneResetTime() {
        return 5;
    }
}
