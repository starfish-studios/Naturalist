package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.common.entity.Rhino;
//? if fabric {
/*import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
*///?} else if forge {
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
//?}
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

//? if fabric {
/*@Environment(EnvType.CLIENT)
*///?} else if forge {
@OnlyIn(Dist.CLIENT)
//?}
public class RhinoModel extends GeoModel<Rhino> {
    @Override
    public ResourceLocation getModelResource(Rhino rhino) {
        return new ResourceLocation(Naturalist.MOD_ID, "geo/entity/rhino.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Rhino rhino) {
        return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/rhino.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Rhino rhino) {
        return new ResourceLocation(Naturalist.MOD_ID, "animations/rhino.rp_anim.json");
    }

    @Override
    public void setCustomAnimations(@NotNull Rhino entity, long instanceId, AnimationState<Rhino> animationState) {
        super.setCustomAnimations(entity, instanceId, animationState);

        if (animationState == null) return;

        EntityModelData extraDataOfType = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        CoreGeoBone head = this.getAnimationProcessor().getBone("head");
        CoreGeoBone bigHorn = this.getAnimationProcessor().getBone("big_horn");
        CoreGeoBone smallHorn = this.getAnimationProcessor().getBone("small_horn");
        CoreGeoBone babyHorn = this.getAnimationProcessor().getBone("baby_horn");
        CoreGeoBone leftEar = this.getAnimationProcessor().getBone("left_ear");
        CoreGeoBone rightEar = this.getAnimationProcessor().getBone("right_ear");

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
