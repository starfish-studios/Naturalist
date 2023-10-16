package com.starfish_studios.naturalist.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.starfish_studios.naturalist.client.model.LizardTailModel;
import com.starfish_studios.naturalist.common.entity.LizardTail;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

@Environment(EnvType.CLIENT)
public class LizardTailRenderer extends GeoEntityRenderer<LizardTail> {
    public LizardTailRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new LizardTailModel());
        this.shadowRadius = 0.4F;
    }

   public RenderType getRenderType(LizardTail animatable, float partialTicks, PoseStack stack, @Nullable MultiBufferSource renderTypeBuffer, @Nullable VertexConsumer vertexBuilder, int packedLightIn, ResourceLocation textureLocation) {
        return RenderType.entityCutoutNoCull(textureLocation);
    }

    @Override
    public void render(LizardTail animatable, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.translate(0, -0.3, 0);
        super.render(animatable, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
