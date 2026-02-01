package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.client.NaturalistDataTickets;

import com.starfish_studios.naturalist.common.entity.Snail;
import software.bernie.geckolib.renderer.base.GeoRenderState;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;import net.minecraft.resources.ResourceLocation;

import net.minecraft.world.item.DyeColor;
import org.jetbrains.annotations.NotNull;

import software.bernie.geckolib.model.GeoModel;

@Environment(EnvType.CLIENT)public class SnailModel extends GeoModel<Snail> {
    @Override
    public ResourceLocation getModelResource(GeoRenderState state) {
        return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "entity/snail");
    }

    @Override
    public @NotNull ResourceLocation getTextureResource(@NotNull GeoRenderState state) {
        String name = state.getOrDefaultGeckolibData(NaturalistDataTickets.ENTITY_NAME, "");
        if (name.contains("Gary")) {
            return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/snail/gary.png");
        }

        int colorId = state.getOrDefaultGeckolibData(NaturalistDataTickets.VARIANT_ID, -1);
        if (colorId != -1) {
            return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID,
                    "textures/entity/snail/" + DyeColor.byId(colorId).getName() + ".png");
        }
        return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/snail.png");
    }

    @Override
    public void addAdditionalStateData(Snail snail, GeoRenderState state) {
        state.addGeckolibData(NaturalistDataTickets.ENTITY_NAME, snail.getName().getString());
        if (snail.getSnailColor() != null) {
            state.addGeckolibData(NaturalistDataTickets.VARIANT_ID, snail.getSnailColor().getId());
        }
    }

    @Override
    public ResourceLocation getAnimationResource(Snail snail) {
        return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "snail");
    }
}

