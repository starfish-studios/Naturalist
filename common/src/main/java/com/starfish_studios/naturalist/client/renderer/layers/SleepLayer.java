package com.starfish_studios.naturalist.client.renderer.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

@Environment(EnvType.CLIENT)
public class SleepLayer<T extends LivingEntity & GeoAnimatable> extends GeoRenderLayer<T> {
    private final ResourceLocation model;
    private final ResourceLocation sleepLayer;

    public SleepLayer(GeoRenderer<T> entityRendererIn, ResourceLocation model, ResourceLocation sleepLayer) {
        super(entityRendererIn);
        this.model = model;
        this.sleepLayer = sleepLayer;
    }

    @Override
    public void render(PoseStack poseStack, T entity, BakedGeoModel bakedModel, RenderType renderType,
                       MultiBufferSource bufferSource, VertexConsumer buffer, float partialTicks,
                       int packedLightIn, int packedOverlay, int renderColor) {

        if (entity.isSleeping()) {
            RenderType renderLayer = RenderType.entityCutoutNoCull(sleepLayer);
            // poseStack.pushPose();
            getRenderer().reRender(getDefaultBakedModel(entity, this.getRenderer()), poseStack, bufferSource, entity, renderLayer, bufferSource.getBuffer(renderLayer), partialTicks, packedLightIn, OverlayTexture.NO_OVERLAY, renderColor);
            // poseStack.popPose();
        }
    }

}
