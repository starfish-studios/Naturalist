package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.common.entity.Caterpillar;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

@Environment(EnvType.CLIENT)
public class CaterpillarModel extends GeoModel<Caterpillar> {
    @Override
    public ResourceLocation getModelResource(Caterpillar object, @Nullable GeoRenderer<Caterpillar> geoRenderer) {
        return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "geo/entity/caterpillar.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Caterpillar object, @Nullable GeoRenderer<Caterpillar> geoRenderer) {
        return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/caterpillar.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Caterpillar animatable) {
        return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "animations/caterpillar.rp_anim.json");
    }
}
