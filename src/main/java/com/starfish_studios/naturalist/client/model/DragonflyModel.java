package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.common.entity.Dragonfly;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

@Environment(EnvType.CLIENT)
public class DragonflyModel extends GeoModel<Dragonfly> {
    public static final ResourceLocation[] TEXTURE_LOCATIONS = new ResourceLocation[]{
            ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/dragonfly/blue.png"),
            ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/dragonfly/green.png"),
            ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/dragonfly/red.png")
    };

    @Override
    @SuppressWarnings("removal")
    public ResourceLocation getModelResource(Dragonfly dragonfly) {
        return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "geo/entity/dragonfly.geo.json");
    }

    @Override
    @SuppressWarnings("removal")
    public ResourceLocation getTextureResource(Dragonfly dragonfly) {
        return TEXTURE_LOCATIONS[dragonfly.getVariant()];
    }

    @Override
    public ResourceLocation getAnimationResource(Dragonfly dragonfly) {
        return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "animations/dragonfly.rp_anim.json");
    }
}
