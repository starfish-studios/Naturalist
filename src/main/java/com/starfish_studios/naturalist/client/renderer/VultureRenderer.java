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
import net.minecraft.client.renderer.entity.state.HoldingEntityRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.dataticket.DataTicket;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.base.GeoRenderState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.texture.OverlayTexture;

import java.util.HashMap;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class VultureRenderer extends GeoEntityRenderer<Vulture, VultureRenderer.VultureRenderState> {
    private final ItemModelResolver itemModelResolver;

    public static class VultureRenderState extends HoldingEntityRenderState implements GeoRenderState {
        private final Map<DataTicket<?>, Object> data = new HashMap<>();
        public ItemStack heldStack = ItemStack.EMPTY;

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

    public VultureRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new VultureModel());
        this.shadowRadius = 0.65F;
        this.itemModelResolver = Minecraft.getInstance().getItemModelResolver();
        this.withRenderLayer(new com.starfish_studios.naturalist.client.renderer.layers.VultureItemLayer(this));
    }

    @Override
    public VultureRenderer.VultureRenderState createRenderState(Vulture entity, Void unused) {
        return new VultureRenderer.VultureRenderState();
    }

    @Override
    public void extractRenderState(Vulture entity, VultureRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.heldStack = entity.getItemBySlot(EquipmentSlot.MAINHAND).copy();
        HoldingEntityRenderState.extractHoldingEntityRenderState(entity, state, this.itemModelResolver);
    }

    @Override
    public float getMotionAnimThreshold(Vulture animatable) {
        return 0.000001f;
    }

    public RenderType getRenderType(Vulture entity, float partialTicks, PoseStack stack,
            @Nullable MultiBufferSource renderTypeBuffer, @Nullable VertexConsumer vertexBuilder, int packedLightIn,
            ResourceLocation textureLocation) {
        return RenderType.entityCutoutNoCull(textureLocation);
    }

    @Override
    public void renderBone(VultureRenderState renderState, PoseStack stack, GeoBone bone, VertexConsumer buffer,
            CameraRenderState cameraState, int packedLight, int packedOverlay, int renderColor) {
        super.renderBone(renderState, stack, bone, buffer, cameraState, packedLight, packedOverlay, renderColor);
    }
}
