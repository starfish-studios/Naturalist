package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.common.entity.Tortoise;

import net.minecraft.resources.ResourceLocation;

import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.constant.dataticket.DataTicket;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.base.GeoRenderState;


public class TortoiseModel extends GeoModel<Tortoise> {
    private static final DataTicket<Integer> VARIANT_ID = DataTicket.create("tortoise_variant", Integer.class);

    @Override
    public void addAdditionalStateData(Tortoise animatable, GeoRenderState state) {
        state.addGeckolibData(VARIANT_ID, animatable.getVariant());
    }

    @Override
    public @NotNull ResourceLocation getModelResource(GeoRenderState state) {
        return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "entity/tortoise");
    }

    @Override
    public ResourceLocation getTextureResource(@NotNull GeoRenderState state) {
        int variant = state.getOrDefaultGeckolibData(VARIANT_ID, 0);
        return switch (variant) {
            case 1 -> ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/tortoise/green.png");
            case 2 -> ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/tortoise/black.png");
            default -> ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/tortoise/brown.png");
        };
    }

    @Override
    public ResourceLocation getAnimationResource(Tortoise tortoise) {
        return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "tortoise");
    }

}

