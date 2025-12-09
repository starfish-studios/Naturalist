package com.starfish_studios.naturalist.world.forge;

import com.mojang.serialization.Codec;
import com.starfish_studios.naturalist.NaturalistConfig;
import com.starfish_studios.naturalist.core.registry.NaturalistEntityTypes;
import com.starfish_studios.naturalist.core.registry.NaturalistTags;
import com.starfish_studios.naturalist.core.registry.forge.NaturalistBiomeModifiers;
import net.minecraft.core.Holder;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;
import org.jetbrains.annotations.NotNull;

public class AddAnimalsBiomeModifier implements BiomeModifier {
    @Override
    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (!phase.equals(Phase.ADD)) return;

        addMobSpawn(builder, biome, NaturalistTags.Biomes.HAS_ALLIGATOR, NaturalistTags.Biomes.BLACKLIST_ALLIGATOR, MobCategory.CREATURE, NaturalistEntityTypes.ALLIGATOR.get(), NaturalistConfig.alligatorSpawnWeight, NaturalistConfig.alligatorSpawnMinGroupSize, NaturalistConfig.alligatorSpawnMaxGroupSize);
        addMobSpawn(builder, biome, NaturalistTags.Biomes.HAS_BASS, NaturalistTags.Biomes.BLACKLIST_BASS, MobCategory.WATER_AMBIENT, NaturalistEntityTypes.BASS.get(), NaturalistConfig.bassSpawnWeight, NaturalistConfig.bassSpawnMinGroupSize, NaturalistConfig.bassSpawnMaxGroupSize);
        addMobSpawn(builder, biome, NaturalistTags.Biomes.HAS_BEAR, NaturalistTags.Biomes.BLACKLIST_BEAR, MobCategory.CREATURE, NaturalistEntityTypes.BEAR.get(), NaturalistConfig.bearSpawnWeight, NaturalistConfig.bearSpawnMinGroupSize, NaturalistConfig.bearSpawnMaxGroupSize);
        addMobSpawn(builder, biome, NaturalistTags.Biomes.HAS_BLUEJAY, NaturalistTags.Biomes.BLACKLIST_BLUEJAY, MobCategory.CREATURE, NaturalistEntityTypes.BLUEJAY.get(), NaturalistConfig.bluejaySpawnWeight, NaturalistConfig.bluejaySpawnMinGroupSize, NaturalistConfig.bluejaySpawnMaxGroupSize);
        addMobSpawn(builder, biome, NaturalistTags.Biomes.HAS_BOAR, NaturalistTags.Biomes.BLACKLIST_BOAR, MobCategory.CREATURE, NaturalistEntityTypes.BOAR.get(), NaturalistConfig.boarSpawnWeight, NaturalistConfig.boarSpawnMinGroupSize, NaturalistConfig.boarSpawnMaxGroupSize);
        addMobSpawn(builder, biome, NaturalistTags.Biomes.HAS_BUTTERFLY, NaturalistTags.Biomes.BLACKLIST_BUTTERFLY, MobCategory.CREATURE, NaturalistEntityTypes.BUTTERFLY.get(), NaturalistConfig.butterflySpawnWeight, NaturalistConfig.butterflySpawnMinGroupSize, NaturalistConfig.butterflySpawnMaxGroupSize);
        addMobSpawn(builder, biome, NaturalistTags.Biomes.HAS_CANARY, NaturalistTags.Biomes.BLACKLIST_CANARY, MobCategory.CREATURE, NaturalistEntityTypes.CANARY.get(), NaturalistConfig.canarySpawnWeight, NaturalistConfig.canarySpawnMinGroupSize, NaturalistConfig.canarySpawnMaxGroupSize);
        addMobSpawn(builder, biome, NaturalistTags.Biomes.HAS_CARDINAL, NaturalistTags.Biomes.BLACKLIST_CARDINAL, MobCategory.CREATURE, NaturalistEntityTypes.CARDINAL.get(), NaturalistConfig.cardinalSpawnWeight, NaturalistConfig.cardinalSpawnMinGroupSize, NaturalistConfig.cardinalSpawnMaxGroupSize);
        addMobSpawn(builder, biome, NaturalistTags.Biomes.HAS_CATFISH, NaturalistTags.Biomes.BLACKLIST_CATFISH, MobCategory.WATER_AMBIENT, NaturalistEntityTypes.CATFISH.get(), NaturalistConfig.catfishSpawnWeight, NaturalistConfig.catfishSpawnMinGroupSize, NaturalistConfig.catfishSpawnMaxGroupSize);
        addMobSpawn(builder, biome, NaturalistTags.Biomes.HAS_CORAL_SNAKE, NaturalistTags.Biomes.BLACKLIST_CORAL_SNAKE, MobCategory.CREATURE, NaturalistEntityTypes.CORAL_SNAKE.get(), NaturalistConfig.coralSnakeSpawnWeight, NaturalistConfig.coralSnakeSpawnMinGroupSize, NaturalistConfig.coralSnakeSpawnMaxGroupSize);
        addMobSpawn(builder, biome, NaturalistTags.Biomes.HAS_DEER, NaturalistTags.Biomes.BLACKLIST_DEER, MobCategory.CREATURE, NaturalistEntityTypes.DEER.get(), NaturalistConfig.deerSpawnWeight, NaturalistConfig.deerSpawnMinGroupSize, NaturalistConfig.deerSpawnMaxGroupSize);
        addMobSpawn(builder, biome, NaturalistTags.Biomes.HAS_DRAGONFLY, NaturalistTags.Biomes.BLACKLIST_DRAGONFLY, MobCategory.AMBIENT, NaturalistEntityTypes.DRAGONFLY.get(), NaturalistConfig.dragonflySpawnWeight, NaturalistConfig.dragonflySpawnMinGroupSize, NaturalistConfig.dragonflySpawnMaxGroupSize);
        addMobSpawn(builder, biome, NaturalistTags.Biomes.HAS_DUCK, NaturalistTags.Biomes.BLACKLIST_DUCK, MobCategory.CREATURE, NaturalistEntityTypes.DUCK.get(), NaturalistConfig.duckSpawnWeight, NaturalistConfig.duckSpawnMinGroupSize, NaturalistConfig.duckSpawnMaxGroupSize);
        addMobSpawn(builder, biome, NaturalistTags.Biomes.HAS_ELEPHANT, NaturalistTags.Biomes.BLACKLIST_ELEPHANT, MobCategory.CREATURE, NaturalistEntityTypes.ELEPHANT.get(), NaturalistConfig.elephantSpawnWeight, NaturalistConfig.elephantSpawnMinGroupSize, NaturalistConfig.elephantSpawnMaxGroupSize);
        addMobSpawn(builder, biome, NaturalistTags.Biomes.HAS_FINCH, NaturalistTags.Biomes.BLACKLIST_FINCH, MobCategory.CREATURE, NaturalistEntityTypes.FINCH.get(), NaturalistConfig.finchSpawnWeight, NaturalistConfig.finchSpawnMinGroupSize, NaturalistConfig.finchSpawnMaxGroupSize);
        addMobSpawn(builder, biome, NaturalistTags.Biomes.HAS_FIREFLY, NaturalistTags.Biomes.BLACKLIST_FIREFLY, MobCategory.AMBIENT, NaturalistEntityTypes.FIREFLY.get(), NaturalistConfig.fireflySpawnWeight, NaturalistConfig.fireflySpawnMinGroupSize, NaturalistConfig.fireflySpawnMaxGroupSize);
        addMobSpawn(builder, biome, BiomeTags.IS_FOREST, null, MobCategory.CREATURE, EntityType.FOX, NaturalistConfig.forestFoxSpawnWeight, NaturalistConfig.forestFoxSpawnMinGroupSize, NaturalistConfig.forestFoxSpawnMaxGroupSize);
        addMobSpawn(builder, biome, BiomeTags.IS_FOREST, null, MobCategory.CREATURE, EntityType.RABBIT, NaturalistConfig.forestRabbitSpawnWeight, NaturalistConfig.forestRabbitSpawnMinGroupSize, NaturalistConfig.forestRabbitSpawnMaxGroupSize);
        addMobSpawn(builder, biome, NaturalistTags.Biomes.HAS_GIRAFFE, NaturalistTags.Biomes.BLACKLIST_GIRAFFE, MobCategory.CREATURE, NaturalistEntityTypes.GIRAFFE.get(), NaturalistConfig.giraffeSpawnWeight, NaturalistConfig.giraffeSpawnMinGroupSize, NaturalistConfig.giraffeSpawnMaxGroupSize);
        addMobSpawn(builder, biome, NaturalistTags.Biomes.HAS_HIPPO, NaturalistTags.Biomes.BLACKLIST_HIPPO, MobCategory.CREATURE, NaturalistEntityTypes.HIPPO.get(), NaturalistConfig.hippoSpawnWeight, NaturalistConfig.hippoSpawnMinGroupSize, NaturalistConfig.hippoSpawnMaxGroupSize);
        addMobSpawn(builder, biome, NaturalistTags.Biomes.HAS_LION, NaturalistTags.Biomes.BLACKLIST_LION, MobCategory.CREATURE, NaturalistEntityTypes.LION.get(), NaturalistConfig.lionSpawnWeight, NaturalistConfig.lionSpawnMinGroupSize, NaturalistConfig.lionSpawnMaxGroupSize);
        addMobSpawn(builder, biome, NaturalistTags.Biomes.HAS_LIZARD, NaturalistTags.Biomes.BLACKLIST_LIZARD, MobCategory.CREATURE, NaturalistEntityTypes.LIZARD.get(), NaturalistConfig.lizardSpawnWeight, NaturalistConfig.lizardSpawnMinGroupSize, NaturalistConfig.lizardSpawnMaxGroupSize);
        addMobSpawn(builder, biome, NaturalistTags.Biomes.HAS_RHINO, NaturalistTags.Biomes.BLACKLIST_RHINO, MobCategory.CREATURE, NaturalistEntityTypes.RHINO.get(), NaturalistConfig.rhinoSpawnWeight, NaturalistConfig.rhinoSpawnMinGroupSize, NaturalistConfig.rhinoSpawnMaxGroupSize);
        addMobSpawn(builder, biome, NaturalistTags.Biomes.HAS_ROBIN, NaturalistTags.Biomes.BLACKLIST_ROBIN, MobCategory.CREATURE, NaturalistEntityTypes.ROBIN.get(), NaturalistConfig.robinSpawnWeight, NaturalistConfig.robinSpawnMinGroupSize, NaturalistConfig.robinSpawnMaxGroupSize);
        addMobSpawn(builder, biome, NaturalistTags.Biomes.HAS_SNAIL, NaturalistTags.Biomes.BLACKLIST_SNAIL, MobCategory.CREATURE, NaturalistEntityTypes.SNAIL.get(), NaturalistConfig.snailSpawnWeight, NaturalistConfig.snailSpawnMinGroupSize, NaturalistConfig.snailSpawnMaxGroupSize);
        addMobSpawn(builder, biome, NaturalistTags.Biomes.HAS_SNAKE, NaturalistTags.Biomes.BLACKLIST_SNAKE, MobCategory.CREATURE, NaturalistEntityTypes.SNAKE.get(), NaturalistConfig.snakeSpawnWeight, NaturalistConfig.snakeSpawnMinGroupSize, NaturalistConfig.snakeSpawnMaxGroupSize);
        addMobSpawn(builder, biome, NaturalistTags.Biomes.HAS_SPARROW, NaturalistTags.Biomes.BLACKLIST_SPARROW, MobCategory.CREATURE, NaturalistEntityTypes.SPARROW.get(), NaturalistConfig.sparrowSpawnWeight, NaturalistConfig.sparrowSpawnMinGroupSize, NaturalistConfig.sparrowSpawnMaxGroupSize);
        addMobSpawn(builder, biome, NaturalistTags.Biomes.HAS_TORTOISE, NaturalistTags.Biomes.BLACKLIST_TORTOISE, MobCategory.CREATURE, NaturalistEntityTypes.TORTOISE.get(), NaturalistConfig.tortoiseSpawnWeight, NaturalistConfig.tortoiseSpawnMinGroupSize, NaturalistConfig.tortoiseSpawnMaxGroupSize);
        addMobSpawn(builder, biome, NaturalistTags.Biomes.HAS_VULTURE, NaturalistTags.Biomes.BLACKLIST_VULTURE, MobCategory.CREATURE, NaturalistEntityTypes.VULTURE.get(), NaturalistConfig.vultureSpawnWeight, NaturalistConfig.vultureSpawnMinGroupSize, NaturalistConfig.vultureSpawnMaxGroupSize);
        addMobSpawn(builder, biome, NaturalistTags.Biomes.HAS_ZEBRA, NaturalistTags.Biomes.BLACKLIST_ZEBRA, MobCategory.CREATURE, NaturalistEntityTypes.ZEBRA.get(), NaturalistConfig.zebraSpawnWeight, NaturalistConfig.zebraSpawnMinGroupSize, NaturalistConfig.zebraSpawnMaxGroupSize);
    }

    private void addMobSpawn(ModifiableBiomeInfo.BiomeInfo.Builder builder, Holder<Biome> biome, TagKey<Biome> tag, TagKey<Biome> blacklistTag, MobCategory mobCategory, EntityType<?> entityType, int weight, int minGroupSize, int maxGroupSize) {
        if (weight <= 0) {
            builder.getMobSpawnSettings().getSpawner(mobCategory).removeIf(spawnerData -> spawnerData.type == entityType);
            return;
        }
        if (!biome.is(tag)) return;
        if (blacklistTag != null && biome.is(blacklistTag)) return;
        builder.getMobSpawnSettings().addSpawn(mobCategory, new MobSpawnSettings.SpawnerData(entityType, weight, minGroupSize, maxGroupSize));
    }

    @Override
    public @NotNull Codec<? extends BiomeModifier> codec() {
        return NaturalistBiomeModifiers.ADD_ANIMALS_CODEC.get();
    }
}

