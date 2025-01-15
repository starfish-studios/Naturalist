package com.starfish_studios.naturalist.common.item;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.material.Fluid;

//TODO: give this a better, shorter name
public class CaughtMobWithVariantsItem extends CaughtMobItem {
    private final int variantCount;

    public CaughtMobWithVariantsItem(EntityType<? extends Mob> entitySupplier, Fluid fluid, SoundEvent emptyingSound, int variantCount, Properties settings) {
        super(entitySupplier, fluid, emptyingSound, settings);
        this.variantCount = variantCount;
    }

    // TODO: Unused right now, 1.21.4??
    /*public void fillItemCategory(CreativeModeTab category, NonNullList<ItemStack> items) {
        for (int i = 0; i < variantCount; i++) {
            ItemStack variantStack = new ItemStack(this);
            CompoundTag compoundTag = new CompoundTag();
            compoundTag.putInt("Variant", i);
            variantStack.setTag(compoundTag);
            items.add(variantStack);
        }
    }*/
}
