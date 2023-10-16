package com.starfish_studios.naturalist.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.client.model.TortoiseModel;
import com.starfish_studios.naturalist.client.renderer.layers.TortoiseSkinLayer;
import com.starfish_studios.naturalist.common.entity.Tortoise;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

@Environment(EnvType.CLIENT)
public class TortoiseRenderer extends GeoEntityRenderer<Tortoise> {
    public TortoiseRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new TortoiseModel());
        this.shadowRadius = 0.8F;
        this.addRenderLayer(new TortoiseSkinLayer(this, new ResourceLocation(Naturalist.MOD_ID, "geo/tortoise.geo.json")));
    }

    @Override
    public void render(Tortoise animatable, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        if (animatable.isBaby()) {
            poseStack.scale(0.5F, 0.5F, 0.5F);
        }else{
            poseStack.scale(1.0F, 1.0F, 1.0F);
        }
        super.render(animatable, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

   public RenderType getRenderType(Tortoise animatable, float partialTicks, PoseStack stack, @Nullable MultiBufferSource renderTypeBuffer, @Nullable VertexConsumer vertexBuilder, int packedLightIn, ResourceLocation textureLocation) {
        return RenderType.entityCutoutNoCull(textureLocation);
    }
}
