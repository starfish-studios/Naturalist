package com.starfish_studios.naturalist.common.block;

/*public class OstrichEggBlock extends TurtleEggBlock {

    private static final VoxelShape EGG_AABB = Block.box(5.0, 0.0, 5.0, 11.0, 8.0, 11.0);
    public OstrichEggBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (this.shouldUpdateHatchLevel(level)) {
            int i = state.getValue(HATCH);
            if (i < 2) {
                level.playSound(null, pos, NaturalistSoundEvents.OSTRICH_EGG_CRACK.get(), SoundSource.BLOCKS, 0.7f, 0.9f + random.nextFloat() * 0.2f);
                level.setBlock(pos, state.setValue(HATCH, i + 1), 2);
            } else {
                level.playSound(null, pos, NaturalistSoundEvents.OSTRICH_EGG_HATCH.get(), SoundSource.BLOCKS, 0.7f, 0.9f + random.nextFloat() * 0.2f);
                level.removeBlock(pos, false);
                for (int j = 0; j < state.getValue(EGGS); ++j) {
                    level.levelEvent(2001, pos, Block.getId(state));
                    Ostrich ostrich = NaturalistEntityTypes.OSTRICH.get().create(level);
                    ostrich.setAge(-24000);
                    ostrich.moveTo((double)pos.getX() + 0.3 + (double)j * 0.2, pos.getY(), (double)pos.getZ() + 0.3, 0.0f, 0.0f);
                    level.addFreshEntity(ostrich);
                }
            }
        }
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!level.isClientSide) {
            level.levelEvent(2005, pos, 0);
        }
    }

    private boolean shouldUpdateHatchLevel(Level level) {
        return level.random.nextInt(500) == 0;
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        if (!entity.isSteppingCarefully()) {
            this.destroyEgg(level, state, pos, entity, 100);
        }
        super.stepOn(level, pos, state, entity);
    }

    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        if (!(entity instanceof Zombie)) {
            this.destroyEgg(level, state, pos, entity, 3);
        }
        super.fallOn(level, state, pos, entity, fallDistance);
    }

    private void destroyEgg(Level level, BlockState state, BlockPos pos, Entity entity, int chance) {
        if (!this.canDestroyEgg(level, entity)) {
            return;
        }
        if (!level.isClientSide && level.random.nextInt(chance) == 0 && state.is(Blocks.TURTLE_EGG)) {
            this.decreaseEggs(level, pos, state);
        }
    }

    private boolean canDestroyEgg(Level level, Entity entity) {
        if (entity instanceof Ostrich) {
            return false;
        }
        if (entity instanceof LivingEntity) {
            return entity instanceof Player || level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
        }
        return false;
    }

    private void decreaseEggs(Level level, BlockPos pos, BlockState state) {
        level.playSound(null, pos, NaturalistSoundEvents.OSTRICH_EGG_BREAK.get(), SoundSource.BLOCKS, 0.7f, 0.9f + level.random.nextFloat() * 0.2f);
        int i = state.getValue(EGGS);
        if (i <= 1) {
            level.destroyBlock(pos, false);
        } else {
            level.setBlock(pos, state.setValue(EGGS, i - 1), 2);
            level.gameEvent(GameEvent.BLOCK_DESTROY, pos, GameEvent.Context.of(state));
            level.levelEvent(2001, pos, Block.getId(state));
        }
    }

    @Override
    public boolean canBeReplaced(BlockState state, BlockPlaceContext useContext) {
        return false;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return EGG_AABB;
    }
}
 */
