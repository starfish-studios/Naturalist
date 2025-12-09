package com.starfish_studios.naturalist.fabric;

import com.google.common.base.Preconditions;
import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.NaturalistConfig;
import com.starfish_studios.naturalist.core.registry.NaturalistEntityAttributes;
import com.starfish_studios.naturalist.core.registry.NaturalistEntityTypes;
import com.starfish_studios.naturalist.core.registry.NaturalistTags;
import com.starfish_studios.naturalist.core.registry.fabric.NaturalistCreativeModeFabric;
import eu.midnightdust.lib.config.MidnightConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;

import java.util.List;

public class NaturalistFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Naturalist.init();

        MidnightConfig.init("naturalist", NaturalistConfig.class);

        addSpawns();
        removeVanillaSpawns();
        NaturalistEntityAttributes.registerFabric();
        Naturalist.registerBrewingRecipes();
        Naturalist.registerCompostables();
        Naturalist.registerSpawnPlacements();
        Naturalist.registerDispenserBehaviors();

        NaturalistCreativeModeFabric.init();
    }

    void addSpawns() {
        addMobSpawn(NaturalistTags.Biomes.HAS_BEAR, NaturalistTags.Biomes.BLACKLIST_BEAR, MobCategory.CREATURE, NaturalistEntityTypes.BEAR.get(), NaturalistConfig.bearSpawnWeight, 1, 2);
        addMobSpawn(NaturalistTags.Biomes.HAS_DEER, NaturalistTags.Biomes.BLACKLIST_DEER, MobCategory.CREATURE, NaturalistEntityTypes.DEER.get(), NaturalistConfig.deerSpawnWeight, 1, 3);
        addMobSpawn(NaturalistTags.Biomes.HAS_SNAIL, NaturalistTags.Biomes.BLACKLIST_SNAIL, MobCategory.CREATURE, NaturalistEntityTypes.SNAIL.get(), NaturalistConfig.snailSpawnWeight, 1, 3);

        addMobSpawn(NaturalistTags.Biomes.HAS_FIREFLY, NaturalistTags.Biomes.BLACKLIST_FIREFLY, MobCategory.AMBIENT, NaturalistEntityTypes.FIREFLY.get(), NaturalistConfig.fireflySpawnWeight, 2, 3);


        addMobSpawn(NaturalistTags.Biomes.HAS_BUTTERFLY, NaturalistTags.Biomes.BLACKLIST_BUTTERFLY, MobCategory.AMBIENT, NaturalistEntityTypes.BUTTERFLY.get(), NaturalistConfig.butterflySpawnWeight, 3, 5);
        addMobSpawn(NaturalistTags.Biomes.HAS_SNAKE, NaturalistTags.Biomes.BLACKLIST_SNAKE, MobCategory.CREATURE, NaturalistEntityTypes.SNAKE.get(), NaturalistConfig.snakeSpawnWeight, 1, 1);
        addMobSpawn(NaturalistTags.Biomes.HAS_RATTLESNAKE, NaturalistTags.Biomes.BLACKLIST_RATTLESNAKE, MobCategory.CREATURE, NaturalistEntityTypes.RATTLESNAKE.get(), NaturalistConfig.rattlesnakeSpawnWeight, 1, 1);
        addMobSpawn(NaturalistTags.Biomes.HAS_CORAL_SNAKE, NaturalistTags.Biomes.BLACKLIST_CORAL_SNAKE, MobCategory.CREATURE, NaturalistEntityTypes.CORAL_SNAKE.get(), NaturalistConfig.coralSnakeSpawnWeight, 1, 1);


        addMobSpawn(NaturalistTags.Biomes.HAS_BLUEJAY, NaturalistTags.Biomes.BLACKLIST_BLUEJAY, MobCategory.CREATURE, NaturalistEntityTypes.BLUEJAY.get(), NaturalistConfig.bluejaySpawnWeight, 1, 4);
        addMobSpawn(NaturalistTags.Biomes.HAS_CANARY, NaturalistTags.Biomes.BLACKLIST_CANARY, MobCategory.CREATURE, NaturalistEntityTypes.CANARY.get(), NaturalistConfig.canarySpawnWeight, 1, 4);
        addMobSpawn(NaturalistTags.Biomes.HAS_CARDINAL, NaturalistTags.Biomes.BLACKLIST_CARDINAL, MobCategory.CREATURE, NaturalistEntityTypes.CARDINAL.get(), NaturalistConfig.cardinalSpawnWeight, 1, 4);
        addMobSpawn(NaturalistTags.Biomes.HAS_ROBIN, NaturalistTags.Biomes.BLACKLIST_ROBIN, MobCategory.CREATURE, NaturalistEntityTypes.ROBIN.get(), NaturalistConfig.robinSpawnWeight, 1, 4);
        addMobSpawn(NaturalistTags.Biomes.HAS_FINCH, NaturalistTags.Biomes.BLACKLIST_FINCH, MobCategory.CREATURE, NaturalistEntityTypes.FINCH.get(), NaturalistConfig.finchSpawnWeight, 1, 4);
        addMobSpawn(NaturalistTags.Biomes.HAS_SPARROW, NaturalistTags.Biomes.BLACKLIST_SPARROW, MobCategory.CREATURE, NaturalistEntityTypes.SPARROW.get(), NaturalistConfig.sparrowSpawnWeight, 1, 4);

        addMobSpawn(BiomeTags.IS_FOREST, null, MobCategory.CREATURE, EntityType.RABBIT, NaturalistConfig.forestRabbitSpawnWeight, 2, 3);
        addMobSpawn(BiomeTags.IS_FOREST, null, MobCategory.CREATURE, EntityType.FOX, NaturalistConfig.forestFoxSpawnWeight, 2, 4);
        addMobSpawn(NaturalistTags.Biomes.HAS_RHINO, NaturalistTags.Biomes.BLACKLIST_RHINO, MobCategory.CREATURE, NaturalistEntityTypes.RHINO.get(), NaturalistConfig.rhinoSpawnWeight, 1, 3);
        addMobSpawn(NaturalistTags.Biomes.HAS_LION, NaturalistTags.Biomes.BLACKLIST_LION, MobCategory.CREATURE, NaturalistEntityTypes.LION.get(), NaturalistConfig.lionSpawnWeight, 3, 5);
        addMobSpawn(NaturalistTags.Biomes.HAS_ELEPHANT, NaturalistTags.Biomes.BLACKLIST_ELEPHANT, MobCategory.CREATURE, NaturalistEntityTypes.ELEPHANT.get(), NaturalistConfig.elephantSpawnWeight, 2, 3);
        addMobSpawn(NaturalistTags.Biomes.HAS_ZEBRA, NaturalistTags.Biomes.BLACKLIST_ZEBRA, MobCategory.CREATURE, NaturalistEntityTypes.ZEBRA.get(), NaturalistConfig.zebraSpawnWeight, 2, 3);
        addMobSpawn(NaturalistTags.Biomes.HAS_GIRAFFE, NaturalistTags.Biomes.BLACKLIST_GIRAFFE, MobCategory.CREATURE, NaturalistEntityTypes.GIRAFFE.get(), NaturalistConfig.giraffeSpawnWeight, 2, 4);
        addMobSpawn(NaturalistTags.Biomes.HAS_HIPPO, NaturalistTags.Biomes.BLACKLIST_HIPPO, MobCategory.CREATURE, NaturalistEntityTypes.HIPPO.get(), NaturalistConfig.hippoSpawnWeight, 3, 4);
        addMobSpawn(NaturalistTags.Biomes.HAS_VULTURE, NaturalistTags.Biomes.BLACKLIST_VULTURE, MobCategory.CREATURE, NaturalistEntityTypes.VULTURE.get(), NaturalistConfig.vultureSpawnWeight, 2, 4);
        addMobSpawn(NaturalistTags.Biomes.HAS_BOAR, NaturalistTags.Biomes.BLACKLIST_BOAR, MobCategory.CREATURE, NaturalistEntityTypes.BOAR.get(), NaturalistConfig.boarSpawnWeight, 4, 4);

        addMobSpawn(NaturalistTags.Biomes.HAS_DRAGONFLY, NaturalistTags.Biomes.BLACKLIST_DRAGONFLY, MobCategory.AMBIENT, NaturalistEntityTypes.DRAGONFLY.get(), NaturalistConfig.dragonflySpawnWeight, 2, 3);
        addMobSpawn(NaturalistTags.Biomes.HAS_CATFISH, NaturalistTags.Biomes.BLACKLIST_CATFISH, MobCategory.WATER_AMBIENT, NaturalistEntityTypes.CATFISH.get(), NaturalistConfig.catfishSpawnWeight, 1, 1);
        addMobSpawn(NaturalistTags.Biomes.HAS_ALLIGATOR, NaturalistTags.Biomes.BLACKLIST_ALLIGATOR, MobCategory.CREATURE, NaturalistEntityTypes.ALLIGATOR.get(), NaturalistConfig.alligatorSpawnWeight, 1, 2);
        addMobSpawn(NaturalistTags.Biomes.HAS_BASS, NaturalistTags.Biomes.BLACKLIST_BASS, MobCategory.WATER_AMBIENT, NaturalistEntityTypes.BASS.get(), NaturalistConfig.bassSpawnWeight, 4, 4);
        addMobSpawn(NaturalistTags.Biomes.HAS_LIZARD, NaturalistTags.Biomes.BLACKLIST_LIZARD, MobCategory.CREATURE, NaturalistEntityTypes.LIZARD.get(), NaturalistConfig.lizardSpawnWeight, 1, 2);
        addMobSpawn(NaturalistTags.Biomes.HAS_TORTOISE, NaturalistTags.Biomes.BLACKLIST_TORTOISE, MobCategory.CREATURE, NaturalistEntityTypes.TORTOISE.get(), NaturalistConfig.tortoiseSpawnWeight, 1, 3);
        addMobSpawn(NaturalistTags.Biomes.HAS_DUCK, NaturalistTags.Biomes.BLACKLIST_DUCK, MobCategory.CREATURE, NaturalistEntityTypes.DUCK.get(), NaturalistConfig.duckSpawnWeight, 1, 3);
    }

    void addMobSpawn(TagKey<Biome> tag, TagKey<Biome> blacklistTag, MobCategory mobCategory, EntityType<?> entityType, int weight, int minGroupSize, int maxGroupSize) {
        if (weight <= 0) {
            ResourceLocation id = BuiltInRegistries.ENTITY_TYPE.getKey(entityType);
            ResourceLocation removalId = new ResourceLocation(id.getNamespace(), id.getPath() + "_removal");
            BiomeModifications.create(removalId).add(
                    ModificationPhase.REMOVALS,
                    biomeSelector -> biomeSelector.hasTag(tag) && (blacklistTag == null || !biomeSelector.hasTag(blacklistTag)),
                    context -> context.getSpawnSettings().removeSpawnsOfEntityType(entityType)
            );
            return;
        }
        BiomeModifications.addSpawn(
                biomeSelector -> biomeSelector.hasTag(tag) && (blacklistTag == null || !biomeSelector.hasTag(blacklistTag)),
                mobCategory,
                entityType,
                weight,
                minGroupSize,
                maxGroupSize
        );
    }

    void removeSpawn(TagKey<Biome> tag, List<EntityType<?>> entityTypes) {
        entityTypes.forEach(entityType -> {
            ResourceLocation id = BuiltInRegistries.ENTITY_TYPE.getKey(entityType);
            Preconditions.checkState(BuiltInRegistries.ENTITY_TYPE.containsKey(id), "Unregistered entity type: %s", entityType);
            BiomeModifications.create(id).add(ModificationPhase.REMOVALS, biomeSelector -> biomeSelector.hasTag(tag), context -> context.getSpawnSettings().removeSpawnsOfEntityType(entityType));
        });
        
    }

    void removeVanillaSpawns() {
        if (NaturalistConfig.removeSwampFarmAnimals) {
            removeSpawn(NaturalistTags.Biomes.REMOVE_SWAMP_FARM_ANIMALS, List.of(EntityType.COW, EntityType.PIG, EntityType.SHEEP, EntityType.CHICKEN));
        }
        if (NaturalistConfig.removeSavannaFarmAnimals) {
            removeSpawn(NaturalistTags.Biomes.REMOVE_SAVANNA_FARM_ANIMALS, List.of(EntityType.COW, EntityType.PIG, EntityType.SHEEP, EntityType.CHICKEN));
        }
    }
}



