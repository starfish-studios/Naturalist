package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.common.entity.Elephant;
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
public class ElephantModel extends GeoModel<Elephant> {
    @Override
    public ResourceLocation getModelResource(Elephant elephant) {
        return new ResourceLocation(Naturalist.MOD_ID, "geo/entity/elephant.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Elephant elephant) {
        return new ResourceLocation(Naturalist.MOD_ID, /*elephant.isDirty() ? "textures/entity/elephant_dirt.png" :*/ "textures/entity/elephant.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Elephant elephant) {
        return new ResourceLocation(Naturalist.MOD_ID, "animations/elephant.animation.json");
    }

    @Override
    public void setCustomAnimations(Elephant animatable, long instanceId, AnimationState<Elephant> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);

        if (animationState == null) return;

        EntityModelData extraDataOfType = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        CoreGeoBone head = this.getAnimationProcessor().getBone("head");
        CoreGeoBone bigTusks = this.getAnimationProcessor().getBone("tusks");
        CoreGeoBone smallTusks = this.getAnimationProcessor().getBone("baby_tusks");
        CoreGeoBone babyTrunk = this.getAnimationProcessor().getBone("trunk4");
        CoreGeoBone leftEar = this.getAnimationProcessor().getBone("left_ear");
        CoreGeoBone rightEar = this.getAnimationProcessor().getBone("right_ear");

        if (animatable.isBaby()) {
            head.setScaleX(1.3F); head.setScaleY(1.3F); head.setScaleZ(1.3F);
            leftEar.setScaleX(1.2F); leftEar.setScaleY(1.2F); leftEar.setScaleZ(1.2F);
            rightEar.setScaleX(1.2F); rightEar.setScaleY(1.2F); rightEar.setScaleZ(1.2F);
        } else {
            head.setScaleX(1.0F);
            head.setScaleY(1.0F);
            head.setScaleZ(1.0F);
            leftEar.setScaleX(1.0F); leftEar.setScaleY(1.0F); leftEar.setScaleZ(1.0F);
            rightEar.setScaleX(1.0F); rightEar.setScaleY(1.0F); rightEar.setScaleZ(1.0F);
        }

        bigTusks.setHidden(animatable.isBaby());
        smallTusks.setHidden(animatable.isBaby());
        smallTusks.setHidden(!animatable.isBaby());

        babyTrunk.setHidden(animatable.isBaby());

        head.setRotX(extraDataOfType.headPitch() * Mth.DEG_TO_RAD);
        head.setRotY(extraDataOfType.netHeadYaw() * Mth.DEG_TO_RAD);
    }
}
