package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.common.entity.Vulture;
//? if fabric {
/*import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
*///?} else if forge {
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
//?}
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

//? if fabric {
/*@Environment(EnvType.CLIENT)
*///?} else if forge {
@OnlyIn(Dist.CLIENT)
//?}
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
        return new ResourceLocation(Naturalist.MOD_ID, "animations/vulture.rp_anim.json");
    }

    @Override
    public void setCustomAnimations(Vulture entity, long instanceId, AnimationState<Vulture> animationState) {
        super.setCustomAnimations(entity, instanceId, animationState);

        if (animationState == null) return;

        EntityModelData extraDataOfType = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        CoreGeoBone head = this.getAnimationProcessor().getBone("head");

        head.setRotX(extraDataOfType.headPitch() * Mth.DEG_TO_RAD);
        head.setRotY(extraDataOfType.netHeadYaw() * Mth.DEG_TO_RAD);
    }
}
