package com.starfish_studios.naturalist.core.registry;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.core.platform.CommonPlatformHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

import java.util.Objects;
import java.util.function.Supplier;

public class NaturalistSoundEvents {

    public static final Supplier<SoundEvent> TORTOISE_HIDE = register("entity.tortoise.hide");
    public static final Supplier<SoundEvent> TORTOISE_THUD = register("entity.tortoise.thud");

    public static final Supplier<SoundEvent> TORTOISE_EGG_BREAK = register("entity.tortoise.egg_break");
    public static final Supplier<SoundEvent> TORTOISE_EGG_CRACK = register("entity.tortoise.egg_crack");
    public static final Supplier<SoundEvent> TORTOISE_EGG_HATCH = register("entity.tortoise.egg_hatch");

    public static final Supplier<SoundEvent> SNAKE_HISS = register("entity.snake.hiss");
    public static final Supplier<SoundEvent> SNAKE_HURT = register("entity.snake.hurt");
    public static final Supplier<SoundEvent> SNAKE_RATTLE = register("entity.snake.rattle");
    public static final Supplier<SoundEvent> SNAIL_CRUSH = register("entity.snail.crush");
    public static final Supplier<SoundEvent> SNAIL_FORWARD = register("entity.snail.forward");
    public static final Supplier<SoundEvent> SNAIL_BACK = register("entity.snail.back");
    public static final Supplier<SoundEvent> BUCKET_FILL_SNAIL = register("item.bucket.fill_snail");
    public static final Supplier<SoundEvent> BUCKET_EMPTY_SNAIL = register("item.bucket.empty_snail");

    public static final Supplier<SoundEvent> BIRD_HURT = register("entity.bird.hurt");
    public static final Supplier<SoundEvent> BIRD_DEATH = register("entity.bird.death");
    public static final Supplier<SoundEvent> BIRD_EAT = register("entity.bird.eat");
    public static final Supplier<SoundEvent> BIRD_FLY = register("entity.bird.fly");
    public static final Supplier<SoundEvent> BIRD_AMBIENT_BLUEJAY = register("entity.bird.ambient_bluejay");
    public static final Supplier<SoundEvent> BIRD_AMBIENT_CANARY = register("entity.bird.ambient_canary");
    public static final Supplier<SoundEvent> BIRD_AMBIENT_ROBIN = register("entity.bird.ambient_robin");
    public static final Supplier<SoundEvent> BIRD_AMBIENT_CARDINAL = register("entity.bird.ambient_cardinal");
    public static final Supplier<SoundEvent> BIRD_AMBIENT_FINCH = register("entity.bird.ambient_finch");
    public static final Supplier<SoundEvent> BIRD_AMBIENT_SPARROW = register("entity.bird.ambient_sparrow");

    public static final Supplier<SoundEvent> FIREFLY_HURT = register("entity.firefly.hurt");
    public static final Supplier<SoundEvent> FIREFLY_DEATH = register("entity.firefly.death");
    public static final Supplier<SoundEvent> FIREFLY_HIDE = register("entity.firefly.hide");

    public static final Supplier<SoundEvent> BEAR_HURT = register("entity.bear.hurt");
    public static final Supplier<SoundEvent> BEAR_DEATH = register("entity.bear.death");
    public static final Supplier<SoundEvent> BEAR_AMBIENT = register("entity.bear.ambient");
    public static final Supplier<SoundEvent> BEAR_AMBIENT_BABY = register("entity.bear.ambient_baby");
    public static final Supplier<SoundEvent> BEAR_HURT_BABY = register("entity.bear.hurt_baby");
    public static final Supplier<SoundEvent> BEAR_SLEEP = register("entity.bear.sleep");
    public static final Supplier<SoundEvent> BEAR_SNIFF = register("entity.bear.sniff");
    public static final Supplier<SoundEvent> BEAR_SPIT = register("entity.bear.spit");
    public static final Supplier<SoundEvent> BEAR_EAT = register("entity.bear.eat");

    public static final Supplier<SoundEvent> DEER_AMBIENT = register("entity.deer.ambient");
    public static final Supplier<SoundEvent> DEER_HURT = register("entity.deer.hurt");
    public static final Supplier<SoundEvent> DEER_AMBIENT_BABY = register("entity.deer.ambient_baby");
    public static final Supplier<SoundEvent> DEER_HURT_BABY = register("entity.deer.hurt_baby");

    public static final Supplier<SoundEvent> RHINO_SCRAPE = register("entity.rhino.scrape");
    public static final Supplier<SoundEvent> RHINO_AMBIENT = register("entity.rhino.ambient");
    public static final Supplier<SoundEvent> RHINO_AMBIENT_BABY = register("entity.rhino.ambient_baby");

    public static final Supplier<SoundEvent> LION_HURT = register("entity.lion.hurt");
    public static final Supplier<SoundEvent> LION_AMBIENT = register("entity.lion.ambient");

    public static final Supplier<SoundEvent> ELEPHANT_HURT = register("entity.elephant.hurt");
    public static final Supplier<SoundEvent> ELEPHANT_AMBIENT = register("entity.elephant.ambient");

    public static final Supplier<SoundEvent> ZEBRA_AMBIENT = register("entity.zebra.ambient");
    public static final Supplier<SoundEvent> ZEBRA_HURT = register("entity.zebra.hurt");
    public static final Supplier<SoundEvent> ZEBRA_DEATH = register("entity.zebra.death");
    public static final Supplier<SoundEvent> ZEBRA_EAT = register("entity.zebra.eat");
    public static final Supplier<SoundEvent> ZEBRA_BREATHE = register("entity.zebra.breathe");
    public static final Supplier<SoundEvent> ZEBRA_ANGRY = register("entity.zebra.angry");
    public static final Supplier<SoundEvent> ZEBRA_JUMP = register("entity.zebra.jump");

    public static final Supplier<SoundEvent> VULTURE_AMBIENT = register("entity.vulture.ambient");
    public static final Supplier<SoundEvent> VULTURE_HURT = register("entity.vulture.hurt");
    public static final Supplier<SoundEvent> VULTURE_DEATH = register("entity.vulture.death");

    public static final Supplier<SoundEvent> GIRAFFE_AMBIENT = register("entity.giraffe.ambient");
    public static final Supplier<SoundEvent> HIPPO_AMBIENT = register("entity.hippo.ambient");
    public static final Supplier<SoundEvent> HIPPO_HURT = register("entity.hippo.hurt");

    public static final Supplier<SoundEvent> BOAR_AMBIENT = register("entity.boar.ambient");
    public static final Supplier<SoundEvent> BOAR_HURT = register("entity.boar.hurt");
    public static final Supplier<SoundEvent> BOAR_DEATH = register("entity.boar.death");

    public static final Supplier<SoundEvent> GATOR_EGG_BREAK = register("entity.alligator.egg_break");
    public static final Supplier<SoundEvent> GATOR_EGG_CRACK = register("entity.alligator.egg_crack");
    public static final Supplier<SoundEvent> GATOR_EGG_HATCH = register("entity.alligator.egg_hatch");

    public static final Supplier<SoundEvent> GATOR_AMBIENT = register("entity.alligator.ambient");
    public static final Supplier<SoundEvent> GATOR_AMBIENT_BABY = register("entity.alligator.ambient_baby");
    public static final Supplier<SoundEvent> GATOR_HURT = register("entity.alligator.hurt");
    public static final Supplier<SoundEvent> GATOR_DEATH = register("entity.alligator.death");

    public static final Supplier<SoundEvent> CATFISH_FLOP = register("entity.catfish.flop");
    public static final Supplier<SoundEvent> BASS_FLOP = register("entity.bass.flop");
    public static final Supplier<SoundEvent> DRAGONFLY_LOOP = register("entity.dragonfly.loop");

    public static final Supplier<SoundEvent> DUCK_AMBIENT = register("entity.duck.ambient");
    public static final Supplier<SoundEvent> DUCK_HURT = register("entity.duck.hurt");
    public static final Supplier<SoundEvent> DUCK_DEATH = register("entity.duck.death");
    public static final Supplier<SoundEvent> DUCK_STEP = register("entity.duck.step");

    private static Supplier<SoundEvent> register(String path) {
        String soundPath = Objects.requireNonNull(path, "sound path");
        ResourceLocation id = new ResourceLocation(Naturalist.MOD_ID, soundPath);
        return CommonPlatformHelper.registerSoundEvent(soundPath, () -> SoundEvent.createVariableRangeEvent(id));
    }

    public static void init() {}
}
