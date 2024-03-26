package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.common.entity.Alligator;
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
public class AlligatorModel extends GeoModel<Alligator> {

    @Override
    public ResourceLocation getModelResource(Alligator alligator) {
        return new ResourceLocation(Naturalist.MOD_ID, "geo/entity/alligator.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Alligator alligator) {
        return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/alligator.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Alligator alligator) {
        return new ResourceLocation(Naturalist.MOD_ID, "animations/alligator.animation.json");
    }

    @Override
    public void setCustomAnimations(Alligator animatable, long instanceId, AnimationState<Alligator> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);

        if (animationState == null) return;

        EntityModelData extraDataOfType = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        CoreGeoBone head = this.getAnimationProcessor().getBone("head");

        if (animatable.isBaby()) {
            head.setScaleX(1.5F);
            head.setScaleY(1.5F);
            head.setScaleZ(1.5F);
        } else {
            head.setScaleX(1.0F);
            head.setScaleY(1.0F);
            head.setScaleZ(1.0F);
        }

        head.setRotY(extraDataOfType.netHeadYaw() * Mth.DEG_TO_RAD);
    }
}
