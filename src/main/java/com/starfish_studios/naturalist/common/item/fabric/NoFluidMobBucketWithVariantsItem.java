package com.starfish_studios.naturalist.common.item.fabric;

import com.starfish_studios.naturalist.common.entity.Snail;
import com.starfish_studios.naturalist.core.registry.NaturalistEntityTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;

public class NoFluidMobBucketWithVariantsItem extends NoFluidMobBucketItem {
    private final int color;
    private final EntityType<?> type;

    public NoFluidMobBucketWithVariantsItem(Supplier<? extends EntityType<?>> entitySupplier, Fluid fluid, @NotNull SoundEvent emptyingSound, int color, Properties settings) {
        super(entitySupplier.get(), fluid, emptyingSound, settings);
        this.color = color;
        this.type = entitySupplier.get();
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flagIn) {
        if (this.type == NaturalistEntityTypes.SNAIL.get()) {
            CustomData customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
            CompoundTag compoundnbt = customData.copyTag();
            if (compoundnbt.contains("Color", 3)) {
                Snail.Color color = Snail.Color.getTypeById(compoundnbt.getInt("Color"));
                tooltip.add((Component.translatable(String.format("item.minecraft.firework_star.%s", color.toString().toLowerCase())).withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY)));
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
