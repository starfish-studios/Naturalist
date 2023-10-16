package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.common.entity.Hippo;
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
public class HippoModel extends GeoModel<Hippo> {
    @Override
    public ResourceLocation getModelResource(Hippo hippo) {
        return new ResourceLocation(Naturalist.MOD_ID, "geo/hippo.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Hippo hippo) {
        return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/hippo.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Hippo hippo) {
        return new ResourceLocation(Naturalist.MOD_ID, "animations/hippo.animation.json");
    }

    @Override
    public void setCustomAnimations(Hippo animatable, long instanceId, AnimationState<Hippo> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);

        if (animationState == null) return;

        EntityModelData extraDataOfType = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        CoreGeoBone head = this.getAnimationProcessor().getBone("head");

        if (animatable.isBaby()) {
            head.setScaleX(1.75F);
            head.setScaleY(1.75F);
            head.setScaleZ(1.75F);
        } else {
            head.setScaleX(1.0F);
            head.setScaleY(1.0F);
            head.setScaleZ(1.0F);
        }

        // head.setRotX(extraDataOfType.headPitch() * Mth.DEG_TO_RAD);  // Commented out as in the original code
        head.setRotY(extraDataOfType.netHeadYaw() * Mth.DEG_TO_RAD);
    }
}
