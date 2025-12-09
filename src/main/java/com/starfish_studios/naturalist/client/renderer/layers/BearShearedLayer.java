package com.starfish_studios.naturalist.client.renderer.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.common.entity.Bear;
//? if fabric {
/*import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
*///?} else if forge {
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
//?}
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

//? if fabric {
/*@Environment(EnvType.CLIENT)
*///?} else if forge {
@OnlyIn(Dist.CLIENT)
//?}
public class BearShearedLayer extends GeoRenderLayer<Bear> {
    private static final ResourceLocation LAYER = new ResourceLocation(Naturalist.MOD_ID, "textures/entity/bear/bear_sheared.png");

    public BearShearedLayer(GeoRenderer<Bear> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack poseStack, Bear entity, BakedGeoModel bakedModel, RenderType renderType,
                       MultiBufferSource bufferSource, VertexConsumer buffer, float partialTicks,
                       int packedLightIn, int packedOverlay) {
        if (entity.isSheared()) {
            RenderType renderLayer = RenderType.entityCutoutNoCull(LAYER);
            getRenderer().reRender(getDefaultBakedModel(entity), poseStack, bufferSource, entity, renderLayer, bufferSource.getBuffer(renderLayer), partialTicks, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        }
    }
}
