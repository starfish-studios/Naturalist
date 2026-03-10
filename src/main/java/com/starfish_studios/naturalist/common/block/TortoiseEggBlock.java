package com.starfish_studios.naturalist.common.block;

import com.starfish_studios.naturalist.common.entity.Tortoise;
import com.starfish_studios.naturalist.core.registry.NaturalistEntityTypes;
import com.starfish_studios.naturalist.core.registry.NaturalistSoundEvents;
import com.starfish_studios.naturalist.core.registry.NaturalistTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TurtleEggBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TortoiseEggBlock extends TurtleEggBlock {

    public static final IntegerProperty VARIANT = IntegerProperty.create("variant", 0, 2);

    public TortoiseEggBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(HATCH, 0)
                .setValue(EGGS, 1)
                .setValue(VARIANT, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block,BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(VARIANT);
    }

    @Override
    public @NotNull ItemStack getCloneItemStack(@NotNull net.minecraft.world.level.LevelReader level, @NotNull BlockPos pos, @NotNull BlockState state) {
        ItemStack stack = super.getCloneItemStack(level, pos, state);
        CompoundTag tag = new CompoundTag();
        tag.putInt("Variant", state.getValue(VARIANT));
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        return stack;
    }

    @Override
    public void randomTick(@NotNull BlockState state, @NotNull ServerLevel level, BlockPos pos, @NotNull RandomSource random) {
        if (!this.shouldUpdateHatchLevel(level)) { return; }
        int hatchStage = state.getValue(HATCH);
        if (hatchStage < 2) {
            level.playSound(null, pos, NaturalistSoundEvents.TORTOISE_EGG_CRACK.get(), SoundSource.BLOCKS, 0.7f, 0.9f + random.nextFloat() * 0.2f);
            level.setBlock(pos, state.setValue(HATCH, hatchStage + 1), 2);
        } else {
            level.playSound(null, pos, NaturalistSoundEvents.TORTOISE_EGG_HATCH.get(), SoundSource.BLOCKS, 0.7f, 0.9f + random.nextFloat() * 0.2f);

            int eggCount = state.getValue(EGGS);
            int variant  = state.getValue(VARIANT);

            level.removeBlock(pos, false);

            for (int i = 0; i < eggCount; i++) {
                level.levelEvent(2001, pos, Block.getId(state));

                Tortoise baby = NaturalistEntityTypes.TORTOISE.get().create(level);
                if (baby != null) {
                    baby.setVariant(variant);
                    baby.setAge(-24000);
                    double x = pos.getX() + 0.3 + i * 0.2;
                    double y = pos.getY();
                    double z = pos.getZ() + 0.3;
                    baby.moveTo(x, y, z, 0.0F, 0.0F);
                    level.addFreshEntity(baby);
                }
            }
        }
    }


    @Override
    public void onPlace(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull BlockState oldState, boolean isMoving) {
        if (!level.isClientSide) {
            level.levelEvent(2005, pos, 0);
        }
    }

    private boolean shouldUpdateHatchLevel(@NotNull Level level) {
        return level.random.nextInt(500) == 0;
    }

    @Override
    public void stepOn(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull Entity entity) {
        if (!entity.isSteppingCarefully()) {
            this.destroyEgg(level, state, pos, entity, 100);
        }
        super.stepOn(level, pos, state, entity);
    }

    @Override
    public void fallOn(@NotNull Level level, @NotNull BlockState state, @NotNull BlockPos pos, @NotNull Entity entity, float fallDistance) {
        if (!(entity instanceof Zombie)) {
            this.destroyEgg(level, state, pos, entity, 3);
        }
        super.fallOn(level, state, pos, entity, fallDistance);
    }

    private void destroyEgg(@NotNull Level level, BlockState state, BlockPos pos, Entity entity, int chance) {
        if (!this.canDestroyEgg(level, entity)) {
            return;
        }
        if (!level.isClientSide && level.random.nextInt(chance) == 0 && state.is(Blocks.TURTLE_EGG)) {
            this.decreaseEggs(level, pos, state);
        }
    }

    // Interaction for testing hatch variants
    @Override
    public void playerDestroy(@NotNull Level level, @NotNull Player player, @NotNull BlockPos pos, @NotNull BlockState state, @Nullable BlockEntity blockEntity, @NotNull ItemStack stack) {
        super.playerDestroy(level, player, pos, state, blockEntity, stack);

        if (!level.isClientSide && stack.getItem() == Items.COMMAND_BLOCK) {
            int variant = state.getValue(VARIANT);
            int eggCount = state.getValue(EGGS);

            level.playSound(null, pos, NaturalistSoundEvents.TORTOISE_EGG_HATCH.get(),
                    SoundSource.BLOCKS, 0.7f, 0.9f + level.random.nextFloat() * 0.2f);
            level.levelEvent(2001, pos, Block.getId(state));

            level.removeBlock(pos, false);

            for (int i = 0; i < eggCount; i++) {
                Tortoise baby = NaturalistEntityTypes.TORTOISE.get().create(level);
                if (baby != null) {
                    baby.setVariant(variant);
                    baby.setAge(-24000);
                    double dx = pos.getX() + 0.3 + i * 0.2;
                    double dy = pos.getY();
                    double dz = pos.getZ() + 0.3;
                    baby.moveTo(dx, dy, dz, 0F, 0F);
                    level.addFreshEntity(baby);
                }
            }
        }
    }

    private boolean canDestroyEgg(@NotNull Level level, Entity entity) {
        if (!(entity instanceof Tortoise) && !(entity.getType().is(NaturalistTags.EntityTypes.SAFE_EGG_WALKERS))) {
            if (!(entity instanceof LivingEntity)) {
                return false;
            } else {
                return entity instanceof Player || level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
            }
        } else {
            return false;
        }
    }

    private void decreaseEggs(Level level, BlockPos pos, BlockState state) {
        level.playSound(null, pos, NaturalistSoundEvents.TORTOISE_EGG_BREAK.get(), SoundSource.BLOCKS, 0.7f, 0.9f + level.random.nextFloat() * 0.2f);
        int i = state.getValue(EGGS);
        if (i <= 1) {
            level.destroyBlock(pos, false);
        } else {
            level.setBlock(pos, state.setValue(EGGS, i - 1), 2);
            level.gameEvent(GameEvent.BLOCK_DESTROY, pos, GameEvent.Context.of(state));
            level.levelEvent(2001, pos, Block.getId(state));
        }
    }
}
