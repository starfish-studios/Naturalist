//? if forge {
package com.starfish_studios.naturalist.forge;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.NaturalistConfig;
import com.starfish_studios.naturalist.core.platform.forge.CommonPlatformHelperImpl;
import com.starfish_studios.naturalist.core.registry.NaturalistEntityAttributes;
import com.starfish_studios.naturalist.core.registry.NaturalistItems;
import com.starfish_studios.naturalist.core.registry.forge.NaturalistCreativeModeTabForge;
import eu.midnightdust.lib.config.MidnightConfig;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;

import static com.starfish_studios.naturalist.core.registry.forge.NaturalistBiomeModifiers.BIOME_MODIFIER_SERIALIZERS;

@SuppressWarnings("deprecation")
@Mod(Naturalist.MOD_ID)
public class NaturalistForge {

    public NaturalistForge() {
        Naturalist.init();

        MidnightConfig.init("naturalist", NaturalistConfig.class);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueue);

        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        CommonPlatformHelperImpl.BLOCKS.register(bus);
        CommonPlatformHelperImpl.ITEMS.register(bus);
        CommonPlatformHelperImpl.SOUND_EVENTS.register(bus);
        CommonPlatformHelperImpl.ENTITY_TYPES.register(bus);
        CommonPlatformHelperImpl.POTIONS.register(bus);
        CommonPlatformHelperImpl.RECIPE_TYPES.register(bus);
        CommonPlatformHelperImpl.RECIPE_SERIALIZERS.register(bus);
        NaturalistCreativeModeTabForge.CREATIVE_MODE_TABS.register(bus);
        BIOME_MODIFIER_SERIALIZERS.register(bus);

        bus.addListener(this::setup);
        bus.addListener(this::createAttributes);

        MinecraftForge.EVENT_BUS.register(this);
    }

    public void enqueue(InterModEnqueueEvent event) {
        if (ModList.get().isLoaded("diet")) {
            dietIntegration(NaturalistItems.DUCK.get(), Foods.CHICKEN);
            dietIntegration(NaturalistItems.COOKED_DUCK.get(), Foods.COOKED_CHICKEN);
            dietIntegration(NaturalistItems.VENISON.get(), Foods.BEEF);
            dietIntegration(NaturalistItems.COOKED_VENISON.get(), Foods.COOKED_BEEF);
            dietIntegration(NaturalistItems.LIZARD_TAIL.get(), NaturalistItems.LIZARD_TAIL.get().getFoodProperties());
            dietIntegration(NaturalistItems.COOKED_LIZARD_TAIL.get(), NaturalistItems.COOKED_LIZARD_TAIL.get().getFoodProperties());
            dietIntegration(NaturalistItems.CATFISH.get(), Foods.SALMON);
            dietIntegration(NaturalistItems.COOKED_CATFISH.get(), Foods.COOKED_SALMON);
            dietIntegration(NaturalistItems.BASS.get(), Foods.COD);
            dietIntegration(NaturalistItems.COOKED_BASS.get(), Foods.COOKED_COD);
        }
    }

    public static void dietIntegration(Item item, FoodProperties food) {
        InterModComms.sendTo("diet", "item",
                () -> new Tuple<Item, BiFunction<Player, ItemStack, Triple<List<ItemStack>, Integer, Float>>>(
                        item,
                        (player, stack) -> new ImmutableTriple<>(Collections.singletonList(stack), food.getNutrition(), food.getSaturationModifier())
                )
        );
    }

    private void setup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            Naturalist.registerBrewingRecipes();
            Naturalist.registerCompostables();
            Naturalist.registerSpawnPlacements();
            Naturalist.registerDispenserBehaviors();
        });
    }

    private void createAttributes(@NotNull EntityAttributeCreationEvent event) {
        NaturalistEntityAttributes.registerForge(event);
    }

}
//?}

