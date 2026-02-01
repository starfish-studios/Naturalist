package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.common.entity.LizardTail;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.constant.dataticket.DataTicket;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.base.GeoRenderState;

@Environment(EnvType.CLIENT)public class LizardTailModel extends GeoModel<LizardTail> {
    private static final DataTicket<Integer> VARIANT_ID = DataTicket.create("lizard_tail_variant", Integer.class);

    public static final ResourceLocation[] TEXTURE_LOCATIONS = new ResourceLocation[] {
            ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/lizard/green_tail.png"),
            ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/lizard/brown_tail.png"),
            ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/lizard/beardie_tail.png"),
            ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/lizard/leopard_gecko_tail.png"),
    };

    @Override
    public void addAdditionalStateData(LizardTail animatable, GeoRenderState state) {
        state.addGeckolibData(VARIANT_ID, animatable.getVariant());
    }

    @Override
    public ResourceLocation getModelResource(GeoRenderState state) {
        return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "entity/lizard_tail");
    }

    @Override
    public ResourceLocation getTextureResource(GeoRenderState state) {
        int variant = state.getOrDefaultGeckolibData(VARIANT_ID, 0);
        return TEXTURE_LOCATIONS[variant];
    }

    @Override
    public ResourceLocation getAnimationResource(LizardTail lizard) {
        return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "lizard_tail");
    }
}

