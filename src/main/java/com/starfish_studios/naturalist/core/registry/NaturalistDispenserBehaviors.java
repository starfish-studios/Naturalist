package com.starfish_studios.naturalist.core.registry;

import com.starfish_studios.naturalist.common.entity.Butterfly;
import com.starfish_studios.naturalist.common.entity.Snail;
import com.starfish_studios.naturalist.common.entity.core.projectile.ThrownDuckEgg;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.item.DispensibleContainerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import org.jetbrains.annotations.NotNull;

public class NaturalistDispenserBehaviors {

    @SuppressWarnings("deprecation")
    public static void register() {
        // Register duck egg dispenser behavior
        DispenserBlock.registerBehavior(NaturalistItems.DUCK_EGG.get(),
                new DefaultDispenseItemBehavior() {
                    @Override
                    public @NotNull ItemStack execute(@NotNull BlockSource source, @NotNull ItemStack stack) {
                        Direction direction = source.state().getValue(DispenserBlock.FACING);
                        Position pos = DispenserBlock.getDispensePosition(source);
                        ServerLevel serverLevel = source.level();
                        ThrownDuckEgg projectile = new ThrownDuckEgg(serverLevel, pos.x(), pos.y(), pos.z());
                        projectile.setItem(stack);
                        projectile.shoot(direction.getStepX(), direction.getStepY() + 0.1F, direction.getStepZ(), 1.5F,
                                6.0F);
                        serverLevel.addFreshEntity(projectile);
                        stack.shrink(1);
                        return stack;
                    }
                });

        // Register bucket dispense behavior
        DispenseItemBehavior dispenseItemBehavior = new DefaultDispenseItemBehavior() {
            private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();

            @Override
            public @NotNull ItemStack execute(@NotNull BlockSource source, ItemStack stack) {
                DispensibleContainerItem dispensibleContainerItem = (DispensibleContainerItem) stack.getItem();
                BlockPos blockPos = source.pos().relative(source.state().getValue(DispenserBlock.FACING));
                Level level = source.level();
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
                Direction direction = source.state().getValue(DispenserBlock.FACING);
                BlockPos blockPos = source.pos().relative(direction);
                ServerLevel serverLevel = source.level();
                Snail snail = NaturalistEntityTypes.SNAIL.get().spawn(serverLevel, stack, null, blockPos,
                        EntitySpawnReason.DISPENSER, true, false);
                if (snail != null) {
                    // TODO: Update to use DataComponents instead of getOrCreateTag in 1.21
                    // snail.setSnailColor(Snail.Color.getTypeById(stack.getOrCreateTag().getInt("Color")));
                    stack.shrink(1);
                    return new ItemStack(Items.BUCKET);
                }
                return stack;
            }
        });

        DispenserBlock.registerBehavior(NaturalistItems.BUTTERFLY.get(), new DefaultDispenseItemBehavior() {
            @Override
            public @NotNull ItemStack execute(@NotNull BlockSource source, @NotNull ItemStack stack) {
                Direction direction = source.state().getValue(DispenserBlock.FACING);
                BlockPos blockPos = source.pos().relative(direction);
                ServerLevel serverLevel = source.level();
                Butterfly butterfly = NaturalistEntityTypes.BUTTERFLY.get().spawn(serverLevel, stack, null, blockPos,
                        EntitySpawnReason.DISPENSER, true, false);
                if (butterfly != null) {
                    // TODO: Update to use DataComponents instead of getOrCreateTag in 1.21
                    // butterfly.setVariant(Butterfly.Variant.getTypeById(stack.getOrCreateTag().getInt("Variant")));
                    stack.shrink(1);
                }
                return stack;
            }
        });
    }
}
