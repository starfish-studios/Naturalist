package com.starfish_studios.naturalist.client.renderer.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.common.entity.Firefly;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

@Environment(EnvType.CLIENT)
public class FireflyGlowLayer extends GeoRenderLayer<Firefly> {
    private static final ResourceLocation TOP_LAYER = new ResourceLocation(Naturalist.MOD_ID, "textures/entity/firefly/firefly_glow_top.png");
    private static final ResourceLocation BACK_LAYER = new ResourceLocation(Naturalist.MOD_ID, "textures/entity/firefly/firefly_glow_back.png");
    private static final ResourceLocation BOTTOM_LAYER = new ResourceLocation(Naturalist.MOD_ID, "textures/entity/firefly/firefly_glow_bottom.png");
    private static final ResourceLocation LEFT_LAYER = new ResourceLocation(Naturalist.MOD_ID, "textures/entity/firefly/firefly_glow_left.png");
    private static final ResourceLocation RIGHT_LAYER = new ResourceLocation(Naturalist.MOD_ID, "textures/entity/firefly/firefly_glow_right.png");
    private static final ResourceLocation MODEL = new ResourceLocation(Naturalist.MOD_ID, "geo/entity/firefly.geo.json");

    public FireflyGlowLayer(GeoRenderer<Firefly> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack poseStack, Firefly animatable, BakedGeoModel bakedModel, RenderType renderType,
                       MultiBufferSource bufferSource, VertexConsumer buffer, float partialTicks,
                       int packedLightIn, int packedOverlay) {
        RenderType top = animatable.isGlowing() ? RenderType.eyes(TOP_LAYER) : RenderType.entityCutoutNoCull(TOP_LAYER);
        RenderType back = animatable.isGlowing() ? RenderType.eyes(BACK_LAYER) : RenderType.entityCutoutNoCull(BACK_LAYER);
        RenderType bottom = animatable.isGlowing() ? RenderType.eyes(BOTTOM_LAYER) : RenderType.entityCutoutNoCull(BOTTOM_LAYER);
        RenderType left = animatable.isGlowing() ? RenderType.eyes(LEFT_LAYER) : RenderType.entityCutoutNoCull(LEFT_LAYER);
        RenderType right = animatable.isGlowing() ? RenderType.eyes(RIGHT_LAYER) : RenderType.entityCutoutNoCull(RIGHT_LAYER);

        // poseStack.pushPose();
        getRenderer().reRender(getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, top, bufferSource.getBuffer(top), partialTicks, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        getRenderer().reRender(getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, back, bufferSource.getBuffer(back), partialTicks, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        getRenderer().reRender(getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, bottom, bufferSource.getBuffer(bottom), partialTicks, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        getRenderer().reRender(getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, left, bufferSource.getBuffer(left), partialTicks, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        getRenderer().reRender(getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, right, bufferSource.getBuffer(right), partialTicks, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        // poseStack.popPose();
    }
}
