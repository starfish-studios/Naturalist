package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.common.entity.Firefly;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

@Environment(EnvType.CLIENT)
public class FireflyModel extends GeoModel<Firefly> {
    @Override
    public ResourceLocation getModelResource(Firefly firefly, @Nullable GeoRenderer<Firefly> geoRenderer) {
        return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "geo/entity/firefly.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Firefly firefly, @Nullable GeoRenderer<Firefly> geoRenderer) {
        return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/firefly/firefly.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Firefly firefly) {
        return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "animations/firefly.rp_anim.json");
    }
}
