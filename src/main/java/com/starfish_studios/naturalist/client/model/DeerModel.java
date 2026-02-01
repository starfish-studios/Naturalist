package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.common.entity.Deer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.constant.dataticket.DataTicket;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.base.GeoRenderState;

@Environment(EnvType.CLIENT)
public class DeerModel extends GeoModel<Deer> {
    private static final DataTicket<Boolean> IS_BABY = DataTicket.create("is_baby", Boolean.class);

    @Override
    public ResourceLocation getModelResource(GeoRenderState state) {
        if (state.getOrDefaultGeckolibData(IS_BABY, false)) {
            return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "entity/fawn");
        }
        return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "entity/deer");
    }

    @Override
    public ResourceLocation getTextureResource(GeoRenderState state) {
        if (state.getOrDefaultGeckolibData(IS_BABY, false)) {
            return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/fawn.png");
        }

        return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/deer.png");
    }

    @Override
    public void addAdditionalStateData(Deer deer, GeoRenderState state) {
        state.addGeckolibData(IS_BABY, deer.isBaby());
    }

    @Override
    public ResourceLocation getAnimationResource(Deer deer) {
        return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "deer");
    }
}
