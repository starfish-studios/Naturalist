package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.common.entity.Catfish;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

@Environment(EnvType.CLIENT)
public class CatfishModel extends GeoModel<Catfish> {
    @Override
    public ResourceLocation getModelResource(Catfish catfish) {
        return new ResourceLocation(Naturalist.MOD_ID, "geo/entity/catfish.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Catfish catfish) {
        return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/catfish.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Catfish catfish) {
        return new ResourceLocation(Naturalist.MOD_ID, "animations/entity/catfish.animation.json");
    }
}
