package com.starfish_studios.naturalist.datagen;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.registry.NaturalistTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

public class NaturalistBiomeTagsProvider extends TagsProvider<Biome> {
    private static final ResourceLocation ATMOSPHERIC_IS_RAINFOREST = ResourceLocation.fromNamespaceAndPath("atmospheric", "is_rainforest");
    private static final ResourceLocation ATMOSPHERIC_KOUSA_JUNGLE = ResourceLocation.fromNamespaceAndPath("atmospheric", "kousa_jungle");

    private static ResourceLocation bop(String name) {
        return ResourceLocation.fromNamespaceAndPath("biomesoplenty", name);
    }

    private static ResourceLocation terralith(String name) {
        return ResourceLocation.fromNamespaceAndPath("terralith", name);
    }

    public NaturalistBiomeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, Registries.BIOME, lookupProvider, Naturalist.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NonNull Provider provider) {
        tag(NaturalistTags.Biomes.HAS_ALLIGATOR)
                .add(Biomes.SWAMP).add(Biomes.MANGROVE_SWAMP).add(Biomes.RIVER)
                .addOptionalTag(Tags.Biomes.IS_SWAMP)
                .addOptionalTag(ATMOSPHERIC_IS_RAINFOREST)
                .addOptional(bop("bayou"))
                .addOptional(terralith("orchid_swamp"))
                .addOptional(terralith("ice_marsh"))
                .addOptional(terralith("warm_river"))
                .addOptional(terralith("desert_oasis"))
                .addOptional(terralith("red_oasis"));

        tag(NaturalistTags.Biomes.HAS_BASS)
                .add(Biomes.SWAMP).add(Biomes.MANGROVE_SWAMP)
                .addTag(BiomeTags.IS_RIVER)
                .addOptionalTag(Tags.Biomes.IS_SWAMP)
                .addOptional(bop("bayou")).addOptional(bop("bog")).addOptional(bop("wetland"))
                .addOptional(terralith("orchid_swamp"))
                .addOptional(terralith("ice_marsh"))
                .addOptional(terralith("warm_river"))
                .addOptional(terralith("desert_oasis"))
                .addOptional(terralith("red_oasis"));

        tag(NaturalistTags.Biomes.HAS_BEAR)
                .addTag(BiomeTags.IS_FOREST).addTag(BiomeTags.IS_TAIGA)
                .addOptionalTag(Tags.Biomes.IS_FOREST).addOptionalTag(Tags.Biomes.IS_TAIGA)
                .addOptional(bop("coniferous_forest")).addOptional(bop("forested_field"))
                .addOptional(bop("orchard")).addOptional(bop("pumpkin_patch"))
                .addOptional(bop("seasonal_forest")).addOptional(bop("woodland"))
                .addOptional(bop("snowy_coniferous_forest")).addOptional(bop("fir_clearing"))
                .addOptional(bop("maple_woods")).addOptional(bop("snowy_maple_woods"))
                .addOptional(terralith("birch_taiga"))
                .addOptional(terralith("cloud_forest"))
                .addOptional(terralith("forested_highlands"))
                .addOptional(terralith("lavender_forest"))
                .addOptional(terralith("moonlight_grove"))
                .addOptional(terralith("moonlight_valley"))
                .addOptional(terralith("sakura_grove"))
                .addOptional(terralith("sakura_valley"))
                .addOptional(terralith("snowy_maple_forest"))
                .addOptional(terralith("temperate_highlands"))
                .addOptional(terralith("wintry_forest"))
                .addOptional(terralith("yellowstone"));

        tag(NaturalistTags.Biomes.HAS_BLUEJAY)
                .addTag(BiomeTags.IS_TAIGA).addTag(BiomeTags.IS_HILL)
                .add(Biomes.ICE_SPIKES).add(Biomes.SNOWY_PLAINS).add(Biomes.SNOWY_SLOPES)
                .addOptionalTag(Tags.Biomes.IS_SNOWY).addOptionalTag(Tags.Biomes.IS_MOUNTAIN)
                .addOptionalTag(Tags.Biomes.IS_CONIFEROUS_TREE).addOptionalTag(Tags.Biomes.IS_TAIGA)
                .addOptionalTag(Tags.Biomes.IS_ICY).addOptionalTag(Tags.Biomes.IS_HILL)
                .addOptional(ATMOSPHERIC_KOUSA_JUNGLE)
                .addOptional(bop("snowy_coniferous_forest")).addOptional(bop("coniferous_forest"))
                .addOptional(bop("highland")).addOptional(bop("jade_cliffs"))
                .addOptional(bop("maple_woods")).addOptional(bop("snowy_maple_woods"))
                .addOptional(bop("moor")).addOptional(bop("muskeg"))
                .addOptional(terralith("alpine_grove"))
                .addOptional(terralith("alpine_highlands"))
                .addOptional(terralith("birch_taiga"))
                .addOptional(terralith("emerald_peaks"))
                .addOptional(terralith("forested_highlands"))
                .addOptional(terralith("haze_mountain"))
                .addOptional(terralith("highlands"))
                .addOptional(terralith("rocky_mountains"))
                .addOptional(terralith("scarlet_mountains"))
                .addOptional(terralith("snowy_maple_forest"))
                .addOptional(terralith("stony_spires"))
                .addOptional(terralith("temperate_highlands"))
                .addOptional(terralith("white_cliffs"))
                .addOptional(terralith("wintry_forest"));

        tag(NaturalistTags.Biomes.HAS_BOAR)
                .addTag(BiomeTags.IS_SAVANNA).addTag(BiomeTags.IS_FOREST)
                .addOptionalTag(Tags.Biomes.IS_SAVANNA)
                .addOptional(bop("prairie")).addOptional(bop("rocky_shrubland"))
                .addOptional(bop("shrubland")).addOptional(bop("woodland"))
                .addOptional(terralith("brushland"))
                .addOptional(terralith("cold_shrubland"))
                .addOptional(terralith("forested_highlands"))
                .addOptional(terralith("hot_shrubland"))
                .addOptional(terralith("lavender_forest"))
                .addOptional(terralith("lavender_valley"))
                .addOptional(terralith("moonlight_grove"))
                .addOptional(terralith("moonlight_valley"))
                .addOptional(terralith("sakura_grove"))
                .addOptional(terralith("sakura_valley"))
                .addOptional(terralith("shrubland"))
                .addOptional(terralith("temperate_highlands"));

        tag(NaturalistTags.Biomes.HAS_BUTTERFLY)
                .addTag(BiomeTags.IS_FOREST)
                .add(Biomes.PLAINS).add(Biomes.SWAMP).add(Biomes.MANGROVE_SWAMP)
                .addOptionalTag(Tags.Biomes.IS_SWAMP)
                .addOptionalTag(Tags.Biomes.IS_FOREST).addOptionalTag(Tags.Biomes.IS_PLAINS)
                .addOptional(bop("bayou")).addOptional(bop("field"))
                .addOptional(bop("lavender_field")).addOptional(bop("mystic_grove"))
                .addOptional(bop("overgrown_greens")).addOptional(bop("pasture"))
                .addOptional(bop("prairie")).addOptional(bop("rocky_shrubland"))
                .addOptional(bop("shrubland"))
                .addOptional(terralith("blooming_plateau"))
                .addOptional(terralith("blooming_valley"))
                .addOptional(terralith("brushland"))
                .addOptional(terralith("lavender_forest"))
                .addOptional(terralith("lavender_valley"))
                .addOptional(terralith("moonlight_grove"))
                .addOptional(terralith("moonlight_valley"))
                .addOptional(terralith("orchid_swamp"))
                .addOptional(terralith("sakura_grove"))
                .addOptional(terralith("sakura_valley"))
                .addOptional(terralith("shrubland"));

        tag(NaturalistTags.Biomes.HAS_CANARY)
                .addTag(BiomeTags.IS_HILL).addTag(BiomeTags.IS_MOUNTAIN)
                .addOptionalTag(Tags.Biomes.IS_MOUNTAIN).addOptionalTag(Tags.Biomes.IS_HILL)
                .addOptional(bop("crag")).addOptional(bop("jade_cliffs")).addOptional(bop("moor"))
                .addOptional(terralith("alpine_grove"))
                .addOptional(terralith("alpine_highlands"))
                .addOptional(terralith("emerald_peaks"))
                .addOptional(terralith("haze_mountain"))
                .addOptional(terralith("highlands"))
                .addOptional(terralith("painted_mountains"))
                .addOptional(terralith("rocky_mountains"))
                .addOptional(terralith("scarlet_mountains"))
                .addOptional(terralith("stony_spires"))
                .addOptional(terralith("temperate_highlands"))
                .addOptional(terralith("white_cliffs"));

        tag(NaturalistTags.Biomes.HAS_CARDINAL)
                .addTag(BiomeTags.IS_FOREST).addTag(BiomeTags.IS_SAVANNA)
                .add(Biomes.SWAMP).add(Biomes.MANGROVE_SWAMP).add(Biomes.DESERT)
                .addOptionalTag(Tags.Biomes.IS_FOREST).addOptionalTag(Tags.Biomes.IS_SAVANNA)
                .addOptionalTag(Tags.Biomes.IS_SWAMP).addOptionalTag(Tags.Biomes.IS_SANDY)
                .addOptionalTag(Tags.Biomes.IS_DESERT)
                .addOptional(bop("bayou")).addOptional(bop("bog"))
                .addOptional(bop("lavender_field")).addOptional(bop("maple_woods"))
                .addOptional(bop("mystic_grove")).addOptional(bop("orchard"))
                .addOptional(bop("overgrown_greens")).addOptional(bop("wetland"))
                .addOptional(terralith("blooming_plateau"))
                .addOptional(terralith("blooming_valley"))
                .addOptional(terralith("brushland"))
                .addOptional(terralith("desert_oasis"))
                .addOptional(terralith("lavender_forest"))
                .addOptional(terralith("lavender_valley"))
                .addOptional(terralith("lush_desert"))
                .addOptional(terralith("moonlight_grove"))
                .addOptional(terralith("moonlight_valley"))
                .addOptional(terralith("orchid_swamp"))
                .addOptional(terralith("red_oasis"))
                .addOptional(terralith("sakura_grove"))
                .addOptional(terralith("sakura_valley"))
                .addOptional(terralith("shrubland"));

        tag(NaturalistTags.Biomes.HAS_CATFISH)
                .add(Biomes.SWAMP).add(Biomes.MANGROVE_SWAMP)
                .addOptionalTag(Tags.Biomes.IS_SWAMP)
                .addOptional(bop("bayou")).addOptional(bop("bog")).addOptional(bop("wetland"))
                .addOptional(terralith("orchid_swamp"))
                .addOptional(terralith("ice_marsh"))
                .addOptional(terralith("warm_river"))
                .addOptional(terralith("desert_oasis"))
                .addOptional(terralith("red_oasis"));

        tag(NaturalistTags.Biomes.HAS_CORAL_SNAKE)
                .addTag(BiomeTags.IS_JUNGLE).addTag(BiomeTags.IS_RIVER)
                .add(Biomes.BEACH).add(Biomes.STONY_SHORE)
                .addOptionalTag(Tags.Biomes.IS_BEACH).addOptionalTag(Tags.Biomes.IS_JUNGLE)
                .addOptionalTag(Tags.Biomes.IS_RIVER)
                .addOptional(bop("rainforest"))
                .addOptional(terralith("amethyst_rainforest"))
                .addOptional(terralith("jungle_mountains"))
                .addOptional(terralith("rocky_jungle"))
                .addOptional(terralith("tropical_jungle"))
                .addOptional(terralith("warm_river"));

        tag(NaturalistTags.Biomes.HAS_DEER)
                .addTag(BiomeTags.IS_FOREST).add(Biomes.CHERRY_GROVE)
                .addOptionalTag(Tags.Biomes.IS_FOREST)
                .addOptional(bop("orchard")).addOptional(bop("pasture"))
                .addOptional(bop("redwood_forest")).addOptional(bop("woodland"))
                .addOptional(bop("snowy_coniferous_forest")).addOptional(bop("snowy_fir_clearing"))
                .addOptional(bop("snowblossom_grove")).addOptional(bop("snowy_maple_woods"))
                .addOptional(bop("lavender_field")).addOptional(bop("mystic_grove"))
                .addOptional(terralith("birch_taiga"))
                .addOptional(terralith("blooming_plateau"))
                .addOptional(terralith("blooming_valley"))
                .addOptional(terralith("cloud_forest"))
                .addOptional(terralith("forested_highlands"))
                .addOptional(terralith("lavender_forest"))
                .addOptional(terralith("lavender_valley"))
                .addOptional(terralith("moonlight_grove"))
                .addOptional(terralith("moonlight_valley"))
                .addOptional(terralith("sakura_grove"))
                .addOptional(terralith("sakura_valley"))
                .addOptional(terralith("snowy_maple_forest"))
                .addOptional(terralith("temperate_highlands"))
                .addOptional(terralith("wintry_forest"));

        tag(NaturalistTags.Biomes.HAS_DRAGONFLY)
                .add(Biomes.SWAMP).add(Biomes.MANGROVE_SWAMP)
                .addOptionalTag(Tags.Biomes.IS_SWAMP)
                .addOptional(bop("bayou"))
                .addOptional(terralith("orchid_swamp"))
                .addOptional(terralith("ice_marsh"))
                .addOptional(terralith("warm_river"))
                .addOptional(terralith("desert_oasis"))
                .addOptional(terralith("red_oasis"));

        tag(NaturalistTags.Biomes.HAS_DUCK)
                .add(Biomes.SWAMP).addTag(BiomeTags.IS_RIVER)
                .addOptionalTag(Tags.Biomes.IS_SWAMP)
                .addOptional(bop("lavender_field")).addOptional(bop("mystic_grove"))
                .addOptional(bop("orchard")).addOptional(bop("prairie"))
                .addOptional(bop("rocky_shrubland")).addOptional(bop("shrubland"))
                .addOptional(terralith("blooming_valley"))
                .addOptional(terralith("brushland"))
                .addOptional(terralith("ice_marsh"))
                .addOptional(terralith("lavender_valley"))
                .addOptional(terralith("moonlight_valley"))
                .addOptional(terralith("orchid_swamp"))
                .addOptional(terralith("sakura_valley"))
                .addOptional(terralith("shrubland"))
                .addOptional(terralith("warm_river"));

        tag(NaturalistTags.Biomes.HAS_ELEPHANT)
                .addTag(BiomeTags.IS_SAVANNA)
                .addOptionalTag(Tags.Biomes.IS_SAVANNA)
                .addOptional(bop("scrubland"))
                .addOptional(terralith("ashen_savanna"))
                .addOptional(terralith("savanna_badlands"))
                .addOptional(terralith("savanna_slopes"));

        tag(NaturalistTags.Biomes.HAS_FINCH)
                .addTag(BiomeTags.IS_SAVANNA).addTag(BiomeTags.IS_FOREST)
                .addOptionalTag(Tags.Biomes.IS_FOREST).addOptionalTag(Tags.Biomes.IS_SAVANNA)
                .addOptional(bop("lavender_field")).addOptional(bop("mediterranean_forest"))
                .addOptional(bop("scrubland"))
                .addOptional(terralith("brushland"))
                .addOptional(terralith("lavender_forest"))
                .addOptional(terralith("lavender_valley"))
                .addOptional(terralith("sakura_grove"))
                .addOptional(terralith("sakura_valley"))
                .addOptional(terralith("savanna_badlands"))
                .addOptional(terralith("savanna_slopes"))
                .addOptional(terralith("shrubland"));

        tag(NaturalistTags.Biomes.HAS_FIREFLY)
                .addTag(BiomeTags.IS_FOREST)
                .add(Biomes.PLAINS).add(Biomes.SWAMP).add(Biomes.MANGROVE_SWAMP).add(Biomes.MUSHROOM_FIELDS)
                .addOptionalTag(Tags.Biomes.IS_FOREST).addOptionalTag(Tags.Biomes.IS_PLAINS)
                .addOptionalTag(Tags.Biomes.IS_SWAMP).addOptionalTag(Tags.Biomes.IS_MUSHROOM)
                .addOptional(bop("bayou")).addOptional(bop("bog"))
                .addOptional(bop("lavender_field")).addOptional(bop("mystic_grove"))
                .addOptional(bop("orchard")).addOptional(bop("pasture"))
                .addOptional(bop("rocky_shrubland")).addOptional(bop("shrubland"))
                .addOptional(bop("wetland"))
                .addOptional(terralith("blooming_plateau"))
                .addOptional(terralith("blooming_valley"))
                .addOptional(terralith("lavender_forest"))
                .addOptional(terralith("lavender_valley"))
                .addOptional(terralith("moonlight_grove"))
                .addOptional(terralith("moonlight_valley"))
                .addOptional(terralith("orchid_swamp"))
                .addOptional(terralith("sakura_grove"))
                .addOptional(terralith("sakura_valley"))
                .addOptional(terralith("shrubland"))
                .addOptional(terralith("wintry_forest"));

        tag(NaturalistTags.Biomes.HAS_GIRAFFE)
                .addTag(BiomeTags.IS_SAVANNA)
                .addOptionalTag(Tags.Biomes.IS_SAVANNA)
                .addOptional(bop("scrubland"))
                .addOptional(terralith("ashen_savanna"))
                .addOptional(terralith("savanna_badlands"))
                .addOptional(terralith("savanna_slopes"));

        tag(NaturalistTags.Biomes.HAS_HIPPO)
                .addTag(BiomeTags.IS_SAVANNA).addTag(BiomeTags.IS_JUNGLE)
                .addOptionalTag(Tags.Biomes.IS_SAVANNA).addOptionalTag(Tags.Biomes.IS_JUNGLE)
                .addOptionalTag(ATMOSPHERIC_IS_RAINFOREST)
                .addOptional(terralith("amethyst_rainforest"))
                .addOptional(terralith("orchid_swamp"))
                .addOptional(terralith("rocky_jungle"))
                .addOptional(terralith("tropical_jungle"))
                .addOptional(terralith("warm_river"));

        tag(NaturalistTags.Biomes.HAS_LION)
                .addTag(BiomeTags.IS_SAVANNA)
                .addOptionalTag(Tags.Biomes.IS_SAVANNA)
                .addOptional(bop("scrubland"))
                .addOptional(terralith("ashen_savanna"))
                .addOptional(terralith("savanna_badlands"))
                .addOptional(terralith("savanna_slopes"));

        tag(NaturalistTags.Biomes.HAS_LIZARD)
                .add(Biomes.SWAMP).add(Biomes.MANGROVE_SWAMP).add(Biomes.DESERT)
                .addTag(BiomeTags.IS_JUNGLE).addTag(BiomeTags.IS_FOREST).addTag(BiomeTags.IS_SAVANNA)
                .addOptionalTag(Tags.Biomes.IS_SWAMP).addOptionalTag(Tags.Biomes.IS_DESERT)
                .addOptionalTag(Tags.Biomes.IS_FOREST).addOptionalTag(Tags.Biomes.IS_SAVANNA)
                .addOptionalTag(Tags.Biomes.IS_JUNGLE)
                .addOptional(bop("field")).addOptional(bop("rainforest"))
                .addOptional(terralith("amethyst_rainforest"))
                .addOptional(terralith("brushland"))
                .addOptional(terralith("desert_canyon"))
                .addOptional(terralith("desert_oasis"))
                .addOptional(terralith("desert_spires"))
                .addOptional(terralith("hot_shrubland"))
                .addOptional(terralith("jungle_mountains"))
                .addOptional(terralith("lush_desert"))
                .addOptional(terralith("rocky_jungle"))
                .addOptional(terralith("savanna_badlands"))
                .addOptional(terralith("savanna_slopes"))
                .addOptional(terralith("shrubland"))
                .addOptional(terralith("tropical_jungle"))
                .addOptional(terralith("warped_mesa"));

        tag(NaturalistTags.Biomes.HAS_RATTLESNAKE)
                .addTag(BiomeTags.IS_BADLANDS).addTag(BiomeTags.IS_SAVANNA)
                .add(Biomes.DESERT)
                .addOptionalTag(Tags.Biomes.IS_SANDY).addOptionalTag(Tags.Biomes.IS_SAVANNA).addOptionalTag(Tags.Biomes.IS_DESERT)
                .addOptional(bop("lush_desert"))
                .addOptional(bop("scrubland"))
                .addOptional(terralith("ancient_sands"))
                .addOptional(terralith("desert_canyon"))
                .addOptional(terralith("desert_oasis"))
                .addOptional(terralith("desert_spires"))
                .addOptional(terralith("gravel_desert"))
                .addOptional(terralith("hot_shrubland"))
                .addOptional(terralith("lush_desert"))
                .addOptional(terralith("painted_mountains"))
                .addOptional(terralith("red_oasis"))
                .addOptional(terralith("savanna_badlands"))
                .addOptional(terralith("savanna_slopes"))
                .addOptional(terralith("warped_mesa"));

        tag(NaturalistTags.Biomes.HAS_RHINO)
                .addTag(BiomeTags.IS_SAVANNA)
                .addOptionalTag(Tags.Biomes.IS_SAVANNA)
                .addOptional(bop("scrubland"))
                .addOptional(terralith("ashen_savanna"))
                .addOptional(terralith("savanna_badlands"))
                .addOptional(terralith("savanna_slopes"));

        tag(NaturalistTags.Biomes.HAS_ROBIN)
                .addTag(BiomeTags.IS_FOREST).addTag(BiomeTags.IS_MOUNTAIN)
                .add(Biomes.PLAINS).add(Biomes.CHERRY_GROVE)
                .addOptionalTag(Tags.Biomes.IS_FOREST).addOptionalTag(Tags.Biomes.IS_MOUNTAIN).addOptionalTag(Tags.Biomes.IS_PLAINS)
                .addOptional(bop("field")).addOptional(bop("orchard"))
                .addOptional(bop("overgrown_greens")).addOptional(bop("pasture"))
                .addOptional(bop("pumpkin_patch")).addOptional(bop("seasonal_forest"))
                .addOptional(terralith("blooming_plateau"))
                .addOptional(terralith("blooming_valley"))
                .addOptional(terralith("cloud_forest"))
                .addOptional(terralith("forested_highlands"))
                .addOptional(terralith("lavender_forest"))
                .addOptional(terralith("lavender_valley"))
                .addOptional(terralith("moonlight_grove"))
                .addOptional(terralith("moonlight_valley"))
                .addOptional(terralith("sakura_grove"))
                .addOptional(terralith("sakura_valley"))
                .addOptional(terralith("temperate_highlands"))
                .addOptional(terralith("white_cliffs"));

        tag(NaturalistTags.Biomes.HAS_SNAIL)
                .addTag(BiomeTags.IS_FOREST).addTag(BiomeTags.IS_SAVANNA)
                .addTag(BiomeTags.IS_RIVER).addTag(BiomeTags.IS_HILL).addTag(BiomeTags.IS_MOUNTAIN)
                .add(Biomes.PLAINS).add(Biomes.SWAMP).add(Biomes.MANGROVE_SWAMP)
                .add(Biomes.LUSH_CAVES).add(Biomes.DRIPSTONE_CAVES).add(Biomes.MUSHROOM_FIELDS)
                .addOptionalTag(Tags.Biomes.IS_FOREST).addOptionalTag(Tags.Biomes.IS_SAVANNA)
                .addOptionalTag(Tags.Biomes.IS_RIVER).addOptionalTag(Tags.Biomes.IS_HILL)
                .addOptionalTag(Tags.Biomes.IS_MOUNTAIN).addOptionalTag(Tags.Biomes.IS_PLAINS)
                .addOptionalTag(Tags.Biomes.IS_SWAMP).addOptionalTag(Tags.Biomes.IS_UNDERGROUND).addOptionalTag(Tags.Biomes.IS_MUSHROOM)
                .addOptional(bop("bayou")).addOptional(bop("bog"))
                .addOptional(bop("orchard")).addOptional(bop("wetland"))
                .addOptional(bop("woodland"))
                .addOptional(terralith("alpine_grove"))
                .addOptional(terralith("blooming_valley"))
                .addOptional(terralith("brushland"))
                .addOptional(terralith("cloud_forest"))
                .addOptional(terralith("forested_highlands"))
                .addOptional(terralith("ice_marsh"))
                .addOptional(terralith("lavender_forest"))
                .addOptional(terralith("lavender_valley"))
                .addOptional(terralith("moonlight_grove"))
                .addOptional(terralith("moonlight_valley"))
                .addOptional(terralith("orchid_swamp"))
                .addOptional(terralith("sakura_grove"))
                .addOptional(terralith("sakura_valley"))
                .addOptional(terralith("shrubland"))
                .addOptional(terralith("temperate_highlands"))
                .addOptional(terralith("warm_river"))
                .addOptional(terralith("wintry_forest"))
                .addOptional(terralith("yellowstone"));

        tag(NaturalistTags.Biomes.HAS_SNAKE)
                .addTag(BiomeTags.IS_FOREST)
                .add(Biomes.PLAINS).add(Biomes.SWAMP).add(Biomes.MANGROVE_SWAMP)
                .addOptionalTag(Tags.Biomes.IS_FOREST).addOptionalTag(Tags.Biomes.IS_PLAINS).addOptionalTag(Tags.Biomes.IS_SWAMP)
                .addOptional(bop("bayou")).addOptional(bop("bog"))
                .addOptional(bop("wetland")).addOptional(bop("lavender_field"))
                .addOptional(terralith("blooming_plateau"))
                .addOptional(terralith("blooming_valley"))
                .addOptional(terralith("brushland"))
                .addOptional(terralith("forested_highlands"))
                .addOptional(terralith("hot_shrubland"))
                .addOptional(terralith("lavender_forest"))
                .addOptional(terralith("lavender_valley"))
                .addOptional(terralith("moonlight_grove"))
                .addOptional(terralith("moonlight_valley"))
                .addOptional(terralith("orchid_swamp"))
                .addOptional(terralith("sakura_grove"))
                .addOptional(terralith("sakura_valley"))
                .addOptional(terralith("shrubland"))
                .addOptional(terralith("warm_river"));

        tag(NaturalistTags.Biomes.HAS_SPARROW)
                .add(Biomes.PLAINS).add(Biomes.CHERRY_GROVE)
                .addOptionalTag(Tags.Biomes.IS_PLAINS)
                .addOptional(bop("mystic_grove")).addOptional(bop("prairie"))
                .addOptional(bop("rocky_shrubland")).addOptional(bop("shrubland"))
                .addOptional(terralith("blooming_plateau"))
                .addOptional(terralith("blooming_valley"))
                .addOptional(terralith("brushland"))
                .addOptional(terralith("cold_shrubland"))
                .addOptional(terralith("lavender_valley"))
                .addOptional(terralith("sakura_valley"))
                .addOptional(terralith("shrubland"));

        tag(NaturalistTags.Biomes.HAS_TORTOISE)
                .add(Biomes.SWAMP).add(Biomes.MANGROVE_SWAMP).add(Biomes.DESERT)
                .addTag(BiomeTags.IS_JUNGLE)
                .addOptionalTag(Tags.Biomes.IS_SWAMP).addOptionalTag(Tags.Biomes.IS_DESERT)
                .addOptionalTag(Tags.Biomes.IS_JUNGLE)
                .addOptional(bop("lush_desert")).addOptional(bop("rainforest"))
                .addOptional(bop("bayou"))
                .addOptional(terralith("amethyst_rainforest"))
                .addOptional(terralith("desert_oasis"))
                .addOptional(terralith("lush_desert"))
                .addOptional(terralith("orchid_swamp"))
                .addOptional(terralith("red_oasis"))
                .addOptional(terralith("rocky_jungle"))
                .addOptional(terralith("tropical_jungle"));

        tag(NaturalistTags.Biomes.HAS_VULTURE)
                .addTag(BiomeTags.IS_SAVANNA).addTag(BiomeTags.IS_BADLANDS)
                .add(Biomes.DESERT)
                .addOptionalTag(Tags.Biomes.IS_SAVANNA).addOptionalTag(Tags.Biomes.IS_DESERT)
                .addOptional(bop("dryland")).addOptional(bop("highland"))
                .addOptional(bop("lush_desert")).addOptional(bop("lush_savanna"))
                .addOptional(bop("scrubland"))
                .addOptional(terralith("ancient_sands"))
                .addOptional(terralith("ashen_savanna"))
                .addOptional(terralith("bryce_canyon"))
                .addOptional(terralith("desert_canyon"))
                .addOptional(terralith("desert_spires"))
                .addOptional(terralith("gravel_desert"))
                .addOptional(terralith("hot_shrubland"))
                .addOptional(terralith("lush_desert"))
                .addOptional(terralith("painted_mountains"))
                .addOptional(terralith("savanna_badlands"))
                .addOptional(terralith("savanna_slopes"))
                .addOptional(terralith("warped_mesa"));

        tag(NaturalistTags.Biomes.HAS_ZEBRA)
                .addTag(BiomeTags.IS_SAVANNA)
                .addOptionalTag(Tags.Biomes.IS_SAVANNA)
                .addOptional(bop("scrubland"))
                .addOptional(terralith("ashen_savanna"))
                .addOptional(terralith("savanna_badlands"))
                .addOptional(terralith("savanna_slopes"));

        coldBlacklist(NaturalistTags.Biomes.BLACKLIST_ALLIGATOR);
        emptyBlacklist(NaturalistTags.Biomes.BLACKLIST_BASS);
        tag(NaturalistTags.Biomes.BLACKLIST_BEAR)
                .addOptionalTag(Tags.Biomes.IS_HOT).addOptionalTag(ATMOSPHERIC_IS_RAINFOREST);
        emptyBlacklist(NaturalistTags.Biomes.BLACKLIST_BLUEJAY);
        emptyBlacklist(NaturalistTags.Biomes.BLACKLIST_BOAR);
        coldBlacklist(NaturalistTags.Biomes.BLACKLIST_BUTTERFLY);
        emptyBlacklist(NaturalistTags.Biomes.BLACKLIST_CANARY);
        emptyBlacklist(NaturalistTags.Biomes.BLACKLIST_CARDINAL);
        emptyBlacklist(NaturalistTags.Biomes.BLACKLIST_CATFISH);
        coldBlacklist(NaturalistTags.Biomes.BLACKLIST_CORAL_SNAKE);
        emptyBlacklist(NaturalistTags.Biomes.BLACKLIST_DEER);
        coldBlacklist(NaturalistTags.Biomes.BLACKLIST_DRAGONFLY);
        emptyBlacklist(NaturalistTags.Biomes.BLACKLIST_DUCK);
        coldBlacklist(NaturalistTags.Biomes.BLACKLIST_ELEPHANT);
        emptyBlacklist(NaturalistTags.Biomes.BLACKLIST_FINCH);
        coldBlacklist(NaturalistTags.Biomes.BLACKLIST_FIREFLY);
        emptyBlacklist(NaturalistTags.Biomes.BLACKLIST_FOREST_FOXES);
        emptyBlacklist(NaturalistTags.Biomes.BLACKLIST_FOREST_RABBITS);
        coldBlacklist(NaturalistTags.Biomes.BLACKLIST_GIRAFFE);
        coldBlacklist(NaturalistTags.Biomes.BLACKLIST_HIPPO);
        coldBlacklist(NaturalistTags.Biomes.BLACKLIST_LION);
        coldBlacklist(NaturalistTags.Biomes.BLACKLIST_LIZARD);
        emptyBlacklist(NaturalistTags.Biomes.BLACKLIST_RATTLESNAKE);
        coldBlacklist(NaturalistTags.Biomes.BLACKLIST_RHINO);
        emptyBlacklist(NaturalistTags.Biomes.BLACKLIST_ROBIN);
        coldBlacklist(NaturalistTags.Biomes.BLACKLIST_SNAIL);
        coldBlacklist(NaturalistTags.Biomes.BLACKLIST_SNAKE);
        emptyBlacklist(NaturalistTags.Biomes.BLACKLIST_SPARROW);
        coldBlacklist(NaturalistTags.Biomes.BLACKLIST_TORTOISE);
        emptyBlacklist(NaturalistTags.Biomes.BLACKLIST_VULTURE);
        coldBlacklist(NaturalistTags.Biomes.BLACKLIST_ZEBRA);
    }

    private void coldBlacklist(TagKey<Biome> tag) {
        tag(tag).addOptionalTag(Tags.Biomes.IS_ICY).addOptionalTag(Tags.Biomes.IS_SNOWY);
    }

    private void emptyBlacklist(TagKey<Biome> tag) {
        tag(tag);
    }
}
