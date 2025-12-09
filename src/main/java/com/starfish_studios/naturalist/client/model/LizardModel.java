package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.common.entity.Lizard;
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
public class LizardModel extends GeoModel<Lizard> {
    public static final ResourceLocation[] TEXTURE_LOCATIONS = new ResourceLocation[]{
            new ResourceLocation(Naturalist.MOD_ID, "textures/entity/lizard/green.png"),
            new ResourceLocation(Naturalist.MOD_ID, "textures/entity/lizard/brown.png"),
            new ResourceLocation(Naturalist.MOD_ID, "textures/entity/lizard/beardie.png"),
            new ResourceLocation(Naturalist.MOD_ID, "textures/entity/lizard/leopard_gecko.png")
    };

    @Override
    public ResourceLocation getModelResource(Lizard lizard) {
        return new ResourceLocation(Naturalist.MOD_ID, "geo/entity/lizard.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(@NotNull Lizard lizard) {
        return TEXTURE_LOCATIONS[Math.min(lizard.getVariant(), TEXTURE_LOCATIONS.length - 1)];
    }

    @Override
    public @NotNull ResourceLocation getAnimationResource(Lizard lizard) {
        return new ResourceLocation(Naturalist.MOD_ID, "animations/lizard.rp_anim.json");
    }

    @Override
    public void setCustomAnimations(@NotNull Lizard entity, long instanceId, AnimationState<Lizard> animationState) {
        super.setCustomAnimations(entity, instanceId, animationState);

        if (animationState == null) return;

        EntityModelData extraDataOfType = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        CoreGeoBone head = this.getAnimationProcessor().getBone("head");
        CoreGeoBone tail = this.getAnimationProcessor().getBone("tail");

        head.setRotX(extraDataOfType.headPitch() * Mth.DEG_TO_RAD);
        head.setRotY(extraDataOfType.netHeadYaw() * Mth.DEG_TO_RAD);

        tail.setHidden(!entity.hasTail());
    }
}
