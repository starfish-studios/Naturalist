package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.common.entity.Bass;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.base.GeoRenderState;

@Environment(EnvType.CLIENT)public class BassModel extends GeoModel<Bass> {
    @Override
    public ResourceLocation getModelResource(GeoRenderState state) {
        return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "entity/bass");
    }

    @Override
    public ResourceLocation getTextureResource(GeoRenderState state) {
        return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/bass.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Bass bass) {
        return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "bass");
    }
}

