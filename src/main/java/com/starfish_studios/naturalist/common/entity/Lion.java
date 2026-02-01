package com.starfish_studios.naturalist.common.entity;

import com.starfish_studios.naturalist.NaturalistConfig;
import com.starfish_studios.naturalist.Naturalist;
import com.mojang.serialization.Codec;

import com.starfish_studios.naturalist.common.entity.core.NaturalistAnimal;
import com.starfish_studios.naturalist.common.entity.core.SleepingAnimal;
import com.starfish_studios.naturalist.common.entity.core.ai.goal.BabyHurtByTargetGoal;
import com.starfish_studios.naturalist.common.entity.core.ai.goal.BabyPanicGoal;
import com.starfish_studios.naturalist.common.entity.core.ai.goal.SleepGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import com.starfish_studios.naturalist.core.registry.NaturalistEntityTypes;
import com.starfish_studios.naturalist.core.registry.NaturalistSoundEvents;
import com.starfish_studios.naturalist.core.registry.NaturalistTags;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.PathType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.starfish_studios.naturalist.common.entity.core.NaturalistGeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.animatable.processing.AnimationController;
import software.bernie.geckolib.animatable.processing.AnimationTest;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;
import net.minecraft.resources.ResourceLocation;

import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class Lion extends NaturalistAnimal implements NaturalistGeoEntity, SleepingAnimal {
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
    private static final EntityDataAccessor<Boolean> SLEEPING = SynchedEntityData.defineId(Lion.class,
            EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> HAS_MANE = SynchedEntityData.defineId(Lion.class,
            EntityDataSerializers.BOOLEAN);

    protected static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation.sf_nba.lion.idle");
    protected static final RawAnimation WALK = RawAnimation.begin().thenLoop("animation.sf_nba.lion.walk");
    protected static final RawAnimation RUN = RawAnimation.begin().thenLoop("animation.sf_nba.lion.run");
    protected static final RawAnimation PREY = RawAnimation.begin().thenLoop("animation.sf_nba.lion.prey");
    protected static final RawAnimation SLEEP = RawAnimation.begin().thenLoop("animation.sf_nba.lion.sleep");
    protected static final RawAnimation SLEEP2 = RawAnimation.begin().thenLoop("animation.sf_nba.lion.sleep2");

    public Lion(@NotNull EntityType<? extends NaturalistAnimal> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
        return new GroundPathNavigation(this, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 24.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.2F)
                .add(Attributes.ATTACK_DAMAGE, 6.0D)
                .add(Attributes.FOLLOW_RANGE, 32.0D);
    }

    @Override
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor level, @NotNull DifficultyInstance difficulty,
            @NotNull EntitySpawnReason reason,
            @Nullable SpawnGroupData spawnData) {
        spawnData = super.finalizeSpawn(level, difficulty, reason, spawnData);
        AgeableMobGroupData ageableMobGroupData;
        if (spawnData == null) {
            spawnData = new AgeableMobGroupData(true);
            this.setHasMane(this.getRandom().nextBoolean());
        }
        if (spawnData instanceof AgeableMobGroupData data && data.getGroupSize() > 2) {
            this.setAge(-24000);
        }
        if (spawnData instanceof AgeableMobGroupData data) {
            data.increaseGroupSizeByOne();
        }

        RandomSource random = level.getRandom();
        Objects.requireNonNull(this.getAttribute(Attributes.FOLLOW_RANGE))
                .addPermanentModifier(new AttributeModifier(
                        ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, "random_spawn_bonus"),
                        random.triangle(0.0, 0.11485000000000001), AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        return spawnData;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel serverLevel, @NotNull AgeableMob ageableMob) {
        return NaturalistEntityTypes.LION.get().create(serverLevel, EntitySpawnReason.BREEDING);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.6D, true));
        this.goalSelector.addGoal(2, new BabyPanicGoal(this, 2.0D));
        this.goalSelector.addGoal(3, new SleepGoal<>(this));
        this.goalSelector.addGoal(4, new LionFollowParentGoal(this, 1.1));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.6));
        this.goalSelector.addGoal(6, new LionFollowLeaderGoal(this, 1.1D, 8.0F, 24.0F));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0f));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new BabyHurtByTargetGoal(this));
        this.targetSelector.addGoal(2,
                new NearestAttackableTargetGoal<LivingEntity>(this, LivingEntity.class, 10, true, false,
                        (entity, level) -> entity.getType().is(NaturalistTags.EntityTypes.LION_HOSTILES)
                                && (NaturalistConfig.lionsAttackBabyHostiles || !entity.isBaby())
                                && !this.isSleeping() && !this.isBaby()));
    }

    @Override
    public boolean isFood(@NotNull ItemStack stack) {
        return false;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SLEEPING, false);
        builder.define(HAS_MANE, false);
    }

    @Override
    public void addAdditionalSaveData(@NotNull ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.store("Mane", Codec.BOOL, this.hasMane());
    }

    @Override
    public void readAdditionalSaveData(@NotNull ValueInput input) {
        super.readAdditionalSaveData(input);
        this.setHasMane(input.read("Mane", Codec.BOOL).orElse(false));
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.isSleeping() || this.isImmobile()) {
            this.jumping = false;
            this.xxa = 0.0F;
            this.zza = 0.0F;
        }
    }

    @Override
    protected void ageBoundaryReached() {
        super.ageBoundaryReached();
        this.setHasMane(this.getRandom().nextBoolean());
    }

    @Override
    public boolean canSleep() {
        long dayTime = this.level().getDayTime();
        if (this.getTarget() != null || this.level().isWaterAt(this.blockPosition())) {
            return false;
        } else {
            return dayTime > 6000 && dayTime < 13000;
        }
    }

    @Override
    public void customServerAiStep(ServerLevel serverLevel) {
        if (this.getMoveControl().hasWanted()) {
            double speedModifier = this.getMoveControl().getSpeedModifier();
            if (speedModifier >= 1.25D && this.onGround()) {
                this.setPose(Pose.STANDING);
                this.setSprinting(true);
            } else {
                this.setPose(Pose.STANDING);
                this.setSprinting(false);
            }
        } else {
            this.setPose(Pose.STANDING);
            this.setSprinting(false);
        }
    }

    @Override
    public boolean isSteppingCarefully() {
        return this.isCrouching() || super.isSteppingCarefully();
    }

    @Override
    public void setSleeping(boolean sleeping) {
        this.entityData.set(SLEEPING, sleeping);
    }

    @Override
    public boolean isSleeping() {
        return this.entityData.get(SLEEPING);
    }

    public void setHasMane(boolean hasMane) {
        this.entityData.set(HAS_MANE, hasMane);
    }

    public boolean hasMane() {
        return this.entityData.get(HAS_MANE);
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return NaturalistSoundEvents.LION_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return NaturalistSoundEvents.LION_AMBIENT.get();
    }

    @Override
    public int getAmbientSoundInterval() {
        return 900;
    }

    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }

    private <E extends software.bernie.geckolib.animatable.GeoAnimatable> PlayState predicate(
            final software.bernie.geckolib.animatable.processing.AnimationTest<E> state) {
        software.bernie.geckolib.animatable.processing.AnimationController<E> controller = state.controller();
        if (this.isSleeping() && this.hasMane()) {
            controller.setAnimation(SLEEP2);
        } else if (this.isSleeping() && !this.hasMane()) {
            controller.setAnimation(SLEEP);
        } else if (this.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6) {
            if (this.isSprinting()) {
                controller.setAnimation(RUN);
                controller.setAnimationSpeed(2.5F);
            } else if (this.isCrouching()) {
                controller.setAnimation(PREY);
                controller.setAnimationSpeed(0.8F);
            } else {
                controller.setAnimation(WALK);
                controller.setAnimationSpeed(1.0F);
            }
        } else {
            controller.setAnimation(IDLE);
            controller.setAnimationSpeed(1.0F);
        }
        return PlayState.CONTINUE;
    }

    private <E extends software.bernie.geckolib.animatable.GeoAnimatable> PlayState attackPredicate(
            software.bernie.geckolib.animatable.processing.AnimationTest<E> state) {
        software.bernie.geckolib.animatable.processing.AnimationController<E> controller = state.controller();
        if (this.swinging && controller.getAnimationState().equals(AnimationController.State.STOPPED)) {
            controller.forceAnimationReset();

            controller.setAnimation(RawAnimation.begin().thenPlay("attack"));
            this.swinging = false;
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(final AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<Lion>("controller", 5, this::predicate));
        controllers.add(new AnimationController<Lion>("attackController", 0, this::attackPredicate));
    }

    static class LionFollowLeaderGoal extends Goal {
        private final Lion mob;
        private final Predicate<Mob> followPredicate;
        @Nullable
        private Lion followingMob;
        private final double speedModifier;
        private final PathNavigation navigation;
        private int timeToRecalcPath;
        private final float stopDistance;
        private float oldWaterCost;
        private final float areaSize;

        public LionFollowLeaderGoal(Lion mob, double speedModifier, float stopDistance, float areaSize) {
            this.mob = mob;
            this.followPredicate = followingMob -> followingMob != null && !followingMob.isBaby();
            this.speedModifier = speedModifier;
            this.navigation = mob.getNavigation();
            this.stopDistance = stopDistance;
            this.areaSize = areaSize;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            if (this.mob.isBaby() || this.mob.hasMane()) {
                return false;
            }
            List<Lion> nearbyLions = this.mob.level().getEntitiesOfClass(Lion.class,
                    this.mob.getBoundingBox().inflate(this.areaSize), this.followPredicate);
            if (!nearbyLions.isEmpty()) {
                for (Lion lion : nearbyLions) {
                    if (!lion.hasMane())
                        continue;
                    if (lion.isInvisible())
                        continue;
                    this.followingMob = lion;
                    return true;
                }
                if (this.followingMob == null) {
                    for (Lion lion : nearbyLions) {
                        if (lion.isBaby())
                            continue;
                        if (lion.isInvisible())
                            continue;
                        this.followingMob = lion;
                        return true;
                    }
                }
            }
            return false;
        }

        @Override
        public boolean canContinueToUse() {
            return this.followingMob != null && !this.navigation.isDone()
                    && this.mob.distanceToSqr(this.followingMob) > (double) (this.stopDistance * this.stopDistance);
        }

        @Override
        public void start() {
            this.timeToRecalcPath = 0;
            this.oldWaterCost = this.mob.getPathfindingMalus(PathType.WATER);
            this.mob.setPathfindingMalus(PathType.WATER, 0.0f);
        }

        @Override
        public void stop() {
            this.followingMob = null;
            this.navigation.stop();
            this.mob.setPathfindingMalus(PathType.WATER, this.oldWaterCost);
        }

        @Override
        public void tick() {
            double f;
            double e;
            if (this.followingMob == null || this.mob.isLeashed()) {
                return;
            }
            this.mob.getLookControl().setLookAt(this.followingMob, 10.0f, this.mob.getMaxHeadXRot());
            if (--this.timeToRecalcPath > 0) {
                return;
            }
            this.timeToRecalcPath = this.adjustedTickDelay(10);
            double d = this.mob.getX() - this.followingMob.getX();
            double g = d * d + (e = this.mob.getY() - this.followingMob.getY()) * e
                    + (f = this.mob.getZ() - this.followingMob.getZ()) * f;
            if (g <= (double) (this.stopDistance * this.stopDistance)) {
                this.navigation.stop();
                LookControl lookControl = this.followingMob.getLookControl();
                if (g <= (double) this.stopDistance || lookControl.getWantedX() == this.mob.getX()
                        && lookControl.getWantedY() == this.mob.getY() && lookControl.getWantedZ() == this.mob.getZ()) {
                    double h = this.followingMob.getX() - this.mob.getX();
                    double i = this.followingMob.getZ() - this.mob.getZ();
                    this.navigation.moveTo(this.mob.getX() - h, this.mob.getY(), this.mob.getZ() - i,
                            this.speedModifier);
                }
                return;
            }
            this.navigation.moveTo(this.followingMob, this.speedModifier);
        }
    }

    static class LionFollowParentGoal extends FollowParentGoal {
        private final Lion lion;

        public LionFollowParentGoal(Lion animal, double speedModifier) {
            super(animal, speedModifier);
            this.lion = animal;
        }

        @Override
        public boolean canUse() {
            return !this.lion.isSleeping() && super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            return !this.lion.isSleeping() && super.canContinueToUse();
        }
    }
}