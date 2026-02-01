package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.client.NaturalistDataTickets;
import com.starfish_studios.naturalist.common.entity.Bear;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;

import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.base.GeoRenderState;
import software.bernie.geckolib.animatable.processing.AnimationState;

@Environment(EnvType.CLIENT)
public class BearModel extends GeoModel<Bear> {
    @Override
    public @NotNull ResourceLocation getModelResource(GeoRenderState state) {
        return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "entity/bear");
    }

    @Override
    public ResourceLocation getTextureResource(@NotNull GeoRenderState state) {
        if (state.getOrDefaultGeckolibData(NaturalistDataTickets.IS_ANGRY, false)) {
            return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/bear/bear_angry.png");
        } else if (state.getOrDefaultGeckolibData(NaturalistDataTickets.IS_SLEEPING, false)) {
            return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/bear/bear_sleep.png");
        } else if (state.getOrDefaultGeckolibData(NaturalistDataTickets.IS_EATING, false)) {
            return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/bear/bear_eat.png");
        }

        return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/bear/bear.png");
    }

    @Override
    public void addAdditionalStateData(Bear bear, GeoRenderState state) {
        state.addGeckolibData(NaturalistDataTickets.IS_ANGRY, bear.isAngry());
        state.addGeckolibData(NaturalistDataTickets.IS_SLEEPING, bear.isSleeping());
        state.addGeckolibData(NaturalistDataTickets.IS_EATING, bear.isEating());
        state.addGeckolibData(NaturalistDataTickets.IS_BABY, bear.isBaby());
    }

    @Override
    public ResourceLocation getAnimationResource(Bear bear) {
        return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "bear");
    }

    @Override
    public void setCustomAnimations(AnimationState<Bear> state) {
        super.setCustomAnimations(state);

        if (Boolean.TRUE.equals(state.getData(NaturalistDataTickets.IS_BABY))) {
            getBone("root").ifPresent(root -> {
                root.setScaleX(0.5f);
                root.setScaleY(0.5f);
                root.setScaleZ(0.5f);
            });
        } else {
            // Fix: Reset scale for adults to prevent state contamination when model
            // instance is reused
            getBone("root").ifPresent(root -> {
                root.setScaleX(1.0f);
                root.setScaleY(1.0f);
                root.setScaleZ(1.0f);
            });
        }

        if (Boolean.TRUE.equals(state.getData(NaturalistDataTickets.IS_EATING))) {
            // V18 Fix: Manually Force Head Rotation to 15 degrees (matching JSON)
            // This overrides LookAt (Head Tracking) AND ensures the correct "Look Down"
            // angle.
            // 15 degrees = 0.261 radians
            getBone("head").ifPresent(head -> {
                head.setRotX((float) Math.toRadians(-35)); // V22 Fix: Negative X = Look DOWN (Positive was Up)
                head.setRotY(0f);
                head.setRotZ(0f);
            });
            getBone("head_rot").ifPresent(headRot -> {
                // Also lock the parent bone to ensure no other animations interfere
                headRot.setRotX(0f);
                headRot.setRotY(0f);
                headRot.setRotZ(0f);
            });
        }
    }
}
