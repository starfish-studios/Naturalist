package com.starfish_studios.naturalist.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.starfish_studios.naturalist.client.model.HippoModel;
import com.starfish_studios.naturalist.common.entity.Hippo;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.constant.dataticket.DataTicket;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.base.GeoRenderState;

import java.util.HashMap;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class HippoRenderer extends GeoEntityRenderer<Hippo, HippoRenderer.HippoRenderState> {
    public HippoRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new HippoModel());
        this.shadowRadius = 1.1F;
    }

    @Override
    public void extractRenderState(Hippo entity, HippoRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.isBaby = entity.isBaby();
    }

    public static class HippoRenderState extends LivingEntityRenderState implements GeoRenderState {
        public boolean isBaby;
        private final Map<DataTicket<?>, Object> data = new HashMap<>();

        @Override
        public <D> void addGeckolibData(DataTicket<D> ticket, D data) {
            this.data.put(ticket, data);
        }

        @Override
        @SuppressWarnings("unchecked")
        public <D> D getGeckolibData(DataTicket<D> ticket) {
            return (D) this.data.get(ticket);
        }

        @Override
        public boolean hasGeckolibData(DataTicket<?> ticket) {
            return this.data.containsKey(ticket);
        }

        @Override
        public Map<DataTicket<?>, Object> getDataMap() {
            return this.data;
        }

        @Override
        public <D> D getOrDefaultGeckolibData(DataTicket<D> ticket, D defaultValue) {
            D data = getGeckolibData(ticket);
            return data != null ? data : defaultValue;
        }
    }

    @Override
    public float getMotionAnimThreshold(Hippo animatable) {
        return 0.000001f;
    }

    public RenderType getRenderType(Hippo entity, float partialTicks, PoseStack stack,
            @Nullable MultiBufferSource renderTypeBuffer, @Nullable VertexConsumer vertexBuilder, int packedLightIn,
            ResourceLocation textureLocation) {
        return RenderType.entityCutoutNoCull(textureLocation);
    }

    // TODO: Re-enable and adapt to GeckoLib 5 API
    /*
     * @Override
     * public void renderRecursively(PoseStack stack, Hippo entity, GeoBone bone,
     * RenderType renderType,
     * MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender,
     * float partialTick,
     * int packedLight,
     * int packedOverlay, float red, float green, float blue, float alpha) {
     * if (bone.getName().equals("botjaw") && animatable.getMainHandItem().getItem()
     * instanceof BlockItem blockItem) {
     * // ...
     * }
     * super.renderRecursively(stack, entity, bone, renderType, bufferSource,
     * buffer, isReRender, partialTick,
     * packedLight, packedOverlay, red, green, blue, alpha);
     * }
     */

    @Override
    public HippoRenderer.HippoRenderState createRenderState(Hippo entity, Void unused) {
        return new HippoRenderer.HippoRenderState();
    }
}
