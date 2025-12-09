package com.starfish_studios.naturalist.world.forge;

import com.mojang.serialization.Codec;
import com.starfish_studios.naturalist.NaturalistConfig;
import com.starfish_studios.naturalist.core.registry.NaturalistTags;
import com.starfish_studios.naturalist.core.registry.forge.NaturalistBiomeModifiers;
import net.minecraft.core.Holder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;
import org.jetbrains.annotations.NotNull;

public class RemoveFarmAnimalsBiomeModifier implements BiomeModifier {
    @Override
    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (!phase.equals(Phase.REMOVE)) return;

        if (NaturalistConfig.removeSwampFarmAnimals) {
            removeFarmAnimals(builder, biome, NaturalistTags.Biomes.REMOVE_SWAMP_FARM_ANIMALS);
        }
        if (NaturalistConfig.removeSavannaFarmAnimals) {
            removeFarmAnimals(builder, biome, NaturalistTags.Biomes.REMOVE_SAVANNA_FARM_ANIMALS);
        }
    }

    private void removeFarmAnimals(ModifiableBiomeInfo.BiomeInfo.Builder builder, Holder<Biome> biome, TagKey<Biome> tag) {
        if (!biome.is(tag)) return;

        builder.getMobSpawnSettings().getSpawner(MobCategory.CREATURE).removeIf(data ->
                data.type == EntityType.COW
                        || data.type == EntityType.PIG
                        || data.type == EntityType.SHEEP
                        || data.type == EntityType.CHICKEN);
    }

    @Override
    public @NotNull Codec<? extends BiomeModifier> codec() {
        return NaturalistBiomeModifiers.REMOVE_FARM_ANIMALS_CODEC.get();
    }
}

