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
    private final ResourceLocation model;
    private static final ResourceLocation DONATELLO = new ResourceLocation(Naturalist.MOD_ID, "textures/entity/tortoise/donatello.png");
    private static final ResourceLocation LEONARDO = new ResourceLocation(Naturalist.MOD_ID, "textures/entity/tortoise/leonardo.png");
    private static final ResourceLocation MICHELANGELO = new ResourceLocation(Naturalist.MOD_ID, "textures/entity/tortoise/michelangelo.png");
    private static final ResourceLocation RAPHAEL = new ResourceLocation(Naturalist.MOD_ID, "textures/entity/tortoise/raphael.png");
    public TortoiseSkinLayer(GeoRenderer<Tortoise> entityRendererIn, ResourceLocation model) {
        super(entityRendererIn);
        this.model = model;
    }

    @Override
    public void render(PoseStack poseStack, Tortoise animatable, BakedGeoModel bakedModel, RenderType renderType,
                       MultiBufferSource bufferSource, VertexConsumer buffer, float partialTicks,
                       int packedLightIn, int packedOverlay) {
        poseStack.pushPose();
        String name = ChatFormatting.stripFormatting(animatable.getName().getString());
        if(name == null) {
            poseStack.popPose();
            return;
        }
        ResourceLocation skin = switch (name) {
            case "Donatello" -> DONATELLO;
            case "Leonardo" -> LEONARDO;
            case "Michelangelo" -> MICHELANGELO;
            case "Raphael" -> RAPHAEL;
            default -> null;
        };
        if(skin == null) {
            poseStack.popPose();
            return;
        }
        RenderType renderType2 = RenderType.entityCutoutNoCull(skin);
        getRenderer().reRender(getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, renderType2, bufferSource.getBuffer(renderType), partialTicks, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        poseStack.popPose();
    }
}
