package com.starfish_studios.naturalist.client.renderer.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.common.entity.Tortoise;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class TortoiseSkinLayer extends GeoRenderLayer<Tortoise> {
    private static final ResourceLocation DONATELLO = ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/tortoise/donatello.png");
    private static final ResourceLocation LEONARDO = ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/tortoise/leonardo.png");
    private static final ResourceLocation MICHELANGELO = ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/tortoise/michelangelo.png");
    private static final ResourceLocation RAPHAEL = ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "textures/entity/tortoise/raphael.png");
    public TortoiseSkinLayer(GeoRenderer<Tortoise> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack poseStack, Tortoise entity, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTicks, int packedLightIn, int packedOverlay, int renderColor) {
        String name = entity.getName().getString();
        ResourceLocation skin = switch (name) {
            case "Donatello" -> DONATELLO;
            case "Leonardo" -> LEONARDO;
            case "Michelangelo" -> MICHELANGELO;
            case "Raphael" -> RAPHAEL;
            default -> null;
        };
        if (skin != null) {
            RenderType renderLayer = RenderType.entityCutoutNoCull(skin);
            getRenderer().reRender(getDefaultBakedModel(entity, this.getRenderer()), poseStack, bufferSource, entity, renderLayer, bufferSource.getBuffer(renderLayer), partialTicks, packedLightIn, OverlayTexture.NO_OVERLAY, renderColor);
        }
    }
}
