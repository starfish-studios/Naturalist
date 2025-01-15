package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.common.entity.Deer;
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
public class DeerModel extends GeoModel<Deer> {
    @Override
    public ResourceLocation getModelResource(Deer deer, @Nullable GeoRenderer<Deer> geoRenderer) {
        return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "geo/entity/deer.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Deer deer, @Nullable GeoRenderer<Deer> geoRenderer) {
        if (deer.isBaby()) {
            return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/deer.png");
        }

        return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/deer.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Deer deer) {
        return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "animations/deer.rp_anim.json");
    }

    @Override
    public void setCustomAnimations(Deer entity, long instanceId, AnimationState<Deer> animationState) {
        super.setCustomAnimations(entity, instanceId, animationState);

        if (animationState == null) return;

        EntityModelData extraDataOfType = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        GeoBone head = this.getAnimationProcessor().getBone("head");
        GeoBone antlers = this.getAnimationProcessor().getBone("antlers");

        if (entity.isBaby()) {
            head.setScaleX(1.4F);
            head.setScaleY(1.4F);
            head.setScaleZ(1.4F);
            head.setScaleX(1.6F);
            head.setScaleY(1.6F);
            head.setScaleZ(1.6F);
        } else {
            head.setScaleX(1.0F);
            head.setScaleY(1.0F);
            head.setScaleZ(1.0F);
        }

        antlers.setHidden(entity.isBaby());

        if (!entity.isEating()) {
            head.setRotX(extraDataOfType.headPitch() * Mth.DEG_TO_RAD);
            head.setRotY(extraDataOfType.netHeadYaw() * Mth.DEG_TO_RAD);
        }
    }

}
