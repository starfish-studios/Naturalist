package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.common.entity.Giraffe;
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
public class GiraffeModel extends GeoModel<Giraffe> {
    @Override
    public ResourceLocation getModelResource(Giraffe giraffe, @Nullable GeoRenderer<Giraffe> geoRenderer) {
        return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "geo/entity/giraffe.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Giraffe giraffe, @Nullable GeoRenderer<Giraffe> geoRenderer) {
        return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/giraffe.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Giraffe giraffe) {
        return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "animations/giraffe.rp_anim.json");
    }

    @Override
    public void setCustomAnimations(Giraffe entity, long instanceId, AnimationState<Giraffe> animationState) {
        super.setCustomAnimations(entity, instanceId, animationState);

        if (animationState == null) return;


        EntityModelData extraDataOfType = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        GeoBone head = this.getAnimationProcessor().getBone("head");

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
