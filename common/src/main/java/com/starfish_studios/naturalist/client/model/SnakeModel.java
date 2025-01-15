package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.common.entity.Snake;
import com.starfish_studios.naturalist.registry.NaturalistEntityTypes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;
import software.bernie.geckolib.renderer.GeoRenderer;

@Environment(EnvType.CLIENT)
public class SnakeModel extends GeoModel<Snake> {
    @Override
    public ResourceLocation getModelResource(Snake snake, @Nullable GeoRenderer<Snake> geoRenderer) {
        return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "geo/entity/snake.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Snake snake, @Nullable GeoRenderer<Snake> geoRenderer) {
        if (snake.getType().equals(NaturalistEntityTypes.CORAL_SNAKE)) {
            return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/snake/coral_snake.png");
        } else if (snake.getType().equals(NaturalistEntityTypes.RATTLESNAKE)) {
            return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/snake/rattlesnake.png");
        } else {
            return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/snake/snake.png");
        }
    }

    @Override
    public ResourceLocation getAnimationResource(Snake snake) {
        return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "animations/snake.rp_anim.json");
    }

    @Override
    public void setCustomAnimations(Snake entity, long instanceId, AnimationState<Snake> animationState) {
        super.setCustomAnimations(entity, instanceId, animationState);

        if (animationState == null) return;

        EntityModelData extraDataOfType = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        GeoBone head = this.getAnimationProcessor().getBone("head");
        GeoBone tail2 = this.getAnimationProcessor().getBone("tail2");
        GeoBone tail4 = this.getAnimationProcessor().getBone("tail4");

        if (!entity.isSleeping()) {
            head.setRotX(extraDataOfType.headPitch() * Mth.DEG_TO_RAD);
            head.setRotY(extraDataOfType.netHeadYaw() * Mth.DEG_TO_RAD);
        }
        if (!entity.getMainHandItem().isEmpty()) {
            tail2.setScaleX(1.5F);
            tail2.setScaleY(1.5F);
        }
        tail4.setHidden(!entity.getType().equals(NaturalistEntityTypes.RATTLESNAKE));
    }
}
