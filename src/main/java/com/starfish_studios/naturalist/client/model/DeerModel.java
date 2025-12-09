package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.common.entity.Deer;
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
public class DeerModel extends GeoModel<Deer> {
    @Override
    public ResourceLocation getModelResource(Deer deer) {
        if (deer.isBaby()) {
            return new ResourceLocation(Naturalist.MOD_ID, "geo/entity/fawn.geo.json");
        }
        return new ResourceLocation(Naturalist.MOD_ID, "geo/entity/deer.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Deer deer) {
        if (deer.isBaby()) {
            return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/fawn.png");
        }

        return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/deer.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Deer deer) {
        return new ResourceLocation(Naturalist.MOD_ID, "animations/deer.rp_anim.json");
    }

    @Override
    public void setCustomAnimations(@NotNull Deer entity, long instanceId, AnimationState<Deer> animationState) {
        super.setCustomAnimations(entity, instanceId, animationState);

        if (animationState == null) return;

        EntityModelData extraDataOfType = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        CoreGeoBone head = this.getAnimationProcessor().getBone("head");

        if (!entity.isEating()) {
            head.setRotX(extraDataOfType.headPitch() * Mth.DEG_TO_RAD);
            head.setRotY(extraDataOfType.netHeadYaw() * Mth.DEG_TO_RAD);
        }
    }

}
