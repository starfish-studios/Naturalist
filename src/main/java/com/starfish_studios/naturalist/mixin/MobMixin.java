package com.starfish_studios.naturalist.mixin;

import com.starfish_studios.naturalist.NaturalistConfig;
import com.starfish_studios.naturalist.common.entity.Firefly;
import com.starfish_studios.naturalist.core.registry.NaturalistTags;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("deprecation")
@Mixin(Mob.class)
public abstract class MobMixin extends LivingEntity {

    protected MobMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "doHurtTarget", at = @At("HEAD"))
    private void naturalist$onDoHurtTarget(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (BuiltInRegistries.ENTITY_TYPE.getKey(this.getType()).equals(BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.FROG))
                && entity instanceof Firefly) {
            this.addEffect(new MobEffectInstance(MobEffects.GLOWING, 60));
        }
    }

    @Inject(method = "checkDespawn", at = @At("HEAD"), cancellable = true)
    private void naturalist$checkDespawnMixin(CallbackInfo ci) {
        if (naturalist$shouldRemoveVanillaFarmAnimal() || naturalist$shouldRemoveNaturalistMob()) {
            this.remove(Entity.RemovalReason.DISCARDED);
            ci.cancel();
        }
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void naturalist$removeOnTick(CallbackInfo ci) {
        if (naturalist$shouldRemoveVanillaFarmAnimal() || naturalist$shouldRemoveNaturalistMob()) {
            this.remove(Entity.RemovalReason.DISCARDED);
            ci.cancel();
        }
    }

    @Unique
    private boolean naturalist$shouldRemoveVanillaFarmAnimal() {
        EntityType<?> type = this.getType();
        if (!(type == EntityType.COW || type == EntityType.PIG || type == EntityType.SHEEP || type == EntityType.CHICKEN)) {
            return false;
        }

        if (NaturalistConfig.removeSwampFarmAnimals && this.level().getBiome(this.blockPosition()).is(NaturalistTags.Biomes.REMOVE_SWAMP_FARM_ANIMALS)) {
            return true;
        }

        if (NaturalistConfig.removeSavannaFarmAnimals && this.level().getBiome(this.blockPosition()).is(NaturalistTags.Biomes.REMOVE_SAVANNA_FARM_ANIMALS)) {
            return true;
        }

        return false;
    }

    @Unique
    private boolean naturalist$shouldRemoveNaturalistMob() {
        if (!this.getType().is(NaturalistTags.EntityTypes.NATURALIST_ENTITIES)) {
            return false;
        }

        if (BuiltInRegistries.ENTITY_TYPE.getKey(this.getType()).getPath().equals("caterpillar")) {
            return false;
        }

        String mobName = naturalist$toCamelCase(BuiltInRegistries.ENTITY_TYPE.getKey(this.getType()).getPath());
        String configFieldName = mobName + "Removed";

        try {
            return NaturalistConfig.class.getField(configFieldName).getBoolean(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Unique
    private String naturalist$toCamelCase(String input) {
        StringBuilder camelCase = new StringBuilder();
        boolean capitalizeNext = false;

        for (char c : input.toCharArray()) {
            if (c == '_') {
                capitalizeNext = true;
            } else {
                camelCase.append(capitalizeNext ? Character.toUpperCase(c) : c);
                capitalizeNext = false;
            }
        }

        return camelCase.toString();
    }

}
