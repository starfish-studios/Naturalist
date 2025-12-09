package com.starfish_studios.naturalist.common.entity;

import com.starfish_studios.naturalist.common.entity.core.ClimbingAnimal;
import com.starfish_studios.naturalist.common.entity.core.NaturalistAnimal;
import com.starfish_studios.naturalist.common.entity.core.SleepingAnimal;
import com.starfish_studios.naturalist.common.entity.core.ai.goal.SearchForItemsGoal;
import com.starfish_studios.naturalist.common.entity.core.ai.goal.SleepGoal;
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
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.Path;
import com.starfish_studios.naturalist.common.entity.core.NaturalistGeoEntity;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.keyframe.event.SoundKeyframeEvent;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.util.GeckoLibUtil;

import org.jetbrains.annotations.Nullable;
import java.util.List;
import java.util.UUID;

public class Snake extends ClimbingAnimal implements SleepingAnimal, NeutralMob, NaturalistGeoEntity {
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
    private static final Ingredient FOOD_ITEMS = Ingredient.of(NaturalistTags.ItemTags.SNAKE_TEMPT_ITEMS);

    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
    private static final EntityDataAccessor<Integer> REMAINING_ANGER_TIME = SynchedEntityData.defineId(Snake.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> SLEEPING = SynchedEntityData.defineId(Snake.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> EAT_COUNTER = SynchedEntityData.defineId(Snake.class, EntityDataSerializers.INT);
    @Nullable
    private UUID persistentAngerTarget;

    protected static final RawAnimation MOVE = RawAnimation.begin().thenPlay("animation.sf_nba.snake.move");
    protected static final RawAnimation SLEEP = RawAnimation.begin().thenLoop("animation.sf_nba.snake.sleep");
    protected static final RawAnimation CLIMB = RawAnimation.begin().thenLoop("animation.sf_nba.snake.climb");
    protected static final RawAnimation ATTACK = RawAnimation.begin().thenPlay("animation.sf_nba.snake.attack");
    protected static final RawAnimation TONGUE = RawAnimation.begin().thenPlay("animation.sf_nba.snake.tongue");
    protected static final RawAnimation RATTLE = RawAnimation.begin().thenLoop("animation.sf_nba.snake.rattle");

    public Snake(EntityType<? extends NaturalistAnimal> entityType, Level level) {
        super(entityType, level);
        this.setCanPickUpLoot(true);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createMobAttributes().add(Attributes.MAX_HEALTH, 10.0D).add(Attributes.FOLLOW_RANGE, 20.0D).add(Attributes.MOVEMENT_SPEED, 0.18D).add(Attributes.ATTACK_DAMAGE, 6.0D);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new SnakeMeleeAttackGoal(this, 1.75D, true));
        this.goalSelector.addGoal(2, new SearchForItemsGoal(this, 1.2F, FOOD_ITEMS, 8.0D, 8.0D));
        this.goalSelector.addGoal(3, new SleepGoal<>(this));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, this::isAngryAt));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Mob.class, 5, true, false, livingEntity -> livingEntity.getType().is(NaturalistTags.EntityTypes.SNAKE_HOSTILES) || (livingEntity instanceof Slime slime && slime.isTiny())));
        this.targetSelector.addGoal(4, new ResetUniversalAngerTargetGoal<>(this, false));
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob ageable) {
        return null;
    }

    @SuppressWarnings("unused")
    public static boolean checkSnakeSpawnRules(EntityType<Snake> entityType, LevelAccessor level, MobSpawnType type, BlockPos pos, RandomSource random) {
        return level.getBlockState(pos.below()).is(BlockTags.RABBITS_SPAWNABLE_ON) && isBrightEnoughToSpawn(level, pos);
    }

    @Override
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor level, @NotNull DifficultyInstance difficulty, @NotNull MobSpawnType reason, 
                                        @Nullable SpawnGroupData spawnData, @Nullable CompoundTag compoundTag) {
        this.populateDefaultEquipmentSlots(random, difficulty);
        return super.finalizeSpawn(level, difficulty, reason, spawnData, compoundTag);
    }

    @Override
    protected void populateDefaultEquipmentSlots(@NotNull RandomSource random, @NotNull DifficultyInstance difficulty) {
        if (random.nextFloat() < 0.2F) {
            float chance = random.nextFloat();
            ItemStack stack;
            if (chance < 0.05F) {
                stack = new ItemStack(Items.RABBIT_FOOT);
            } else if (chance < 0.1F) {
                stack = new ItemStack(Items.SLIME_BALL);
            } else if (chance < 0.15F) {
                stack = new ItemStack(Items.FEATHER);
            } else if (chance < 0.3F) {
                stack = new ItemStack(Items.RABBIT);
            } else {
                stack = new ItemStack(Items.CHICKEN);
            }

            this.setItemSlot(EquipmentSlot.MAINHAND, stack);
        }
    }

    @Override
    public boolean isFood(@NotNull ItemStack stack) {
        return FOOD_ITEMS.test(stack);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SLEEPING, false);
        this.entityData.define(EAT_COUNTER, 0);
        this.entityData.define(REMAINING_ANGER_TIME, 0);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.readPersistentAngerSaveData(this.level(), compoundTag);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        this.addPersistentAngerSaveData(compoundTag);
    }

    public boolean isEating() {
        return this.entityData.get(EAT_COUNTER) > 0;
    }

    public void eat(boolean eat) {
        this.entityData.set(EAT_COUNTER, eat ? 1 : 0);
    }

    private int getEatCounter() {
        return this.entityData.get(EAT_COUNTER);
    }

    private void setEatCounter(int amount) {
        this.entityData.set(EAT_COUNTER, amount);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide) {
            this.updatePersistentAnger((ServerLevel)this.level(), true);
        }
        if (this.isSleeping() || this.isImmobile()) {
            this.jumping = false;
            this.xxa = 0.0F;
            this.zza = 0.0F;
        }
        this.handleEating();
        if (!this.getMainHandItem().isEmpty()) {
            if (this.isAngry()) {
                this.stopBeingAngry();
            }
        }
        if (this.canRattle() && !this.isSleeping()) {
            this.playSound(NaturalistSoundEvents.SNAKE_RATTLE.get(), 0.15F, 1.0F);
        }
    }

    private void handleEating() {
        if (!this.isEating() && !this.isSleeping() && !this.getMainHandItem().isEmpty()) {
            this.eat(true);
        } else if (this.getMainHandItem().isEmpty()) {
            this.eat(false);
        }
        if (this.isEating()) {
            if (!this.level().isClientSide && this.getEatCounter() > 6000) {
                if (!this.getMainHandItem().isEmpty()) {
                    if (!this.level().isClientSide) {
                        this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                        this.gameEvent(GameEvent.EAT);
                    }
                }
                this.eat(false);
                return;
            }
            this.setEatCounter(this.getEatCounter() + 1);
        }
    }

    @Override
    public boolean canTakeItem(@NotNull ItemStack stack) {
        EquipmentSlot slot = getEquipmentSlotForItem(stack);
        if (!this.getItemBySlot(slot).isEmpty()) {
            return false;
        } else {
            return slot == EquipmentSlot.MAINHAND && super.canTakeItem(stack);
        }
    }

    @Override
    protected void pickUpItem(@NotNull ItemEntity itemEntity) {
        ItemStack stack = itemEntity.getItem();
        if (this.getMainHandItem().isEmpty() && FOOD_ITEMS.test(stack)) {
            this.onItemPickup(itemEntity);
            this.setItemSlot(EquipmentSlot.MAINHAND, stack);
            this.handDropChances[EquipmentSlot.MAINHAND.getIndex()] = 2.0F;
            this.take(itemEntity, stack.getCount());
            itemEntity.discard();
        }
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        if (!this.getMainHandItem().isEmpty() && !this.level().isClientSide) {
            ItemEntity itemEntity = new ItemEntity(this.level(), this.getX() + this.getLookAngle().x, this.getY() + 1.0D, this.getZ() + this.getLookAngle().z, this.getMainHandItem());
            itemEntity.setPickUpDelay(80);
            itemEntity.setThrower(this.getUUID());
            this.playSound(SoundEvents.FOX_SPIT, 1.0F, 1.0F);
            this.level().addFreshEntity(itemEntity);
            this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
        }
        return super.hurt(source, amount);
    }

    @Override
    protected float getClimbSpeedMultiplier() {
        return 0.5F;
    }

    @Override
    public float getSpeed() {
        return this.getMainHandItem().isEmpty() ? super.getSpeed() : super.getSpeed() * 0.5F;
    }

    @Override
    public boolean canSleep() {
        long dayTime = this.level().getDayTime();
        if (this.isAngry() || this.level().isWaterAt(this.blockPosition())) {
            return false;
        } else if (dayTime > 18000 && dayTime < 23000) {
            return false;
        } else return dayTime > 12000 && dayTime < 28000;
    }

    @Override
    public void setSleeping(boolean sleeping) {
        this.entityData.set(SLEEPING, sleeping);
    }

    @Override
    public boolean isSleeping() {
        return this.entityData.get(SLEEPING);
    }

    @Override
    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.random));
    }

    @Override
    public void setRemainingPersistentAngerTime(int pTime) {
        this.entityData.set(REMAINING_ANGER_TIME, pTime);
    }

    @Override
    public int getRemainingPersistentAngerTime() {
        return this.entityData.get(REMAINING_ANGER_TIME);
    }

    @Override
    public void setPersistentAngerTarget(@Nullable UUID target) {
        this.persistentAngerTarget = target;
    }

    @Nullable
    @Override
    public UUID getPersistentAngerTarget() {
        return this.persistentAngerTarget;
    }

    @Override
    public boolean doHurtTarget(@NotNull Entity entity) {
        if ((this.getType().equals(NaturalistEntityTypes.CORAL_SNAKE.get()) || this.getType().equals(NaturalistEntityTypes.RATTLESNAKE.get())) && entity instanceof LivingEntity living) {
            living.addEffect(new MobEffectInstance(MobEffects.POISON, 40));
        }
        return super.doHurtTarget(entity);
    }

    private boolean canRattle() {
        List<Player> players = this.level().getNearbyPlayers(TargetingConditions.forNonCombat().range(4.0D), this, this.getBoundingBox().inflate(4.0D, 2.0D, 4.0D));
        if(!players.isEmpty() && this.getType().equals(NaturalistEntityTypes.RATTLESNAKE.get()) && !players.get(0).isCreative()){
            this.setTarget(players.get(0));
        } else {
            this.setTarget(null);
        }
        return !players.isEmpty() && this.getType().equals(NaturalistEntityTypes.RATTLESNAKE.get());
    }

    @Override
    protected float getSoundVolume() {
        return 0.15F;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return NaturalistSoundEvents.SNAKE_HURT.get();
    }

    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }

    private <E extends Snake> @NotNull PlayState predicate(final AnimationState<E> event) {
        if (this.isSleeping()) {
            event.getController().setAnimation(SLEEP);
            return PlayState.CONTINUE;
        } else if (this.isClimbing()) {
            event.getController().setAnimation(CLIMB);
            return PlayState.CONTINUE;
        } else if (!(event.getLimbSwingAmount() > -0.04F && event.getLimbSwingAmount() < 0.04F)) {
            event.getController().setAnimation(MOVE);
            return PlayState.CONTINUE;
        }
        event.getController().forceAnimationReset();
        
        return PlayState.STOP;
    }

    private <E extends Snake> PlayState attackPredicate(final AnimationState<E> event) {
        if (this.swinging && event.getController().getAnimationState().equals(AnimationController.State.STOPPED)) {
            event.getController().forceAnimationReset();
        
            event.getController().setAnimation(ATTACK);
            this.swinging = false;
        }
        return PlayState.CONTINUE;
    }

    private <E extends Snake> @NotNull PlayState tonguePredicate(final AnimationState<E> event) {
        if (this.random.nextInt(1000) < this.ambientSoundTime && !this.isSleeping() && event.getController().getAnimationState().equals(AnimationController.State.STOPPED)) {
            event.getController().forceAnimationReset();
        
            event.getController().setAnimation(TONGUE);
        }
        return PlayState.CONTINUE;
    }

    private <E extends Snake> @NotNull PlayState rattlePredicate(final AnimationState<E> event) {
        if (this.canRattle() && !this.isSleeping()) {
            event.getController().setAnimation(RATTLE);
            return PlayState.CONTINUE;
        }
        event.getController().forceAnimationReset();
        
        return PlayState.STOP;
    }

    private void soundListener(@NotNull SoundKeyframeEvent<Snake> event) {
        Snake snake = event.getAnimatable();
        if (snake.level().isClientSide) {
            if (event.getKeyframeData().getSound().equals("hiss")) {
                snake.level().playLocalSound(snake.getX(), snake.getY(), snake.getZ(), NaturalistSoundEvents.SNAKE_HISS.get(), snake.getSoundSource(), snake.getSoundVolume(), snake.getVoicePitch(), false);
            }
        }
    }

    @Override
    public void registerControllers(final AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 10, this::predicate));
        controllers.add(new AnimationController<>(this, "attackController", 0, this::attackPredicate));

        AnimationController<Snake> tongueController = new AnimationController<>(this, "tongueController", 0, this::tonguePredicate);
        tongueController.setSoundKeyframeHandler(this::soundListener);
        controllers.add(tongueController);
        controllers.add(new AnimationController<>(this, "rattleController", 0, this::rattlePredicate));
    }

    static class SnakeMeleeAttackGoal extends MeleeAttackGoal {

        private long lastCanUseCheck;

        public SnakeMeleeAttackGoal(@NotNull PathfinderMob mob, double speedModifier, boolean pFollowingTargetEvenIfNotSeen) {
            super(mob, speedModifier, pFollowingTargetEvenIfNotSeen);
        }

        @Override
        public boolean canUse() {
            return mob.getMainHandItem().isEmpty() && testUse();
        }

        boolean testUse(){
            long l = this.mob.level().getGameTime();
            if (l - this.lastCanUseCheck < 20L) {
                return false;
            } else {
                this.lastCanUseCheck = l;
                LivingEntity livingEntity = this.mob.getTarget();
                if (livingEntity == null) {
                    return false;
                } else if (!livingEntity.isAlive()) {
                    return false;
                } else {
                    Path path = this.mob.getNavigation().createPath(livingEntity, 0);
                    if (path != null) {
                        return true;
                    } else {
                        return this.getAttackReachSqr(livingEntity) >= this.mob.distanceToSqr(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ());
                    }
                }
            }
        }

        @Override
        public boolean canContinueToUse() {
            return mob.getMainHandItem().isEmpty() && super.canContinueToUse();
        }

        @Override
        protected double getAttackReachSqr(LivingEntity attackTarget) {
            return 4.0F + attackTarget.getBbWidth();
        }
    }
}
