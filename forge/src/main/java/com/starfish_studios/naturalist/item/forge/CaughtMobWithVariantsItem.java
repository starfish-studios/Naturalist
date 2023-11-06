package com.starfish_studios.naturalist.item.forge;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;

import java.util.function.Supplier;

//TODO: give this a better, shorter name
public class CaughtMobWithVariantsItem extends CaughtMobItem {
    private final int variantCount;

    public CaughtMobWithVariantsItem(Supplier<? extends EntityType<?>> entitySupplier, Supplier<? extends Fluid> fluidSupplier, Supplier<? extends SoundEvent> soundSupplier, int variantCount, Properties properties) {
        super(entitySupplier, fluidSupplier, soundSupplier, properties);
        this.variantCount = variantCount;
    }


    @Override
    public void fillItemCategory(CreativeModeTab category, NonNullList<ItemStack> items) {
        for (int i = 0; i < variantCount; i++) {
            ItemStack variantStack = new ItemStack(this);
            CompoundTag compoundTag = new CompoundTag();
            compoundTag.putInt("Variant", i);
            variantStack.setTag(compoundTag);
            items.add(variantStack);
        }
    }
}
