package com.starfish_studios.naturalist.registry;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.common.entity.*;
import com.starfish_studios.naturalist.common.entity.core.projectile.ThrownDuckEgg;
import com.starfish_studios.naturalist.platform.CommonPlatformHelper;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import java.util.function.Supplier;

public class NaturalistEntityTypes {
    public static final EntityType<Alligator> ALLIGATOR = register("alligator", EntityType.Builder.of(Alligator::new, MobCategory.CREATURE).sized(1.8F, 0.8F).clientTrackingRange(10));
    public static final EntityType<Bass> BASS = register("bass", EntityType.Builder.of(Bass::new, MobCategory.WATER_AMBIENT).sized(0.7f, 0.4f).clientTrackingRange(4));
    public static final EntityType<Bear> BEAR = register("bear", EntityType.Builder.of(Bear::new, MobCategory.CREATURE).sized(1.4F, 1.7F).clientTrackingRange(10));
    public static final EntityType<Bird> BLUEJAY = register("bluejay", EntityType.Builder.of(Bird::new, MobCategory.CREATURE).sized(0.5F, 0.6F).clientTrackingRange(8));
    public static final EntityType<Boar> BOAR = register("boar", EntityType.Builder.of(Boar::new, MobCategory.CREATURE).sized(0.9f, 0.9f).clientTrackingRange(10));
    public static final EntityType<Butterfly> BUTTERFLY = register("butterfly", EntityType.Builder.of(Butterfly::new, MobCategory.AMBIENT).sized(0.7F, 0.6F).clientTrackingRange(8));
    public static final EntityType<Bird> CANARY = register("canary", EntityType.Builder.of(Bird::new, MobCategory.CREATURE).sized(0.5F, 0.6F).clientTrackingRange(8));
    public static final EntityType<Bird> CARDINAL = register("cardinal", EntityType.Builder.of(Bird::new, MobCategory.CREATURE).sized(0.5F, 0.6F).clientTrackingRange(8));
    public static final EntityType<Caterpillar> CATERPILLAR = register("caterpillar", EntityType.Builder.of(Caterpillar::new, MobCategory.CREATURE).sized(0.4F, 0.4F).clientTrackingRange(10));
    public static final EntityType<Catfish> CATFISH = register("catfish", EntityType.Builder.of(Catfish::new, MobCategory.WATER_AMBIENT).sized(0.7f, 0.4f).clientTrackingRange(4));
    public static final EntityType<Snake> CORAL_SNAKE = register("coral_snake", EntityType.Builder.of(Snake::new, MobCategory.CREATURE).sized(0.6F, 0.7F).clientTrackingRange(8));
    public static final EntityType<Deer> DEER = register("deer", EntityType.Builder.of(Deer::new, MobCategory.CREATURE).sized(1.3F, 1.6F).clientTrackingRange(10));
    public static final EntityType<Dragonfly> DRAGONFLY = register("dragonfly", EntityType.Builder.of(Dragonfly::new, MobCategory.AMBIENT).sized(0.9F, 0.7F).clientTrackingRange(8));
    public static final EntityType<Duck> DUCK = register("duck", EntityType.Builder.of(Duck::new, MobCategory.CREATURE).sized(0.6F, 1.0F).clientTrackingRange(10));
    public static final EntityType<ThrownDuckEgg> DUCK_EGG = register("duck_egg", EntityType.Builder.<ThrownDuckEgg>of(ThrownDuckEgg::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(16));
    public static final EntityType<Elephant> ELEPHANT = register("elephant", EntityType.Builder.of(Elephant::new, MobCategory.CREATURE).sized(2.5F, 3.5F).clientTrackingRange(10));
    public static final EntityType<Firefly> FIREFLY = register("firefly", EntityType.Builder.of(Firefly::new, MobCategory.AMBIENT).sized( 0.7F, 0.6F).clientTrackingRange(8));
    public static final EntityType<Bird> FINCH = register("finch", EntityType.Builder.of(Bird::new, MobCategory.CREATURE).sized(0.5F, 0.6F).clientTrackingRange(8));
    public static final EntityType<Giraffe> GIRAFFE = register("giraffe", EntityType.Builder.of(Giraffe::new, MobCategory.CREATURE).sized(1.9f, 5.4f).clientTrackingRange(10));
    public static final EntityType<Hippo> HIPPO = register("hippo", EntityType.Builder.of(Hippo::new, MobCategory.CREATURE).sized(1.8F, 1.8F).clientTrackingRange(10));
    public static final EntityType<Lion> LION = register("lion", EntityType.Builder.of(Lion::new, MobCategory.CREATURE).sized(1.5F, 1.8F).clientTrackingRange(10));
    public static final EntityType<Lizard> LIZARD = register("lizard", EntityType.Builder.of(Lizard::new, MobCategory.CREATURE).sized(0.8F, 0.5F).clientTrackingRange(0));
    public static final EntityType<LizardTail> LIZARD_TAIL = register("lizard_tail", EntityType.Builder.of(LizardTail::new, MobCategory.CREATURE).sized(0.7f, 0.5f).clientTrackingRange(10));
//    public static final EntityType<Ostrich> OSTRICH = register("ostrich", EntityType.Builder.of(Ostrich::new, MobCategory.CREATURE).sized(1.4F, 2.1F).clientTrackingRange(10));
    public static final EntityType<Snake> RATTLESNAKE = register("rattlesnake", EntityType.Builder.of(Snake::new, MobCategory.CREATURE).sized(0.6F, 0.7F).clientTrackingRange(8));
    public static final EntityType<Rhino> RHINO = register("rhino", EntityType.Builder.of(Rhino::new, MobCategory.CREATURE).sized(2.5F, 3.0F).clientTrackingRange(10));
    public static final EntityType<Bird> ROBIN = register("robin", EntityType.Builder.of(Bird::new, MobCategory.CREATURE).sized(0.5F, 0.6F).clientTrackingRange(8));
    public static final EntityType<Snail> SNAIL = register("snail", EntityType.Builder.of(Snail::new, MobCategory.CREATURE).sized(0.7F, 0.7F).clientTrackingRange(10));
    public static final EntityType<Snake> SNAKE = register("snake", EntityType.Builder.of(Snake::new, MobCategory.CREATURE).sized(0.6F, 0.7F).clientTrackingRange(8));
    public static final EntityType<Bird> SPARROW = register("sparrow", EntityType.Builder.of(Bird::new, MobCategory.CREATURE).sized(0.5F, 0.6F).clientTrackingRange(8));
    public static final EntityType<Tortoise> TORTOISE = register("tortoise", EntityType.Builder.of(Tortoise::new, MobCategory.CREATURE).sized(1.2F, 0.875F).clientTrackingRange(10));
    public static final EntityType<Vulture> VULTURE = register("vulture", EntityType.Builder.of(Vulture::new, MobCategory.CREATURE).sized(0.9f, 0.5f).clientTrackingRange(10));
    public static final EntityType<Zebra> ZEBRA = register("zebra", EntityType.Builder.of(Zebra::new, MobCategory.CREATURE).sized(1.3964844F, 1.5F).clientTrackingRange(10));

    public static void init() {}

    private static <T extends Entity> EntityType<T> register(ResourceKey<EntityType<?>> $$0, EntityType.Builder<T> $$1) {
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, $$0, $$1.build($$0));
    }

    private static ResourceKey<EntityType<?>> naturalistEntityId(String $$0) {
        return ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, $$0));
    }

    private static <T extends Entity> EntityType<T> register(String $$0, EntityType.Builder<T> $$1) {
        return register(naturalistEntityId($$0), $$1);
    }
}
