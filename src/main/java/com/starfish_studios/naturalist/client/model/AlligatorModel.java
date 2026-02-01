package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.common.entity.Alligator;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;import net.minecraft.resources.ResourceLocation;

import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.base.GeoRenderState;

import java.util.HashMap;
import java.util.Map;

@Environment(EnvType.CLIENT)public class AlligatorModel extends GeoModel<Alligator> {
    private final Map<Long, Float> lerpedTailRotMap = new HashMap<>();
    private final Map<Long, Float> lerpedTailPitchMap = new HashMap<>();
    private final Map<Long, Float> lerpedRootPitchMap = new HashMap<>();

    @Override
    public ResourceLocation getModelResource(GeoRenderState state) {
        return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "entity/alligator");
    }

    @Override
    public ResourceLocation getTextureResource(GeoRenderState state) {
        return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/alligator/alligator.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Alligator alligator) {
        return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "alligator");
    }
}

