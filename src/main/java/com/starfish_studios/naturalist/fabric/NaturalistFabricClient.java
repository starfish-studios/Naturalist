package com.starfish_studios.naturalist.fabric;

import com.starfish_studios.naturalist.NaturalistClient;
import com.starfish_studios.naturalist.client.model.ZebraModel;
import com.starfish_studios.naturalist.client.renderer.ZebraRenderer;
import com.starfish_studios.naturalist.client.renderer.HippoRenderer;
import com.starfish_studios.naturalist.core.registry.NaturalistEntityTypes;
// import com.starfish_studios.naturalist.core.registry.NaturalistItems;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.world.item.SpawnEggItem;
//import net.minecraft.client.renderer.item.ItemProperties;
// import net.minecraft.nbt.CompoundTag;
// import net.minecraft.resources.ResourceLocation;

public class NaturalistFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        NaturalistClient.init();
        registerEntityRenders();
        EntityModelLayerRegistry.registerModelLayer(ZebraRenderer.LAYER_LOCATION, ZebraModel::createBodyLayer);

        EntityRendererRegistry.register(NaturalistEntityTypes.HIPPO.get(), HippoRenderer::new);
        EntityRendererRegistry.register(NaturalistEntityTypes.ZEBRA.get(), ZebraRenderer::new);

        // TODO: Reimplement ItemProperties using Item Model Predicates
        /*
         * ItemProperties.register(NaturalistRegistry.MOTH.get(), new
         * ResourceLocation("variant"), (stack, world, entity, num) -> {
         * CompoundTag compoundTag = stack.getTag();
         * if (compoundTag != null && compoundTag.contains("Variant")) {
         * return (float)compoundTag.getInt("Variant") / 2;
         * }
         * return 0;
         * });
         */

    }

    private void registerEntityRenders() {
        EntityRendererRegistry.register(NaturalistEntityTypes.DUCK_EGG.get(),
                (context) -> new ThrownItemRenderer<>(context, 1.0F, false));

        // registerSpawnEggColors();
    }

    // private void registerSpawnEggColors() {
    // net.minecraft.core.registries.BuiltInRegistries.ITEM.forEach(item -> {
    // if (item instanceof SpawnEggItem spawnEggItem) {
    // ColorProviderRegistry.ITEM.register((stack, tintIndex) ->

    // // spawnEggItem.getColor(tintIndex), item);
    // }
    // });
    // }
}
