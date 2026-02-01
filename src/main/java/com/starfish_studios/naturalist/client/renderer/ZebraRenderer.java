package com.starfish_studios.naturalist.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.client.model.ZebraModel;
import com.starfish_studios.naturalist.common.entity.Zebra;
import net.minecraft.client.renderer.MultiBufferSource;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.AbstractHorseRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.HorseRenderState;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public class ZebraRenderer extends AbstractHorseRenderer<Zebra, ZebraRenderer.ZebraRenderState, ZebraModel> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "zebra"), "main");

    public ZebraRenderer(EntityRendererProvider.@NotNull Context context) {
        super(context, new ZebraModel(context.bakeLayer(LAYER_LOCATION)),
                new ZebraModel(context.bakeLayer(LAYER_LOCATION)));
        this.shadowRadius = 1.1F;
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull ZebraRenderState state) {
        return ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/zebra.png");
    }

    @Override
    public ZebraRenderState createRenderState() {
        return new ZebraRenderState();
    }

    @Override
    public void extractRenderState(Zebra entity, ZebraRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.hasChest = entity.hasChest();
    }

    public static class ZebraRenderState extends HorseRenderState {
        public boolean hasChest;
    }
}
