package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.common.entity.Elephant;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;import net.minecraft.resources.ResourceLocation;

import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.base.GeoRenderState;

@Environment(EnvType.CLIENT)public class ElephantModel extends GeoModel<Elephant> {
    @Override
    public ResourceLocation getModelResource(GeoRenderState state) {
        return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "entity/elephant");
    }

    @Override
    public ResourceLocation getTextureResource(GeoRenderState state) {
        return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/elephant.png");
    }

    @Override
    public @NotNull ResourceLocation getAnimationResource(Elephant elephant) {
        return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "elephant");
    }

}

