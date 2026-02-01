package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.common.entity.Butterfly;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.constant.dataticket.DataTicket;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.base.GeoRenderState;

@Environment(EnvType.CLIENT)public class ButterflyModel extends GeoModel<Butterfly> {
    private static final DataTicket<String> VARIANT_NAME = DataTicket.create("butterfly_variant", String.class);

    @Override
    public void addAdditionalStateData(Butterfly animatable, GeoRenderState state) {
        state.addGeckolibData(VARIANT_NAME, animatable.getVariant().getName());
    }

    @Override
    public @NotNull ResourceLocation getModelResource(GeoRenderState state) {
        return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "entity/butterfly");
    }

    @Override
    public @NotNull ResourceLocation getTextureResource(GeoRenderState state) {
        String name = state.getOrDefaultGeckolibData(VARIANT_NAME, "monarch");
        return switch (name) {
            case "monarch" ->
                ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/butterfly/monarch.png");
            case "clouded_yellow" -> ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID,
                    "textures/entity/butterfly/clouded_yellow.png");
            case "blue_morpho" ->
                ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/butterfly/blue_morpho.png");
            case "green_swallowtail" -> ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID,
                    "textures/entity/butterfly/green_swallowtail.png");
            case "jade_green_swallowtail" -> ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID,
                    "textures/entity/butterfly/jade_green_swallowtail.png");
            case "purple_emperor" -> ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID,
                    "textures/entity/butterfly/purple_emperor.png");
            case "red_admiral" ->
                ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/butterfly/red_admiral.png");
            default ->
                ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/butterfly/monarch.png");
        };
    }

    @Override
    public ResourceLocation getAnimationResource(Butterfly butterfly) {
        return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "butterfly");
    }
}

