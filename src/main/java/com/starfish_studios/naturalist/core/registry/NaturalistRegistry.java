package com.starfish_studios.naturalist.core.registry;

import com.starfish_studios.naturalist.common.block.*;
import com.starfish_studios.naturalist.common.entity.Butterfly;
import com.starfish_studios.naturalist.common.entity.Snail;
import com.starfish_studios.naturalist.common.item.BugNetItem;
import com.starfish_studios.naturalist.common.item.DuckEggItem;
import com.starfish_studios.naturalist.common.item.GlowGoopItem;
import com.starfish_studios.naturalist.core.platform.CommonPlatformHelper;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.PushReaction;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static com.starfish_studios.naturalist.core.platform.CommonPlatformHelper.registerItem;
import static com.starfish_studios.naturalist.core.platform.CommonPlatformHelper.registerMobBucketItem;

public class NaturalistRegistry {

    public static List<ItemStack> collectAllItemStacks() {
        List<ItemStack> stacks = new ArrayList<>();
        try {
            Field[] fields = NaturalistRegistry.class.getDeclaredFields();
            for (Field field : fields) {
                if (!Modifier.isStatic(field.getModifiers())) continue;
                if (!Supplier.class.isAssignableFrom(field.getType())) continue;

                try {
                    @SuppressWarnings("unchecked")
                    Supplier<?> supplier = (Supplier<?>) field.get(null);
                    if (supplier == null) continue;
                    Object value = supplier.get();

                    if (value instanceof Block block) {
                        if (block == GLOW_GOOP_BLOCK.get() || block == CHRYSALIS_BLOCK.get()) {
                            continue;
                        }
                        stacks.add(new ItemStack(block));
                    }
                    else if (value instanceof Item item) {
                        stacks.add(new ItemStack(item));
                    }
                } catch (Exception e) {
                    com.starfish_studios.naturalist.Naturalist.LOGGER.error("Failed to collect item from field {}: {}", field.getName(), e.getMessage());
                }
            }
        } catch (Exception e) {
            com.starfish_studios.naturalist.Naturalist.LOGGER.error("Failed to collect item stacks for creative tab", e);
        }
        com.starfish_studios.naturalist.Naturalist.LOGGER.info("Naturalist creative tab: collected {} items", stacks.size());
        return stacks;
    }


    public static final Supplier<Item> BUSHMEAT = registerItem("bushmeat", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).build())));
    public static final Supplier<Item> COOKED_BUSHMEAT = registerItem("cooked_bushmeat", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(8).build())));
    public static final Supplier<Item> FUR = registerItem("fur", () -> new Item(new Item.Properties()));

    //region BLOCKS & ITEMS
    public static final Supplier<Block> ALLIGATOR_EGG = registerBlock("alligator_egg", () -> new AlligatorEggBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.TURTLE_EGG)));
    public static final Supplier<Item> DUCK_EGG = registerItem("duck_egg", () -> new DuckEggItem(new Item.Properties()));
    public static final Supplier<Block> TORTOISE_EGG = registerBlock("tortoise_egg", () -> new TortoiseEggBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.TURTLE_EGG)));
    public static final Supplier<Item> COOKED_EGG = registerItem("cooked_egg", () -> new Item(new Item.Properties().food(Foods.BREAD)));
    public static final Supplier<Block> SNAIL_EGGS = registerBlock("snail_eggs", () -> new SnailEggBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.FROGSPAWN)));
//    public static final Supplier<Block> CATTAIL = registerBlock("cattail", () -> new CattailBlock(BlockBehaviour.Properties.of().noCollission().randomTicks().instabreak().sound(SoundType.SMALL_DRIPLEAF).offsetType(BlockBehaviour.OffsetType.XZ)));
//    public static final Supplier<Item> CATTAIL_FLUFF = registerItem("cattail_fluff", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> ANTLER = registerItem("antler", () -> new Item(new Item.Properties()));
    public static final Supplier<Block> GLOW_GOOP_BLOCK = registerBlockOnly("glow_goop", () -> new GlowGoopBlock(BlockBehaviour.Properties.of().strength(0.5F).replaceable().noOcclusion().noCollission().lightLevel(GlowGoopBlock.LIGHT_EMISSION).sound(SoundType.HONEY_BLOCK)));
    public static final Supplier<Item> GLOW_GOOP = CommonPlatformHelper.registerItem("glow_goop", () -> new GlowGoopItem(GLOW_GOOP_BLOCK.get(), new Item.Properties()));
    public static final Supplier<Block> TEDDY_BEAR = registerBlock("teddy_bear", () -> new TeddyBearBlock(BlockBehaviour.Properties.of().strength(0.8f).sound(SoundType.WOOL).noOcclusion()));
    public static final Supplier<Item> DUCK = registerItem("duck", () -> new Item(new Item.Properties().food(Foods.CHICKEN)));
    public static final Supplier<Item> COOKED_DUCK = registerItem("cooked_duck", () -> new Item(new Item.Properties().food(Foods.COOKED_CHICKEN)));
    public static final Supplier<Item> VENISON = registerItem("venison", () -> new Item(new Item.Properties().food(Foods.MUTTON)));
    public static final Supplier<Item> COOKED_VENISON = registerItem("cooked_venison", () -> new Item(new Item.Properties().food(Foods.COOKED_MUTTON)));
    public static final Supplier<Item> LIZARD_TAIL = registerItem("lizard_tail", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8F).effect(new MobEffectInstance(MobEffects.POISON, 100, 0), 1.0f).build())));
    public static final Supplier<Item> COOKED_LIZARD_TAIL = registerItem("cooked_lizard_tail", () -> new Item(new Item.Properties().food(Foods.BAKED_POTATO)));
    public static final Supplier<Item> CATFISH_BUCKET = registerMobBucketItem("catfish_bucket", NaturalistEntityTypes.CATFISH, () -> Fluids.WATER, () -> SoundEvents.BUCKET_EMPTY_FISH);
    public static final Supplier<Item> BASS_BUCKET = registerMobBucketItem("bass_bucket", NaturalistEntityTypes.BASS, () -> Fluids.WATER, () -> SoundEvents.BUCKET_EMPTY_FISH);
    public static final Supplier<Item> CATFISH = registerItem("catfish", () -> new Item(new Item.Properties().food(Foods.SALMON)));
    public static final Supplier<Item> COOKED_CATFISH = registerItem("cooked_catfish", () -> new Item(new Item.Properties().food(Foods.COOKED_SALMON)));
    public static final Supplier<Item> BASS = registerItem("bass", () -> new Item(new Item.Properties().food(Foods.COD)));
    public static final Supplier<Item> COOKED_BASS = registerItem("cooked_bass", () -> new Item(new Item.Properties().food(Foods.COOKED_COD)));
    public static final Supplier<Item> BUG_NET = registerItem("bug_net", () -> new BugNetItem(new Item.Properties().durability(64)));
    public static final Supplier<Block> CHRYSALIS_BLOCK = registerBlockOnly("chrysalis", () -> new ChrysalisBlock(BlockBehaviour.Properties.of().randomTicks().strength(0.2F, 3.0F).sound(SoundType.GRASS).noOcclusion().noCollission().pushReaction(PushReaction.DESTROY)));
    public static final Supplier<Item> CHRYSALIS = registerItem("chrysalis", () -> new BlockItem(CHRYSALIS_BLOCK.get(), new Item.Properties().stacksTo(1)));
    public static final Supplier<Item> CATERPILLAR = CommonPlatformHelper.registerCaughtMobItem("caterpillar", NaturalistEntityTypes.CATERPILLAR, () -> Fluids.EMPTY, NaturalistSoundEvents.SNAIL_FORWARD);
    public static final Supplier<Item> BUTTERFLY = CommonPlatformHelper.registerCaughtMobItem("butterfly", NaturalistEntityTypes.BUTTERFLY, () -> Fluids.EMPTY, NaturalistSoundEvents.BIRD_FLY, Butterfly.Variant.values().length);
    public static final Supplier<Item> SNAIL_SHELL = registerItem("snail_shell", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> SNAIL_BUCKET = CommonPlatformHelper.registerNoFluidMobBucketItem("snail_bucket", NaturalistEntityTypes.SNAIL, () -> Fluids.EMPTY, NaturalistSoundEvents.BUCKET_EMPTY_SNAIL, Snail.Color.values().length);
//    public static final Supplier<Block> DUCKWEED_BLOCK = registerBlockOnly("duckweed", () -> new WaterlilyBlock(BlockBehaviour.Properties.of().noCollission().randomTicks().instabreak().sound(SoundType.SMALL_DRIPLEAF).replaceable().ignitedByLava().pushReaction(PushReaction.DESTROY)));
//    public static final Supplier<Item> DUCKWEED = CommonPlatformHelper.registerItem("duckweed", () -> new PlaceOnWaterBlockItem(DUCKWEED_BLOCK.get(), new Item.Properties()));
    // endregion


    public static final Supplier<Block> AZURE_FROGLASS = registerBlock("azure_froglass", () -> new TransparentBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS)));
    public static final Supplier<Block> VERDANT_FROGLASS = registerBlock("verdant_froglass", () -> new TransparentBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS)));
    public static final Supplier<Block> CRIMSON_FROGLASS = registerBlock("crimson_froglass", () -> new TransparentBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS)));
    public static final Supplier<Block> AZURE_FROGLASS_PANE = registerBlock("azure_froglass_pane", () -> new IronBarsBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS_PANE)));
    public static final Supplier<Block> VERDANT_FROGLASS_PANE = registerBlock("verdant_froglass_pane", () -> new IronBarsBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS_PANE)));
    public static final Supplier<Block> CRIMSON_FROGLASS_PANE = registerBlock("crimson_froglass_pane", () -> new IronBarsBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS_PANE)));
    public static final Supplier<Block> SHELLSTONE = registerBlock("shellstone", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE)));
    public static final Supplier<Block> SHELLSTONE_STAIRS = registerBlock("shellstone_stairs", () -> new StairBlock(SHELLSTONE.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE)));
    public static final Supplier<Block> SHELLSTONE_SLAB = registerBlock("shellstone_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE)));
    public static final Supplier<Block> SHELLSTONE_WALL = registerBlock("shellstone_wall", () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE)));
    public static final Supplier<Block> SHELLSTONE_BRICKS = registerBlock("shellstone_bricks", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE)));
    public static final Supplier<Block> SHELLSTONE_BRICK_STAIRS = registerBlock("shellstone_brick_stairs", () -> new StairBlock(SHELLSTONE_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE)));
    public static final Supplier<Block> SHELLSTONE_BRICK_SLAB = registerBlock("shellstone_brick_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE)));
    public static final Supplier<Block> SHELLSTONE_BRICK_WALL = registerBlock("shellstone_brick_wall", () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE)));
    public static final Supplier<Block> CUT_SHELLSTONE = registerBlock("cut_shellstone", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE)));
    public static final Supplier<Block> CUT_SHELLSTONE_STAIRS = registerBlock("cut_shellstone_stairs", () -> new StairBlock(CUT_SHELLSTONE.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE)));
    public static final Supplier<Block> CUT_SHELLSTONE_SLAB = registerBlock("cut_shellstone_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE)));
    public static final Supplier<Block> CUT_SHELLSTONE_WALL = registerBlock("cut_shellstone_wall", () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE)));
    public static final Supplier<Block> SMOOTH_SHELLSTONE = registerBlock("smooth_shellstone", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE)));
    public static final Supplier<Block> SMOOTH_SHELLSTONE_STAIRS = registerBlock("smooth_shellstone_stairs", () -> new StairBlock(SMOOTH_SHELLSTONE.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE)));
    public static final Supplier<Block> SMOOTH_SHELLSTONE_SLAB = registerBlock("smooth_shellstone_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE)));
    public static final Supplier<Block> SMOOTH_SHELLSTONE_WALL = registerBlock("smooth_shellstone_wall", () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE)));


    //region SPAWN EGGS
    public static final Supplier<SpawnEggItem> ALLIGATOR_SPAWN_EGG = CommonPlatformHelper.registerSpawnEggItem("alligator_spawn_egg", NaturalistEntityTypes.ALLIGATOR, 6184228, 13810273);
    public static final Supplier<SpawnEggItem> BASS_SPAWN_EGG = CommonPlatformHelper.registerSpawnEggItem("bass_spawn_egg", NaturalistEntityTypes.BASS, 8159273, 14729339);
    public static final Supplier<SpawnEggItem> BEAR_SPAWN_EGG = CommonPlatformHelper.registerSpawnEggItem("bear_spawn_egg", NaturalistEntityTypes.BEAR, 6569255, 13150577);
    public static final Supplier<SpawnEggItem> BLUEJAY_SPAWN_EGG = CommonPlatformHelper.registerSpawnEggItem("bluejay_spawn_egg", NaturalistEntityTypes.BLUEJAY, 2830129, 4289464);
    public static final Supplier<SpawnEggItem> BOAR_SPAWN_EGG = CommonPlatformHelper.registerSpawnEggItem("boar_spawn_egg", NaturalistEntityTypes.BOAR, 6768433, 9854549);
    public static final Supplier<SpawnEggItem> BUTTERFLY_SPAWN_EGG = CommonPlatformHelper.registerSpawnEggItem("butterfly_spawn_egg", NaturalistEntityTypes.BUTTERFLY, 15165706, 6828564);
    public static final Supplier<SpawnEggItem> CANARY_SPAWN_EGG = CommonPlatformHelper.registerSpawnEggItem("canary_spawn_egg", NaturalistEntityTypes.CANARY, 16704333, 13999625);
    public static final Supplier<SpawnEggItem> CARDINAL_SPAWN_EGG = CommonPlatformHelper.registerSpawnEggItem("cardinal_spawn_egg", NaturalistEntityTypes.CARDINAL, 13772840, 4465186);
    public static final Supplier<SpawnEggItem> CATFISH_SPAWN_EGG = CommonPlatformHelper.registerSpawnEggItem("catfish_spawn_egg", NaturalistEntityTypes.CATFISH, 8416033, 12233092);
    public static final Supplier<SpawnEggItem> CATERPILLAR_SPAWN_EGG = CommonPlatformHelper.registerSpawnEggItem("caterpillar_spawn_egg", NaturalistEntityTypes.CATERPILLAR, 3815473, 15647488);
    public static final Supplier<SpawnEggItem> CORAL_SNAKE_SPAWN_EGG = CommonPlatformHelper.registerSpawnEggItem("coral_snake_spawn_egg", NaturalistEntityTypes.CORAL_SNAKE, 3485226, 12261376);
    public static final Supplier<SpawnEggItem> DEER_SPAWN_EGG = CommonPlatformHelper.registerSpawnEggItem("deer_spawn_egg", NaturalistEntityTypes.DEER, 10318165, 14531208);
    public static final Supplier<SpawnEggItem> DRAGONFLY_SPAWN_EGG = CommonPlatformHelper.registerSpawnEggItem("dragonfly_spawn_egg", NaturalistEntityTypes.DRAGONFLY, 7507200, 16771840);
    public static final Supplier<SpawnEggItem> DUCK_SPAWN_EGG = CommonPlatformHelper.registerSpawnEggItem("duck_spawn_egg", NaturalistEntityTypes.DUCK, 13286315, 2333491);
    public static final Supplier<SpawnEggItem> ELEPHANT_SPAWN_EGG = CommonPlatformHelper.registerSpawnEggItem("elephant_spawn_egg", NaturalistEntityTypes.ELEPHANT, 9539213, 6643034);
    public static final Supplier<SpawnEggItem> FINCH_SPAWN_EGG = CommonPlatformHelper.registerSpawnEggItem("finch_spawn_egg", NaturalistEntityTypes.FINCH, 12013877, 6576975);
    public static final Supplier<SpawnEggItem> FIREFLY_SPAWN_EGG = CommonPlatformHelper.registerSpawnEggItem("firefly_spawn_egg", NaturalistEntityTypes.FIREFLY, 6764577, 16768800);
    public static final Supplier<SpawnEggItem> GIRAFFE_SPAWN_EGG = CommonPlatformHelper.registerSpawnEggItem("giraffe_spawn_egg", NaturalistEntityTypes.GIRAFFE, 14329967, 7619616);
    public static final Supplier<SpawnEggItem> HIPPO_SPAWN_EGG = CommonPlatformHelper.registerSpawnEggItem("hippo_spawn_egg", NaturalistEntityTypes.HIPPO, 15702682, 9004386);
    public static final Supplier<SpawnEggItem> LION_SPAWN_EGG = CommonPlatformHelper.registerSpawnEggItem("lion_spawn_egg", NaturalistEntityTypes.LION, 14990722, 6699537);
    public static final Supplier<SpawnEggItem> LIZARD_SPAWN_EGG = CommonPlatformHelper.registerSpawnEggItem("lizard_spawn_egg", NaturalistEntityTypes.LIZARD, 10853166, 15724462);
    public static final Supplier<SpawnEggItem> RATTLESNAKE_SPAWN_EGG = CommonPlatformHelper.registerSpawnEggItem("rattlesnake_spawn_egg", NaturalistEntityTypes.RATTLESNAKE, 16039772, 7293214);
    public static final Supplier<SpawnEggItem> RHINO_SPAWN_EGG = CommonPlatformHelper.registerSpawnEggItem("rhino_spawn_egg", NaturalistEntityTypes.RHINO, 7626842, 10982025);
    public static final Supplier<SpawnEggItem> ROBIN_SPAWN_EGG = CommonPlatformHelper.registerSpawnEggItem("robin_spawn_egg", NaturalistEntityTypes.ROBIN, 4865860, 16620592);
    public static final Supplier<SpawnEggItem> SNAKE_SPAWN_EGG = CommonPlatformHelper.registerSpawnEggItem("snake_spawn_egg", NaturalistEntityTypes.SNAKE, 8813107, 15524255);
    public static final Supplier<SpawnEggItem> SNAIL_SPAWN_EGG = CommonPlatformHelper.registerSpawnEggItem("snail_spawn_egg", NaturalistEntityTypes.SNAIL, 5457209, 8811878);
    public static final Supplier<SpawnEggItem> SPARROW_SPAWN_EGG = CommonPlatformHelper.registerSpawnEggItem("sparrow_spawn_egg", NaturalistEntityTypes.SPARROW, 6504493, 14603707);
    public static final Supplier<SpawnEggItem> TORTOISE_SPAWN_EGG = CommonPlatformHelper.registerSpawnEggItem("tortoise_spawn_egg", NaturalistEntityTypes.TORTOISE, 15724462, 11765582);
    public static final Supplier<SpawnEggItem> VULTURE_SPAWN_EGG = CommonPlatformHelper.registerSpawnEggItem("vulture_spawn_egg", NaturalistEntityTypes.VULTURE, 4010022, 15325376);
    public static final Supplier<SpawnEggItem> ZEBRA_SPAWN_EGG = CommonPlatformHelper.registerSpawnEggItem("zebra_spawn_egg", NaturalistEntityTypes.ZEBRA, 15263457, 1710104);
    //endregion



    public static void init() {
    }

    public static <T extends Block> Supplier<T> registerBlock(String name, Supplier<T> block) {
        Supplier<T> supplier = CommonPlatformHelper.registerBlock(name, block);
        registerItem(name, () -> new BlockItem(supplier.get(), new Item.Properties()));
        return supplier;
    }

    public static <T extends Block> Supplier<T> registerBlockOnly(String name, Supplier<T> block) {
        return CommonPlatformHelper.registerBlock(name, block);
    }

    public static void addAllToCreativeTab() {
        Field[] fields = NaturalistRegistry.class.getDeclaredFields();
        for (Field field : fields) {
            ParameterizedType type = (ParameterizedType) field.getGenericType();
            Type rawType = type.getRawType();
            Type[] typeArguments = type.getActualTypeArguments();

            if (rawType == Supplier.class) {
                if (typeArguments.length == 1) {
                    Class<?> arg = (Class<?>) typeArguments[0];
                    if (Block.class.isAssignableFrom(arg)) {
                        NaturalistRegistry.addAllToCreativeTab();
                    } else if (Item.class.isAssignableFrom(arg) || SpawnEggItem.class.isAssignableFrom(arg)) {
                        NaturalistRegistry.addAllToCreativeTab();
                    }
                }
            }
        }
    }
}
