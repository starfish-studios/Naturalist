package com.starfish_studios.naturalist.registry;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.common.block.*;
import com.starfish_studios.naturalist.common.entity.Butterfly;
import com.starfish_studios.naturalist.common.entity.Snail;
import com.starfish_studios.naturalist.common.item.*;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.PushReaction;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class NaturalistRegistry {

    //region BLOCKS & ITEMS
    public static final Block ALLIGATOR_EGG = registerBlock("alligator_egg", AlligatorEggBlock::new, BlockBehaviour.Properties.ofLegacyCopy(Blocks.TURTLE_EGG));
    public static final Item DUCK_EGG = registerItem("duck_egg", DuckEggItem::new);
    public static final Block TORTOISE_EGG = registerBlock("tortoise_egg", TortoiseEggBlock::new, BlockBehaviour.Properties.ofLegacyCopy(Blocks.TURTLE_EGG));
    public static final Item COOKED_EGG = registerItem("cooked_egg", properties -> new Item(properties.food(Foods.BREAD)));
    public static final Block SNAIL_EGGS = registerBlock("snail_eggs", SnailEggBlock::new, BlockBehaviour.Properties.ofLegacyCopy(Blocks.FROGSPAWN));
    public static final Block CATTAIL = registerBlock("cattail", CattailBlock::new, BlockBehaviour.Properties.of().noCollission().randomTicks().instabreak().sound(SoundType.SMALL_DRIPLEAF).offsetType(BlockBehaviour.OffsetType.XZ));
    public static final Item CATTAIL_FLUFF = registerItem("cattail_fluff");
    public static final Item ANTLER = registerItem("antler");
    public static final Block GLOW_GOOP_BLOCK = registerOnlyBlock("glow_goop", GlowGoopBlock::new, BlockBehaviour.Properties.of().strength(0.5F).replaceable().noOcclusion().noCollission().lightLevel(GlowGoopBlock.LIGHT_EMISSION).sound(SoundType.HONEY_BLOCK));
    public static final Item GLOW_GOOP = registerItem("glow_goop", properties -> new GlowGoopItem(GLOW_GOOP_BLOCK, properties));
    public static final Item BEAR_FUR = registerItem("bear_fur");
    public static final Block TEDDY_BEAR = registerBlock("teddy_bear", TeddyBearBlock::new, BlockBehaviour.Properties.of().strength(0.8f).sound(SoundType.WOOL).noOcclusion());
    public static final Item DUCK = registerItem("duck", properties -> new Item(properties.food(Foods.CHICKEN)));
    public static final Item COOKED_DUCK = registerItem("cooked_duck", properties -> new Item(properties.food(Foods.COOKED_CHICKEN)));
    public static final Item VENISON = registerItem("venison", properties -> new Item(properties.food(Foods.MUTTON)));
    public static final Item COOKED_VENISON = registerItem("cooked_venison", properties -> new Item(properties.food(Foods.COOKED_MUTTON)));
    public static final Item LIZARD_TAIL = registerItem("lizard_tail", properties -> new Item(properties.food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8F).build(), ModConsumables.LIZARD_TAIL)));
    public static final Item COOKED_LIZARD_TAIL = registerItem("cooked_lizard_tail", properties -> new Item(properties.food(Foods.BAKED_POTATO)));
    public static final Item CATFISH_BUCKET = registerMobBucketItem("catfish_bucket", () -> NaturalistEntityTypes.CATFISH, () -> Fluids.WATER, () -> SoundEvents.BUCKET_EMPTY_FISH);
    public static final Item BASS_BUCKET = registerMobBucketItem("bass_bucket", () -> NaturalistEntityTypes.BASS, () -> Fluids.WATER, () -> SoundEvents.BUCKET_EMPTY_FISH);
    public static final Item CATFISH = registerItem("catfish", properties -> new Item(properties.food(Foods.SALMON)));
    public static final Item COOKED_CATFISH = registerItem("cooked_catfish", properties -> new Item(properties.food(Foods.COOKED_SALMON)));
    public static final Item BASS = registerItem("bass", properties -> new Item(properties.food(Foods.COD)));
    public static final Item COOKED_BASS = registerItem("cooked_bass", properties -> new Item(properties.food(Foods.COOKED_COD)));
    public static final Item BUG_NET = registerItem("bug_net", properties -> new Item(properties.durability(64)));
    public static final Block CHRYSALIS_BLOCK = registerOnlyBlock("chrysalis", ChrysalisBlock::new, BlockBehaviour.Properties.of().randomTicks().strength(0.2F, 3.0F).sound(SoundType.GRASS).noOcclusion().noCollission().pushReaction(PushReaction.DESTROY));
    public static final Item CHRYSALIS = registerItem("chrysalis", properties -> new BlockItem(CHRYSALIS_BLOCK, properties.stacksTo(1).useBlockDescriptionPrefix()));
    public static final Item CATERPILLAR = registerCaughtMobItem("caterpillar", () -> NaturalistEntityTypes.CATERPILLAR, () -> Fluids.EMPTY, NaturalistSoundEvents.SNAIL_FORWARD);
    public static final Item BUTTERFLY = registerCaughtMobItem("butterfly", () -> NaturalistEntityTypes.BUTTERFLY, () -> Fluids.EMPTY, NaturalistSoundEvents.BIRD_FLY, Butterfly.Variant.values().length);
    public static final Item SNAIL_SHELL = registerItem("snail_shell");
    public static final Item SNAIL_BUCKET = registerNoFluidMobBucketItem("snail_bucket", () -> NaturalistEntityTypes.SNAIL, () -> Fluids.EMPTY, NaturalistSoundEvents.BUCKET_EMPTY_SNAIL, Snail.Color.values().length);
    public static final Block DUCKWEED_BLOCK = registerOnlyBlock("duckweed", WaterlilyBlock::new, BlockBehaviour.Properties.of().noCollission().randomTicks().instabreak().sound(SoundType.SMALL_DRIPLEAF).replaceable().ignitedByLava().pushReaction(PushReaction.DESTROY));
    public static final Item DUCKWEED = registerItem("duckweed", properties -> new PlaceOnWaterBlockItem(DUCKWEED_BLOCK, properties.useBlockDescriptionPrefix()));
    // endregion

    public static final Block AZURE_FROGLASS = registerBlock("azure_froglass", TransparentBlock::new, BlockBehaviour.Properties.ofLegacyCopy(Blocks.GLASS));
    public static final Block VERDANT_FROGLASS = registerBlock("verdant_froglass", TransparentBlock::new, BlockBehaviour.Properties.ofLegacyCopy(Blocks.GLASS));
    public static final Block CRIMSON_FROGLASS = registerBlock("crimson_froglass", TransparentBlock::new, BlockBehaviour.Properties.ofLegacyCopy(Blocks.GLASS));
    public static final Block AZURE_FROGLASS_PANE = registerBlock("azure_froglass_pane", IronBarsBlock::new, BlockBehaviour.Properties.ofLegacyCopy(Blocks.GLASS_PANE));
    public static final Block VERDANT_FROGLASS_PANE = registerBlock("verdant_froglass_pane", IronBarsBlock::new, BlockBehaviour.Properties.ofLegacyCopy(Blocks.GLASS_PANE));
    public static final Block CRIMSON_FROGLASS_PANE = registerBlock("crimson_froglass_pane", IronBarsBlock::new, BlockBehaviour.Properties.ofLegacyCopy(Blocks.GLASS_PANE));
    public static final Block SHELLSTONE = registerBlock("shellstone", Block::new, BlockBehaviour.Properties.ofLegacyCopy(Blocks.SANDSTONE));
    public static final Block SHELLSTONE_STAIRS = registerBlock("shellstone_stairs", properties -> new StairBlock(SHELLSTONE.defaultBlockState(), properties), BlockBehaviour.Properties.ofLegacyCopy(Blocks.SANDSTONE));
    public static final Block SHELLSTONE_SLAB = registerBlock("shellstone_slab", SlabBlock::new, BlockBehaviour.Properties.ofLegacyCopy(Blocks.SANDSTONE));
    public static final Block SHELLSTONE_WALL = registerBlock("shellstone_wall", WallBlock::new, BlockBehaviour.Properties.ofLegacyCopy(Blocks.SANDSTONE));
    public static final Block SHELLSTONE_BRICKS = registerBlock("shellstone_bricks", Block::new, BlockBehaviour.Properties.ofLegacyCopy(Blocks.SANDSTONE));
    public static final Block SHELLSTONE_BRICK_STAIRS = registerBlock("shellstone_brick_stairs", properties -> new StairBlock(SHELLSTONE_BRICKS.defaultBlockState(), properties), BlockBehaviour.Properties.ofLegacyCopy(Blocks.SANDSTONE));
    public static final Block SHELLSTONE_BRICK_SLAB = registerBlock("shellstone_brick_slab", SlabBlock::new, BlockBehaviour.Properties.ofLegacyCopy(Blocks.SANDSTONE));
    public static final Block SHELLSTONE_BRICK_WALL = registerBlock("shellstone_brick_wall", WallBlock::new, BlockBehaviour.Properties.ofLegacyCopy(Blocks.SANDSTONE));
    public static final Block CUT_SHELLSTONE = registerBlock("cut_shellstone", Block::new, BlockBehaviour.Properties.ofLegacyCopy(Blocks.SANDSTONE));
    public static final Block CUT_SHELLSTONE_STAIRS = registerBlock("cut_shellstone_stairs", properties -> new StairBlock(CUT_SHELLSTONE.defaultBlockState(), properties), BlockBehaviour.Properties.ofLegacyCopy(Blocks.SANDSTONE));
    public static final Block CUT_SHELLSTONE_SLAB = registerBlock("cut_shellstone_slab", SlabBlock::new, BlockBehaviour.Properties.ofLegacyCopy(Blocks.SANDSTONE));
    public static final Block CUT_SHELLSTONE_WALL = registerBlock("cut_shellstone_wall", WallBlock::new, BlockBehaviour.Properties.ofLegacyCopy(Blocks.SANDSTONE));
    public static final Block SMOOTH_SHELLSTONE = registerBlock("smooth_shellstone", Block::new, BlockBehaviour.Properties.ofLegacyCopy(Blocks.SANDSTONE));
    public static final Block SMOOTH_SHELLSTONE_STAIRS = registerBlock("smooth_shellstone_stairs", properties -> new StairBlock(SMOOTH_SHELLSTONE.defaultBlockState(), properties), BlockBehaviour.Properties.ofLegacyCopy(Blocks.SANDSTONE));
    public static final Block SMOOTH_SHELLSTONE_SLAB = registerBlock("smooth_shellstone_slab", SlabBlock::new, BlockBehaviour.Properties.ofLegacyCopy(Blocks.SANDSTONE));
    public static final Block SMOOTH_SHELLSTONE_WALL = registerBlock("smooth_shellstone_wall", WallBlock::new, BlockBehaviour.Properties.ofLegacyCopy(Blocks.SANDSTONE));


    //region SPAWN EGGS
    public static final Item ALLIGATOR_SPAWN_EGG = registerItem("alligator_spawn_egg", properties -> new SpawnEggItem(NaturalistEntityTypes.ALLIGATOR, properties));
    public static final Item BASS_SPAWN_EGG = registerItem("bass_spawn_egg", properties -> new SpawnEggItem(NaturalistEntityTypes.BASS, properties));
    public static final Item BEAR_SPAWN_EGG = registerItem("bear_spawn_egg", properties -> new SpawnEggItem(NaturalistEntityTypes.BEAR, properties));
    public static final Item BLUEJAY_SPAWN_EGG = registerItem("bluejay_spawn_egg", properties -> new SpawnEggItem(NaturalistEntityTypes.BLUEJAY, properties));
    public static final Item BOAR_SPAWN_EGG = registerItem("boar_spawn_egg", properties -> new SpawnEggItem(NaturalistEntityTypes.BOAR, properties));
    public static final Item BUTTERFLY_SPAWN_EGG = registerItem("butterfly_spawn_egg", properties -> new SpawnEggItem(NaturalistEntityTypes.BUTTERFLY, properties));
    public static final Item CANARY_SPAWN_EGG = registerItem("canary_spawn_egg", properties -> new SpawnEggItem(NaturalistEntityTypes.CANARY, properties));
    public static final Item CARDINAL_SPAWN_EGG = registerItem("cardinal_spawn_egg", properties -> new SpawnEggItem(NaturalistEntityTypes.CARDINAL, properties));
    public static final Item CATFISH_SPAWN_EGG = registerItem("catfish_spawn_egg", properties -> new SpawnEggItem(NaturalistEntityTypes.CATFISH, properties));
    public static final Item CATERPILLAR_SPAWN_EGG = registerItem("caterpillar_spawn_egg", properties -> new SpawnEggItem(NaturalistEntityTypes.CATERPILLAR, properties));
    public static final Item CORAL_SNAKE_SPAWN_EGG = registerItem("coral_snake_spawn_egg", properties -> new SpawnEggItem(NaturalistEntityTypes.CORAL_SNAKE, properties));
    public static final Item DEER_SPAWN_EGG = registerItem("deer_spawn_egg", properties -> new SpawnEggItem(NaturalistEntityTypes.DEER, properties));
    public static final Item DRAGONFLY_SPAWN_EGG = registerItem("dragonfly_spawn_egg", properties -> new SpawnEggItem(NaturalistEntityTypes.DRAGONFLY, properties));
    public static final Item DUCK_SPAWN_EGG = registerItem("duck_spawn_egg", properties -> new SpawnEggItem(NaturalistEntityTypes.DUCK, properties));
    public static final Item ELEPHANT_SPAWN_EGG = registerItem("elephant_spawn_egg", properties -> new SpawnEggItem(NaturalistEntityTypes.ELEPHANT, properties));
    public static final Item FINCH_SPAWN_EGG = registerItem("finch_spawn_egg", properties -> new SpawnEggItem(NaturalistEntityTypes.FINCH, properties));
    public static final Item FIREFLY_SPAWN_EGG = registerItem("firefly_spawn_egg", properties -> new SpawnEggItem(NaturalistEntityTypes.FIREFLY, properties));
    public static final Item GIRAFFE_SPAWN_EGG = registerItem("giraffe_spawn_egg", properties -> new SpawnEggItem(NaturalistEntityTypes.GIRAFFE, properties));
    public static final Item HIPPO_SPAWN_EGG = registerItem("hippo_spawn_egg", properties -> new SpawnEggItem(NaturalistEntityTypes.HIPPO, properties));
    public static final Item LION_SPAWN_EGG = registerItem("lion_spawn_egg", properties -> new SpawnEggItem(NaturalistEntityTypes.LION, properties));
    public static final Item LIZARD_SPAWN_EGG = registerItem("lizard_spawn_egg", properties -> new SpawnEggItem(NaturalistEntityTypes.LIZARD, properties));
    public static final Item RATTLESNAKE_SPAWN_EGG = registerItem("rattlesnake_spawn_egg", properties -> new SpawnEggItem(NaturalistEntityTypes.RATTLESNAKE, properties));
    public static final Item RHINO_SPAWN_EGG = registerItem("rhino_spawn_egg", properties -> new SpawnEggItem(NaturalistEntityTypes.RHINO, properties));
    public static final Item ROBIN_SPAWN_EGG = registerItem("robin_spawn_egg", properties -> new SpawnEggItem(NaturalistEntityTypes.ROBIN, properties));
    public static final Item SNAKE_SPAWN_EGG = registerItem("snake_spawn_egg", properties -> new SpawnEggItem(NaturalistEntityTypes.SNAKE, properties));
    public static final Item SNAIL_SPAWN_EGG = registerItem("snail_spawn_egg", properties -> new SpawnEggItem(NaturalistEntityTypes.SNAIL, properties));
    public static final Item SPARROW_SPAWN_EGG = registerItem("sparrow_spawn_egg", properties -> new SpawnEggItem(NaturalistEntityTypes.SPARROW, properties));
    public static final Item TORTOISE_SPAWN_EGG = registerItem("tortoise_spawn_egg", properties -> new SpawnEggItem(NaturalistEntityTypes.TORTOISE, properties));
    public static final Item VULTURE_SPAWN_EGG = registerItem("vulture_spawn_egg", properties -> new SpawnEggItem(NaturalistEntityTypes.VULTURE, properties));
    public static final Item ZEBRA_SPAWN_EGG = registerItem("zebra_spawn_egg", properties -> new SpawnEggItem(NaturalistEntityTypes.ZEBRA, properties));
    //endregion



    public static void init() {
    }

    // BLOCK REGISTRY
    private static ResourceKey<Block> naturalistBlockId(String id) {
        return ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, id));
    }

    public static Block registerBlock(String id, Function<BlockBehaviour.Properties, Block> propertiesBlockFunction, BlockBehaviour.Properties properties) {
        return registerBlock(naturalistBlockId(id), propertiesBlockFunction, properties);
    }

    private static Block registerBlock(ResourceKey<Block> blockResourceKey, Function<BlockBehaviour.Properties, Block> propertiesBlockFunction, BlockBehaviour.Properties properties) {
        Block block = propertiesBlockFunction.apply(properties.setId(blockResourceKey));
        Block blockRegistry = Registry.register(BuiltInRegistries.BLOCK, blockResourceKey, block);
        registerBlock(blockRegistry);

        return blockRegistry;
    }

    public static Block registerOnlyBlock(String id, Function<BlockBehaviour.Properties, Block> propertiesBlockFunction, BlockBehaviour.Properties properties) {
        return registerOnlyBlock(naturalistBlockId(id), propertiesBlockFunction, properties);
    }

    private static Block registerOnlyBlock(ResourceKey<Block> blockResourceKey, Function<BlockBehaviour.Properties, Block> propertiesBlockFunction, BlockBehaviour.Properties properties) {
        Block block = propertiesBlockFunction.apply(properties.setId(blockResourceKey));
        return Registry.register(BuiltInRegistries.BLOCK, blockResourceKey, block);
    }

    // ITEM REGISTRY
    private static ResourceKey<Item> naturalistItemId(String $$0) {
        return ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, $$0));
    }

    private static ResourceKey<Item> blockIdToItemId(ResourceKey<Block> $$0) {
        return ResourceKey.create(Registries.ITEM, $$0.location());
    }

    public static Item registerBlock(Block $$0) {
        return registerBlock($$0, BlockItem::new);
    }

    public static Item registerBlock(Block $$0, BiFunction<Block, Item.Properties, Item> $$1) {
        return registerBlock($$0, $$1, new Item.Properties());
    }

    public static Item registerBlock(Block $$0, BiFunction<Block, Item.Properties, Item> $$1, Item.Properties $$2) {
        return registerItem(blockIdToItemId($$0.builtInRegistryHolder().key()), $$2x -> $$1.apply($$0, $$2x), $$2.useBlockDescriptionPrefix());
    }

    public static Item registerItem(String $$0, Function<Item.Properties, Item> $$1) {
        return registerItem(naturalistItemId($$0), $$1, new Item.Properties());
    }

    public static Item registerItem(String $$0) {
        return registerItem(naturalistItemId($$0), Item::new, new Item.Properties());
    }

    public static Item registerItem(ResourceKey<Item> $$0, Function<Item.Properties, Item> $$1, Item.Properties $$2) {
        Item $$3 = $$1.apply($$2.setId($$0));
        if ($$3 instanceof BlockItem $$4) {
            $$4.registerBlocks(Item.BY_BLOCK, $$3);
        }

        return Registry.register(BuiltInRegistries.ITEM, $$0, $$3);
    }

    public static Item registerNoFluidMobBucketItem(String name, Supplier<? extends EntityType<? extends Mob>> entitySupplier, Supplier<? extends Fluid> fluidSupplier, Supplier<? extends SoundEvent> soundSupplier) {
        return registerItem(name, properties -> new NoFluidMobBucketItem(entitySupplier.get(), fluidSupplier.get(), soundSupplier.get(), properties.stacksTo(1)));
    }

    public static Item registerNoFluidMobBucketItem(String name, Supplier<? extends EntityType<? extends Mob>> entitySupplier, Supplier<? extends Fluid> fluidSupplier, Supplier<? extends SoundEvent> soundSupplier, int color) {
        return registerItem(name, properties -> new NoFluidMobBucketWithVariantsItem(entitySupplier, fluidSupplier.get(), soundSupplier.get(), color, properties.stacksTo(1)));
    }

    public static Item registerMobBucketItem(String name, Supplier<? extends EntityType<? extends Mob>> entitySupplier, Supplier<? extends Fluid> fluidSupplier, Supplier<? extends SoundEvent> soundSupplier) {
        return registerItem(name, properties -> new MobBucketItem(entitySupplier.get(), fluidSupplier.get(), soundSupplier.get(), properties.stacksTo(1)));
    }

    public static Item registerCaughtMobItem(String name, Supplier<? extends EntityType<? extends Mob>> entitySupplier, Supplier<? extends Fluid> fluidSupplier, Supplier<? extends SoundEvent> soundSupplier) {
        return registerItem(name, properties -> new CaughtMobItem(entitySupplier.get(), fluidSupplier.get(), soundSupplier.get(), properties.stacksTo(1)));
    }

    public static Item registerCaughtMobItem(String name, Supplier<? extends EntityType<? extends Mob>> entitySupplier, Supplier<? extends Fluid> fluidSupplier, Supplier<? extends SoundEvent> soundSupplier, int variantAmount) {
        return registerItem(name, properties -> new CaughtMobWithVariantsItem(entitySupplier.get(), fluidSupplier.get(), soundSupplier.get(), variantAmount, properties.stacksTo(1)));
    }
}
