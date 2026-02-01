package com.starfish_studios.naturalist.common.item;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import java.util.function.Supplier;

public class NaturalistSpawnEggItem extends SpawnEggItem {
    private final Supplier<EntityType<? extends Mob>> entityTypeSupplier;
    private final int backgroundColor;
    private final int highlightColor;

    public NaturalistSpawnEggItem(Supplier<EntityType<? extends Mob>> entityTypeSupplier, int backgroundColor,
            int highlightColor, Item.Properties properties) {
        super(properties);
        this.entityTypeSupplier = entityTypeSupplier;
        this.backgroundColor = backgroundColor;
        this.highlightColor = highlightColor;
    }

    public NaturalistSpawnEggItem(Supplier<EntityType<? extends Mob>> entityTypeSupplier, Item.Properties properties) {
        this(entityTypeSupplier, 0, 0, properties);
    }

    @Override
    public EntityType<?> getType(ItemStack stack) {
        return entityTypeSupplier.get();
    }

    public int getColor(int tintIndex) {
        return tintIndex == 0 ? backgroundColor : highlightColor;
    }
}
