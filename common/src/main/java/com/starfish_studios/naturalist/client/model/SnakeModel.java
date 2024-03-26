package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.common.entity.Snake;
import com.starfish_studios.naturalist.core.registry.NaturalistEntityTypes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

@Environment(EnvType.CLIENT)
public class SnakeModel extends GeoModel<Snake> {
    @Override
    public ResourceLocation getModelResource(Snake snake) {
        return new ResourceLocation(Naturalist.MOD_ID, "geo/entity/snake.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Snake snake) {
        if (snake.getType().equals(NaturalistEntityTypes.CORAL_SNAKE.get())) {
            return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/snake/coral_snake.png");
        } else if (snake.getType().equals(NaturalistEntityTypes.RATTLESNAKE.get())) {
            return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/snake/rattlesnake.png");
        } else {
            return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/snake/snake.png");
        }
    }

    @Override
    public ResourceLocation getAnimationResource(Snake snake) {
        return new ResourceLocation(Naturalist.MOD_ID, "animations/snake.animation.json");
    }

    @Override
    public void setCustomAnimations(Snake animatable, long instanceId, AnimationState<Snake> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);

        if (animationState == null) return;

        EntityModelData extraDataOfType = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        CoreGeoBone head = this.getAnimationProcessor().getBone("head");
        CoreGeoBone tail2 = this.getAnimationProcessor().getBone("tail2");
        CoreGeoBone tail4 = this.getAnimationProcessor().getBone("tail4");

        if (!animatable.isSleeping()) {
            head.setRotX(extraDataOfType.headPitch() * Mth.DEG_TO_RAD);
            head.setRotY(extraDataOfType.netHeadYaw() * Mth.DEG_TO_RAD);
        }
        if (!animatable.getMainHandItem().isEmpty()) {
            tail2.setScaleX(1.5F);
            tail2.setScaleY(1.5F);
        }
        tail4.setHidden(!animatable.getType().equals(NaturalistEntityTypes.RATTLESNAKE.get()));
    }
}
