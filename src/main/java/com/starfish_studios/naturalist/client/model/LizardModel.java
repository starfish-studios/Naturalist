package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.common.entity.Lizard;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;import net.minecraft.resources.ResourceLocation;

import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.constant.dataticket.DataTicket;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.base.GeoRenderState;

@Environment(EnvType.CLIENT)public class LizardModel extends GeoModel<Lizard> {
    private static final DataTicket<Integer> VARIANT_ID = DataTicket.create("lizard_variant", Integer.class);

    public static final ResourceLocation[] TEXTURE_LOCATIONS = new ResourceLocation[] {
            ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/lizard/green.png"),
            ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/lizard/brown.png"),
            ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/lizard/beardie.png"),
            ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/lizard/leopard_gecko.png")
    };

    @Override
    public void addAdditionalStateData(Lizard animatable, GeoRenderState state) {
        state.addGeckolibData(VARIANT_ID, animatable.getVariant());
    }

    @Override
    public ResourceLocation getModelResource(GeoRenderState state) {
        return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "entity/lizard");
    }

    @Override
    public ResourceLocation getTextureResource(@NotNull GeoRenderState state) {
        int variant = state.getOrDefaultGeckolibData(VARIANT_ID, 0);
        return TEXTURE_LOCATIONS[Math.min(variant, TEXTURE_LOCATIONS.length - 1)];
    }

    @Override
    public @NotNull ResourceLocation getAnimationResource(Lizard lizard) {
        return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "lizard");
    }

}

