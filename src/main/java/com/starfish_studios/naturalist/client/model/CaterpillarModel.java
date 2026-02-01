package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.common.entity.Caterpillar;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.base.GeoRenderState;

@Environment(EnvType.CLIENT)public class CaterpillarModel extends GeoModel<Caterpillar> {
    @Override
    public ResourceLocation getModelResource(GeoRenderState state) {
        return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "entity/caterpillar");
    }

    @Override
    public ResourceLocation getTextureResource(GeoRenderState state) {
        return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/caterpillar.png");
    }

    @Override
    public @NotNull ResourceLocation getAnimationResource(Caterpillar animatable) {
        return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "caterpillar");
    }
}

