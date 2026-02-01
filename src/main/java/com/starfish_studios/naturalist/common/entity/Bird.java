package com.starfish_studios.naturalist.common.entity;

import net.minecraft.world.entity.ai.util.LandRandomPos;

import com.starfish_studios.naturalist.common.entity.core.NaturalistAnimal;
import com.starfish_studios.naturalist.core.registry.NaturalistTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
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

import java.util.EnumSet;

public class Bird extends NaturalistAnimal implements FlyingAnimal, NaturalistGeoEntity {
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
    private static final EntityDataAccessor<Integer> VARIANT_ID = SynchedEntityData.defineId(Bird.class,
            EntityDataSerializers.INT);
    protected static final RawAnimation OLD_FLY = RawAnimation.begin().thenLoop("animation.bird.fly");
    protected static final RawAnimation FLY = RawAnimation.begin().thenLoop("animation.sf_nba.bird.fly");
    protected static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation.sf_nba.bird.idle");
    protected static final RawAnimation SIT = RawAnimation.begin().thenLoop("animation.sf_nba.bird.sit");
    protected static final RawAnimation EAT = RawAnimation.begin().thenLoop("animation.sf_nba.bird.eat");
    protected static final RawAnimation HOP = RawAnimation.begin().thenLoop("animation.sf_nba.bird.hop");
    public float flap;
    public float flapSpeed;
    public float oFlapSpeed;
    public float oFlap;
    private float flapping = 1.0F;
    private float nextFlap = 1.0F;
    private int eatAnimationTick;

    public Bird(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new FlyingMoveControl(this, 10, false);
        this.setPathfindingMalus(PathType.DANGER_FIRE, -1.0F);
        this.setPathfindingMalus(PathType.DAMAGE_FIRE, -1.0F);
        this.setPathfindingMalus(PathType.COCOA, -1.0F);
    }

    private static final Ingredient FOOD_ITEMS = Ingredient.of(Items.WHEAT_SEEDS);

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new PanicGoal(this, 1.25D));
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new BirdTemptGoal(this, 1.0D, FOOD_ITEMS, false));
        this.goalSelector.addGoal(2, new BirdWanderGoal(this, 1.0D));
        // 1.21: FollowOwnerGoal constructor is (animal, speed, startDistance,
        // stopDistance)
        this.goalSelector.addGoal(3, new FollowOwnerGoal(this, 1.5D, 5.0F, 1.0F));
        this.goalSelector.addGoal(2, new SitWhenOrderedToGoal(this));
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, @NotNull DifficultyInstance difficulty,
            @NotNull EntitySpawnReason reason,
            @Nullable SpawnGroupData spawnData) {
        this.setVariant(this.random.nextInt(4));
        if (spawnData == null) {
            spawnData = new AgeableMobGroupData(false);
        }
        return super.finalizeSpawn(level, difficulty, reason, spawnData);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 6.0D).add(Attributes.FLYING_SPEED, 0.8F)
                .add(Attributes.MOVEMENT_SPEED, 0.2F).add(Attributes.TEMPT_RANGE, 10.0D);
    }

    public static boolean checkBirdSpawnRules(EntityType<? extends Bird> entityType, ServerLevelAccessor levelAccessor,
            EntitySpawnReason spawnReason, BlockPos blockPos, RandomSource randomSource) {
        return (levelAccessor.getBlockState(blockPos.below()).is(NaturalistTags.BlockTags.BIRDS_SPAWNABLE_ON)
                || levelAccessor.getBlockState(blockPos).is(BlockTags.LEAVES))
                && isBrightEnoughToSpawn(levelAccessor, blockPos);
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
        FlyingPathNavigation navigation = new FlyingPathNavigation(this, level);
        navigation.setCanOpenDoors(false);
        navigation.setCanFloat(true);
        // setCanPassDoors removed in 1.21
        return navigation;
    }

    public int getVariant() {
        return Mth.clamp(this.entityData.get(VARIANT_ID), 0, 3);
    }

    public void setVariant(int variant) {
        this.entityData.set(VARIANT_ID, variant);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(VARIANT_ID, 0);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(NaturalistTags.ItemTags.BIRD_FOOD_ITEMS);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        this.calculateFlapping();
        if (this.level().isClientSide()) {
            this.eatAnimationTick = Math.max(0, this.eatAnimationTick - 1);
        }
    }

    private void calculateFlapping() {
        this.oFlap = this.flap;
        this.oFlapSpeed = this.flapSpeed;
        this.flapSpeed += (float) (this.onGround() || this.isPassenger() ? -1 : 4) * 0.3F;
        this.flapSpeed = Mth.clamp(this.flapSpeed, 0.0F, 1.0F);
        if (!this.onGround() && this.flapping < 1.0F) {
            this.flapping = 1.0F;
        }
        this.flapping *= 0.9F;
        Vec3 vec3 = this.getDeltaMovement();
        if (!this.onGround() && vec3.y < 0.0D) {
            this.setDeltaMovement(vec3.multiply(1.0D, 0.6D, 1.0D));
        }
        this.flap += this.flapping * 2.0F;
    }

    @Override
    public boolean isFlying() {
        return !this.onGround();
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (this.isTame()) {
            if (this.isFood(stack) && this.getHealth() < this.getMaxHealth()) { // Heal
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                this.heal(2);
                this.playSound(SoundEvents.PARROT_EAT, 1.0f, 1.0f);
                this.eatAnimationTick = 40;
                return InteractionResult.SUCCESS;
            } else if (this.isOwnedBy(player) && !this.isFood(stack)) { // Sit
                InteractionResult interactionResult = super.mobInteract(player, hand);
                if (interactionResult.consumesAction() && !this.isBaby() || !this.isOwnedBy(player))
                    return interactionResult;
                this.setOrderedToSit(!this.isOrderedToSit());
                return InteractionResult.SUCCESS;
            }
        } else if (stack.is(NaturalistTags.ItemTags.BIRD_TEMPT_ITEMS)) { // Tame
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            if (!this.level().isClientSide()) {
                if (this.random.nextInt(3) == 0) {
                    this.tame(player);
                    this.navigation.stop();
                    this.setTarget(null);
                    this.setOrderedToSit(true);
                    this.level().broadcastEntityEvent(this, (byte) 7);
                } else {
                    this.level().broadcastEntityEvent(this, (byte) 6);
                }
            }
            return InteractionResult.SUCCESS;
        }

        return super.mobInteract(player, hand);
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 10) {
            this.eatAnimationTick = 40;
        } else {
            super.handleEntityEvent(id);
        }
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob otherParent) {
        return null; // Birds don't breed
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.PARROT_AMBIENT;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource source) {
        return SoundEvents.PARROT_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.PARROT_DEATH;
    }

    @Override
    protected void playStepSound(@NotNull BlockPos pos, @NotNull BlockState state) {
        this.playSound(SoundEvents.PARROT_STEP, 0.15F, 1.0F);
    }

    @Override
    protected boolean isFlapping() {
        return this.flyDist > this.nextFlap;
    }

    @Override
    protected void onFlap() {
        this.playSound(SoundEvents.PARROT_FLY, 0.15F, 1.0F);
        this.nextFlap = this.flyDist + this.flapSpeed / 2.0F;
    }

    @Override
    public boolean isPushable() {
        return true;
    }

    @Override
    protected void doPush(Entity entity) {
        if (!(entity instanceof Player)) {
            super.doPush(entity);
        }
    }

    // @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {
    }

    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }

    // GeckoLib 5: predicate uses plain Bird type instead of generic E
    protected @NotNull PlayState predicate(final @NotNull AnimationTest<Bird> state) {
        software.bernie.geckolib.animatable.processing.AnimationController<Bird> controller = state.controller();
        if (this.eatAnimationTick > 0) {
            controller.setAnimation(EAT);
            return PlayState.CONTINUE;
        } else if (this.isInSittingPose()) {
            controller.setAnimation(SIT);
            return PlayState.CONTINUE;
        } else if (this.isFlying()) {
            controller.setAnimation(FLY);
            return PlayState.CONTINUE;
        } else if (this.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6) {
            controller.setAnimation(HOP);
            return PlayState.CONTINUE;
        } else {
            controller.setAnimation(IDLE);
            return PlayState.CONTINUE;
        }
    }

    @Override
    public void registerControllers(final AnimatableManager.ControllerRegistrar controllers) {
        // GeckoLib 5: constructor is (name, ticks, predicate) - no "this" parameter
        controllers.add(new AnimationController<Bird>("controller", 5, this::predicate));
    }

    static class BirdWanderGoal extends WaterAvoidingRandomFlyingGoal {
        public BirdWanderGoal(PathfinderMob mob, double speedModifier) {
            super(mob, speedModifier);
        }

        @Override
        @Nullable
        protected Vec3 getPosition() {
            Vec3 vec3 = null;
            if (this.mob.isInWater()) {
                vec3 = LandRandomPos.getPos(this.mob, 15, 15);
            }

            if (this.mob.getRandom().nextFloat() >= this.probability) {
                vec3 = this.getTreePos();
            }

            return vec3 == null ? super.getPosition() : vec3;
        }

        @Nullable
        private Vec3 getTreePos() {
            BlockPos blockPos = this.mob.blockPosition();
            BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
            BlockPos.MutableBlockPos mutableBlockPos2 = new BlockPos.MutableBlockPos();

            for (BlockPos blockPos2 : BlockPos.betweenClosed(Mth.floor(this.mob.getX() - 3.0D),
                    Mth.floor(this.mob.getY() - 6.0D), Mth.floor(this.mob.getZ() - 3.0D),
                    Mth.floor(this.mob.getX() + 3.0D), Mth.floor(this.mob.getY() + 6.0D),
                    Mth.floor(this.mob.getZ() + 3.0D))) {
                if (!blockPos.equals(blockPos2)) {
                    BlockState blockState = this.mob.level()
                            .getBlockState(mutableBlockPos2.setWithOffset(blockPos2, Direction.DOWN));
                    boolean bl = blockState.is(NaturalistTags.BlockTags.BIRDS_PERCH_ON);
                    if (bl && this.mob.level().isEmptyBlock(blockPos2)
                            && this.mob.level().isEmptyBlock(mutableBlockPos.setWithOffset(blockPos2, Direction.UP))) {
                        return Vec3.atBottomCenterOf(blockPos2);
                    }
                }
            }

            return null;
        }
    }

    static class BirdTemptGoal extends TemptGoal {
        public BirdTemptGoal(PathfinderMob mob, double speedModifier, Ingredient items, boolean canScare) {
            super(mob, speedModifier, items, canScare);
        }

        @Override
        public boolean canUse() {
            // In 1.21 isNight is deprecated/removed in some contexts, using isDay() check.
            long time = this.mob.level().getDayTime() % 24000;
            return (time >= 13000 && time <= 23000) && super.canUse();
        }
    }
}
