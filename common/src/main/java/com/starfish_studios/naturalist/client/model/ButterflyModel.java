package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.common.entity.Butterfly;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

@Environment(EnvType.CLIENT)
public class ButterflyModel extends GeoModel<Butterfly> {
    @Override
    public ResourceLocation getModelResource(Butterfly butterfly, @Nullable GeoRenderer<Butterfly> geoRenderer) {
        return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "geo/entity/butterfly.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Butterfly butterfly, @Nullable GeoRenderer<Butterfly> geoRenderer) {
        if (butterfly.getVariant().getName().equals("cabbage_white")) {
            return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/butterfly/cabbage_white.png");
        } else if (butterfly.getVariant().getName().equals("monarch")) {
            return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/butterfly/monarch.png");
        } else if (butterfly.getVariant().getName().equals("clouded_yellow")) {
            return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/butterfly/clouded_yellow.png");
        } else if (butterfly.getVariant().getName().equals("swallowtail")) {
            return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/butterfly/swallowtail.png");
        } else if (butterfly.getVariant().getName().equals("blue_morpho")) {
            return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/butterfly/blue_morpho.png");
        } else {
            return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/butterfly/monarch.png");
        }
    }

    @Override
    public ResourceLocation getAnimationResource(Butterfly butterfly) {
        return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "animations/butterfly.rp_anim.json");
    }
}
