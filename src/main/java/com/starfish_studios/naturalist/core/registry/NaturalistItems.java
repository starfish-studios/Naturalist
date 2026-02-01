package com.starfish_studios.naturalist.core.registry;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.common.entity.Butterfly;
import com.starfish_studios.naturalist.common.entity.Snail;
import com.starfish_studios.naturalist.common.item.BugNetItem;
import com.starfish_studios.naturalist.common.item.DuckEggItem;
import com.starfish_studios.naturalist.core.platform.CommonPlatformHelper;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluids;

import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings("deprecation")
public class NaturalistItems {
        // Helper to create item resource key
        private static ResourceKey<Item> itemKey(String name) {
                return ResourceKey.create(Registries.ITEM,
                                ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, name));
        }

        // meat() method removed in 1.21
        private static final FoodProperties RAW_BUSHMEAT_FOOD = new FoodProperties.Builder().nutrition(3)
                        .build();
        private static final FoodProperties COOKED_BUSHMEAT_FOOD = new FoodProperties.Builder().nutrition(8)
                        .build();
        private static final FoodProperties LIZARD_TAIL_FOOD = new FoodProperties.Builder()
                        .nutrition(2)
                        .saturationModifier(0.8F)
                        .build();

        public static final Supplier<Item> BUSHMEAT = food("bushmeat", RAW_BUSHMEAT_FOOD);
        public static final Supplier<Item> COOKED_BUSHMEAT = food("cooked_bushmeat", COOKED_BUSHMEAT_FOOD);
        public static final Supplier<Item> FUR = simpleItem("fur");

        public static final Supplier<Item> DUCK_EGG = registerItem("duck_egg",
                        props -> new DuckEggItem(props));
        public static final Supplier<Item> COOKED_EGG = food("cooked_egg", Foods.BREAD);
        public static final Supplier<Item> ANTLER = simpleItem("antler");

        // GLOW_GOOP and CHRYSALIS are registered in NaturalistBlocks to avoid circular
        // dependency
        // Access them via NaturalistBlocks.GLOW_GOOP_ITEM and
        // NaturalistBlocks.CHRYSALIS_ITEM

        // Convenience getters that delegate to NaturalistBlocks
        // These are available after init() is called
        public static Supplier<Item> getGlowGoop() {
                return NaturalistBlocks.GLOW_GOOP_ITEM;
        }

        public static Supplier<Item> getChrysalis() {
                return NaturalistBlocks.CHRYSALIS_ITEM;
        }

        public static final Supplier<Item> DUCK = food("duck", Foods.CHICKEN);
        public static final Supplier<Item> COOKED_DUCK = food("cooked_duck", Foods.COOKED_CHICKEN);
        public static final Supplier<Item> VENISON = food("venison", Foods.MUTTON);
        public static final Supplier<Item> COOKED_VENISON = food("cooked_venison", Foods.COOKED_MUTTON);

        public static final Supplier<Item> LIZARD_TAIL = food("lizard_tail", LIZARD_TAIL_FOOD);
        public static final Supplier<Item> COOKED_LIZARD_TAIL = food("cooked_lizard_tail", Foods.BAKED_POTATO);

        public static final Supplier<Item> CATFISH_BUCKET = waterMobBucket("catfish_bucket",
                        NaturalistEntityTypes.CATFISH);
        public static final Supplier<Item> BASS_BUCKET = waterMobBucket("bass_bucket", NaturalistEntityTypes.BASS);
        public static final Supplier<Item> SNAIL_BUCKET = CommonPlatformHelper.registerNoFluidMobBucketItem(
                        "snail_bucket",
                        NaturalistEntityTypes.SNAIL, () -> Fluids.EMPTY, NaturalistSoundEvents.BUCKET_EMPTY_SNAIL,
                        Snail.Color.values().length);

        public static final Supplier<Item> CATFISH = food("catfish", Foods.SALMON);
        public static final Supplier<Item> COOKED_CATFISH = food("cooked_catfish", Foods.COOKED_SALMON);
        public static final Supplier<Item> BASS = food("bass", Foods.COD);
        public static final Supplier<Item> COOKED_BASS = food("cooked_bass", Foods.COOKED_COD);

        public static final Supplier<Item> BUG_NET = registerItem("bug_net",
                        props -> new BugNetItem(props.durability(64)));
        public static final Supplier<Item> CATERPILLAR = CommonPlatformHelper.registerCaughtMobItem("caterpillar",
                        NaturalistEntityTypes.CATERPILLAR, () -> Fluids.EMPTY, NaturalistSoundEvents.SNAIL_FORWARD);
        public static final Supplier<Item> BUTTERFLY = CommonPlatformHelper.registerCaughtMobItem("butterfly",
                        NaturalistEntityTypes.BUTTERFLY, () -> Fluids.EMPTY, NaturalistSoundEvents.BIRD_FLY,
                        Butterfly.Variant.values().length);
        public static final Supplier<Item> SNAIL_SHELL = simpleItem("snail_shell");

        public static final Supplier<Item> ALLIGATOR_SPAWN_EGG = spawnEgg("alligator_spawn_egg",
                        NaturalistEntityTypes.ALLIGATOR, 6184228, 13810273);
        public static final Supplier<Item> BASS_SPAWN_EGG = spawnEgg("bass_spawn_egg",
                        NaturalistEntityTypes.BASS,
                        8159273, 14729339);
        public static final Supplier<Item> BEAR_SPAWN_EGG = spawnEgg("bear_spawn_egg",
                        NaturalistEntityTypes.BEAR,
                        6569255, 13150577);
        public static final Supplier<Item> BLUEJAY_SPAWN_EGG = spawnEgg("bluejay_spawn_egg",
                        NaturalistEntityTypes.BLUEJAY, 2830129, 4289464);
        public static final Supplier<Item> BOAR_SPAWN_EGG = spawnEgg("boar_spawn_egg",
                        NaturalistEntityTypes.BOAR,
                        6768433, 9854549);
        public static final Supplier<Item> BUTTERFLY_SPAWN_EGG = spawnEgg("butterfly_spawn_egg",
                        NaturalistEntityTypes.BUTTERFLY, 15165706, 6828564);
        public static final Supplier<Item> CANARY_SPAWN_EGG = spawnEgg("canary_spawn_egg",
                        NaturalistEntityTypes.CANARY, 16704333, 13999625);
        public static final Supplier<Item> CARDINAL_SPAWN_EGG = spawnEgg("cardinal_spawn_egg",
                        NaturalistEntityTypes.CARDINAL, 13772840, 4465186);
        public static final Supplier<Item> CATFISH_SPAWN_EGG = spawnEgg("catfish_spawn_egg",
                        NaturalistEntityTypes.CATFISH, 8416033, 12233092);
        public static final Supplier<Item> CATERPILLAR_SPAWN_EGG = spawnEgg("caterpillar_spawn_egg",
                        NaturalistEntityTypes.CATERPILLAR, 3815473, 15647488);
        public static final Supplier<Item> CORAL_SNAKE_SPAWN_EGG = spawnEgg("coral_snake_spawn_egg",
                        NaturalistEntityTypes.CORAL_SNAKE, 3485226, 12261376);
        public static final Supplier<Item> DEER_SPAWN_EGG = spawnEgg("deer_spawn_egg",
                        NaturalistEntityTypes.DEER,
                        10318165, 14531208);
        public static final Supplier<Item> DRAGONFLY_SPAWN_EGG = spawnEgg("dragonfly_spawn_egg",
                        NaturalistEntityTypes.DRAGONFLY, 7507200, 16771840);
        public static final Supplier<Item> DUCK_SPAWN_EGG = spawnEgg("duck_spawn_egg",
                        NaturalistEntityTypes.DUCK,
                        13286315, 2333491);
        public static final Supplier<Item> ELEPHANT_SPAWN_EGG = spawnEgg("elephant_spawn_egg",
                        NaturalistEntityTypes.ELEPHANT, 9539213, 6643034);
        public static final Supplier<Item> FINCH_SPAWN_EGG = spawnEgg("finch_spawn_egg",
                        NaturalistEntityTypes.FINCH, 12013877, 6576975);
        public static final Supplier<Item> FIREFLY_SPAWN_EGG = spawnEgg("firefly_spawn_egg",
                        NaturalistEntityTypes.FIREFLY, 6764577, 16768800);
        public static final Supplier<Item> GIRAFFE_SPAWN_EGG = spawnEgg("giraffe_spawn_egg",
                        NaturalistEntityTypes.GIRAFFE, 14329967, 7619616);
        public static final Supplier<Item> HIPPO_SPAWN_EGG = spawnEgg("hippo_spawn_egg",
                        NaturalistEntityTypes.HIPPO, 15702682, 9004386);
        public static final Supplier<Item> LION_SPAWN_EGG = spawnEgg("lion_spawn_egg",
                        NaturalistEntityTypes.LION,
                        14990722, 6699537);
        public static final Supplier<Item> LIZARD_SPAWN_EGG = spawnEgg("lizard_spawn_egg",
                        NaturalistEntityTypes.LIZARD, 10853166, 15724462);
        public static final Supplier<Item> RATTLESNAKE_SPAWN_EGG = spawnEgg("rattlesnake_spawn_egg",
                        NaturalistEntityTypes.RATTLESNAKE, 16039772, 7293214);
        public static final Supplier<Item> RHINO_SPAWN_EGG = spawnEgg("rhino_spawn_egg",
                        NaturalistEntityTypes.RHINO, 7626842, 10982025);
        public static final Supplier<Item> ROBIN_SPAWN_EGG = spawnEgg("robin_spawn_egg",
                        NaturalistEntityTypes.ROBIN, 4865860, 16620592);
        public static final Supplier<Item> SNAKE_SPAWN_EGG = spawnEgg("snake_spawn_egg",
                        NaturalistEntityTypes.SNAKE, 8813107, 15524255);
        public static final Supplier<Item> SNAIL_SPAWN_EGG = spawnEgg("snail_spawn_egg",
                        NaturalistEntityTypes.SNAIL, 5457209, 8811878);
        public static final Supplier<Item> SPARROW_SPAWN_EGG = spawnEgg("sparrow_spawn_egg",
                        NaturalistEntityTypes.SPARROW, 6504493, 14603707);
        public static final Supplier<Item> TORTOISE_SPAWN_EGG = spawnEgg("tortoise_spawn_egg",
                        NaturalistEntityTypes.TORTOISE, 15724462, 11765582);
        public static final Supplier<Item> VULTURE_SPAWN_EGG = spawnEgg("vulture_spawn_egg",
                        NaturalistEntityTypes.VULTURE, 4010022, 15325376);
        public static final Supplier<Item> ZEBRA_SPAWN_EGG = spawnEgg("zebra_spawn_egg",
                        NaturalistEntityTypes.ZEBRA, 15263457, 1710104);

        public static void init() {
        }

        // MC 1.21 requires Item.Properties to have setId() called before creating the
        // Item
        private static <T extends Item> Supplier<T> registerItem(String name, Function<Item.Properties, T> factory) {
                Item.Properties props = new Item.Properties().setId(itemKey(name));
                return CommonPlatformHelper.registerItem(name, () -> factory.apply(props));
        }

        private static Supplier<Item> simpleItem(String name) {
                return registerItem(name, Item::new);
        }

        private static Supplier<Item> food(String name, FoodProperties properties) {
                Item.Properties props = new Item.Properties().food(properties).setId(itemKey(name));
                return CommonPlatformHelper.registerItem(name, () -> new Item(props));
        }

        private static <T extends Mob> Supplier<Item> waterMobBucket(String name, Supplier<EntityType<T>> type) {
                return CommonPlatformHelper.registerMobBucketItem(name, type, () -> Fluids.WATER,
                                () -> SoundEvents.BUCKET_EMPTY_FISH);
        }

        private static <T extends Mob> Supplier<Item> spawnEgg(String name, Supplier<EntityType<T>> type,
                        int primaryColor, int secondaryColor) {
                return CommonPlatformHelper.registerSpawnEggItem(name, type, primaryColor, secondaryColor);
        }
}
