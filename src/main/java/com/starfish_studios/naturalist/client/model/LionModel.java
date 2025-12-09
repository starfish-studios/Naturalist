package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.common.entity.Lion;
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
import org.jetbrains.annotations.Nullable;
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
public class LionModel extends GeoModel<Lion> {
    @Override
    public @NotNull ResourceLocation getModelResource(Lion entity) {
        return new ResourceLocation(Naturalist.MOD_ID, "geo/entity/lion.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Lion entity) {
        return (entity.isSleeping() && entity.hasMane()) && !entity.isBaby() ? new ResourceLocation(Naturalist.MOD_ID, "textures/entity/lion/lion_sleep.png") :
                (!entity.hasMane() && entity.isSleeping() || entity.isBaby() && entity.isSleeping()) ? new ResourceLocation(Naturalist.MOD_ID, "textures/entity/lion/lioness_sleep.png") :
                        (!entity.hasMane() && !entity.isAggressive() || entity.isBaby()) ? new ResourceLocation(Naturalist.MOD_ID, "textures/entity/lion/lioness.png") :
                                (entity.isAggressive()) && !entity.isBaby() && entity.hasMane() ? new ResourceLocation(Naturalist.MOD_ID, "textures/entity/lion/lion_angry.png") :
                                        (!entity.hasMane() && entity.isAggressive()) || entity.isBaby() && entity.isAggressive() ? new ResourceLocation(Naturalist.MOD_ID, "textures/entity/lion/lioness_angry.png") :
                                                new ResourceLocation(Naturalist.MOD_ID, "textures/entity/lion/lion.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Lion entity) {
        return new ResourceLocation(Naturalist.MOD_ID, "animations/lion.rp_anim.json");
    }

    @Override
    public void setCustomAnimations(Lion entity, long instanceId, @Nullable AnimationState<Lion> animationState) {
        super.setCustomAnimations(entity, instanceId, animationState);

        if (animationState == null) return;

        EntityModelData extraDataOfType = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        CoreGeoBone head = this.getAnimationProcessor().getBone("head");
        CoreGeoBone mane = this.getAnimationProcessor().getBone("mane");

        if (entity.isBaby()) {
            head.setScaleX(1.4F);
            head.setScaleY(1.4F);
            head.setScaleZ(1.4F);
        } else {
            head.setScaleX(1.0F);
            head.setScaleY(1.0F);
            head.setScaleZ(1.0F);
        }

        mane.setHidden(!entity.hasMane() || entity.isBaby());

        if (!entity.isSleeping()) {
            head.setRotX(extraDataOfType.headPitch() * Mth.DEG_TO_RAD);
            head.setRotY(extraDataOfType.netHeadYaw() * Mth.DEG_TO_RAD);
        }
    }
}
