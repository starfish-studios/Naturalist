package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.common.entity.Catfish;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

@Environment(EnvType.CLIENT)
public class CatfishModel extends GeoModel<Catfish> {
    @Override
    public ResourceLocation getModelResource(Catfish catfish, @Nullable GeoRenderer<Catfish> geoRenderer) {
        return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "geo/entity/catfish.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Catfish catfish, @Nullable GeoRenderer<Catfish> geoRenderer) {
        return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/catfish.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Catfish catfish) {
        return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "animations/catfish.rp_anim.json");
    }
}
