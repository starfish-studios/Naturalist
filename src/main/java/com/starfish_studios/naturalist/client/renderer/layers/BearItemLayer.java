package com.starfish_studios.naturalist.client.renderer.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.starfish_studios.naturalist.client.renderer.NaturalistGeoRenderState;
import com.starfish_studios.naturalist.common.entity.Bear;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;

import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Quaternionf;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class BearItemLayer extends GeoRenderLayer<Bear, Void, NaturalistGeoRenderState> {
    public BearItemLayer(GeoEntityRenderer<Bear, NaturalistGeoRenderState> renderer) {
        super(renderer);
    }

    // Implementing render method based on GL5 patterns (RenderState first)
    // If this signature is wrong, the IDE will complain.
    @Override
    public void submitRenderTask(NaturalistGeoRenderState state, PoseStack poseStack, BakedGeoModel bakedModel,
            net.minecraft.client.renderer.SubmitNodeCollector renderTasks,
            net.minecraft.client.renderer.state.CameraRenderState cameraState,
            int packedLight, int packedOverlay, int renderColor, boolean didRenderModel) {

        if (!state.bearHeldStack.isEmpty()) {
            bakedModel.getBone("snout").ifPresent(snout -> {
                poseStack.pushPose();

                // Manual Transform to Snout relative to ROOT
                // Note: If GeoRenderLayer is called after model transforms, we might only need
                // bone relative transforms?
                // But usually submitRenderTask is called at the entity root.
                // We need to apply the bone's transform?
                // "snout" bone usage suggests we might need to find the bone's global transform
                // or simple offsets.
                // The original code in renderBone used these hardcoded offsets:
                poseStack.mulPose(new Quaternionf(-0.7071f, 0.0f, 0.0f, 0.7071f));
                if (state.isBaby) {
                    poseStack.translate(0.0D, 0.6D, 0.4D);
                } else {
                    poseStack.translate(0.0D, 1.1D, 0.8D);
                }

                final net.minecraft.client.renderer.item.ItemStackRenderState stackRenderState = new net.minecraft.client.renderer.item.ItemStackRenderState();
                final Minecraft mc = Minecraft.getInstance();

                mc.getItemModelResolver().updateForTopItem(
                        stackRenderState,
                        state.bearHeldStack,
                        ItemDisplayContext.GROUND,
                        mc.level,
                        null,
                        0);

                // Use the provided SubmitNodeCollector
                stackRenderState.submit(poseStack, renderTasks, packedLight, OverlayTexture.NO_OVERLAY, 0);

                poseStack.popPose();
            });
        }
    }
}
