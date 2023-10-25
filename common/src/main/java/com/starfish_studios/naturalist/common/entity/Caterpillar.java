package com.starfish_studios.naturalist.common.entity;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.common.block.ChrysalisBlock;
import com.starfish_studios.naturalist.common.entity.core.Catchable;
import com.starfish_studios.naturalist.common.entity.core.ClimbingAnimal;
import com.starfish_studios.naturalist.core.registry.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class Caterpillar extends ClimbingAnimal implements GeoEntity, Catchable {
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
    private static final EntityDataAccessor<Boolean> FROM_HAND;

    public Caterpillar(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 5.0D).add(Attributes.MOVEMENT_SPEED, 0.1F);
    }


    @Override
    @NotNull
    public MobType getMobType() {
        return MobType.ARTHROPOD;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new CocoonGoal(this, 1.0F, 5, 2));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0F));
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @javax.annotation.Nullable SpawnGroupData pSpawnData, @javax.annotation.Nullable CompoundTag pDataTag) {
        this.setAge(0);
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob mob) {
        return null;
    }

    @Override
    protected float getClimbSpeedMultiplier() {
        return 0.5F;
    }

    @Override
    public boolean isFood(ItemStack pStack) {
        return this.isBaby() && pStack.is(ItemTags.FLOWERS);
    }

    @Override
    public float getScale() {
        return 1.0f;
    }

    @Override
    public boolean causeFallDamage(float pFallDistance, float pMultiplier, DamageSource pSource) {
        return false;
    }

    @Override
    protected void checkFallDamage(double pY, boolean pOnGround, BlockState pState, BlockPos pPos) {
    }
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }

    protected <E extends Caterpillar> PlayState predicate(final AnimationState<E> event) {
        if (this.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6) {
            {
                event.getController().setAnimation(RawAnimation.begin().thenLoop("crawl"));
                return PlayState.CONTINUE;
            }
        } else {
            event.getController().setAnimation(RawAnimation.begin().thenLoop("idle"));
            return PlayState.CONTINUE;
        }
    }

    @Override
    public void registerControllers(final AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 5, this::predicate));
    }

    public ItemStack getHandItemStack() {
        return new ItemStack(NaturalistItems.CATERPILLAR.get());
    }



    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(FROM_HAND, false);
    }

    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("FromHand", this.fromHand());
    }

    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setFromHand(compound.getBoolean("FromHand"));
    }

    public void saveToHandTag(ItemStack stack) {
        Catchable.saveDefaultDataToHandTag(this, stack);
        CompoundTag compoundTag = stack.getOrCreateTag();
        compoundTag.putInt("Age", this.getAge());

    }

    public void loadFromHandTag(CompoundTag tag) {
        Catchable.loadDefaultDataFromHandTag(this, tag);

        if (tag.contains("Age")) {
            this.setAge(tag.getInt("Age"));
        }
    }

    public boolean fromHand() {
        return this.entityData.get(FROM_HAND);
    }

    public void setFromHand(boolean fromHand) {
        this.entityData.set(FROM_HAND, fromHand);
    }

    public @NotNull InteractionResult mobInteract(Player player, InteractionHand hand) {
        return Catchable.catchAnimal(player, hand, this, true).orElse(super.mobInteract(player, hand));
    }

    public boolean requiresCustomPersistence() {
        return super.requiresCustomPersistence() || this.fromHand();
    }

    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    public ItemStack getCaughtItemStack() {
        return new ItemStack(NaturalistItems.CATERPILLAR.get());
    }

    @Override
    public SoundEvent getPickupSound() {
        return null;
    }


    private static class CocoonGoal extends MoveToBlockGoal {
        private final Caterpillar caterpillar;
        private Direction facing = Direction.NORTH;
        private BlockPos logPos = BlockPos.ZERO;

        public CocoonGoal(Caterpillar pMob, double pSpeedModifier, int pSearchRange, int pVerticalSearchRange) {
            super(pMob, pSpeedModifier, pSearchRange, pVerticalSearchRange);
            this.caterpillar = pMob;
        }

        @Override
        public boolean canUse() {
            return !caterpillar.isBaby() && super.canUse();
        }


        @Override
        protected boolean isValidTarget(LevelReader pLevel, BlockPos pPos) {
            if (pLevel.getBlockState(pPos).isAir()) {
                for (Direction direction : Direction.Plane.HORIZONTAL) {
                    if (pLevel.getBlockState(pPos.relative(direction)).is(BlockTags.LOGS) && pLevel.getBlockState(pPos.relative(direction).below()).is(BlockTags.LOGS)) {
                        this.facing = direction;
                        this.logPos = pPos.relative(direction);
                        return true;
                    }
                }
                return false;
            }
            return false;
        }

        @Override
        public void tick() {
            BlockPos targetPos = this.getMoveToTarget();
            if (!targetPos.closerToCenterThan(caterpillar.position(), this.acceptedDistance())) {
                ++this.tryTicks;
                if (this.shouldRecalculatePath()) {
                    caterpillar.getNavigation().moveTo(targetPos.getX() + 0.5D, targetPos.getY(), targetPos.getZ() + 0.5D, this.speedModifier);
                }
            } else {
                --this.tryTicks;
            }
            caterpillar.getLookControl().setLookAt(logPos.getX() + 0.5D, logPos.getY() + 1, logPos.getZ() + 0.5D, 10.0F, this.caterpillar.getMaxHeadXRot());
            Level level = caterpillar.level();
            if (this.isValidTarget(level, caterpillar.blockPosition())) {
                if (!level.isClientSide) {
                    ((ServerLevel) level).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, NaturalistBlocks.CHRYSALIS.get().defaultBlockState()), caterpillar.getX(), caterpillar.getY(), caterpillar.getZ(), 50, caterpillar.getBbWidth() / 4.0F, caterpillar.getBbHeight() / 4.0F, caterpillar.getBbWidth() / 4.0F, 0.05D);
                }
                caterpillar.discard();
                level.setBlockAndUpdate(caterpillar.blockPosition(), NaturalistBlocks.CHRYSALIS.get().defaultBlockState().setValue(ChrysalisBlock.FACING, facing));
                level.playSound(null, caterpillar.blockPosition(), SoundEvents.GRASS_PLACE, SoundSource.BLOCKS, 0.7F, 0.9F + level.random.nextFloat() * 0.2F);
            }
        }

        @Override
        protected void moveMobToBlock() {
            caterpillar.getNavigation().moveTo(logPos.getX() + 0.5D, logPos.getY() + 1.0D, logPos.getZ() + 0.5D, this.speedModifier);
        }

        @Override
        protected BlockPos getMoveToTarget() {
            return logPos.above();
        }
    }


    static {
        FROM_HAND = SynchedEntityData.defineId(Caterpillar.class, EntityDataSerializers.BOOLEAN);
    }
}