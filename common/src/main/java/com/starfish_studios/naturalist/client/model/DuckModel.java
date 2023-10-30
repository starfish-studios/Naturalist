package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.common.entity.Duck;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.data.EntityModelData;
import software.bernie.geckolib.model.GeoModel;

@Environment(EnvType.CLIENT)
public class DuckModel extends GeoModel<Duck> {
    @Override
    public ResourceLocation getModelResource(Duck animal) {
        return new ResourceLocation(Naturalist.MOD_ID, "geo/entity/duck.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Duck animal) {
        if (animal.getName().getString().equals("Queso")) {
            return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/duck/queso.png");
        }
        else if (animal.getName().getString().equals("Ducky")) {
            return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/duck/rubber_ducky.png");
        }
        return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/duck/duck.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Duck animal) {
        return new ResourceLocation(Naturalist.MOD_ID, "animations/entity/duck.animation.json");
    }

    @Override
    public void setCustomAnimations(Duck animatable, long instanceId, AnimationState<Duck> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);

        if (animationState == null) return;

        EntityModelData extraDataOfType = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        CoreGeoBone head = this.getAnimationProcessor().getBone("head");

        if (animatable.isBaby()) {
            head.setScaleX(1.7F);
            head.setScaleY(1.7F);
            head.setScaleZ(1.7F);
        } else {
            head.setScaleX(1.0F);
            head.setScaleY(1.0F);
            head.setScaleZ(1.0F);
        }

        head.setRotX(extraDataOfType.headPitch() * Mth.DEG_TO_RAD);
        head.setRotY(extraDataOfType.netHeadYaw() * Mth.DEG_TO_RAD);
    }

}
