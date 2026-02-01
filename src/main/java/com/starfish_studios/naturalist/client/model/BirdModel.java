package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.client.NaturalistDataTickets;
import com.starfish_studios.naturalist.common.entity.Bird;
import com.starfish_studios.naturalist.core.registry.NaturalistEntityTypes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;import net.minecraft.resources.ResourceLocation;

import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.base.GeoRenderState;

@Environment(EnvType.CLIENT)public class BirdModel extends GeoModel<Bird> {

    @Override
    public ResourceLocation getTextureResource(GeoRenderState state) {
        int variant = state.getOrDefaultGeckolibData(NaturalistDataTickets.VARIANT_ID, 0);

        if (variant == 0) {
            return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/bird/bluejay.png");
        } else if (variant == 1) {
            return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/bird/canary.png");
        } else if (variant == 2) {
            return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/bird/cardinal.png");
        } else if (variant == 3) {
            return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/bird/finch.png");
        } else if (variant == 4) {
            return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/bird/sparrow.png");
        } else {
            return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/bird/robin.png");
        }
    }

    @Override
    public ResourceLocation getModelResource(GeoRenderState state) {
        return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "entity/bird");
    }

    @Override
    public void addAdditionalStateData(Bird bird, GeoRenderState state) {
        int variant = 5; // Default robin
        if (bird.getType().equals(NaturalistEntityTypes.BLUEJAY.get()))
            variant = 0;
        else if (bird.getType().equals(NaturalistEntityTypes.CANARY.get()))
            variant = 1;
        else if (bird.getType().equals(NaturalistEntityTypes.CARDINAL.get()))
            variant = 2;
        else if (bird.getType().equals(NaturalistEntityTypes.FINCH.get()))
            variant = 3;
        else if (bird.getType().equals(NaturalistEntityTypes.SPARROW.get()))
            variant = 4;

        state.addGeckolibData(NaturalistDataTickets.VARIANT_ID, variant);
    }

    @Override
    public ResourceLocation getAnimationResource(Bird bird) {
        return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "bird");
    }

}

