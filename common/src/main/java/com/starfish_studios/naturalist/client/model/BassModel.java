package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.common.entity.Bass;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

@Environment(EnvType.CLIENT)
public class BassModel extends GeoModel<Bass> {
    @Override
    public ResourceLocation getModelResource(Bass bass, @Nullable GeoRenderer<Bass> geoRenderer) {
        return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "geo/entity/bass.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Bass bass, @Nullable GeoRenderer<Bass> geoRenderer) {
        return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/bass.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Bass bass) {
        return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "animations/bass.rp_anim.json");
    }
}
