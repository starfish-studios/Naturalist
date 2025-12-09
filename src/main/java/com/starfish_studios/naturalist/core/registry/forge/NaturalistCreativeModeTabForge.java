//? if forge {
package com.starfish_studios.naturalist.core.registry.forge;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.core.registry.NaturalistCreativeTabContents;
import com.starfish_studios.naturalist.core.registry.NaturalistItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;


@Mod.EventBusSubscriber(modid = Naturalist.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class NaturalistCreativeModeTabForge {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Naturalist.MOD_ID);

    @SuppressWarnings("unused")
    public static final RegistryObject<CreativeModeTab> ITEM_GROUP = CREATIVE_MODE_TABS.register("item_group",
            () -> CreativeModeTab.builder()
                    .icon(NaturalistItems.BUG_NET.get()::getDefaultInstance)
                    .title(Component.translatable("itemGroup.naturalist.tab"))
                    .displayItems((featureFlagSet, output) -> NaturalistCreativeTabContents.addItems(output))
                    .build()
    );
}
//?}
