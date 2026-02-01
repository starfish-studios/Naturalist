package com.starfish_studios.naturalist.common.entity;

import net.minecraft.world.item.Items;

import com.starfish_studios.naturalist.common.entity.core.NaturalistAnimal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import com.starfish_studios.naturalist.core.registry.NaturalistEntityTypes;
import com.starfish_studios.naturalist.core.registry.NaturalistItems;
import com.starfish_studios.naturalist.core.registry.NaturalistSoundEvents;
import com.starfish_studios.naturalist.core.registry.NaturalistTags;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
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

public class Duck extends NaturalistAnimal implements NaturalistGeoEntity {
    private static final Ingredient FOOD_ITEMS = Ingredient.of(Items.BREAD); // TODO: Fix
                                                                             // Ingredient.of(NaturalistTags.ItemTags.DUCK_FOOD_ITEMS);
    public float flap;
    public float flapSpeed;
    public float oFlapSpeed;
    public float oFlap;
    public float flapping = 1.0F;
    private float nextFlap = 1.0F;
    public int eggTime;

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    protected static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation.sf_nba.duck.idle");
    protected static final RawAnimation WALK = RawAnimation.begin().thenLoop("animation.sf_nba.duck.walk");
    protected static final RawAnimation SWIM = RawAnimation.begin().thenLoop("animation.sf_nba.duck.swim");
    protected static final RawAnimation FLAP = RawAnimation.begin().thenLoop("animation.sf_nba.duck.flap");

    public Duck(@NotNull EntityType<? extends NaturalistAnimal> entityType, Level level) {
        super(entityType, level);
        this.eggTime = this.random.nextInt(6000) + 6000;
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
        return new GroundPathNavigation(this, level);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.4));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.0, FOOD_ITEMS, false));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.1));
        this.goalSelector.addGoal(5, new RandomSwimmingGoal(this, 1.0, 10));
        this.goalSelector.addGoal(6, new RandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }

    // getStandingEyeHeight removed in 1.21 - EntityDimensions.height is private

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 4.0).add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.TEMPT_RANGE, 10.0D);
    }

    @Override
    public boolean isFood(@NotNull ItemStack stack) {
        return FOOD_ITEMS.test(stack);
    }

    @Override
    protected float getWaterSlowDown() {
        return 0.9F;
    }

    @Nullable
    @Override
    public Duck getBreedOffspring(@NotNull ServerLevel serverLevel, @NotNull AgeableMob ageableMob) {
        return NaturalistEntityTypes.DUCK.get().create(serverLevel, EntitySpawnReason.BREEDING);
    }

    @SuppressWarnings("unused")
    public static boolean checkDuckSpawnRules(EntityType<Duck> entityType, ServerLevelAccessor levelAccessor,
            EntitySpawnReason spawnType, BlockPos blockPos, RandomSource randomSource) {
        return levelAccessor.getBlockState(blockPos.below()).is(NaturalistTags.BlockTags.DUCKS_SPAWNABLE_ON)
                && isBrightEnoughToSpawn(levelAccessor, blockPos);
    }

    private static boolean isBrightEnoughToSpawn(LevelAccessor levelAccessor, BlockPos blockPos) {
        return levelAccessor.getLightEmission(blockPos) > 8
                || levelAccessor.getBlockState(blockPos.below()).getFluidState().is(FluidTags.WATER);
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return NaturalistSoundEvents.DUCK_AMBIENT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return NaturalistSoundEvents.DUCK_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return NaturalistSoundEvents.DUCK_DEATH.get();
    }

    @Override
    protected void playStepSound(@NotNull BlockPos pos, @NotNull BlockState state) {
        this.playSound(NaturalistSoundEvents.DUCK_STEP.get(), 0.1f, 1.2f);
    }

    protected boolean isFlapping() {
        return this.flyDist > this.nextFlap;
    }

    protected void onFlap() {
        this.nextFlap = this.flyDist + this.flapSpeed / 2.0F;
    }

    public boolean causeFallDamage(float fallDistance, float multiplier, @NotNull DamageSource source) {
        return false;
    }

    @Override
    public void customServerAiStep(ServerLevel serverLevel) {
        super.customServerAiStep(serverLevel);
        if (this.getMoveControl().hasWanted()) {
            this.setSprinting(this.getMoveControl().getSpeedModifier() >= 1.2D);
        } else {
            this.setSprinting(false);
            this.flapping = 0.9F;
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        this.oFlap = this.flap;
        this.oFlapSpeed = this.flapSpeed;
        this.flapSpeed += (this.onGround() ? -1.0F : 4.0F) * 0.3F;
        this.flapSpeed = Mth.clamp(this.flapSpeed, 0.0F, 1.0F);
        if (!this.onGround() && this.flapping < 1.0F) {
            this.flapping = 1.0F;
        }

        this.flapping *= 0.9F;
        Vec3 vec3 = this.getDeltaMovement();
        if (!this.onGround() && vec3.y < 0.0) {
            this.setDeltaMovement(vec3.multiply(1.0, 0.6, 1.0));
        }

        this.flap += this.flapping * 2.0F;
        if (!this.level().isClientSide() && this.isAlive() && !this.isBaby() && --this.eggTime <= 0) {
            this.playSound(SoundEvents.CHICKEN_EGG, 1.0F,
                    (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
            this.spawnAtLocation((net.minecraft.server.level.ServerLevel) this.level(), NaturalistItems.DUCK_EGG.get());
            this.gameEvent(GameEvent.ENTITY_PLACE);
            this.eggTime = this.random.nextInt(6000) + 6000;
        }

    }

    // getExperienceReward() signature changed in 1.21
    // @Override
    // public int getExperienceReward() {
    // return 10;
    // }

    @Override
    public void addAdditionalSaveData(@NotNull ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.store("EggLayTime", com.mojang.serialization.Codec.INT, this.eggTime);
    }

    @Override
    public void readAdditionalSaveData(@NotNull ValueInput input) {
        super.readAdditionalSaveData(input);
        this.eggTime = input.read("EggLayTime", com.mojang.serialization.Codec.INT).orElse(0);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }

    protected <E extends software.bernie.geckolib.animatable.GeoAnimatable> PlayState predicate(
            final AnimationTest<E> state) {
        software.bernie.geckolib.animatable.processing.AnimationController<E> controller = state.controller();
        if (this.isInWater()) {
            controller.setAnimation(SWIM);
            controller.setAnimationSpeed(1.0D);
            return PlayState.CONTINUE;
        } else if (this.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6) {
            if (this.isSprinting()) {
                controller.setAnimation(WALK);
                controller.setAnimationSpeed(2.0D);
                return PlayState.CONTINUE;
            } else {
                controller.setAnimation(WALK);
                controller.setAnimationSpeed(1.5D);
                return PlayState.CONTINUE;
            }
        } else {
            controller.setAnimation(IDLE);
            controller.setAnimationSpeed(1.0D);
        }
        return PlayState.CONTINUE;
    }

    protected <E extends software.bernie.geckolib.animatable.GeoAnimatable> PlayState flapPredicate(
            software.bernie.geckolib.animatable.processing.AnimationTest<E> state) {
        AnimationController<E> controller = state.controller();
        if (!this.onGround() && !this.isInWater()) {
            controller.setAnimation(FLAP);
            controller.setAnimationSpeed(1.0D);
            return PlayState.CONTINUE;
        }
        state.controller().forceAnimationReset();

        return PlayState.STOP;
    }

    @Override
    public void registerControllers(final AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<Duck>("controller", 5, this::predicate));
        controllers.add(new AnimationController<Duck>("flapController", 2, this::flapPredicate));
    }

}