package com.starfish_studios.naturalist.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.starfish_studios.naturalist.client.model.BearModel;
import com.starfish_studios.naturalist.common.entity.Bear;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.world.entity.EquipmentSlot;
import org.joml.Quaternionf;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.client.renderer.layers.BearItemLayer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.entity.state.HoldingEntityRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollector;

@Environment(EnvType.CLIENT)
public class BearRenderer extends GeoEntityRenderer<Bear, NaturalistGeoRenderState> {
    private final ItemModelResolver itemModelResolver;

    public BearRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BearModel());
        this.shadowRadius = 0.9F;
        // Aggressively clear layers using Reflection (handles protected/obfuscated
        // fields)
        try {
            Class<?> clazz = this.getClass();
            while (clazz != null && clazz != Object.class) {
                for (java.lang.reflect.Field field : clazz.getDeclaredFields()) {
                    // Check for List type (candidates for 'layers' or 'renderLayers')
                    if (java.util.List.class.isAssignableFrom(field.getType())) {
                        field.setAccessible(true);
                        Object obj = field.get(this);
                        if (obj instanceof java.util.List) {
                            java.util.List<?> list = (java.util.List<?>) obj;
                            if (!list.isEmpty()) {
                                // Check content type to identify Layer lists
                                Object first = list.get(0);
                                String className = first.getClass().getName();
                                // Clear if it's a GeoRenderLayer or Vanilla RenderLayer
                                if (first instanceof software.bernie.geckolib.renderer.layer.GeoRenderLayer ||
                                        className.contains("RenderLayer") ||
                                        className.contains("Layer")) {
                                    // System.out.println("[BEAR_DEBUG] Cleared Layer List containing: " +
                                    // className);
                                    list.clear();
                                }
                            }
                        }
                    }
                }
                clazz = clazz.getSuperclass();
            }
        } catch (Exception e) {
            // e.printStackTrace();
        }

        this.itemModelResolver = Minecraft.getInstance().getItemModelResolver();

        // Register custom layer to handle item rendering
        this.withRenderLayer(new BearItemLayer(this));
    }

    @Override
    public void extractRenderState(Bear entity, NaturalistGeoRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);

        state.isSheared = entity.isSheared();
        state.isBaby = entity.isBaby();
        // Use bearHeldStack for custom rendering
        state.bearHeldStack = entity.getItemBySlot(EquipmentSlot.MAINHAND);

        // Extract base state (which might populate heldStack/heldItemState)
        HoldingEntityRenderState.extractHoldingEntityRenderState(entity, state, this.itemModelResolver);

    }

    @Override
    public ResourceLocation getTextureLocation(NaturalistGeoRenderState state) {
        return super.getTextureLocation(state);
    }

    @Override
    public float getMotionAnimThreshold(Bear animatable) {
        return 0.000001f;
    }

    @Override
    public void renderBone(NaturalistGeoRenderState state, PoseStack stack,
            GeoBone bone, VertexConsumer buffer,
            CameraRenderState cameraState, int packedLight, int packedOverlay, int renderColor) {

        // V32: Ghost Item Fix - Strict Buffer Check
        // Filter out Shadow/Outline buffers (which usually appear as
        // net.minecraft.class_287 or similar wrappers)
        // Only render if we are in the main render pass (BufferSource).
        // V33 Fix: Remove strict check. Use Captured Buffer Source.
        /*
         * Item rendering moved to BearItemLayer (BlockAndItemGeoLayer)
         * which handles MultiBufferSource correctly, fixing the Ghost Item bug.
         */

        super.renderBone(state, stack, bone, buffer, cameraState, packedLight,
                packedOverlay, renderColor);
    }

    @Override
    public NaturalistGeoRenderState createRenderState(Bear entity, Void unused) {
        return new NaturalistGeoRenderState();
    }
}
