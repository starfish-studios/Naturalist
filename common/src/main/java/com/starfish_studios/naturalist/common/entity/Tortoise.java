package com.starfish_studios.naturalist.common.entity;

import com.starfish_studios.naturalist.common.entity.core.EggLayingAnimal;
import com.starfish_studios.naturalist.common.entity.core.HidingAnimal;
import com.starfish_studios.naturalist.common.entity.core.ai.goal.EggLayingBreedGoal;
import com.starfish_studios.naturalist.common.entity.core.ai.goal.HideGoal;
import com.starfish_studios.naturalist.common.entity.core.ai.goal.LayEggGoal;
import com.starfish_studios.naturalist.registry.NaturalistEntityTypes;
import com.starfish_studios.naturalist.registry.NaturalistRegistry;
import com.starfish_studios.naturalist.registry.NaturalistSoundEvents;
import com.starfish_studios.naturalist.registry.NaturalistTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import com.starfish_studios.naturalist.common.entity.core.NaturalistGeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.keyframe.event.SoundKeyframeEvent;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;

public class Tortoise extends TamableAnimal implements NaturalistGeoEntity, HidingAnimal, EggLayingAnimal {
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
    private static final EntityDataAccessor<Integer> VARIANT_ID = SynchedEntityData.defineId(Tortoise.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> HAS_EGG = SynchedEntityData.defineId(Tortoise.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> LAYING_EGG = SynchedEntityData.defineId(Tortoise.class, EntityDataSerializers.BOOLEAN);
    int layEggCounter;
    boolean isDigging;

    protected static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation.sf_nba.tortoise.idle");
    protected static final RawAnimation WALK = RawAnimation.begin().thenLoop("animation.sf_nba.tortoise.walk");
    protected static final RawAnimation SIT = RawAnimation.begin().thenLoop("animation.sf_nba.tortoise.sit");
    protected static final RawAnimation HIDE = RawAnimation.begin().thenPlay("animation.sf_nba.tortoise.hide").thenLoop("animation.sf_nba.tortoise.hide_idle");
    protected static final RawAnimation DIG = RawAnimation.begin().thenLoop("animation.sf_nba.tortoise.dig");
    protected static final RawAnimation HURT = RawAnimation.begin().thenLoop("animation.sf_nba.tortoise.hurt");


    public Tortoise(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createAnimalAttributes().add(Attributes.MOVEMENT_SPEED, 0.17f).add(Attributes.MAX_HEALTH, 20.0).add(Attributes.ATTACK_DAMAGE, 2.0).add(Attributes.KNOCKBACK_RESISTANCE, 0.6);
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.SHIELD_BLOCK;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        Tortoise tortoise = NaturalistEntityTypes.TORTOISE.create(level, EntitySpawnReason.BREEDING);
        if (otherParent instanceof Tortoise tortoiseParent) {
            assert tortoise != null;
            if (this.getVariant() == tortoiseParent.getVariant()) {
                tortoise.setVariant(this.getVariant());
            } else {
                tortoise.setVariant(this.random.nextBoolean() ? tortoiseParent.getVariant() : this.getVariant());
            }
            tortoise.setOwnerUUID(this.random.nextBoolean() ? tortoiseParent.getOwnerUUID() : this.getOwnerUUID());
        }
        return tortoise;
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason reason, @Nullable SpawnGroupData spawnData) {
        Holder<Biome> holder = level.getBiome(this.blockPosition());
        if (holder.is(Biomes.SWAMP) || holder.is(Biomes.MANGROVE_SWAMP)) {
            this.setVariant(1);
        } else if (holder.is(BiomeTags.IS_JUNGLE) || holder.is(Biomes.DARK_FOREST)) {
            this.setVariant(2);
        } else {
            this.setVariant(0);
        }
        return super.finalizeSpawn(level, difficulty, reason, spawnData);
    }

    @Override
    public void setTame(boolean tamed, boolean applyTamingSideEffects) {
        super.setTame(tamed, applyTamingSideEffects);
        if (tamed) {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(30.0);
            this.setHealth(30.0f);
        } else {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(20.0);
        }
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(4.0);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new EggLayingBreedGoal<>(this, 1.0));
        this.goalSelector.addGoal(1, new LayEggGoal<>(this, 1.0));
        this.goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(1, new HideGoal<>(this));
        this.goalSelector.addGoal(2, new TemptGoal(this, 1.0, (itemStack) -> itemStack.is(NaturalistTags.ItemTags.TORTOISE_TEMPT_ITEMS), false));
        this.goalSelector.addGoal(3, new FollowOwnerGoal(this, 1.0, 10.0f, 5.0f));
        this.goalSelector.addGoal(4, new BreedGoal(this, 1.0));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 10.0f));
    }

    @Override
    public boolean canMate(Animal otherAnimal) {
        if (!this.isTame()) {
            return false;
        }
        if (!(otherAnimal instanceof Tortoise tortoise)) {
            return false;
        }
        return tortoise.isTame() && super.canMate(otherAnimal);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(NaturalistTags.ItemTags.TORTOISE_TEMPT_ITEMS);
    }

    @Override
    public void knockback(double strength, double x, double z) {
        super.knockback(this.isInSittingPose() || this.canHide() ? strength / 4 : strength, x, z);
    }

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource source, float amount) {
        return super.hurtServer(serverLevel, source, this.canHide() ? amount * 0.8F : amount);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        InteractionResult interactionResult;
        ItemStack itemStack = player.getItemInHand(hand);
        if (this.level().isClientSide) {
            if (this.isTame() && this.isOwnedBy(player)) {
                return InteractionResult.SUCCESS;
            }
            if (this.isFood(itemStack) && (this.getHealth() < this.getMaxHealth() || !this.isTame())) {
                return InteractionResult.SUCCESS;
            }
            if (itemStack.getItem() instanceof ShearsItem) {
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.PASS;
        }
        if (this.isTame()) {
            if (this.isOwnedBy(player)) {
                if (this.isFood(itemStack) && this.getHealth() < this.getMaxHealth()) {
                    this.usePlayerItem(player, hand, itemStack);
                    this.heal(3.0F);
                    return InteractionResult.CONSUME;
                }
                InteractionResult interactionResult2 = super.mobInteract(player, hand);
                if (!interactionResult2.consumesAction() || this.isBaby()) {
                    this.setOrderedToSit(!this.isOrderedToSit());
                }
                return interactionResult2;
            }
        } else if (this.isFood(itemStack)) {
            this.usePlayerItem(player, hand, itemStack);
            if (this.random.nextInt(3) == 0) {
                this.tame(player);
                this.setOrderedToSit(true);
                this.level().broadcastEntityEvent(this, (byte)7);
            } else {
                this.level().broadcastEntityEvent(this, (byte)6);
            }
            this.setPersistenceRequired();
            return InteractionResult.CONSUME;
        }
        if ((interactionResult = super.mobInteract(player, hand)).consumesAction()) {
            this.setPersistenceRequired();
        }
        return interactionResult;
    }

    @Override
    public boolean canHide() {
        if (this.isTame()) {
            return false;
        }
        if (this.level() instanceof ServerLevel serverLevel) {
            List<Player> players = serverLevel.getNearbyPlayers(TargetingConditions.forNonCombat().range(5.0D).selector((livingEntity, level) -> EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(livingEntity) && !livingEntity.isDiscrete() && !livingEntity.isHolding((itemStack) -> itemStack.is(NaturalistTags.ItemTags.TORTOISE_TEMPT_ITEMS))), this, this.getBoundingBox().inflate(5.0D, 3.0D, 5.0D));
            return !players.isEmpty();
        } else {
            return false;
        }
    }

    @Override
    protected float getWaterSlowDown() {
        return 0.96f;
    }

    //TODO: 1.21.4
    /*@Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions) {
        return dimensions.height * 0.3F;
    }*/

    @Override
    public double getFluidJumpThreshold() {
        return 0.4;
    }

    //TODO: 1.21.4
    /*@Override
    public boolean canBreatheUnderwater() {
        return true;
    }*/

    // ENTITY DATA

    public int getVariant() {
        return Mth.clamp(this.entityData.get(VARIANT_ID), 0, 2);
    }

    public void setVariant(int variant) {
        this.entityData.set(VARIANT_ID, variant);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(VARIANT_ID, 0);
        builder.define(HAS_EGG, false);
        builder.define(LAYING_EGG, false);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Variant", this.getVariant());
        compound.putBoolean("HasEgg", this.hasEgg());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setVariant(compound.getInt("Variant"));
        this.setHasEgg(compound.getBoolean("HasEgg"));
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        super.playStepSound(pos, state);
    }

    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }

    private <T extends Tortoise> PlayState predicate(final AnimationState<T> event) {
        if (this.isInSittingPose()) {
            event.getController().setAnimation(SIT);
            return PlayState.CONTINUE;
        } else if (this.isLayingEgg())  {
            event.getController().setAnimation(DIG);
            return PlayState.CONTINUE;
        } else if (this.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6) {
            event.getController().setAnimation(WALK);
            if (this.isBaby()) {
                event.getController().setAnimationSpeed(2.0D);
            } else {
                event.getController().setAnimationSpeed(1.3D);
            }
            return PlayState.CONTINUE;
        } else {
            event.getController().setAnimation(IDLE);
            return PlayState.CONTINUE;
        }
    }

    private <T extends Tortoise> PlayState hidePredicate(final AnimationState<T> event) {
        if( this.canHide()) {
            event.getController().setAnimation(HIDE);
            return PlayState.CONTINUE;
        }
        event.getController().forceAnimationReset();

        return PlayState.STOP;
    }

    private <T extends Tortoise> PlayState hurtPredicate(final AnimationState<T> event) {
        if(this.hurtTime > 0) {
            event.getController().setAnimation(HURT);
            return PlayState.CONTINUE;
        }
        event.getController().forceAnimationReset();
        
        return PlayState.STOP;
    }

    @Override
    public void registerControllers(final AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 5, this::predicate));
        controllers.add(new AnimationController<>(this, "hurtController", 5, this::hurtPredicate));
        controllers.add(new AnimationController<>(this, "hideController", 0, this::hidePredicate).setSoundKeyframeHandler(this::soundListener));
    }

    private void soundListener(SoundKeyframeEvent<Tortoise> event) {
        Tortoise animatable = event.getAnimatable();
        if (animatable.level().isClientSide) {
            if (event.getKeyframeData().getSound().equals("hide")) {
                animatable.level().playLocalSound(animatable.getX(), animatable.getY(), animatable.getZ(), NaturalistSoundEvents.TORTOISE_HIDE.get(), animatable.getSoundSource(), 0.5F, 1.0F, false);
            }
            if (event.getKeyframeData().getSound().equals("thud")) {
                animatable.level().playLocalSound(animatable.getX(), animatable.getY(), animatable.getZ(), NaturalistSoundEvents.TORTOISE_THUD.get(), animatable.getSoundSource(), 0.5F, 1.0F, false);
            }
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
    public boolean isLayingEgg() {
        return this.entityData.get(LAYING_EGG);
    }

    @Override
    public void setLayingEgg(boolean isLayingEgg) {
        this.entityData.set(LAYING_EGG, isLayingEgg);
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
    public Block getEggBlock() {
        return NaturalistRegistry.TORTOISE_EGG;
    }

    @Override
    public TagKey<Block> getEggLayableBlockTag() {
        return NaturalistTags.BlockTags.TORTOISE_EGG_LAYABLE_ON;
    }

    @Override
    public boolean canFallInLove() {
        return super.canFallInLove() && !this.hasEgg();
    }

    @Override
    public void aiStep() {
        super.aiStep();
        BlockPos pos = this.blockPosition();
        if (this.isAlive() && this.isLayingEgg() && this.layEggCounter >= 1 && this.layEggCounter % 5 == 0 && this.level().getBlockState(pos.below()).is(this.getEggLayableBlockTag())) {
            this.level().levelEvent(2001, pos, Block.getId(this.level().getBlockState(pos.below())));
        }
    }
}
