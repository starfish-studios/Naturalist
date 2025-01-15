package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.common.entity.Bird;
import com.starfish_studios.naturalist.registry.NaturalistEntityTypes;
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
public class BirdModel extends GeoModel<Bird> {

    @Override
    public ResourceLocation getTextureResource(Bird bird, @Nullable GeoRenderer<Bird> geoRenderer) {
        if (bird.getType().equals(NaturalistEntityTypes.BLUEJAY)) {
            return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/bird/bluejay.png");
        } else if (bird.getType().equals(NaturalistEntityTypes.CANARY)) {
            return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/bird/canary.png");
        } else if (bird.getType().equals(NaturalistEntityTypes.CARDINAL)) {
            return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/bird/cardinal.png");
        } else if (bird.getType().equals(NaturalistEntityTypes.FINCH)) {
            return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/bird/finch.png");
        } else if (bird.getType().equals(NaturalistEntityTypes.SPARROW)) {
            return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/bird/sparrow.png");
        } else {
            return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/bird/robin.png");
        }
    }

    @Override
    public ResourceLocation getModelResource(Bird bird, @Nullable GeoRenderer<Bird> geoRenderer) {
        return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "geo/entity/bird.geo.json");
    }

    @Override
    public ResourceLocation getAnimationResource(Bird bird) {
        return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "animations/bird.rp_anim.json");
    }

    @Override
    public void setCustomAnimations(Bird entity, long instanceId, AnimationState<Bird> animationState) {
        super.setCustomAnimations(entity, instanceId, animationState);

        if (animationState == null) return;

        EntityModelData extraDataOfType = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        GeoBone head = this.getAnimationProcessor().getBone("head");

        head.setRotX(extraDataOfType.headPitch() * Mth.DEG_TO_RAD);
        head.setRotY(extraDataOfType.netHeadYaw() * Mth.DEG_TO_RAD);
    }
}
