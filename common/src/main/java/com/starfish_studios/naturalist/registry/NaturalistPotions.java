package com.starfish_studios.naturalist.registry;

import com.starfish_studios.naturalist.platform.CommonPlatformHelper;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.alchemy.Potion;

public class NaturalistPotions {
    public static final Holder<Potion> FOREST_DASHER = CommonPlatformHelper.registerPotion("forest_dasher", new Potion("forest_dasher", new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 400, 1), new MobEffectInstance(MobEffects.WEAKNESS, 400, 0)));
    public static final Holder<Potion> LONG_FOREST_DASHER = CommonPlatformHelper.registerPotion("long_forest_dasher", new Potion("forest_dasher", new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 800, 1), new MobEffectInstance(MobEffects.WEAKNESS, 800, 0)));
    public static final Holder<Potion> STRONG_FOREST_DASHER = CommonPlatformHelper.registerPotion("strong_forest_dasher", new Potion("forest_dasher", new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 400, 2), new MobEffectInstance(MobEffects.WEAKNESS, 400, 1)));

    public static void init() {}
}
