package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.client.NaturalistDataTickets;
import com.starfish_studios.naturalist.common.entity.Lion;
import software.bernie.geckolib.renderer.base.GeoRenderState;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;

@Environment(EnvType.CLIENT)public class LionModel extends GeoModel<Lion> {
        @Override
        public @NotNull ResourceLocation getModelResource(GeoRenderState state) {
                return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "entity/lion");
        }

        @Override
        public ResourceLocation getTextureResource(GeoRenderState state) {
                boolean isSleeping = state.getOrDefaultGeckolibData(NaturalistDataTickets.IS_SLEEPING, false);
                boolean hasMane = state.getOrDefaultGeckolibData(NaturalistDataTickets.HAS_MANE, false);
                boolean isBaby = state.getOrDefaultGeckolibData(NaturalistDataTickets.IS_BABY, false);
                boolean isAggressive = state.getOrDefaultGeckolibData(NaturalistDataTickets.IS_AGGRESSIVE, false);

                return (isSleeping && hasMane) && !isBaby
                                ? ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID,
                                                "textures/entity/lion/lion_sleep.png")
                                : (!hasMane && isSleeping || isBaby && isSleeping)
                                                ? ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID,
                                                                "textures/entity/lion/lioness_sleep.png")
                                                : (!hasMane && !isAggressive || isBaby)
                                                                ? ResourceLocation.fromNamespaceAndPath(
                                                                                Naturalist.MOD_ID,
                                                                                "textures/entity/lion/lioness.png")
                                                                : (isAggressive) && !isBaby && hasMane
                                                                                ? ResourceLocation.fromNamespaceAndPath(
                                                                                                Naturalist.MOD_ID,
                                                                                                "textures/entity/lion/lion_angry.png")
                                                                                : (!hasMane && isAggressive)
                                                                                                || isBaby && isAggressive
                                                                                                                ? ResourceLocation
                                                                                                                                .fromNamespaceAndPath(
                                                                                                                                                Naturalist.MOD_ID,
                                                                                                                                                "textures/entity/lion/lioness_angry.png")
                                                                                                                : ResourceLocation
                                                                                                                                .fromNamespaceAndPath(
                                                                                                                                                Naturalist.MOD_ID,
                                                                                                                                                "textures/entity/lion/lion.png");
        }

        @Override
        public void addAdditionalStateData(Lion lion, GeoRenderState state) {
                state.addGeckolibData(NaturalistDataTickets.IS_SLEEPING, lion.isSleeping());
                state.addGeckolibData(NaturalistDataTickets.HAS_MANE, lion.hasMane());
                state.addGeckolibData(NaturalistDataTickets.IS_BABY, lion.isBaby());
                state.addGeckolibData(NaturalistDataTickets.IS_AGGRESSIVE, lion.isAggressive());
        }

        @Override
        public ResourceLocation getAnimationResource(Lion entity) {
                return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "lion");
        }
}

