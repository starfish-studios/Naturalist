package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.common.entity.Alligator;
import com.starfish_studios.naturalist.common.entity.Whale;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

import static com.starfish_studios.naturalist.Naturalist.MOD_ID;

public class WhaleModel extends DefaultedEntityGeoModel<Whale> {

    public WhaleModel() {
        super(new ResourceLocation(MOD_ID, "whale"), true);
    }

    @Override
    public ResourceLocation getTextureResource(Whale animatable) {
        return new ResourceLocation(MOD_ID, "textures/entity/whale/whale.png");
    }

    @Override
    public void setCustomAnimations(Whale animatable, long instanceId, AnimationState<Whale> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);

        if (animationState == null) return;
        EntityModelData extraDataOfType = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        CoreGeoBone head = this.getAnimationProcessor().getBone("head");

        CoreGeoBone root = this.getAnimationProcessor().getBone("root");

        root.setRotX(extraDataOfType.headPitch() * (Mth.DEG_TO_RAD / 7));
        root.setRotZ(Mth.clamp(Mth.lerp(0.1F, Mth.cos(animatable.yBodyRot * 0.1F) * 0.1F, 1.0F), -15F, 15F));

    }

}
