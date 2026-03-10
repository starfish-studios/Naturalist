package com.starfish_studios.naturalist.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.starfish_studios.naturalist.client.model.BearModel;
import com.starfish_studios.naturalist.client.renderer.layers.BearShearedLayer;
import com.starfish_studios.naturalist.common.entity.Alligator;
import com.starfish_studios.naturalist.common.entity.Bear;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemDisplayContext;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

@Environment(EnvType.CLIENT)
public class BearRenderer extends GeoEntityRenderer<Bear> {
    public BearRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BearModel());
        this.shadowRadius = 0.9F;
        this.addRenderLayer(new BearShearedLayer(this));
    }

    @Override
    public float getMotionAnimThreshold(Bear animatable) {
        return 0.000001f;
    }

    @Override
    public void render(@NotNull Bear entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        if (entity.isBaby()) {
            poseStack.scale(0.5F, 0.5F, 0.5F);
        }
        else {
            poseStack.scale(1.0F, 1.0F, 1.0F);
        }
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    @Override
    public void renderRecursively(PoseStack stack, Bear entity, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight,
        int packedOverlay, int colour) {
        if (bone.getName().equals("snout")) {
            stack.pushPose();
            stack.mulPose(new Quaternionf(-0.7071f, 0.0f, 0.0f, 0.7071f));
            stack.translate(0.0D, 1.3D, 0.8D);
            Minecraft.getInstance().getItemRenderer().renderStatic(entity.getItemBySlot(EquipmentSlot.MAINHAND), ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, packedLight, packedOverlay, stack, bufferSource, entity.level(), 0);
            stack.popPose();
            buffer = bufferSource.getBuffer(RenderType.entityTranslucent(getTextureLocation(entity)));
        }
        super.renderRecursively(stack, entity, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
    }
}
