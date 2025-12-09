package com.starfish_studios.naturalist.core.registry;

import com.starfish_studios.naturalist.common.entity.Butterfly;
import com.starfish_studios.naturalist.common.entity.Snail;
import com.starfish_studios.naturalist.common.item.BugNetItem;
import com.starfish_studios.naturalist.common.item.DuckEggItem;
import com.starfish_studios.naturalist.common.item.GlowGoopItem;
import com.starfish_studios.naturalist.core.platform.CommonPlatformHelper;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.material.Fluids;

import java.util.function.Supplier;

import static com.starfish_studios.naturalist.core.platform.CommonPlatformHelper.registerItem;
import static com.starfish_studios.naturalist.core.platform.CommonPlatformHelper.registerMobBucketItem;

@SuppressWarnings("deprecation")
public class NaturalistItems {
    private static final FoodProperties RAW_BUSHMEAT_FOOD = new FoodProperties.Builder().nutrition(3).meat().build();
    private static final FoodProperties COOKED_BUSHMEAT_FOOD = new FoodProperties.Builder().nutrition(8).meat().build();
    private static final FoodProperties LIZARD_TAIL_FOOD = new FoodProperties.Builder()
            .nutrition(2)
            .saturationMod(0.8F)
            .meat()
            .effect(new MobEffectInstance(MobEffects.POISON, 100, 0), 1.0f)
            .build();

    public static final Supplier<Item> BUSHMEAT = food("bushmeat", RAW_BUSHMEAT_FOOD);
    public static final Supplier<Item> COOKED_BUSHMEAT = food("cooked_bushmeat", COOKED_BUSHMEAT_FOOD);
    public static final Supplier<Item> FUR = simpleItem("fur");

    public static final Supplier<Item> DUCK_EGG = registerItem("duck_egg", () -> new DuckEggItem(new Item.Properties()));
    public static final Supplier<Item> COOKED_EGG = food("cooked_egg", Foods.BREAD);
    public static final Supplier<Item> ANTLER = simpleItem("antler");

    public static final Supplier<Item> GLOW_GOOP = CommonPlatformHelper.registerItem("glow_goop", () -> new GlowGoopItem(NaturalistBlocks.GLOW_GOOP_BLOCK.get(), new Item.Properties()));
    public static final Supplier<Item> CHRYSALIS = registerItem("chrysalis", () -> new BlockItem(NaturalistBlocks.CHRYSALIS_BLOCK.get(), new Item.Properties().stacksTo(1)));

    public static final Supplier<Item> DUCK = food("duck", Foods.CHICKEN);
    public static final Supplier<Item> COOKED_DUCK = food("cooked_duck", Foods.COOKED_CHICKEN);
    public static final Supplier<Item> VENISON = food("venison", Foods.MUTTON);
    public static final Supplier<Item> COOKED_VENISON = food("cooked_venison", Foods.COOKED_MUTTON);

    public static final Supplier<Item> LIZARD_TAIL = food("lizard_tail", LIZARD_TAIL_FOOD);
    public static final Supplier<Item> COOKED_LIZARD_TAIL = food("cooked_lizard_tail", Foods.BAKED_POTATO);

    public static final Supplier<Item> CATFISH_BUCKET = waterMobBucket("catfish_bucket", NaturalistEntityTypes.CATFISH);
    public static final Supplier<Item> BASS_BUCKET = waterMobBucket("bass_bucket", NaturalistEntityTypes.BASS);
    public static final Supplier<Item> SNAIL_BUCKET = CommonPlatformHelper.registerNoFluidMobBucketItem("snail_bucket", NaturalistEntityTypes.SNAIL, () -> Fluids.EMPTY, NaturalistSoundEvents.BUCKET_EMPTY_SNAIL, Snail.Color.values().length);

    public static final Supplier<Item> CATFISH = food("catfish", Foods.SALMON);
    public static final Supplier<Item> COOKED_CATFISH = food("cooked_catfish", Foods.COOKED_SALMON);
    public static final Supplier<Item> BASS = food("bass", Foods.COD);
    public static final Supplier<Item> COOKED_BASS = food("cooked_bass", Foods.COOKED_COD);

    public static final Supplier<Item> BUG_NET = registerItem("bug_net", () -> new BugNetItem(new Item.Properties().durability(64)));
    public static final Supplier<Item> CATERPILLAR = CommonPlatformHelper.registerCaughtMobItem("caterpillar", NaturalistEntityTypes.CATERPILLAR, () -> Fluids.EMPTY, NaturalistSoundEvents.SNAIL_FORWARD);
    public static final Supplier<Item> BUTTERFLY = CommonPlatformHelper.registerCaughtMobItem("butterfly", NaturalistEntityTypes.BUTTERFLY, () -> Fluids.EMPTY, NaturalistSoundEvents.BIRD_FLY, Butterfly.Variant.values().length);
    public static final Supplier<Item> SNAIL_SHELL = simpleItem("snail_shell");

    public static final Supplier<SpawnEggItem> ALLIGATOR_SPAWN_EGG = spawnEgg("alligator_spawn_egg", NaturalistEntityTypes.ALLIGATOR, 6184228, 13810273);
    public static final Supplier<SpawnEggItem> BASS_SPAWN_EGG = spawnEgg("bass_spawn_egg", NaturalistEntityTypes.BASS, 8159273, 14729339);
    public static final Supplier<SpawnEggItem> BEAR_SPAWN_EGG = spawnEgg("bear_spawn_egg", NaturalistEntityTypes.BEAR, 6569255, 13150577);
    public static final Supplier<SpawnEggItem> BLUEJAY_SPAWN_EGG = spawnEgg("bluejay_spawn_egg", NaturalistEntityTypes.BLUEJAY, 2830129, 4289464);
    public static final Supplier<SpawnEggItem> BOAR_SPAWN_EGG = spawnEgg("boar_spawn_egg", NaturalistEntityTypes.BOAR, 6768433, 9854549);
    public static final Supplier<SpawnEggItem> BUTTERFLY_SPAWN_EGG = spawnEgg("butterfly_spawn_egg", NaturalistEntityTypes.BUTTERFLY, 15165706, 6828564);
    public static final Supplier<SpawnEggItem> CANARY_SPAWN_EGG = spawnEgg("canary_spawn_egg", NaturalistEntityTypes.CANARY, 16704333, 13999625);
    public static final Supplier<SpawnEggItem> CARDINAL_SPAWN_EGG = spawnEgg("cardinal_spawn_egg", NaturalistEntityTypes.CARDINAL, 13772840, 4465186);
    public static final Supplier<SpawnEggItem> CATFISH_SPAWN_EGG = spawnEgg("catfish_spawn_egg", NaturalistEntityTypes.CATFISH, 8416033, 12233092);
    public static final Supplier<SpawnEggItem> CATERPILLAR_SPAWN_EGG = spawnEgg("caterpillar_spawn_egg", NaturalistEntityTypes.CATERPILLAR, 3815473, 15647488);
    public static final Supplier<SpawnEggItem> CORAL_SNAKE_SPAWN_EGG = spawnEgg("coral_snake_spawn_egg", NaturalistEntityTypes.CORAL_SNAKE, 3485226, 12261376);
    public static final Supplier<SpawnEggItem> DEER_SPAWN_EGG = spawnEgg("deer_spawn_egg", NaturalistEntityTypes.DEER, 10318165, 14531208);
    public static final Supplier<SpawnEggItem> DRAGONFLY_SPAWN_EGG = spawnEgg("dragonfly_spawn_egg", NaturalistEntityTypes.DRAGONFLY, 7507200, 16771840);
    public static final Supplier<SpawnEggItem> DUCK_SPAWN_EGG = spawnEgg("duck_spawn_egg", NaturalistEntityTypes.DUCK, 13286315, 2333491);
    public static final Supplier<SpawnEggItem> ELEPHANT_SPAWN_EGG = spawnEgg("elephant_spawn_egg", NaturalistEntityTypes.ELEPHANT, 9539213, 6643034);
    public static final Supplier<SpawnEggItem> FINCH_SPAWN_EGG = spawnEgg("finch_spawn_egg", NaturalistEntityTypes.FINCH, 12013877, 6576975);
    public static final Supplier<SpawnEggItem> FIREFLY_SPAWN_EGG = spawnEgg("firefly_spawn_egg", NaturalistEntityTypes.FIREFLY, 6764577, 16768800);
    public static final Supplier<SpawnEggItem> GIRAFFE_SPAWN_EGG = spawnEgg("giraffe_spawn_egg", NaturalistEntityTypes.GIRAFFE, 14329967, 7619616);
    public static final Supplier<SpawnEggItem> HIPPO_SPAWN_EGG = spawnEgg("hippo_spawn_egg", NaturalistEntityTypes.HIPPO, 15702682, 9004386);
    public static final Supplier<SpawnEggItem> LION_SPAWN_EGG = spawnEgg("lion_spawn_egg", NaturalistEntityTypes.LION, 14990722, 6699537);
    public static final Supplier<SpawnEggItem> LIZARD_SPAWN_EGG = spawnEgg("lizard_spawn_egg", NaturalistEntityTypes.LIZARD, 10853166, 15724462);
    public static final Supplier<SpawnEggItem> RATTLESNAKE_SPAWN_EGG = spawnEgg("rattlesnake_spawn_egg", NaturalistEntityTypes.RATTLESNAKE, 16039772, 7293214);
    public static final Supplier<SpawnEggItem> RHINO_SPAWN_EGG = spawnEgg("rhino_spawn_egg", NaturalistEntityTypes.RHINO, 7626842, 10982025);
    public static final Supplier<SpawnEggItem> ROBIN_SPAWN_EGG = spawnEgg("robin_spawn_egg", NaturalistEntityTypes.ROBIN, 4865860, 16620592);
    public static final Supplier<SpawnEggItem> SNAKE_SPAWN_EGG = spawnEgg("snake_spawn_egg", NaturalistEntityTypes.SNAKE, 8813107, 15524255);
    public static final Supplier<SpawnEggItem> SNAIL_SPAWN_EGG = spawnEgg("snail_spawn_egg", NaturalistEntityTypes.SNAIL, 5457209, 8811878);
    public static final Supplier<SpawnEggItem> SPARROW_SPAWN_EGG = spawnEgg("sparrow_spawn_egg", NaturalistEntityTypes.SPARROW, 6504493, 14603707);
    public static final Supplier<SpawnEggItem> TORTOISE_SPAWN_EGG = spawnEgg("tortoise_spawn_egg", NaturalistEntityTypes.TORTOISE, 15724462, 11765582);
    public static final Supplier<SpawnEggItem> VULTURE_SPAWN_EGG = spawnEgg("vulture_spawn_egg", NaturalistEntityTypes.VULTURE, 4010022, 15325376);
    public static final Supplier<SpawnEggItem> ZEBRA_SPAWN_EGG = spawnEgg("zebra_spawn_egg", NaturalistEntityTypes.ZEBRA, 15263457, 1710104);

    public static void init() {
    }

    private static Supplier<Item> simpleItem(String name) {
        return registerItem(name, () -> new Item(new Item.Properties()));
    }

    private static Supplier<Item> food(String name, FoodProperties properties) {
        return registerItem(name, () -> new Item(new Item.Properties().food(properties)));
    }

    private static <T extends Mob> Supplier<Item> waterMobBucket(String name, Supplier<EntityType<T>> type) {
        return registerMobBucketItem(name, type, () -> Fluids.WATER, () -> SoundEvents.BUCKET_EMPTY_FISH);
    }

    private static <T extends Mob> Supplier<SpawnEggItem> spawnEgg(String name, Supplier<EntityType<T>> type, int primaryColor, int secondaryColor) {
        return CommonPlatformHelper.registerSpawnEggItem(name, type, primaryColor, secondaryColor);
    }
}

