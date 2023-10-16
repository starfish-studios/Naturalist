package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.common.entity.Tortoise;
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
public class TortoiseModel extends GeoModel<Tortoise> {

    @Override
    public ResourceLocation getModelResource(Tortoise tortoise) {
        return new ResourceLocation(Naturalist.MOD_ID, "geo/tortoise.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Tortoise tortoise) {
        switch (tortoise.getVariant()) {
            case 1:
                return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/tortoise/green.png");
            case 2:
                return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/tortoise/black.png");
        }
        return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/tortoise/brown.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Tortoise tortoise) {
        return new ResourceLocation(Naturalist.MOD_ID, "animations/tortoise.animation.json");
    }

    @Override
    public void setCustomAnimations(Tortoise animatable, long instanceId, AnimationState<Tortoise> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);

        if (animationState == null) return;

        EntityModelData extraDataOfType = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        CoreGeoBone head = this.getAnimationProcessor().getBone("head");

        if (animatable.isBaby()) {
            head.setScaleX(1.4F);
            head.setScaleY(1.4F);
            head.setScaleZ(1.4F);
        } else {
            head.setScaleX(1.0F);
            head.setScaleY(1.0F);
            head.setScaleZ(1.0F);
        }

        head.setRotX(extraDataOfType.headPitch() * Mth.DEG_TO_RAD);
        head.setRotY(extraDataOfType.netHeadYaw() * Mth.DEG_TO_RAD);
    }
}
