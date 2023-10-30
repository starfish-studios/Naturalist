package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.common.entity.Vulture;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

@Environment(EnvType.CLIENT)
public class VultureModel extends GeoModel<Vulture> {
    @Override
    public ResourceLocation getModelResource(Vulture vulture) {
        return new ResourceLocation(Naturalist.MOD_ID, "geo/entity/vulture.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Vulture vulture) {
        return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/vulture.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Vulture vulture) {
        return new ResourceLocation(Naturalist.MOD_ID, "animations/entity/vulture.animation.json");
    }

    @Override
    public void setCustomAnimations(Vulture animatable, long instanceId, AnimationState<Vulture> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);

        if (animationState == null) return;

        EntityModelData extraDataOfType = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        CoreGeoBone head = this.getAnimationProcessor().getBone("head");

        head.setRotX(extraDataOfType.headPitch() * Mth.DEG_TO_RAD);
        head.setRotY(extraDataOfType.netHeadYaw() * Mth.DEG_TO_RAD);
    }
}
