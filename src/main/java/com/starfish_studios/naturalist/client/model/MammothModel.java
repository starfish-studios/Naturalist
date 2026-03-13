package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.common.entity.Mammoth;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

@Environment(EnvType.CLIENT)
public class MammothModel extends GeoModel<Mammoth> {
    @Override
    @SuppressWarnings("removal")
    public @NotNull ResourceLocation getModelResource(Mammoth entity) {
        return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "geo/entity/mammoth.geo.json");
    }

    @Override
    @SuppressWarnings("removal")
    public ResourceLocation getTextureResource(@NotNull Mammoth entity) {
        return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/mammoth.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Mammoth entity) {
        return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "animations/mammoth.rp_anim.json");
    }

    @Override
    public void setCustomAnimations(Mammoth entity, long instanceId, AnimationState<Mammoth> animationState) {
        super.setCustomAnimations(entity, instanceId, animationState);

        if (animationState == null) return;

        EntityModelData extraDataOfType = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        GeoBone head = this.getAnimationProcessor().getBone("head");

        if (entity.isBaby()) {
            head.setScaleX(1.6F);
            head.setScaleY(1.6F);
            head.setScaleZ(1.6F);
        } else {
            head.setScaleX(1.0F);
            head.setScaleY(1.0F);
            head.setScaleZ(1.0F);
        }

        head.setRotX(extraDataOfType.headPitch() * Mth.DEG_TO_RAD);
        head.setRotY(extraDataOfType.netHeadYaw() * Mth.DEG_TO_RAD);

        // Show/hide saddle and chest bones
        GeoBone saddle = this.getAnimationProcessor().getBone("saddle");
        GeoBone chests = this.getAnimationProcessor().getBone("chests");

        saddle.setHidden(!entity.isSaddled());
        chests.setHidden(!entity.hasChest());
    }
}
