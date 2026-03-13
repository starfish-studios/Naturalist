package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.common.entity.FennecFox;
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
public class FennecFoxModel extends GeoModel<FennecFox> {
    @Override
    @SuppressWarnings("removal")
    public @NotNull ResourceLocation getModelResource(FennecFox entity) {
        return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "geo/entity/fennec_fox.geo.json");
    }

    @Override
    @SuppressWarnings("removal")
    public ResourceLocation getTextureResource(@NotNull FennecFox entity) {
        String variantName = FennecFox.VARIANT_NAMES[entity.getVariant()];
        return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/fennec_fox/" + variantName + ".png");
    }

    @Override
    public ResourceLocation getAnimationResource(FennecFox entity) {
        return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "animations/fennec_fox.rp_anim.json");
    }

    @Override
    public void setCustomAnimations(FennecFox entity, long instanceId, AnimationState<FennecFox> animationState) {
        super.setCustomAnimations(entity, instanceId, animationState);

        if (animationState == null) return;

        EntityModelData extraDataOfType = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        GeoBone head = this.getAnimationProcessor().getBone("head");

        if (entity.isBaby()) {
            head.setScaleX(1.5F);
            head.setScaleY(1.5F);
            head.setScaleZ(1.5F);
        } else {
            head.setScaleX(1.0F);
            head.setScaleY(1.0F);
            head.setScaleZ(1.0F);
        }

        head.setRotX(extraDataOfType.headPitch() * Mth.DEG_TO_RAD);
        head.setRotY(extraDataOfType.netHeadYaw() * Mth.DEG_TO_RAD);
    }
}
