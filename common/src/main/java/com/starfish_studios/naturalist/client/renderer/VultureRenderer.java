package com.starfish_studios.naturalist.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.starfish_studios.naturalist.client.model.VultureModel;
import com.starfish_studios.naturalist.common.entity.Vulture;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemDisplayContext;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

@Environment(EnvType.CLIENT)
public class VultureRenderer extends GeoEntityRenderer<Vulture> {
    public VultureRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new VultureModel());
        this.shadowRadius = 0.65F;
    }

   public RenderType getRenderType(Vulture animatable, float partialTicks, PoseStack stack, @Nullable MultiBufferSource renderTypeBuffer, @Nullable VertexConsumer vertexBuilder, int packedLightIn, ResourceLocation textureLocation) {
        return RenderType.entityCutoutNoCull(textureLocation);
    }

    @Override
    public void renderRecursively(PoseStack stack, Vulture animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight,
        int packedOverlay, float red, float green, float blue, float alpha) {
        if (bone.getName().equals("head")) {
            stack.pushPose();
            stack.mulPose(new Quaternionf(-0.7071f, 0.0f, 0.0f, 0.7071f));
            stack.translate(0.0D, 0.6D, 0.2D);
//            stack.scale(1.0F,1.0F,1.0F);
            Minecraft.getInstance().getItemRenderer().renderStatic(animatable.getItemBySlot(EquipmentSlot.MAINHAND), ItemDisplayContext.GROUND, packedLight, packedOverlay, stack, bufferSource, animatable.level(), 0);
            stack.popPose();
            buffer = bufferSource.getBuffer(RenderType.entityTranslucent(getTextureLocation(animatable)));
        }
        super.renderRecursively(stack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
