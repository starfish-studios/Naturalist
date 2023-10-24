package com.starfish_studios.naturalist.common.item.fabric;

import com.starfish_studios.naturalist.common.entity.Butterfly;
import com.starfish_studios.naturalist.common.entity.Snail;
import com.starfish_studios.naturalist.core.registry.NaturalistEntityTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;

import java.util.List;
import java.util.function.Supplier;

public class NoFluidMobBucketWithVariantsItem extends NoFluidMobBucketItem {
    private final int color;
    private final EntityType<?> type;

    public NoFluidMobBucketWithVariantsItem(Supplier<? extends EntityType<?>> entitySupplier, Fluid fluid, SoundEvent emptyingSound, int color, Properties settings) {
        super(entitySupplier.get(), fluid, emptyingSound, settings);
        this.color = color;
        this.type = entitySupplier.get();
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flagIn) {
        if (this.type == NaturalistEntityTypes.SNAIL.get()) {
            CompoundTag compoundnbt = stack.getTag();
            if (compoundnbt != null && compoundnbt.contains("Color", 3)) {
                Snail.Color color = Snail.Color.getTypeById(compoundnbt.getInt("Color"));
                tooltip.add((Component.translatable(String.format("tooltip.naturalist.%s", color.toString().toLowerCase())).withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY)));
            }
        }
    }

    /* public void fillItemCategory(CreativeModeTab category, NonNullList<ItemStack> items) {
        for (int i = 0; i < color; i++) {
            ItemStack colorStack = new ItemStack(this);
            CompoundTag compoundTag = new CompoundTag();
            compoundTag.putInt("Color", i);
            colorStack.setTag(compoundTag);
            items.add(colorStack);
        }
    }

     */
}
