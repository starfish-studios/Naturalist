package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.common.entity.Giraffe;
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
public class GiraffeModel extends GeoModel<Giraffe> {
    @Override
    public ResourceLocation getModelResource(Giraffe giraffe) {
        return new ResourceLocation(Naturalist.MOD_ID, "geo/entity/giraffe.geo.json");
    }

    @Override
    public @NotNull ResourceLocation getTextureResource(Giraffe giraffe) {
        return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/giraffe.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Giraffe giraffe) {
        return new ResourceLocation(Naturalist.MOD_ID, "animations/giraffe.rp_anim.json");
    }

    @Override
    public void setCustomAnimations(@NotNull Giraffe entity, long instanceId, AnimationState<Giraffe> animationState) {
        super.setCustomAnimations(entity, instanceId, animationState);

        if (animationState == null) return;


        EntityModelData extraDataOfType = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        CoreGeoBone head = this.getAnimationProcessor().getBone("head");

        if (entity.isBaby()) {
            head.setScaleX(1.3F);
            head.setScaleY(1.3F);
            head.setScaleZ(1.3F);
        } else {
            head.setScaleX(1.0F);
            head.setScaleY(1.0F);
            head.setScaleZ(1.0F);
        }

        head.setRotX(extraDataOfType.headPitch() * Mth.DEG_TO_RAD);
        head.setRotY(extraDataOfType.netHeadYaw() * Mth.DEG_TO_RAD);
    }
}
