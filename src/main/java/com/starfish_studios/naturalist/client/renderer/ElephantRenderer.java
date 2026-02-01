package com.starfish_studios.naturalist.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.starfish_studios.naturalist.client.model.ElephantModel;
import com.starfish_studios.naturalist.common.entity.Elephant;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

@Environment(EnvType.CLIENT)
public class ElephantRenderer extends GeoEntityRenderer<Elephant, NaturalistGeoRenderState> {
    public ElephantRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ElephantModel());
        this.shadowRadius = 1.5F;
    }

    @Override
    public void extractRenderState(Elephant entity, NaturalistGeoRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
    }

    @Override
    public float getMotionAnimThreshold(Elephant animatable) {
        return 0.000001f;
    }

    // render() method removed as it is final in GeckoLib 5.

    public RenderType getRenderType(Elephant entity, float partialTicks, PoseStack stack,
            @Nullable MultiBufferSource renderTypeBuffer, @Nullable VertexConsumer vertexBuilder, int packedLightIn,
            @NotNull ResourceLocation textureLocation) {
        return RenderType.entityCutoutNoCull(textureLocation);
    }

    @Override
    public NaturalistGeoRenderState createRenderState(Elephant entity, Void unused) {
        return new NaturalistGeoRenderState();
    }
}



