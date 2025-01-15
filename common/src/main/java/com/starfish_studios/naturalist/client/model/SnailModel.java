package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.common.entity.Snail;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.DyeColor;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;
import software.bernie.geckolib.renderer.GeoRenderer;

@Environment(EnvType.CLIENT)
public class SnailModel extends GeoModel<Snail> {
    @Override
    public ResourceLocation getModelResource(Snail snail, @Nullable GeoRenderer<Snail> geoRenderer) {
        return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "geo/entity/snail.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Snail snail, @Nullable GeoRenderer<Snail> geoRenderer) {
        if (snail.getName().getString().contains("Gary")) {
            return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/snail/gary.png");
        } else if (snail.getSnailColor() != null) {
            int color = snail.getSnailColor().getId();
            return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/snail/" + DyeColor.byId(color).getName() + ".png");
        }
        return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/snail.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Snail snail) {
        return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "animations/snail.rp_anim.json");
    }

    @Override
    public void setCustomAnimations(Snail animatable, long instanceId, AnimationState<Snail> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);

        if (animationState == null) return;

        EntityModelData extraDataOfType = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

        GeoBone leftEye = this.getAnimationProcessor().getBone("left_eye");
        GeoBone rightEye = this.getAnimationProcessor().getBone("right_eye");
        GeoBone eyes = this.getAnimationProcessor().getBone("eyes");

        if (animatable.isBaby()) {
            eyes.setScaleX(1.5F);
            eyes.setScaleY(1.5F);
            eyes.setScaleZ(1.5F);
        } else {
            eyes.setScaleX(1.0F);
            eyes.setScaleY(1.0F);
            eyes.setScaleZ(1.0F);
        }

        if (!animatable.isClimbing() || !animatable.canHide()) {
            leftEye.setRotX(extraDataOfType.headPitch() * Mth.DEG_TO_RAD);
            leftEye.setRotY(extraDataOfType.netHeadYaw() * Mth.DEG_TO_RAD);
            rightEye.setRotX(extraDataOfType.headPitch() * Mth.DEG_TO_RAD);
            rightEye.setRotY(extraDataOfType.netHeadYaw() * Mth.DEG_TO_RAD);
        }
    }
}
