package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.common.entity.Lion;
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
public class LionModel extends GeoModel<Lion> {
    @Override
    public ResourceLocation getModelResource(Lion lion) {
        return new ResourceLocation(Naturalist.MOD_ID, "geo/lion.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Lion lion) {
        return (lion.isSleeping() && lion.hasMane()) && !lion.isBaby() ? new ResourceLocation(Naturalist.MOD_ID, "textures/entity/lion_sleep.png") :
                (!lion.hasMane() && lion.isSleeping() || lion.isBaby() && lion.isSleeping()) ? new ResourceLocation(Naturalist.MOD_ID, "textures/entity/lioness_sleep.png") :
                        (!lion.hasMane() && !lion.isAggressive() || lion.isBaby()) ? new ResourceLocation(Naturalist.MOD_ID, "textures/entity/lioness.png") :
                                (lion.isAggressive()) && !lion.isBaby() && lion.hasMane() ? new ResourceLocation(Naturalist.MOD_ID, "textures/entity/lion_angry.png") :
                                        (!lion.hasMane() && lion.isAggressive()) || lion.isBaby() && lion.isAggressive() ? new ResourceLocation(Naturalist.MOD_ID, "textures/entity/lioness_angry.png") :
                                                new ResourceLocation(Naturalist.MOD_ID, "textures/entity/lion.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Lion lion) {
        return new ResourceLocation(Naturalist.MOD_ID, "animations/lion.animation.json");
    }

    @Override
    public void setCustomAnimations(Lion animatable, long instanceId, AnimationState<Lion> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);

        if (animationState == null) return;

        EntityModelData extraDataOfType = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        CoreGeoBone head = this.getAnimationProcessor().getBone("head");
        CoreGeoBone mane = this.getAnimationProcessor().getBone("mane");

        if (animatable.isBaby()) {
            head.setScaleX(1.4F);
            head.setScaleY(1.4F);
            head.setScaleZ(1.4F);
        } else {
            head.setScaleX(1.0F);
            head.setScaleY(1.0F);
            head.setScaleZ(1.0F);
        }

        mane.setHidden(!animatable.hasMane() || animatable.isBaby());

        if (!animatable.isSleeping()) {
            head.setRotX(extraDataOfType.headPitch() * Mth.DEG_TO_RAD);
            head.setRotY(extraDataOfType.netHeadYaw() * Mth.DEG_TO_RAD);
        }
    }
}
