//? if forge {
package com.starfish_studios.naturalist.forge;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.NaturalistClient;
import com.starfish_studios.naturalist.client.model.ZebraModel;
import com.starfish_studios.naturalist.client.renderer.ZebraRenderer;
import com.starfish_studios.naturalist.core.registry.NaturalistEntityTypes;
import com.starfish_studios.naturalist.core.registry.NaturalistItems;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(modid = Naturalist.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class NaturalistForgeClient {
    @SubscribeEvent
    public static void init(FMLClientSetupEvent event) {
        NaturalistClient.init();
        registerEntityRenderers();

        ItemProperties.register(NaturalistItems.BUTTERFLY.get(), new ResourceLocation("variant"), (stack, world, entity, num) -> {
            CompoundTag compoundTag = stack.getTag();
            if (compoundTag != null && compoundTag.contains("Variant")) {
                return (float)compoundTag.getInt("Variant") / 4;
            }
            return 0.0F;
        });

        ItemProperties.register(NaturalistItems.SNAIL_BUCKET.get(), new ResourceLocation("color"), (stack, world, entity, num) -> {
            CompoundTag compoundTag = stack.getTag();
            if (compoundTag != null && compoundTag.contains("Color")) {
                return (float)compoundTag.getInt("Color") / 15;
            }
            return 0.8F;
        });
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.@NotNull RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ZebraRenderer.LAYER_LOCATION, ZebraModel::createBodyLayer);
    }

    public static void registerEntityRenderers() {
        EntityRenderers.register(NaturalistEntityTypes.DUCK_EGG.get(), ThrownItemRenderer::new);
    }
}
//?}

