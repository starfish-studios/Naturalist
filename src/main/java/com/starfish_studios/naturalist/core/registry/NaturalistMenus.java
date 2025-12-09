package com.starfish_studios.naturalist.core.registry;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class NaturalistMenus {
    @FunctionalInterface
    public interface MenuFactory<T extends AbstractContainerMenu> {
        T create(int syncId, Inventory inventory, FriendlyByteBuf byteBuf);
    }
}