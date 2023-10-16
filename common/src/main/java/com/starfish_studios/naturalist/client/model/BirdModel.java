package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.common.entity.Bird;
import com.starfish_studios.naturalist.core.registry.NaturalistEntityTypes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

@Environment(EnvType.CLIENT)
public class BirdModel extends GeoModel<Bird> {

    @Override
    public ResourceLocation getTextureResource(Bird bird) {
        if (bird.getType().equals(NaturalistEntityTypes.BLUEJAY.get())) {
            return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/bluejay.png");
        } else if (bird.getType().equals(NaturalistEntityTypes.CANARY.get())) {
            return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/canary.png");
        } else if (bird.getType().equals(NaturalistEntityTypes.CARDINAL.get())) {
            return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/cardinal.png");
        } else {
            return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/robin.png");
        }
    }

    @Override
    public ResourceLocation getModelResource(Bird bird) {
        return new ResourceLocation(Naturalist.MOD_ID, "geo/bird.geo.json");
    }

    @Override
    public ResourceLocation getAnimationResource(Bird bird) {
        return new ResourceLocation(Naturalist.MOD_ID, "animations/bird.animation.json");
    }

    @Override
    public void setCustomAnimations(Bird animatable, long instanceId, AnimationState<Bird> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);

        if (animationState == null) return;

        EntityModelData extraDataOfType = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        CoreGeoBone head = this.getAnimationProcessor().getBone("head");

        head.setRotX(extraDataOfType.headPitch() * Mth.DEG_TO_RAD);
        head.setRotY(extraDataOfType.netHeadYaw() * Mth.DEG_TO_RAD);
    }
}
