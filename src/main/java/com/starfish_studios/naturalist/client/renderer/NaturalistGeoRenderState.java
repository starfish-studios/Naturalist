package com.starfish_studios.naturalist.client.renderer;

import net.minecraft.client.renderer.entity.state.HoldingEntityRenderState;
import software.bernie.geckolib.renderer.base.GeoRenderState;
import software.bernie.geckolib.constant.dataticket.DataTicket;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import java.util.Map;
import java.util.HashMap;

/**
 * Shared base RenderState for GeckoLib 5 entity renderers.
 * GeckoLib 5 requires RenderState extending EntityRenderState & implementing
 * GeoRenderState.
 */
public class NaturalistGeoRenderState extends HoldingEntityRenderState implements GeoRenderState {
    private final Map<DataTicket<?>, Object> data = new HashMap<>();
    public boolean isSheared;
    public boolean isBaby;
    public net.minecraft.world.item.ItemStack heldStack = net.minecraft.world.item.ItemStack.EMPTY;
    // Shadow Variable for BearRenderer usage (to avoid default GeoEntityRenderer
    // handling)
    public net.minecraft.world.item.ItemStack bearHeldStack = net.minecraft.world.item.ItemStack.EMPTY;
    public final ItemStackRenderState heldItemState = new ItemStackRenderState();
    public net.minecraft.client.renderer.MultiBufferSource bufferSource;

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
