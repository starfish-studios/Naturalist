package com.starfish_studios.naturalist.common.entity;

import com.starfish_studios.naturalist.common.entity.core.EggLayingAnimal;
import com.starfish_studios.naturalist.common.entity.core.NaturalistAnimal;
import com.starfish_studios.naturalist.common.entity.core.ai.goal.*;
import com.starfish_studios.naturalist.core.registry.NaturalistBlocks;
import com.starfish_studios.naturalist.core.registry.NaturalistEntityTypes;
import com.starfish_studios.naturalist.core.registry.NaturalistSoundEvents;
import com.starfish_studios.naturalist.core.registry.NaturalistTags;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.BreathAirGoal;
import net.minecraft.world.entity.ai.goal.TryFindWaterGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import com.starfish_studios.naturalist.common.entity.core.NaturalistGeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.animatable.processing.AnimationController;
import software.bernie.geckolib.animatable.processing.AnimationTest;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Alligator extends NaturalistAnimal implements NaturalistGeoEntity, EggLayingAnimal {
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
    // TODO: Fix Ingredient.of(TagKey) - using placeholder for now
    private static final Ingredient FOOD_ITEMS = Ingredient.of(Items.COD);

    private static final EntityDataAccessor<Boolean> HAS_EGG = SynchedEntityData.defineId(Alligator.class,
            EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> LAYING_EGG = SynchedEntityData.defineId(Alligator.class,
            EntityDataSerializers.BOOLEAN);

    protected static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation.sf_nba.alligator.idle");
    protected static final RawAnimation WALK = RawAnimation.begin().thenLoop("animation.sf_nba.alligator.walk");
    protected static final RawAnimation SWIM = RawAnimation.begin().thenLoop("animation.sf_nba.alligator.swim");
    protected static final RawAnimation BITE = RawAnimation.begin().thenPlay("animation.sf_nba.alligator.bite");

    int layEggCounter;
    private float tailRot = Float.NaN;

    public Alligator(EntityType<? extends NaturalistAnimal> entityType, Level level) {
        super(entityType, level);
        this.setPathfindingMalus(PathType.WATER, 0.0f);
        this.moveControl = new AlligatorMoveControl(this);
        this.lookControl = new AlligatorLookControl(this, 20);
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
        return new AmphibiousPathNavigation(this, level);
    }

    @SuppressWarnings("unused")
    public static boolean checkAlligatorSpawnRules(EntityType<Alligator> entityType, ServerLevelAccessor levelAccessor,
            EntitySpawnReason spawnType, BlockPos blockPos, RandomSource randomSource) {
        return levelAccessor.getBlockState(blockPos.below()).is(NaturalistTags.BlockTags.ALLIGATORS_SPAWNABLE_ON)
                && isBrightEnoughToSpawn(levelAccessor, blockPos);
    }

    private static boolean isBrightEnoughToSpawn(LevelAccessor levelAccessor, BlockPos blockPos) {
        return levelAccessor.getRawBrightness(blockPos, 0) > 8;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return this.isBaby() ? NaturalistSoundEvents.GATOR_AMBIENT_BABY.get() : NaturalistSoundEvents.GATOR_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return this.isBaby() ? NaturalistSoundEvents.GATOR_AMBIENT_BABY.get() : NaturalistSoundEvents.GATOR_DEATH.get();
    }

    @Override
    public float getVoicePitch() {
        return this.isBaby() ? super.getVoicePitch() * 0.65F : super.getVoicePitch();
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return this.isBaby() ? NaturalistSoundEvents.GATOR_AMBIENT_BABY.get()
                : NaturalistSoundEvents.GATOR_AMBIENT.get();
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob otherParent) {
        return NaturalistEntityTypes.ALLIGATOR.get().create(level,
                net.minecraft.world.entity.EntitySpawnReason.BREEDING);
    }

    public static AttributeSupplier.@NotNull Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.2)
                .add(Attributes.MAX_HEALTH, 30.0)
                .add(Attributes.ATTACK_DAMAGE, 6.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.60);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new BreathAirGoal(this));
        this.goalSelector.addGoal(0, new TryFindWaterGoal(this));
        this.goalSelector.addGoal(1, new EggLayingBreedGoal<>(this, 1.0));
        this.goalSelector.addGoal(1, new LayEggGoal<>(this, 1.0));
        this.goalSelector.addGoal(2, new CloseMeleeAttackGoal(this, 1.2D, true));
        this.goalSelector.addGoal(3, new BabyPanicGoal(this, 1.25D));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.2D));
        this.goalSelector.addGoal(6, new RandomSwimmingGoal(this, 1.0D, 10));
        this.goalSelector.addGoal(6, new RandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new BabyHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<Player>(this, Player.class, 10, true, false,
                (entity, level) -> !this.isBaby() && entity.isInWater()));
        this.targetSelector.addGoal(1,
                new NearestAttackableTargetGoal<LivingEntity>(this, LivingEntity.class, 10, true, false,
                        (entity, level) -> {
                            if (entity instanceof Alligator)
                                return false;
                            Iterable<BlockPos> list = BlockPos.betweenClosed(entity.blockPosition().offset(-2, -2, -2),
                                    entity.blockPosition().offset(2, 2, 2));
                            boolean isEntityNearAlligatorEggs = false;
                            for (BlockPos pos : list) {
                                if (level().getBlockState(pos).is(NaturalistBlocks.ALLIGATOR_EGG.get())) {
                                    isEntityNearAlligatorEggs = true;
                                    break;
                                }
                            }
                            return !this.isBaby() && isEntityNearAlligatorEggs;
                        }));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<LivingEntity>(this, LivingEntity.class, 10, true,
                false,
                (entity, level) -> !this.isBaby()
                        && entity.getType().is(NaturalistTags.EntityTypes.ALLIGATOR_HOSTILES)));
    }

    @Override
    public boolean isFood(@NotNull ItemStack stack) {
        return FOOD_ITEMS.test(stack);
    }

    @Override
    protected float getWaterSlowDown() {
        return 0.98F;
    }

    @Override
    public int getMaxHeadXRot() {
        return 1;
    }

    @Override
    public int getMaxHeadYRot() {
        return 1;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean canBreatheUnderwater() {
        return false;
    }

    @Override
    public int getMaxAirSupply() {
        return 6000; // Similar to Axolotl, but Alligators need to surface occasionally
    }

    protected void handleAirSupply(int airSupply) {
        // Handle air supply like Dolphins/Axolotls - decrease when not in water
        if (this.isAlive() && !this.isInWaterOrRain()) {
            this.setAirSupply(airSupply - 1);
            if (this.getAirSupply() == -20) {
                this.setAirSupply(0);
                this.hurt(this.damageSources().drown(), 2.0F);
            }
        } else {
            this.setAirSupply(this.getMaxAirSupply());
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public void travel(@NotNull Vec3 travelVector) {
        if (this.isInWater()) {
            this.moveRelative(this.getSpeed(), travelVector);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9));
        } else {
            super.travel(travelVector);
        }
    }

    @Override
    public boolean hasEgg() {
        return this.entityData.get(HAS_EGG);
    }

    @Override
    public void setHasEgg(boolean hasEgg) {
        this.entityData.set(HAS_EGG, hasEgg);
    }

    @Override
    public Block getEggBlock() {
        return NaturalistBlocks.ALLIGATOR_EGG.get();
    }

    @Override
    public @NotNull TagKey<Block> getEggLayableBlockTag() {
        return NaturalistTags.BlockTags.ALLIGATOR_EGG_LAYABLE_ON;
    }

    @Override
    public boolean isLayingEgg() {
        return this.entityData.get(LAYING_EGG);
    }

    @Override
    public void setLayingEgg(boolean isLayingEgg) {
        this.layEggCounter = isLayingEgg ? 1 : 0;
        this.entityData.set(LAYING_EGG, isLayingEgg);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(HAS_EGG, false);
        builder.define(LAYING_EGG, false);
    }

    @Override
    protected void addAdditionalSaveData(net.minecraft.world.level.storage.ValueOutput valueOutput) {
        super.addAdditionalSaveData(valueOutput);
        valueOutput.store("HasEgg", com.mojang.serialization.Codec.BOOL, this.hasEgg());
    }

    @Override
    protected void readAdditionalSaveData(net.minecraft.world.level.storage.ValueInput valueInput) {
        super.readAdditionalSaveData(valueInput);
        this.setHasEgg(valueInput.read("HasEgg", com.mojang.serialization.Codec.BOOL).orElse(false));
    }

    @Override
    public int getLayEggCounter() {
        return this.layEggCounter;
    }

    @Override
    public void setLayEggCounter(int layEggCounter) {
        this.layEggCounter = layEggCounter;
    }

    @Override
    public boolean canFallInLove() {
        return super.canFallInLove() && !this.hasEgg();
    }

    @Override
    public void baseTick() {
        int i = this.getAirSupply();
        super.baseTick();
        if (!this.isNoAi()) {
            this.handleAirSupply(i);
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();

        // Initialize tail rotation on first tick
        if (Float.isNaN(this.tailRot)) {
            this.tailRot = this.yBodyRot;
        }

        // Tail rotation lags behind body rotation (yBodyRot)
        float targetTailRot = this.yBodyRot;
        float tailDiff = Mth.wrapDegrees(targetTailRot - this.tailRot);
        this.tailRot = Mth.wrapDegrees(this.tailRot + tailDiff * 0.2F);

        BlockPos pos = this.blockPosition();
        if (this.isAlive() && this.isLayingEgg() && this.layEggCounter >= 1 && this.layEggCounter % 5 == 0
                && this.level().getBlockState(pos.below()).is(this.getEggLayableBlockTag())) {
            this.level().levelEvent(2001, pos, Block.getId(this.level().getBlockState(pos.below())));
        }
    }

    public float getTailRot() {
        return this.tailRot;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }

    protected <E extends Alligator> PlayState predicate(final AnimationTest<E> state) {
        AnimationController<E> controller = state.controller();
        float limbSwingAmount = this.walkAnimation.speed();

        if (this.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6) {
            if (this.isInWater()) {
                controller.setAnimation(SWIM);
                controller.setAnimationSpeed(1.0D + limbSwingAmount * 1.5D);
            } else {
                controller.setAnimation(WALK);
                if (this.isBaby() || this.getTarget() != null) {
                    controller.setAnimationSpeed(2.0D + limbSwingAmount * 1.5D);
                } else {
                    controller.setAnimationSpeed(1.5D + limbSwingAmount * 1.0D);
                }
            }
        } else {
            controller.setAnimation(IDLE);
            controller.setAnimationSpeed(0.6D);
        }
        return PlayState.CONTINUE;
    }

    private <E extends software.bernie.geckolib.animatable.GeoAnimatable> PlayState attackPredicate(
            software.bernie.geckolib.animatable.processing.AnimationTest<E> state) {
        AnimationController<E> controller = state.controller();
        if (this.swinging && controller.getAnimationState().equals(AnimationController.State.STOPPED)) {
            controller.forceAnimationReset();
            controller.setAnimation(BITE);
            this.swinging = false;
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(final AnimatableManager.@NotNull ControllerRegistrar controllers) {
        controllers.add(new AnimationController<Alligator>("controller", 5, this::predicate));
        controllers.add(new AnimationController<Alligator>("attackController", 2, this::attackPredicate));
    }

    static class AlligatorMoveControl extends SmoothSwimmingMoveControl {

        public AlligatorMoveControl(Alligator alligator) {
            super(alligator, 85, 10, 0.1F, 0.5F, false);
        }

        @Override
        public void tick() {
            super.tick();
        }
    }

    static class AlligatorLookControl extends SmoothSwimmingLookControl {
        public AlligatorLookControl(Alligator alligator, int maxYRotFromCenter) {
            super(alligator, maxYRotFromCenter);
        }

        @Override
        public void tick() {
            super.tick();
        }
    }
}