package com.starfish_studios.naturalist.client.renderer.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.starfish_studios.naturalist.client.renderer.VultureRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import org.joml.Quaternionf;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class VultureItemLayer extends
        GeoRenderLayer<com.starfish_studios.naturalist.common.entity.Vulture, Void, VultureRenderer.VultureRenderState> {
    public VultureItemLayer(
            GeoEntityRenderer<com.starfish_studios.naturalist.common.entity.Vulture, VultureRenderer.VultureRenderState> renderer) {
        super(renderer);
    }

    @Override
    public void submitRenderTask(VultureRenderer.VultureRenderState state, PoseStack poseStack,
            BakedGeoModel bakedModel,
            net.minecraft.client.renderer.SubmitNodeCollector renderTasks,
            net.minecraft.client.renderer.state.CameraRenderState cameraState,
            int packedLight, int packedOverlay, int renderColor, boolean didRenderModel) {

        if (!state.heldStack.isEmpty()) {
            bakedModel.getBone("held_item").ifPresent(heldItemBone -> {
                poseStack.pushPose();

                // Transforms matching VultureRenderer.renderBone logic
                poseStack.mulPose(new Quaternionf(-0.7071f, 0.0f, 0.0f, 0.7071f));
                poseStack.translate(0.0D, 1.1D, 0.25D);

                final net.minecraft.client.renderer.item.ItemStackRenderState stackRenderState = new net.minecraft.client.renderer.item.ItemStackRenderState();
                final Minecraft mc = Minecraft.getInstance();

                mc.getItemModelResolver().updateForTopItem(
                        stackRenderState,
                        state.heldStack,
                        ItemDisplayContext.GROUND,
                        mc.level,
                        null,
                        0);

                // Use the provided SubmitNodeCollector to fix ghost items
                stackRenderState.submit(poseStack, renderTasks, packedLight, OverlayTexture.NO_OVERLAY, 0);

                poseStack.popPose();
            });
        }
    }
}
