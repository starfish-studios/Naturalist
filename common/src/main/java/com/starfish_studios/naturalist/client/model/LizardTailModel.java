package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.common.entity.LizardTail;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

@Environment(EnvType.CLIENT)
public class LizardTailModel extends GeoModel<LizardTail> {
    public static final ResourceLocation[] TEXTURE_LOCATIONS = new ResourceLocation[]{
            ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/lizard/green_tail.png"),
            ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/lizard/brown_tail.png"),
            ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/lizard/beardie_tail.png"),
            ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/lizard/leopard_gecko_tail.png"),
    };

    @Override
    public ResourceLocation getModelResource(LizardTail lizard, @Nullable GeoRenderer<LizardTail> geoRenderer) {
        return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "geo/entity/lizard_tail.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(LizardTail lizard, @Nullable GeoRenderer<LizardTail> geoRenderer) {
        return TEXTURE_LOCATIONS[lizard.getVariant()];
    }

    @Override
    public ResourceLocation getAnimationResource(LizardTail lizard) {
        return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "animations/lizard_tail.rp_anim.json");
    }
}
