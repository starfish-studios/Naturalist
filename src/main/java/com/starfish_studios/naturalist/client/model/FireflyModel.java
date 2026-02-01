package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.common.entity.Firefly;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.base.GeoRenderState;

@Environment(EnvType.CLIENT)public class FireflyModel extends GeoModel<Firefly> {
    @Override
    public ResourceLocation getModelResource(GeoRenderState state) {
        return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "entity/firefly");
    }

    @Override
    public ResourceLocation getTextureResource(GeoRenderState state) {
        return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/firefly/firefly.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Firefly firefly) {
        return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "firefly");
    }
}

