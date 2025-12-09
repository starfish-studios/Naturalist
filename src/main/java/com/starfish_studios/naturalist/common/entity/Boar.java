package com.starfish_studios.naturalist.common.entity;

import com.starfish_studios.naturalist.common.entity.core.NaturalistAnimal;
import com.starfish_studios.naturalist.common.entity.core.NaturalistGeoEntity;
import com.starfish_studios.naturalist.common.entity.core.ai.goal.BabyPanicGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import com.starfish_studios.naturalist.core.registry.NaturalistEntityTypes;
import com.starfish_studios.naturalist.core.registry.NaturalistSoundEvents;
import com.starfish_studios.naturalist.core.registry.NaturalistTags;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Zoglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.UUID;
import java.util.function.Predicate;

public class Boar extends NaturalistAnimal implements NeutralMob, NaturalistGeoEntity {
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
    private static final Ingredient FOOD_ITEMS = Ingredient.of(NaturalistTags.ItemTags.BOAR_FOOD_ITEMS);
    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
    private int remainingPersistentAngerTime;
    @Nullable
    private UUID persistentAngerTarget;

    protected static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation.sf_nba.boar.idle");
    protected static final RawAnimation WALK = RawAnimation.begin().thenLoop("animation.sf_nba.boar.walk");
    protected static final RawAnimation RUN = RawAnimation.begin().thenLoop("animation.sf_nba.boar.run");

    public Boar(EntityType<? extends NaturalistAnimal> entityType, Level level) {
        super(entityType, level);
    }


    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
        return new GroundPathNavigation(this, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 14.0).add(Attributes.MOVEMENT_SPEED, 0.2).add(Attributes.ATTACK_DAMAGE, 1.0D);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob otherParent) {
        return NaturalistEntityTypes.BOAR.get().create(level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new BreedGoal(this, 1.0));
        this.goalSelector.addGoal(2, new TemptGoal(this, 1.2, FOOD_ITEMS, false));
        this.goalSelector.addGoal(3, new BoarAvoidPlayerGoal(this, Player.class, 16.0f, 1.5D, 1.5D, entity -> !entity.isHolding(FOOD_ITEMS)));
        this.goalSelector.addGoal(4, new BoarMeleeAttackGoal(this, 1.2, false));
        this.goalSelector.addGoal(5, new BabyPanicGoal(this, 1.4));
        this.goalSelector.addGoal(6, new FollowParentGoal(this, 1.1));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 6.0f));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, this::isAngryAt));
        this.targetSelector.addGoal(3, new ResetUniversalAngerTargetGoal<>(this, false));
    }

    public boolean isAggro() {
        return this.getHealth() > this.getMaxHealth() / 2;
    }

    @Override
    public boolean isFood(@NotNull ItemStack stack) {
        return FOOD_ITEMS.test(stack);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide) {
            if (!this.isAggro()) {
                this.stopBeingAngry();
            }
            this.updatePersistentAnger((ServerLevel)this.level(), true);
        }
    }

    @Override
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor level, @NotNull DifficultyInstance difficulty, @NotNull MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag compoundTag) {
        super.finalizeSpawn(level, difficulty, reason, spawnData, compoundTag);

        return spawnData;
    }

    @Override
    public void customServerAiStep() {
        super.customServerAiStep();
        if (this.getMoveControl().hasWanted()) {
            this.setSprinting(this.getMoveControl().getSpeedModifier() >= 1.2D);
        } else {
            this.setSprinting(false);
        }
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return NaturalistSoundEvents.BOAR_AMBIENT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return NaturalistSoundEvents.BOAR_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return NaturalistSoundEvents.BOAR_DEATH.get();
    }

    @Override
    protected void playStepSound(@NotNull BlockPos pos, @NotNull BlockState state) {
        this.playSound(SoundEvents.PIG_STEP, 0.15f, 1.0f);
    }

    @Override
    public float getVoicePitch() {
        return this.isBaby() ? (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 0.75F : (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 0.5F;
    }

    @Override
    public void thunderHit(@NotNull ServerLevel level, @NotNull LightningBolt lightning) {
        super.thunderHit(level, lightning);
        if (level.getDifficulty() != Difficulty.PEACEFUL) {
            Zoglin zoglin = EntityType.ZOGLIN.create(level);
            assert zoglin != null;
            zoglin.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), this.getXRot());
            zoglin.setNoAi(this.isNoAi());
            zoglin.setBaby(this.isBaby());
            if (this.hasCustomName()) {
                zoglin.setCustomName(this.getCustomName());
                zoglin.setCustomNameVisible(this.isCustomNameVisible());
            }
            zoglin.setPersistenceRequired();
            level.addFreshEntity(zoglin);
            this.discard();
        }
    }

    @Override
    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.random));
    }

    @Override
    public void setRemainingPersistentAngerTime(int remainingPersistentAngerTime) {
        this.remainingPersistentAngerTime = remainingPersistentAngerTime;
    }

    @Override
    public int getRemainingPersistentAngerTime() {
        return this.remainingPersistentAngerTime;
    }

    @Override
    public void setPersistentAngerTarget(@Nullable UUID persistentAngerTarget) {
        this.persistentAngerTarget = persistentAngerTarget;
    }

    @Override
    @Nullable
    public UUID getPersistentAngerTarget() {
        return this.persistentAngerTarget;
    }
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }
    protected <E extends Boar> PlayState predicate(final @NotNull AnimationState<E> event) {
        if (this.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6) {
            if (this.isSprinting()) {
                event.getController().setAnimation(RUN);
                event.getController().setAnimationSpeed(2.0D);
            } else {
                event.getController().setAnimation(WALK);
                event.getController().setAnimationSpeed(1.5D);
            }
        } else {
            event.getController().setAnimation(IDLE);
            event.getController().setAnimationSpeed(1.0D);
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(final AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 10, this::predicate));
    }

    static class BoarMeleeAttackGoal extends MeleeAttackGoal {
        private final Boar boar;

        public BoarMeleeAttackGoal(Boar mob, double speedModifier, boolean followingTargetEvenIfNotSeen) {
            super(mob, speedModifier, followingTargetEvenIfNotSeen);
            this.boar = mob;
        }

        @Override
        public boolean canUse() {
            return this.boar.isAggro() && !this.boar.isBaby() && super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            return this.boar.isAggro() && !this.boar.isBaby() && super.canContinueToUse();
        }
    }

    static class BoarAvoidPlayerGoal extends AvoidEntityGoal<Player> {
        private final Boar boar;

        public BoarAvoidPlayerGoal(Boar mob, Class<Player> entityClassToAvoid, float maxDistance, double walkSpeedModifier, double sprintSpeedModifier, Predicate<LivingEntity> predicateOnAvoidEntity) {
            super(mob, entityClassToAvoid, maxDistance, walkSpeedModifier, sprintSpeedModifier, predicateOnAvoidEntity);
            this.boar = mob;
        }

        @Override
        public boolean canUse() {
            return !this.boar.isAggro() && !this.boar.isBaby() && super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            return !this.boar.isAggro() && !this.boar.isBaby() && super.canContinueToUse();
        }

        @Override
        public void start() {
            this.boar.stopBeingAngry();
            super.start();
        }
    }
}
