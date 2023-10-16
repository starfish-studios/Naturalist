package com.starfish_studios.naturalist.common.entity;

import com.starfish_studios.naturalist.core.registry.*;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.AbstractSchoolingFish;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class Bass extends AbstractSchoolingFish implements GeoEntity {
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    public Bass(EntityType<? extends AbstractSchoolingFish> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public int getMaxSchoolSize() {
        return 5;
    }

    @Override
    public ItemStack getBucketItemStack() {
        return new ItemStack(NaturalistItems.BASS_BUCKET.get());
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.SALMON_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SALMON_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.SALMON_HURT;
    }

    @Override
    protected SoundEvent getFlopSound() {
        return NaturalistSoundEvents.BASS_FLOP.get();
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }

    protected <E extends Bass> PlayState predicate(final AnimationState<E> event) {
        if (!this.isInWater()) {
            event.getController().setAnimation(RawAnimation.begin().thenLoop("bass.flop"));
        } else {
            event.getController().setAnimation(RawAnimation.begin().thenLoop("bass.swim"));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(final AnimatableManager.ControllerRegistrar controllers) {
        // TODO: this was 5
        // data.setResetSpeedInTicks(5);
        controllers.add(new AnimationController<>(this, "controller", 5, this::predicate));
    }

}
