package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.common.entity.Bear;
//? if fabric {
/*import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
*///?} else if forge {
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
//?}
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Items;
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
public class BearModel extends GeoModel<Bear> {
    @Override
    public @NotNull ResourceLocation getModelResource(Bear bear) {
        return new ResourceLocation(Naturalist.MOD_ID, "geo/entity/bear.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(@NotNull Bear bear) {
        if (bear.isAngry()) {
            return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/bear/bear_angry.png");
        } else if (bear.isSleeping()) {
            return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/bear/bear_sleep.png");
        } else if (bear.isEating()) {
            if (bear.getMainHandItem().is(Items.SWEET_BERRIES)) {
                return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/bear/bear_berries.png");
            } else if (bear.getMainHandItem().is(Items.HONEYCOMB)) {
                return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/bear/bear_honey.png");
            }
            return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/bear/bear.png");
        }

        return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/bear/bear.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Bear bear) {
        return new ResourceLocation(Naturalist.MOD_ID, "animations/bear.rp_anim.json");
    }

    @Override
    public void setCustomAnimations(Bear entity, long instanceId, AnimationState<Bear> animationState) {
        super.setCustomAnimations(entity, instanceId, animationState);

        if (animationState == null) return;

        EntityModelData extraDataOfType = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        CoreGeoBone head = this.getAnimationProcessor().getBone("head");

        if (entity.isBaby()) {
            head.setScaleX(1.8F);
            head.setScaleY(1.8F);
            head.setScaleZ(1.8F);
        } else {
            head.setScaleX(1.0F);
            head.setScaleY(1.0F);
            head.setScaleZ(1.0F);
        }

        if (!entity.isSleeping() && !entity.isEating() && !entity.isSitting()) {
            head.setRotX(extraDataOfType.headPitch() * Mth.DEG_TO_RAD);
            head.setRotY(extraDataOfType.netHeadYaw() * Mth.DEG_TO_RAD);
        }
    }
}
