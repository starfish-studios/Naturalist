package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.common.entity.Deer;
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
public class DeerModel extends GeoModel<Deer> {
    @Override
    public ResourceLocation getModelResource(Deer deer) {
        return new ResourceLocation(Naturalist.MOD_ID, "geo/deer.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Deer deer) {
        if (deer.isBaby()) {
            return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/deer.png");
        }

        return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/deer.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Deer deer) {
        return new ResourceLocation(Naturalist.MOD_ID, "animations/deer.animation.json");
    }

    @Override
    public void setCustomAnimations(Deer animatable, long instanceId, AnimationState<Deer> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);

        if (animationState == null) return;

        EntityModelData extraDataOfType = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        CoreGeoBone head = this.getAnimationProcessor().getBone("head");
        CoreGeoBone antlers = this.getAnimationProcessor().getBone("antlers");

        if (animatable.isBaby()) {
            head.setScaleX(1.4F);
            head.setScaleY(1.4F);
            head.setScaleZ(1.4F);
            head.setScaleX(1.6F);
            head.setScaleY(1.6F);
            head.setScaleZ(1.6F);
        } else {
            head.setScaleX(1.0F);
            head.setScaleY(1.0F);
            head.setScaleZ(1.0F);
        }

        antlers.setHidden(animatable.isBaby());

        if (!animatable.isEating()) {
            head.setRotX(extraDataOfType.headPitch() * Mth.DEG_TO_RAD);
            head.setRotY(extraDataOfType.netHeadYaw() * Mth.DEG_TO_RAD);
        }
    }

}
