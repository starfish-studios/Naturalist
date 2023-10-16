package com.starfish_studios.naturalist.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.starfish_studios.naturalist.client.model.HippoModel;
import com.starfish_studios.naturalist.common.entity.Hippo;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

@Environment(EnvType.CLIENT)
public class HippoRenderer extends GeoEntityRenderer<Hippo> {
    public HippoRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new HippoModel());
        this.shadowRadius = 1.1F;
    }

    @Override
    public void render(Hippo animatable, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        if (animatable.isBaby()) {
            poseStack.scale(0.5F, 0.5F, 0.5F);
        }else{
            poseStack.scale(1.0F, 1.0F, 1.0F);
        }
        super.render(animatable, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

   public RenderType getRenderType(Hippo animatable, float partialTicks, PoseStack stack, @Nullable MultiBufferSource renderTypeBuffer, @Nullable VertexConsumer vertexBuilder, int packedLightIn, ResourceLocation textureLocation) {
        return RenderType.entityCutoutNoCull(textureLocation);
    }

    @Override
    public void renderRecursively(PoseStack stack, Hippo animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight,
        int packedOverlay, float red, float green, float blue, float alpha) {
        if (bone.getName().equals("botjaw") && animatable.getMainHandItem().getItem() instanceof BlockItem blockItem) {
            stack.pushPose();
            stack.mulPose(new Quaternionf());
            stack.translate(-0.4D, 0.76D, -1.8D);
            stack.scale(0.675F,0.675F,0.675F);
            Minecraft.getInstance().getBlockRenderer().renderSingleBlock(blockItem.getBlock().defaultBlockState(), stack, bufferSource, packedLight, packedOverlay);
            stack.popPose();
            buffer = bufferSource.getBuffer(RenderType.entityTranslucent(getTextureLocation(animatable)));
        }
        super.renderRecursively(stack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
