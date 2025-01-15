package com.starfish_studios.naturalist.fabric;

import com.google.common.base.Preconditions;
import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.common.entity.*;
import com.starfish_studios.naturalist.core.registry.fabric.NaturalistConfigFabric;
import com.starfish_studios.naturalist.registry.NaturalistRegistry;
import com.starfish_studios.naturalist.registry.NaturalistEntityTypes;
import com.starfish_studios.naturalist.registry.NaturalistTags;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBiomeTags;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.List;

public class NaturalistFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        AutoConfig.register(NaturalistConfigFabric.class, GsonConfigSerializer::new);
        Naturalist.init();
        addSpawns();
        addFeatures();
        registerEntityAttributes();
        Naturalist.registerCompostables();
        Naturalist.registerSpawnPlacements();
        Naturalist.registerDispenserBehaviors();
    }


    public void addFeatures() {
        BiomeModifications.addFeature(
                (biomeSelector) -> biomeSelector.getBiomeKey().equals(Biomes.SWAMP),
                GenerationStep.Decoration.VEGETAL_DECORATION,
                getPlacedFeatureKey("patch_cattail")
        );
        BiomeModifications.addFeature(
                (biomeSelector) -> biomeSelector.getBiomeKey().equals(Biomes.SWAMP),
                GenerationStep.Decoration.RAW_GENERATION,
                getPlacedFeatureKey("swamp_mud")
        );
        BiomeModifications.addFeature(
                (biomeSelector) -> biomeSelector.getBiomeKey().equals(Biomes.SWAMP),
                GenerationStep.Decoration.VEGETAL_DECORATION,
                getPlacedFeatureKey("patch_duckweed")
        );
        BiomeModifications.addFeature(
                (biomeSelector) -> biomeSelector.getBiomeKey().equals(Biomes.SAVANNA),
                GenerationStep.Decoration.LAKES,
                getPlacedFeatureKey("savanna_lake")
        );
    }


    private ResourceKey<PlacedFeature> getPlacedFeatureKey(String key) {
        return ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, key));
    }


    void registerEntityAttributes() {
        FabricDefaultAttributeRegistry.register(NaturalistEntityTypes.SNAIL, Snail.createAttributes());
        FabricDefaultAttributeRegistry.register(NaturalistEntityTypes.BEAR, Bear.createAttributes());
        FabricDefaultAttributeRegistry.register(NaturalistEntityTypes.BUTTERFLY, Butterfly.createAttributes());
        // FabricDefaultAttributeRegistry.register(NaturalistEntityTypes.MOTH, Moth.createAttributes());
        FabricDefaultAttributeRegistry.register(NaturalistEntityTypes.FIREFLY, Firefly.createAttributes());
        FabricDefaultAttributeRegistry.register(NaturalistEntityTypes.SNAKE, Snake.createAttributes());
        FabricDefaultAttributeRegistry.register(NaturalistEntityTypes.CORAL_SNAKE, Snake.createAttributes());
        FabricDefaultAttributeRegistry.register(NaturalistEntityTypes.RATTLESNAKE, Snake.createAttributes());
        FabricDefaultAttributeRegistry.register(NaturalistEntityTypes.DEER, Deer.createAttributes());


        // BIRDS

        FabricDefaultAttributeRegistry.register(NaturalistEntityTypes.BLUEJAY, Bird.createAttributes());
        FabricDefaultAttributeRegistry.register(NaturalistEntityTypes.CANARY, Bird.createAttributes());
        FabricDefaultAttributeRegistry.register(NaturalistEntityTypes.CARDINAL, Bird.createAttributes());
        FabricDefaultAttributeRegistry.register(NaturalistEntityTypes.ROBIN, Bird.createAttributes());
        FabricDefaultAttributeRegistry.register(NaturalistEntityTypes.FINCH, Bird.createAttributes());
        FabricDefaultAttributeRegistry.register(NaturalistEntityTypes.SPARROW, Bird.createAttributes());


        FabricDefaultAttributeRegistry.register(NaturalistEntityTypes.CATERPILLAR, Caterpillar.createAttributes());
        FabricDefaultAttributeRegistry.register(NaturalistEntityTypes.RHINO, Rhino.createAttributes());
        FabricDefaultAttributeRegistry.register(NaturalistEntityTypes.LION, Lion.createAttributes());
        FabricDefaultAttributeRegistry.register(NaturalistEntityTypes.ELEPHANT, Elephant.createAttributes());
        FabricDefaultAttributeRegistry.register(NaturalistEntityTypes.ZEBRA, AbstractHorse.createBaseHorseAttributes());
        FabricDefaultAttributeRegistry.register(NaturalistEntityTypes.GIRAFFE, Giraffe.createAttributes());
        FabricDefaultAttributeRegistry.register(NaturalistEntityTypes.HIPPO, Hippo.createAttributes());
        FabricDefaultAttributeRegistry.register(NaturalistEntityTypes.VULTURE, Vulture.createAttributes());
        FabricDefaultAttributeRegistry.register(NaturalistEntityTypes.BOAR, Boar.createAttributes());
        FabricDefaultAttributeRegistry.register(NaturalistEntityTypes.DRAGONFLY, Dragonfly.createAttributes());
        FabricDefaultAttributeRegistry.register(NaturalistEntityTypes.CATFISH, Catfish.createAttributes());
        FabricDefaultAttributeRegistry.register(NaturalistEntityTypes.ALLIGATOR, Alligator.createAttributes());
        FabricDefaultAttributeRegistry.register(NaturalistEntityTypes.BASS, AbstractFish.createAttributes());
        FabricDefaultAttributeRegistry.register(NaturalistEntityTypes.LIZARD, Lizard.createAttributes());
        FabricDefaultAttributeRegistry.register(NaturalistEntityTypes.LIZARD_TAIL, LizardTail.createAttributes());
        FabricDefaultAttributeRegistry.register(NaturalistEntityTypes.TORTOISE, Tortoise.createAttributes());
        FabricDefaultAttributeRegistry.register(NaturalistEntityTypes.DUCK, Duck.createAttributes());

        //FabricDefaultAttributeRegistry.register(NaturalistEntityTypes.TOUCAN, Toucan.createAttributes());
        //FabricDefaultAttributeRegistry.register(NaturalistEntityTypes.CRAB, Crab.createAttributes());
        //FabricDefaultAttributeRegistry.register(NaturalistEntityTypes.MOOSE, Moose.createAttributes());
        //FabricDefaultAttributeRegistry.register(NaturalistEntityTypes.CAPYBARA, Capybara.createAttributes());
        //FabricDefaultAttributeRegistry.register(NaturalistEntityTypes.EMPEROR_PENGUIN, EmperorPenguin.createAttributes());
    }

    void addSpawns() {
        NaturalistConfigFabric config = AutoConfig.getConfigHolder(NaturalistConfigFabric.class).getConfig();
        addMobSpawn(NaturalistTags.Biomes.HAS_BEAR, MobCategory.CREATURE, NaturalistEntityTypes.BEAR, config.bearSpawnWeight, 1, 2);
        addMobSpawn(NaturalistTags.Biomes.HAS_DEER, MobCategory.CREATURE, NaturalistEntityTypes.DEER, config.deerSpawnWeight, 1, 3);
        addMobSpawn(NaturalistTags.Biomes.HAS_SNAIL, MobCategory.CREATURE, NaturalistEntityTypes.SNAIL, config.snailSpawnWeight, 1, 3);

        addMobSpawn(NaturalistTags.Biomes.HAS_FIREFLY, MobCategory.AMBIENT, NaturalistEntityTypes.FIREFLY, config.fireflySpawnWeight, 2, 3);


        addMobSpawn(NaturalistTags.Biomes.HAS_BUTTERFLY, MobCategory.CREATURE, NaturalistEntityTypes.BUTTERFLY, config.butterflySpawnWeight, 3, 5);
        addMobSpawn(NaturalistTags.Biomes.HAS_SNAKE, MobCategory.CREATURE, NaturalistEntityTypes.SNAKE, config.snakeSpawnWeight, 1, 1);
        addMobSpawn(NaturalistTags.Biomes.HAS_RATTLESNAKE, MobCategory.CREATURE, NaturalistEntityTypes.RATTLESNAKE, config.rattlesnakeSpawnWeight, 1, 1);
        addMobSpawn(NaturalistTags.Biomes.HAS_CORAL_SNAKE, MobCategory.CREATURE, NaturalistEntityTypes.CORAL_SNAKE, config.coralSnakeSpawnWeight, 1, 1);


        addMobSpawn(NaturalistTags.Biomes.HAS_BLUEJAY, MobCategory.CREATURE, NaturalistEntityTypes.BLUEJAY, config.bluejaySpawnWeight, 1, 4);
        addMobSpawn(NaturalistTags.Biomes.HAS_CANARY, MobCategory.CREATURE, NaturalistEntityTypes.CANARY, config.canarySpawnWeight, 1, 4);
        addMobSpawn(NaturalistTags.Biomes.HAS_CARDINAL, MobCategory.CREATURE, NaturalistEntityTypes.CARDINAL, config.cardinalSpawnWeight, 1, 4);
        addMobSpawn(NaturalistTags.Biomes.HAS_ROBIN, MobCategory.CREATURE, NaturalistEntityTypes.ROBIN, config.robinSpawnWeight, 1, 4);
        addMobSpawn(NaturalistTags.Biomes.HAS_FINCH, MobCategory.CREATURE, NaturalistEntityTypes.FINCH, config.finchSpawnWeight, 1, 4);
        addMobSpawn(NaturalistTags.Biomes.HAS_SPARROW, MobCategory.CREATURE, NaturalistEntityTypes.SPARROW, config.sparrowSpawnWeight, 1, 4);

        addMobSpawn(BiomeTags.IS_FOREST, MobCategory.CREATURE, EntityType.RABBIT, config.forestRabbitSpawnWeight, 2, 3);
        addMobSpawn(BiomeTags.IS_FOREST, MobCategory.CREATURE, EntityType.FOX, config.forestFoxSpawnWeight, 2, 4);
        addMobSpawn(NaturalistTags.Biomes.HAS_RHINO, MobCategory.CREATURE, NaturalistEntityTypes.RHINO, config.rhinoSpawnWeight, 1, 3);
        addMobSpawn(NaturalistTags.Biomes.HAS_LION, MobCategory.CREATURE, NaturalistEntityTypes.LION, config.lionSpawnWeight, 3, 5);
        addMobSpawn(NaturalistTags.Biomes.HAS_ELEPHANT, MobCategory.CREATURE, NaturalistEntityTypes.ELEPHANT, config.elephantSpawnWeight, 2, 3);
        addMobSpawn(NaturalistTags.Biomes.HAS_ZEBRA, MobCategory.CREATURE, NaturalistEntityTypes.ZEBRA, config.zebraSpawnWeight, 2, 3);
        addMobSpawn(NaturalistTags.Biomes.HAS_GIRAFFE, MobCategory.CREATURE, NaturalistEntityTypes.GIRAFFE, config.giraffeSpawnWeight, 2, 4);
        addMobSpawn(NaturalistTags.Biomes.HAS_HIPPO, MobCategory.CREATURE, NaturalistEntityTypes.HIPPO, config.hippoSpawnWeight, 3, 4);
        addMobSpawn(NaturalistTags.Biomes.HAS_VULTURE, MobCategory.CREATURE, NaturalistEntityTypes.VULTURE, config.vultureSpawnWeight, 2, 4);
        addMobSpawn(NaturalistTags.Biomes.HAS_BOAR, MobCategory.CREATURE, NaturalistEntityTypes.BOAR, config.boarSpawnWeight, 4, 4);

        addMobSpawn(NaturalistTags.Biomes.HAS_DRAGONFLY, MobCategory.CREATURE, NaturalistEntityTypes.DRAGONFLY, config.dragonflySpawnWeight, 2, 3);
        addMobSpawn(NaturalistTags.Biomes.HAS_CATFISH, MobCategory.WATER_AMBIENT, NaturalistEntityTypes.CATFISH, config.catfishSpawnWeight, 1, 1);
        addMobSpawn(NaturalistTags.Biomes.HAS_ALLIGATOR, MobCategory.CREATURE, NaturalistEntityTypes.ALLIGATOR, config.alligatorSpawnWeight, 1, 2);
        addMobSpawn(NaturalistTags.Biomes.HAS_BASS, MobCategory.WATER_AMBIENT, NaturalistEntityTypes.BASS, config.bassSpawnWeight, 4, 4);
        addMobSpawn(NaturalistTags.Biomes.HAS_LIZARD, MobCategory.CREATURE, NaturalistEntityTypes.LIZARD, config.lizardSpawnWeight, 1, 2);
        addMobSpawn(NaturalistTags.Biomes.HAS_TORTOISE, MobCategory.CREATURE, NaturalistEntityTypes.TORTOISE, config.tortoiseSpawnWeight, 1, 3);
        addMobSpawn(NaturalistTags.Biomes.HAS_DUCK, MobCategory.CREATURE, NaturalistEntityTypes.DUCK, config.duckSpawnWeight, 1, 3);
        if (config.removeSavannaFarmAnimals) {
            removeSpawn(BiomeTags.IS_SAVANNA, List.of(EntityType.SHEEP, EntityType.PIG, EntityType.CHICKEN, EntityType.COW));
        }
        if (config.removeSwampFarmAnimals) {
            removeSpawn(ConventionalBiomeTags.SWAMP, List.of(EntityType.SHEEP, EntityType.PIG, EntityType.CHICKEN, EntityType.COW));
        }
        if (config.removeForestPigs) {
            removeSpawn(ConventionalBiomeTags.FOREST, List.of(EntityType.PIG));
        }
    }

    void addMobSpawn(TagKey<Biome> tag, MobCategory mobCategory, EntityType<?> entityType, int weight, int minGroupSize, int maxGroupSize) {
        BiomeModifications.addSpawn(biomeSelector -> biomeSelector.hasTag(tag), mobCategory, entityType, weight, minGroupSize, maxGroupSize);
    }

    void removeSpawn(TagKey<Biome> tag, List<EntityType<?>> entityTypes) {
        entityTypes.forEach(entityType -> {
            ResourceLocation id = BuiltInRegistries.ENTITY_TYPE.getKey(entityType);
            Preconditions.checkState(BuiltInRegistries.ENTITY_TYPE.containsKey(id), "Unregistered entity type: %s", entityType);
            BiomeModifications.create(id).add(ModificationPhase.REMOVALS, biomeSelector -> biomeSelector.hasTag(tag), context -> context.getSpawnSettings().removeSpawnsOfEntityType(entityType));
        });
        
    }
}
