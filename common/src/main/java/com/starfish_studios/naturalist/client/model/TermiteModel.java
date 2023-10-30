package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.common.entity.Termite;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

@Environment(EnvType.CLIENT)
public class TermiteModel extends GeoModel<Termite> {
    @Override
    public ResourceLocation getModelResource(Termite object) {
        return new ResourceLocation(Naturalist.MOD_ID, "geo/entity/termite.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Termite object) {
        return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/termite.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Termite animatable) {
        return new ResourceLocation(Naturalist.MOD_ID, "animations/entity/termite.animation.json");
    }
}
