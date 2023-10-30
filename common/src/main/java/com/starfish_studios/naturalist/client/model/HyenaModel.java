package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.common.entity.Hyena;
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
public class HyenaModel extends GeoModel<Hyena> {

    @Override
    public ResourceLocation getModelResource(Hyena hyena) {
        return new ResourceLocation(Naturalist.MOD_ID, "geo/entity/hyena.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Hyena hyena) {
        return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/hyena.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Hyena hyena) {
        return new ResourceLocation(Naturalist.MOD_ID, "animations/entity/hyena.animation.json");
    }

    @Override
    public void setCustomAnimations(Hyena animatable, long instanceId, AnimationState<Hyena> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);

        if (animationState == null) return;

        EntityModelData extraDataOfType = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        CoreGeoBone head = this.getAnimationProcessor().getBone("head");
        CoreGeoBone leftEar = this.getAnimationProcessor().getBone("leftEar");
        CoreGeoBone rightEar = this.getAnimationProcessor().getBone("rightEar");

        if (animatable.isBaby()) {
            head.setScaleX(1.3F); head.setScaleY(1.3F); head.setScaleZ(1.3F);
            leftEar.setScaleX(1.2F); leftEar.setScaleY(1.2F); leftEar.setScaleZ(1.2F);
            rightEar.setScaleX(1.2F); rightEar.setScaleY(1.2F); rightEar.setScaleZ(1.2F);
        }

        head.setRotX(extraDataOfType.headPitch() * Mth.DEG_TO_RAD);
        head.setRotY(extraDataOfType.netHeadYaw() * Mth.DEG_TO_RAD);
    }
}
