package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.common.entity.Lizard;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;
import software.bernie.geckolib.renderer.GeoRenderer;

@Environment(EnvType.CLIENT)
public class LizardModel extends GeoModel<Lizard> {
    public static final ResourceLocation[] TEXTURE_LOCATIONS = new ResourceLocation[]{
            ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/lizard/green.png"),
            ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/lizard/brown.png"),
            ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/lizard/beardie.png"),
            ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/lizard/leopard_gecko.png")
    };

    @Override
    public ResourceLocation getModelResource(Lizard lizard, @Nullable GeoRenderer<Lizard> geoRenderer) {
        return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "geo/entity/lizard.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Lizard lizard, @Nullable GeoRenderer<Lizard> geoRenderer) {
        return TEXTURE_LOCATIONS[Math.min(lizard.getVariant(), TEXTURE_LOCATIONS.length - 1)];
    }

    @Override
    public ResourceLocation getAnimationResource(Lizard lizard) {
        return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "animations/lizard.rp_anim.json");
    }

    @Override
    public void setCustomAnimations(Lizard entity, long instanceId, AnimationState<Lizard> animationState) {
        super.setCustomAnimations(entity, instanceId, animationState);

        if (animationState == null) return;

        EntityModelData extraDataOfType = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        GeoBone head = this.getAnimationProcessor().getBone("head");
        GeoBone tail = this.getAnimationProcessor().getBone("tail");
        GeoBone basiliskBody = this.getAnimationProcessor().getBone("basilisk_body");
        GeoBone basiliskTail = this.getAnimationProcessor().getBone("basilisk_tail");
        GeoBone beardieHead = this.getAnimationProcessor().getBone("beardie_head");
        GeoBone beardieBody = this.getAnimationProcessor().getBone("beardie_body");
        GeoBone gecko = this.getAnimationProcessor().getBone("gecko");

        head.setRotX(extraDataOfType.headPitch() * Mth.DEG_TO_RAD);
        head.setRotY(extraDataOfType.netHeadYaw() * Mth.DEG_TO_RAD);

        tail.setHidden(!entity.hasTail());
    }
}
