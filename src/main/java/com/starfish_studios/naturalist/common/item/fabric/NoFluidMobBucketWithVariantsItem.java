package com.starfish_studios.naturalist.common.item.fabric;

import com.starfish_studios.naturalist.common.entity.Snail;
import com.starfish_studios.naturalist.core.registry.NaturalistEntityTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;

public class NoFluidMobBucketWithVariantsItem extends NoFluidMobBucketItem {
    private final EntityType<?> type;

    @SuppressWarnings("unused")
    public NoFluidMobBucketWithVariantsItem(Supplier<? extends EntityType<?>> entitySupplier, Fluid fluid, @NotNull SoundEvent emptyingSound, int color, Properties settings) {
        super(entitySupplier.get(), fluid, emptyingSound, settings);
        this.type = entitySupplier.get();
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag lore) {
        if (this.type == NaturalistEntityTypes.SNAIL.get()) {
            CompoundTag component = stack.getTag();
            if (component != null && component.contains("Color", 3)) {
                Snail.Color color = Snail.Color.getTypeById(component.getInt("Color"));
                tooltip.add((Component.translatable(String.format("item.minecraft.firework_star.%s", color.toString().toLowerCase())).withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY)));
            }
        }
    }
}
