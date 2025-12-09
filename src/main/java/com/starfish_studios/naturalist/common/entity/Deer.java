package com.starfish_studios.naturalist.common.entity;

import com.starfish_studios.naturalist.common.entity.core.NaturalistAnimal;
import com.starfish_studios.naturalist.common.entity.core.NaturalistGeoEntity;
import com.starfish_studios.naturalist.common.entity.core.ai.goal.BigPanicGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import com.starfish_studios.naturalist.core.registry.NaturalistEntityTypes;
import com.starfish_studios.naturalist.core.registry.NaturalistSoundEvents;
import com.starfish_studios.naturalist.core.registry.NaturalistTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;

public class Deer extends NaturalistAnimal implements NaturalistGeoEntity {
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
    private int panicTicks = 0;
    private int eatAnimationTick;

    protected static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation.sf_nba.deer.idle");
    protected static final RawAnimation WALK = RawAnimation.begin().thenLoop("animation.sf_nba.deer.walk");
    protected static final RawAnimation RUN = RawAnimation.begin().thenLoop("animation.sf_nba.deer.run");
    protected static final RawAnimation BABY_RUN = RawAnimation.begin().thenLoop("animation.sf_nba.deer.baby_run");
    protected static final RawAnimation EAT = RawAnimation.begin().thenLoop("animation.sf_nba.deer.eat");

    public Deer(EntityType<? extends NaturalistAnimal> entityType, @NotNull Level level) {
        super(entityType, level);
        this.setMaxUpStep(2.0F);
    }


    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
        return new GroundPathNavigation(this, level);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob mob) {
        return NaturalistEntityTypes.DEER.get().create(level);
    }

    public static AttributeSupplier.@NotNull Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 8.0D).add(Attributes.MOVEMENT_SPEED, 0.2F);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new BigPanicGoal(this, 1.6D));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.25D, Ingredient.of(Items.APPLE), true));
        this.goalSelector.addGoal(4, new AvoidEntityGoal<>(this, Player.class, 8.0F, 1.5D, 2.0D, livingEntity -> EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(livingEntity) && !livingEntity.isDiscrete()));
        this.goalSelector.addGoal(4, new AvoidEntityGoal<>(this, Monster.class, 4.0F, 1.5D, 2.0D));
        this.goalSelector.addGoal(4, new AvoidEntityGoal<>(this, Animal.class, 10.0F, 1.5D, 2.0D, livingEntity -> livingEntity.getType().is(NaturalistTags.EntityTypes.DEER_PREDATORS)));
        this.goalSelector.addGoal(5, new FollowParentGoal(this, 1.5D));
        EatBlockGoal eatBlockGoal = new EatBlockGoal(this);
        this.goalSelector.addGoal(6, eatBlockGoal);
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return this.isBaby() ? NaturalistSoundEvents.DEER_HURT_BABY.get() : NaturalistSoundEvents.DEER_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return this.isBaby() ? NaturalistSoundEvents.DEER_AMBIENT_BABY.get() : NaturalistSoundEvents.DEER_AMBIENT.get();
    }
    @Override
    public float getVoicePitch() {
        return this.isBaby() ? super.getVoicePitch() * 0.65F : super.getVoicePitch();
    }
    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(Items.APPLE);
    }

    @Override
    public void aiStep() {
        if (this.level().isClientSide) {
            this.eatAnimationTick = Math.max(0, this.eatAnimationTick - 1);
        }
        super.aiStep();
    }

    @Override
    public void handleEntityEvent(byte pId) {
        if (pId == 10) {
            this.eatAnimationTick = 40;
        } else {
            super.handleEntityEvent(pId);
        }
    }

    public boolean isEating() {
        return this.eatAnimationTick > 0;
    }

    @Override
    public void ate() {
        if (this.isBaby()) {
            this.ageUp(60);
        }
    }

    @Override
    public void customServerAiStep() {
        super.customServerAiStep();
        this.setSprinting(this.getMoveControl().hasWanted() &&
                this.getMoveControl().getSpeedModifier() >= 1.5D);
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        boolean lastHurt = super.hurt(source, amount);
        if (lastHurt) {
            int ticks = 100 + this.random.nextInt(100);
            this.panicTicks = ticks;
            List<? extends Deer> deers = this.level().getEntitiesOfClass(Deer.class, this.getBoundingBox().inflate(8.0D, 4.0D, 8.0D));
            for (Deer deer : deers) {
                deer.panicTicks = ticks;
            }
        }
        return lastHurt;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {
            if (panicTicks >= 0) {
                panicTicks--;
            }
            if (panicTicks == 0 && this.getLastHurtByMob() != null) {
                this.setLastHurtByMob(null);
            }
        }
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }

    protected <E extends Deer> PlayState predicate(final @NotNull AnimationState<E> event) {
        if (this.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6) {
            if (this.isSprinting()) {
                if (this.isBaby()) {
                    event.getController().setAnimation(BABY_RUN);
                    event.getController().setAnimationSpeed(1.0D);
                } else {
                    event.getController().setAnimation(RUN);
                    event.getController().setAnimationSpeed(2.3D);
                }
            } else {
                event.getController().setAnimation(WALK);
                if (this.isBaby()) {
                    event.getController().setAnimationSpeed(1.2D);
                } else {
                    event.getController().setAnimationSpeed(1.0D);
                }
            }
        } else {
            event.getController().setAnimation(IDLE);
            if (this.isBaby()) {
                event.getController().setAnimationSpeed(1.5D);
            } else {
                event.getController().setAnimationSpeed(1.0D);
            }
        }
        return PlayState.CONTINUE;
    }

    protected <E extends Deer> PlayState eatPredicate(final @NotNull AnimationState<E> event) {
        if (this.isEating()) {
            event.getController().setAnimation(EAT);
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    @Override
    public void registerControllers(final AnimatableManager.@NotNull ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 5, this::predicate));
        controllers.add(new AnimationController<>(this, "eat_controller", 5, this::eatPredicate));
    }
}
