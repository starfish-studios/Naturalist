package com.starfish_studios.naturalist.forge.event;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.NaturalistConfig;
import com.starfish_studios.naturalist.core.registry.NaturalistTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("deprecation")
@Mod.EventBusSubscriber(modid = Naturalist.MOD_ID)
public class RemovalEvents {

    private static final Map<String, Field> REMOVAL_FIELDS = new HashMap<>();

    static {
        for (Field field : NaturalistConfig.class.getFields()) {
            if (Modifier.isStatic(field.getModifiers())
                    && field.getType() == boolean.class
                    && field.getName().endsWith("Removed")) {
                REMOVAL_FIELDS.put(field.getName(), field);
            }
        }
    }

    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();
        Level level = entity.level();
        if (level.isClientSide) return;

        if (shouldRemoveVanillaFarmAnimal(entity, entity.blockPosition()) || shouldRemoveNaturalistMob(entity)) {
            entity.remove(RemovalReason.DISCARDED);
        }
    }

    private static boolean shouldRemoveVanillaFarmAnimal(LivingEntity entity, BlockPos pos) {
        EntityType<?> type = entity.getType();
        if (!(type == EntityType.COW || type == EntityType.PIG || type == EntityType.SHEEP || type == EntityType.CHICKEN)) {
            return false;
        }

        if (NaturalistConfig.removeSwampFarmAnimals && entity.level().getBiome(pos).is(NaturalistTags.Biomes.REMOVE_SWAMP_FARM_ANIMALS)) {
            return true;
        }

        return NaturalistConfig.removeSavannaFarmAnimals && entity.level().getBiome(pos).is(NaturalistTags.Biomes.REMOVE_SAVANNA_FARM_ANIMALS);
    }

    private static boolean shouldRemoveNaturalistMob(LivingEntity entity) {
        EntityType<?> type = entity.getType();
        if (!type.is(NaturalistTags.EntityTypes.NATURALIST_ENTITIES)) {
            return false;
        }

        ResourceLocation id = BuiltInRegistries.ENTITY_TYPE.getKey(type);
        if ("caterpillar".equals(id.getPath())) {
            return false;
        }

        String configFieldName = toCamelCase(id.getPath()) + "Removed";
        Field field = REMOVAL_FIELDS.get(configFieldName);
        if (field == null) {
            return false;
        }

        try {
            return field.getBoolean(null);
        } catch (IllegalAccessException e) {
            return false;
        }
    }

    private static String toCamelCase(String input) {
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

