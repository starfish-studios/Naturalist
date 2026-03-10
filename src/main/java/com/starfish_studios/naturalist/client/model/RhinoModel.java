package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.common.entity.Rhino;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

@Environment(EnvType.CLIENT)
public class RhinoModel extends GeoModel<Rhino> {
    @Override
    @SuppressWarnings("removal")
    public ResourceLocation getModelResource(Rhino rhino) {
        return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "geo/entity/rhino.geo.json");
    }

    @Override
    @SuppressWarnings("removal")
    public ResourceLocation getTextureResource(Rhino rhino) {
        return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/rhino.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Rhino rhino) {
        return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "animations/rhino.rp_anim.json");
    }

    @Override
    public void setCustomAnimations(@NotNull Rhino entity, long instanceId, AnimationState<Rhino> animationState) {
        super.setCustomAnimations(entity, instanceId, animationState);

        if (animationState == null) return;

        EntityModelData extraDataOfType = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        GeoBone head = this.getAnimationProcessor().getBone("head");
        GeoBone bigHorn = this.getAnimationProcessor().getBone("big_horn");
        GeoBone smallHorn = this.getAnimationProcessor().getBone("small_horn");
        GeoBone babyHorn = this.getAnimationProcessor().getBone("baby_horn");
        GeoBone leftEar = this.getAnimationProcessor().getBone("left_ear");
        GeoBone rightEar = this.getAnimationProcessor().getBone("right_ear");

        if (entity.isBaby()) {
            head.setScaleX(1.4F);
            head.setScaleY(1.4F);
            head.setScaleZ(1.4F);
            leftEar.setScaleX(1.1F);
            leftEar.setScaleY(1.1F);
            leftEar.setScaleZ(1.1F);
            rightEar.setScaleX(1.1F);
            rightEar.setScaleY(1.1F);
            rightEar.setScaleZ(1.1F);
        } else {
            head.setScaleX(1.0F);
            head.setScaleY(1.0F);
            head.setScaleZ(1.0F);
            leftEar.setScaleX(1.0F);
            leftEar.setScaleY(1.0F);
            leftEar.setScaleZ(1.0F);
            rightEar.setScaleX(1.0F);
            rightEar.setScaleY(1.0F);
            rightEar.setScaleZ(1.0F);
        }

        if (!entity.isSprinting()) {
            head.setRotY(extraDataOfType.netHeadYaw() * Mth.DEG_TO_RAD);
        }

        bigHorn.setHidden(entity.isBaby());
        smallHorn.setHidden(entity.isBaby());
        babyHorn.setHidden(!entity.isBaby());
    }
}
