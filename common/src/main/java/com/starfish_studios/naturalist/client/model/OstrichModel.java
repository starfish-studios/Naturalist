package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

/* @Environment(EnvType.CLIENT)
public class OstrichModel extends GeoModel<Ostrich> {
    @Override
    public ResourceLocation getModelResource(Ostrich ostrich) {
        return new ResourceLocation(Naturalist.MOD_ID, "geo/entity/ostrich.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Ostrich ostrich) {
        if (ostrich.isBaby()) {
            return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/ostrich.png");
        }
        return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/ostrich.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Ostrich ostrich) {
        return new ResourceLocation(Naturalist.MOD_ID, "animations/entity/ostrich.animation.json");
    }

    @Override
    public void setCustomAnimations(Ostrich animatable, long instanceId, AnimationState<Ostrich> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);

        if (animationState == null) return;

        EntityModelData extraDataOfType = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        CoreGeoBone head = this.getAnimationProcessor().getBone("head");
        CoreGeoBone leftWing = this.getAnimationProcessor().getBone("left_wing");
        CoreGeoBone rightWing = this.getAnimationProcessor().getBone("right_wing");
        CoreGeoBone saddle = this.getAnimationProcessor().getBone("saddle");

        saddle.setHidden(!animatable.isSaddled());

        if (animatable.isBaby()) {
            head.setScaleX(1.5F); head.setScaleY(1.5F); head.setScaleZ(1.5F);
            leftWing.setScaleX(1.2F); leftWing.setScaleY(1.2F); leftWing.setScaleZ(1.2F);
            rightWing.setScaleX(1.2F); rightWing.setScaleY(1.2F); rightWing.setScaleZ(1.2F);
        } else {
            head.setScaleY(1.0F); head.setScaleZ(1.0F); head.setScaleX(1.0F);
            leftWing.setScaleX(1.0F); leftWing.setScaleY(1.0F); leftWing.setScaleZ(1.0F);
            rightWing.setScaleX(1.0F); rightWing.setScaleY(1.0F); rightWing.setScaleZ(1.0F);
        }

        head.setRotX(extraDataOfType.headPitch() * Mth.DEG_TO_RAD);
        head.setRotY(extraDataOfType.netHeadYaw() * Mth.DEG_TO_RAD);
    }
}

 */
