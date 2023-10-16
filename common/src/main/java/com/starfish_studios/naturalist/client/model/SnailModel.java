package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.common.entity.Snail;
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
public class SnailModel extends GeoModel<Snail> {
    @Override
    public ResourceLocation getModelResource(Snail snail) {
        return new ResourceLocation(Naturalist.MOD_ID, "geo/snail.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Snail snail) {
        return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/snail.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Snail snail) {
        return new ResourceLocation(Naturalist.MOD_ID, "animations/snail.animation.json");
    }

    @Override
    public void setCustomAnimations(Snail animatable, long instanceId, AnimationState<Snail> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);

        if (animationState == null) return;

        EntityModelData extraDataOfType = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        CoreGeoBone leftEye = this.getAnimationProcessor().getBone("left_eye");
        CoreGeoBone rightEye = this.getAnimationProcessor().getBone("right_eye");

        if (!animatable.isClimbing() || !animatable.canHide()) {
            leftEye.setRotX(extraDataOfType.headPitch() * Mth.DEG_TO_RAD);
            leftEye.setRotY(extraDataOfType.netHeadYaw() * Mth.DEG_TO_RAD);
            rightEye.setRotX(extraDataOfType.headPitch() * Mth.DEG_TO_RAD);
            rightEye.setRotY(extraDataOfType.netHeadYaw() * Mth.DEG_TO_RAD);
        }
    }
}
