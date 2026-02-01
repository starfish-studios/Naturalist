package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.client.NaturalistDataTickets;
import com.starfish_studios.naturalist.common.entity.Snake;
import com.starfish_studios.naturalist.core.registry.NaturalistEntityTypes;
import software.bernie.geckolib.renderer.base.GeoRenderState;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

@Environment(EnvType.CLIENT)public class SnakeModel extends GeoModel<Snake> {
    @Override
    public ResourceLocation getModelResource(GeoRenderState state) {
        return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "entity/snake");
    }

    @Override
    public ResourceLocation getTextureResource(GeoRenderState state) {
        int variant = state.getOrDefaultGeckolibData(NaturalistDataTickets.VARIANT_ID, 0);

        if (variant == 1) {
            return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/snake/coral_snake.png");
        } else if (variant == 2) {
            return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/snake/rattlesnake.png");
        } else {
            return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/snake/snake.png");
        }
    }

    @Override
    public void addAdditionalStateData(Snake snake, GeoRenderState state) {
        int variant = 0; // Default snake
        if (snake.getType().equals(NaturalistEntityTypes.CORAL_SNAKE.get()))
            variant = 1;
        else if (snake.getType().equals(NaturalistEntityTypes.RATTLESNAKE.get()))
            variant = 2;

        state.addGeckolibData(NaturalistDataTickets.VARIANT_ID, variant);
    }

    @Override
    public ResourceLocation getAnimationResource(Snake snake) {
        return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "snake");
    }
}

