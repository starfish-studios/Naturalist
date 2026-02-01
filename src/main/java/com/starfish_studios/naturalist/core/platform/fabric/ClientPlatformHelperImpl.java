package com.starfish_studios.naturalist.core.platform.fabric;

import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class ClientPlatformHelperImpl {
    public static void setRenderLayer(@NotNull Supplier<Block> block, RenderType type) {
        // BlockRenderLayerMap was removed in newer Fabric API versions
        // In MC 1.21, block render types are typically set via model JSON or block
        // properties
        // For cutout blocks, the block model itself specifies the render layer
        // This method is kept for API compatibility but may be a no-op
    }

    public static <T extends Entity> void registerEntityRenderers(@NotNull Supplier<EntityType<T>> type,
            EntityRendererProvider<T> renderProvider) {
        EntityRendererRegistry.register(type.get(), renderProvider);
    }
}
