package com.starfish_studios.naturalist.common.item.fabric;

import com.starfish_studios.naturalist.common.entity.Snail;
import com.starfish_studios.naturalist.core.registry.NaturalistEntityTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class NoFluidMobBucketWithVariantsItem extends NoFluidMobBucketItem {
    private final EntityType<?> type;

    @SuppressWarnings("unused")
    public NoFluidMobBucketWithVariantsItem(
            Supplier<? extends EntityType<? extends net.minecraft.world.entity.Mob>> entitySupplier, Fluid fluid,
            @NotNull SoundEvent emptyingSound, int color, Properties settings) {
        super(entitySupplier.get(), fluid, emptyingSound, settings);
        this.type = entitySupplier.get();
    }

    // 1.21.10 API: appendHoverText signature uses Consumer<Component> instead of
    // List<Component>
    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context,
            @NotNull TooltipDisplay display, @NotNull Consumer<Component> tooltip,
            @NotNull TooltipFlag lore) {
        super.appendHoverText(stack, context, display, tooltip, lore);
        if (this.type == NaturalistEntityTypes.SNAIL.get()) {
            // In 1.21, use CustomData component instead of getTag()
            CustomData customData = stack.get(DataComponents.BUCKET_ENTITY_DATA);
            if (customData != null) {
                CompoundTag tag = customData.copyTag();
                if (tag.contains("Color")) {
                    // CompoundTag methods return Optional in 1.21
                    int colorId = tag.getInt("Color").orElse(0);
                    Snail.Color color = Snail.Color.getTypeById(colorId);
                    tooltip.accept(Component
                            .translatable(
                                    String.format("item.minecraft.firework_star.%s", color.toString().toLowerCase()))
                            .withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));
                }
            }
        }
    }
}
