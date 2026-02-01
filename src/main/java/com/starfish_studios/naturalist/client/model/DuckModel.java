package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.client.NaturalistDataTickets;
import com.starfish_studios.naturalist.common.entity.Duck;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;import net.minecraft.resources.ResourceLocation;

import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.base.GeoRenderState;

@Environment(EnvType.CLIENT)public class DuckModel extends GeoModel<Duck> {
    @Override
    public ResourceLocation getModelResource(GeoRenderState state) {
        return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "entity/duck");
    }

    @Override
    public ResourceLocation getTextureResource(GeoRenderState state) {
        String name = state.getOrDefaultGeckolibData(NaturalistDataTickets.ENTITY_NAME, "");
        if (name.equalsIgnoreCase("Queso")) {
            return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/duck/queso.png");
        } else if (name.equalsIgnoreCase("Donald")) {
            return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/duck/donald.png");
        }
        return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/duck/duck.png");
    }

    @Override
    public void addAdditionalStateData(Duck duck, GeoRenderState state) {
        state.addGeckolibData(NaturalistDataTickets.ENTITY_NAME, duck.getName().getString());
    }

    @Override
    public @NotNull ResourceLocation getAnimationResource(Duck animal) {
        return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "duck");
    }

}

