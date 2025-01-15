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
    private static final ResourceLocation GLOW = ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/firefly/glow.png");
    private static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "geo/entity/firefly.geo.json");

    public FireflyGlowLayer(GeoRenderer<Firefly> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack poseStack, Firefly entity, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTicks, int packedLightIn, int packedOverlay, int renderColor) {
        RenderType glow = entity.isGlowing() ? RenderType.eyes(GLOW) : RenderType.entityCutoutNoCull(GLOW);

        getRenderer().reRender(getDefaultBakedModel(entity, this.getRenderer()), poseStack, bufferSource, entity, glow, bufferSource.getBuffer(glow), partialTicks, packedLightIn, OverlayTexture.NO_OVERLAY, renderColor);
    }
}
