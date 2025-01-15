package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.common.entity.Dragonfly;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

@Environment(EnvType.CLIENT)
public class DragonflyModel extends GeoModel<Dragonfly> {
    public static final ResourceLocation[] TEXTURE_LOCATIONS = new ResourceLocation[]{
            ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/dragonfly/blue.png"),
            ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/dragonfly/green.png"),
            ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/dragonfly/red.png")
    };

    @Override
    public ResourceLocation getModelResource(Dragonfly dragonfly, @Nullable GeoRenderer<Dragonfly> geoRenderer) {
        return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "geo/entity/dragonfly.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Dragonfly dragonfly, @Nullable GeoRenderer<Dragonfly> geoRenderer) {
        return TEXTURE_LOCATIONS[dragonfly.getVariant()];
    }

    @Override
    public ResourceLocation getAnimationResource(Dragonfly dragonfly) {
        return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "animations/dragonfly.rp_anim.json");
    }
}
