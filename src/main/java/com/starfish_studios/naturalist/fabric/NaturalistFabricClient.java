package com.starfish_studios.naturalist.fabric;

import com.starfish_studios.naturalist.NaturalistClient;
import com.starfish_studios.naturalist.client.model.ZebraModel;
import com.starfish_studios.naturalist.client.renderer.ZebraRenderer;
import com.starfish_studios.naturalist.core.registry.NaturalistEntityTypes;
import com.starfish_studios.naturalist.core.registry.NaturalistRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.component.CustomData;

public class NaturalistFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        NaturalistClient.init();
        registerEntityRenders();
        EntityModelLayerRegistry.registerModelLayer(ZebraRenderer.LAYER_LOCATION, ZebraModel::createBodyLayer);

        ItemProperties.register(NaturalistRegistry.BUTTERFLY.get(), ResourceLocation.withDefaultNamespace("variant"), (stack, world, entity, num) -> {
            CompoundTag compoundTag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
            if (compoundTag.contains("Variant")) {
                return (float)compoundTag.getInt("Variant") / 5;
            }
            return 0.2F;
        });

        ItemProperties.register(NaturalistRegistry.SNAIL_BUCKET.get(), ResourceLocation.withDefaultNamespace("color"), (stack, world, entity, num) -> {
            CompoundTag compoundTag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
            if (compoundTag.contains("Color")) {
                return (float)compoundTag.getInt("Color") / 15;
            }
            return 0.8F;
        });

        /* ItemProperties.register(NaturalistRegistry.MOTH.get(), ResourceLocation.fromNamespaceAndPath("variant"), (stack, world, entity, num) -> {
            CompoundTag compoundTag = stack.getTag();
            if (compoundTag != null && compoundTag.contains("Variant")) {
                return (float)compoundTag.getInt("Variant") / 2;
            }
            return 0;
        });
         */

    }

    private void registerEntityRenders() {
        EntityRendererRegistry.register(NaturalistEntityTypes.DUCK_EGG.get(), (context) -> new ThrownItemRenderer<>(context, 1.0F, false));
    }
}
