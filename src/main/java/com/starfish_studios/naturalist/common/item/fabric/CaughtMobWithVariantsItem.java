package com.starfish_studios.naturalist.common.item.fabric;

import net.minecraft.core.*;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.*;
import net.minecraft.sounds.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.material.*;
import org.jetbrains.annotations.NotNull;

//TODO: give this a better, shorter name
public class CaughtMobWithVariantsItem extends CaughtMobItem {
    private final int variantCount;

    public CaughtMobWithVariantsItem(@NotNull EntityType<?> entitySupplier, @NotNull Fluid fluid, SoundEvent emptyingSound, int variantCount, @NotNull Properties settings) {
        super(entitySupplier, fluid, emptyingSound, settings);
        this.variantCount = variantCount;
    }

    public void fillItemCategory(CreativeModeTab category, NonNullList<ItemStack> items) {
        for (int i = 0; i < variantCount; i++) {
            ItemStack variantStack = new ItemStack(this);
            CompoundTag compoundTag = new CompoundTag();
            compoundTag.putInt("Variant", i);
            variantStack.set(DataComponents.CUSTOM_DATA, CustomData.of(compoundTag));
            items.add(variantStack);
        }
    }
}
