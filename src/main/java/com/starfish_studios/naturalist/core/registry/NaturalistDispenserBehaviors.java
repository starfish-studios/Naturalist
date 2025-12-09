package com.starfish_studios.naturalist.core.registry;

import com.starfish_studios.naturalist.common.entity.Butterfly;
import com.starfish_studios.naturalist.common.entity.Snail;
import com.starfish_studios.naturalist.common.entity.core.projectile.ThrownDuckEgg;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.AbstractProjectileDispenseBehavior;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.DispensibleContainerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import org.jetbrains.annotations.NotNull;

public class NaturalistDispenserBehaviors {

    @SuppressWarnings("deprecation")
    public static void register() {
        DispenserBlock.registerBehavior(NaturalistItems.DUCK_EGG.get(), new AbstractProjectileDispenseBehavior() {
            @Override
            protected @NotNull Projectile getProjectile(@NotNull Level level, @NotNull Position position, @NotNull ItemStack stack) {
                return Util.make(new ThrownDuckEgg(level, position.x(), position.y(), position.z()), thrownDuckEgg -> thrownDuckEgg.setItem(stack));
            }
        });

        DispenseItemBehavior dispenseItemBehavior = new DefaultDispenseItemBehavior() {
            private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();

            @Override
            public @NotNull ItemStack execute(@NotNull BlockSource source, ItemStack stack) {
                DispensibleContainerItem dispensibleContainerItem = (DispensibleContainerItem) stack.getItem();
                BlockPos blockPos = source.getPos().relative(source.getBlockState().getValue(DispenserBlock.FACING));
                Level level = source.getLevel();
                if (dispensibleContainerItem.emptyContents(null, level, blockPos, null)) {
                    dispensibleContainerItem.checkExtraContent(null, level, stack, blockPos);
                    return new ItemStack(Items.BUCKET);
                }
                return this.defaultDispenseItemBehavior.dispense(source, stack);
            }
        };

        DispenserBlock.registerBehavior(NaturalistItems.SNAIL_BUCKET.get(), dispenseItemBehavior);
        DispenserBlock.registerBehavior(NaturalistItems.BASS_BUCKET.get(), dispenseItemBehavior);
        DispenserBlock.registerBehavior(NaturalistItems.CATFISH_BUCKET.get(), dispenseItemBehavior);

        DispenserBlock.registerBehavior(NaturalistItems.SNAIL_BUCKET.get(), new DefaultDispenseItemBehavior() {
            @Override
            public @NotNull ItemStack execute(@NotNull BlockSource source, @NotNull ItemStack stack) {
                Direction direction = source.getBlockState().getValue(DispenserBlock.FACING);
                BlockPos blockPos = source.getPos().relative(direction);
                ServerLevel serverLevel = source.getLevel();
                Snail snail = NaturalistEntityTypes.SNAIL.get().spawn(serverLevel, stack, null, blockPos, MobSpawnType.DISPENSER, true, false);
                if (snail != null) {
                    snail.setSnailColor(Snail.Color.getTypeById(stack.getOrCreateTag().getInt("Color")));
                    stack.shrink(1);
                    return new ItemStack(Items.BUCKET);
                }
                return stack;
            }
        });

        DispenserBlock.registerBehavior(NaturalistItems.BUTTERFLY.get(), new DefaultDispenseItemBehavior() {
            @Override
            public @NotNull ItemStack execute(@NotNull BlockSource source, @NotNull ItemStack stack) {
                Direction direction = source.getBlockState().getValue(DispenserBlock.FACING);
                BlockPos blockPos = source.getPos().relative(direction);
                ServerLevel serverLevel = source.getLevel();
                Butterfly butterfly = NaturalistEntityTypes.BUTTERFLY.get().spawn(serverLevel, stack, null, blockPos, MobSpawnType.DISPENSER, true, false);
                if (butterfly != null) {
                    butterfly.setVariant(Butterfly.Variant.getTypeById(stack.getOrCreateTag().getInt("Variant")));
                    stack.shrink(1);
                }
                return stack;
            }
        });
    }
}

