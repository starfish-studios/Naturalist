package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.common.entity.Caterpillar;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

@Environment(EnvType.CLIENT)
public class CaterpillarModel extends GeoModel<Caterpillar> {
    @Override
    public ResourceLocation getModelResource(Caterpillar object) {
        return new ResourceLocation(Naturalist.MOD_ID, "geo/caterpillar.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Caterpillar object) {
        return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/caterpillar.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Caterpillar animatable) {
        return new ResourceLocation(Naturalist.MOD_ID, "animations/caterpillar.animation.json");
    }
}
