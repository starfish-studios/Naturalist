package com.starfish_studios.naturalist.common.entity;

import com.starfish_studios.naturalist.common.entity.core.NaturalistAnimal;
import com.starfish_studios.naturalist.common.entity.core.ai.goal.FlyingWanderGoal;
import com.starfish_studios.naturalist.core.registry.NaturalistSoundEvents;
import com.starfish_studios.naturalist.core.registry.NaturalistTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.starfish_studios.naturalist.common.entity.core.NaturalistGeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class Firefly extends NaturalistAnimal implements FlyingAnimal, NaturalistGeoEntity {
    private static final EntityDataAccessor<Integer> GLOW_TICKS_REMAINING = SynchedEntityData.defineId(Firefly.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> SUN_TICKS = SynchedEntityData.defineId(Firefly.class, EntityDataSerializers.INT);
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    protected static final RawAnimation FLY = RawAnimation.begin().thenLoop("animation.sf_nba.firefly.fly");

    @Override
    @NotNull
    public MobType getMobType() {
        return MobType.ARTHROPOD;
    }

    public Firefly(EntityType<? extends NaturalistAnimal> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new FlyingMoveControl(this, 20, true);
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.WATER, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.WATER_BORDER, 16.0F);
        this.setPathfindingMalus(BlockPathTypes.COCOA, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.FENCE, -1.0F);
    }



    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
        FlyingPathNavigation navigation = new FlyingPathNavigation(this, level) {
            public boolean isStableDestination(BlockPos pos) {
                return !level().getBlockState(pos.below()).isAir();
            }
        };
        navigation.setCanOpenDoors(false);
        navigation.setCanFloat(false);
        navigation.setCanPassDoors(true);
        return navigation;
    }

    @Override
    protected float getStandingEyeHeight(@NotNull Pose pose, @NotNull EntityDimensions size) {
        return size.height * 0.5F;
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float pMultiplier, @NotNull DamageSource source) {
        return false;
    }

    @Override
    protected void checkFallDamage(double pY, boolean onGround, @NotNull BlockState state, @NotNull BlockPos pos) {
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new FireflyHideInGrassGoal(this, 1.2F, 10, 4));
        this.goalSelector.addGoal(2, new FlyingWanderGoal(this));
        this.goalSelector.addGoal(3, new FloatGoal(this));
    }

    public static AttributeSupplier.@NotNull Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 6.0D).add(Attributes.FLYING_SPEED, 0.6F).add(Attributes.MOVEMENT_SPEED, 0.3F);
    }

    @SuppressWarnings("unused")
    public static boolean checkFireflySpawnRules(EntityType<? extends Firefly> type, ServerLevelAccessor level, MobSpawnType reason, BlockPos pos, RandomSource random) {
        return Monster.isDarkEnoughToSpawn(level, pos, random) && level.getBlockState(pos.below()).is(NaturalistTags.BlockTags.FIREFLIES_SPAWNABLE_ON);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob ageable) {
        return null;
    }

    @Override
    public boolean isFood(@NotNull ItemStack stack) {
        return false;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(GLOW_TICKS_REMAINING, 0);
        this.entityData.define(SUN_TICKS, 0);
    }

    public boolean isGlowing() {
        return this.entityData.get(GLOW_TICKS_REMAINING) > 0;
    }

    public int getGlowTicksRemaining() {
        return this.entityData.get(GLOW_TICKS_REMAINING);
    }

    private void setGlowTicks(int ticks) {
        this.entityData.set(GLOW_TICKS_REMAINING, ticks);
    }

    public int getSunTicks() {
        return this.entityData.get(SUN_TICKS);
    }

    private void setSunTicks(int ticks) {
        this.entityData.set(SUN_TICKS, ticks);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        int ticks = this.getGlowTicksRemaining();
        if (ticks > 0) {
            this.setGlowTicks(ticks - 1);
        }
        if (this.canGlow()) {
            if (this.random.nextFloat() <= 0.01 && !this.isGlowing()) {
                this.setGlowTicks(40 + this.random.nextInt(20));
            }
        }
        if (this.isSunBurnTick()) {
            this.setSunTicks(this.getSunTicks() + 1);
            if (this.getSunTicks() > 600) {
                BlockPos pos = this.blockPosition();
                if (!level().isClientSide) {
                    for(int i = 0; i < 20; ++i) {
                        double x = random.nextGaussian() * 0.02D;
                        double y = random.nextGaussian() * 0.02D;
                        double z = random.nextGaussian() * 0.02D;
                        ((ServerLevel)level()).sendParticles(ParticleTypes.POOF, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, 1, x, y, z, 0.15F);
                    }
                }
                level().playSound(null, this.blockPosition(), NaturalistSoundEvents.FIREFLY_HIDE.get(), SoundSource.NEUTRAL, 0.7F, 0.9F + level().random.nextFloat() * 0.2F);
                this.discard();
            }
        }
    }

    private boolean canGlow() {
        if (!this.level().isClientSide) {
            return this.level().isNight() || this.level().getMaxLocalRawBrightness(this.blockPosition()) < 8;
        }
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    protected boolean isSunBurnTick() {
        if (this.level().isDay() && !this.hasCustomName() && !this.level().isClientSide) {
            return this.getLightLevelDependentMagicValue() > 0.5F;
        }

        return false;
    }

    @Override
    public boolean isFlapping() {
        return this.isFlying() && this.tickCount % Mth.ceil(1.4959966F) == 0;
    }

    @Override
    public boolean isFlying() {
        return true;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return !this.hasCustomName();
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return NaturalistSoundEvents.FIREFLY_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return NaturalistSoundEvents.FIREFLY_DEATH.get();
    }

    @Override
    public double getBoneResetTime() {
        return 2;
    }

    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }

    private <E extends Firefly> PlayState predicate(final AnimationState<E> event) {
        event.getController().setAnimation(FLY);
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(final AnimatableManager.@NotNull ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 2, this::predicate));
    }

    static class FireflyHideInGrassGoal extends MoveToBlockGoal {
        private final Firefly firefly;

        public FireflyHideInGrassGoal(Firefly mob, double speedModifier, int pSearchRange, int pVerticalSearchRange) {
            super(mob, speedModifier, pSearchRange, pVerticalSearchRange);
            this.firefly = mob;
        }

        @Override
        public boolean canUse() {
            return firefly.isSunBurnTick() && super.canUse();
        }

        @Override
        protected boolean isValidTarget(LevelReader level, @NotNull BlockPos pos) {
            return level.getBlockState(pos).is(Blocks.GRASS) || level.getBlockState(pos).is(Blocks.FERN) || level.getBlockState(pos).is(Blocks.TALL_GRASS);
        }

        @Override
        public void tick() {
            super.tick();
            Level level = firefly.level();
            if (this.isReachedTarget()) {
                if (!level.isClientSide) {
                    ((ServerLevel)level).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.GRASS.defaultBlockState()), firefly.getX(), firefly.getY(), firefly.getZ(), 50, firefly.getBbWidth() / 4.0F, firefly.getBbHeight() / 4.0F, firefly.getBbWidth() / 4.0F, 0.05D);
                }
                level.playSound(null, firefly.blockPosition(), NaturalistSoundEvents.FIREFLY_HIDE.get(), SoundSource.NEUTRAL, 0.7F, 0.9F + level.random.nextFloat() * 0.2F);
                firefly.discard();
            }
        }

        @Override
        protected @NotNull BlockPos getMoveToTarget() {
            return this.blockPos;
        }
    }
}
