package com.starfish_studios.naturalist.core.registry;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.core.platform.CommonPlatformHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

import java.util.function.Supplier;

public class NaturalistSoundEvents {

    // MISC SOUNDS

    public static final Supplier<SoundEvent> BUTTERFLY_AMBIENT = CommonPlatformHelper.registerSoundEvent("butterfly_ambient", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.butterfly.ambient")));
    public static final Supplier<SoundEvent> BUTTERFLY_HURT = CommonPlatformHelper.registerSoundEvent("butterfly_hurt", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.butterfly.hurt")));
    public static final Supplier<SoundEvent> SNAKE_HISS = CommonPlatformHelper.registerSoundEvent("snake_hiss", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.snake.hiss")));
    public static final Supplier<SoundEvent> SNAKE_HURT = CommonPlatformHelper.registerSoundEvent("snake_hurt", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.snake.hurt")));
    public static final Supplier<SoundEvent> SNAKE_RATTLE = CommonPlatformHelper.registerSoundEvent("snake_rattle", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.snake.rattle")));
    public static final Supplier<SoundEvent> SNAIL_CRUSH = CommonPlatformHelper.registerSoundEvent("snail_crush", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.snail.crush")));
    public static final Supplier<SoundEvent> SNAIL_FORWARD = CommonPlatformHelper.registerSoundEvent("snail_forward", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.snail.forward")));
    public static final Supplier<SoundEvent> SNAIL_BACK = CommonPlatformHelper.registerSoundEvent("snail_back", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.snail.back")));
    public static final Supplier<SoundEvent> BUCKET_FILL_SNAIL = CommonPlatformHelper.registerSoundEvent("bucket_fill_snail", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "item.bucket.fill_snail")));
    public static final Supplier<SoundEvent> BUCKET_EMPTY_SNAIL = CommonPlatformHelper.registerSoundEvent("bucket_empty_snail", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "item.bucket.empty_snail")));
    public static final Supplier<SoundEvent> BIRD_HURT = CommonPlatformHelper.registerSoundEvent("bird_hurt", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.bird.hurt")));
    public static final Supplier<SoundEvent> BIRD_DEATH = CommonPlatformHelper.registerSoundEvent("bird_death", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.bird.death")));
    public static final Supplier<SoundEvent> BIRD_EAT = CommonPlatformHelper.registerSoundEvent("bird_eat", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.bird.eat")));
    public static final Supplier<SoundEvent> BIRD_FLY = CommonPlatformHelper.registerSoundEvent("bird_fly", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.bird.fly")));

   // BIRDS

    public static final Supplier<SoundEvent> BIRD_AMBIENT_BLUEJAY = CommonPlatformHelper.registerSoundEvent("bird_ambient_bluejay", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.bird.ambient_bluejay")));
    public static final Supplier<SoundEvent> BIRD_AMBIENT_CANARY = CommonPlatformHelper.registerSoundEvent("bird_ambient_canary", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.bird.ambient_canary")));
    public static final Supplier<SoundEvent> BIRD_AMBIENT_ROBIN = CommonPlatformHelper.registerSoundEvent("bird_ambient_robin", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.bird.ambient_robin")));
    public static final Supplier<SoundEvent> BIRD_AMBIENT_CARDINAL = CommonPlatformHelper.registerSoundEvent("bird_ambient_cardinal", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.bird.ambient_cardinal")));
    public static final Supplier<SoundEvent> BIRD_AMBIENT_FINCH = CommonPlatformHelper.registerSoundEvent("bird_ambient_finch", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.bird.ambient_finch")));
    public static final Supplier<SoundEvent> BIRD_AMBIENT_SPARROW = CommonPlatformHelper.registerSoundEvent("bird_ambient_sparrow", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.bird.ambient_sparrow")));

    public static final Supplier<SoundEvent> FIREFLY_AMBIENT = CommonPlatformHelper.registerSoundEvent("firefly_ambient", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.firefly.ambient")));
    public static final Supplier<SoundEvent> FIREFLY_HURT = CommonPlatformHelper.registerSoundEvent("firefly_hurt", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.firefly.hurt")));
    public static final Supplier<SoundEvent> FIREFLY_DEATH = CommonPlatformHelper.registerSoundEvent("firefly_death", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.firefly.death")));
    public static final Supplier<SoundEvent> FIREFLY_HIDE = CommonPlatformHelper.registerSoundEvent("firefly_hide", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.firefly.hide")));



    // FOREST SOUNDS

    public static final Supplier<SoundEvent> BEAR_HURT = CommonPlatformHelper.registerSoundEvent("bear_hurt", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.bear.hurt")));
    public static final Supplier<SoundEvent> BEAR_DEATH = CommonPlatformHelper.registerSoundEvent("bear_death", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.bear.death")));
    public static final Supplier<SoundEvent> BEAR_AMBIENT = CommonPlatformHelper.registerSoundEvent("bear_ambient", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.bear.ambient")));
    public static final Supplier<SoundEvent> BEAR_AMBIENT_BABY = CommonPlatformHelper.registerSoundEvent("bear_ambient_baby", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.bear.ambient_baby")));
    public static final Supplier<SoundEvent> BEAR_HURT_BABY = CommonPlatformHelper.registerSoundEvent("bear_hurt_baby", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.bear.hurt_baby")));
    public static final Supplier<SoundEvent> BEAR_SLEEP = CommonPlatformHelper.registerSoundEvent("bear_sleep", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.bear.sleep")));
    public static final Supplier<SoundEvent> BEAR_SNIFF = CommonPlatformHelper.registerSoundEvent("bear_sniff", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.bear.sniff")));
    public static final Supplier<SoundEvent> BEAR_SPIT = CommonPlatformHelper.registerSoundEvent("bear_spit", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.bear.spit")));
    public static final Supplier<SoundEvent> BEAR_EAT = CommonPlatformHelper.registerSoundEvent("bear_eat", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.bear.eat")));
    public static final Supplier<SoundEvent> BEAR_ATTACK = CommonPlatformHelper.registerSoundEvent("bear_attack", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.bear.attack")));
    public static final Supplier<SoundEvent> DEER_AMBIENT = CommonPlatformHelper.registerSoundEvent("deer_ambient", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.deer.ambient")));
    public static final Supplier<SoundEvent> DEER_HURT = CommonPlatformHelper.registerSoundEvent("deer_hurt", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.deer.hurt")));
    public static final Supplier<SoundEvent> DEER_AMBIENT_BABY = CommonPlatformHelper.registerSoundEvent("deer_ambient_baby", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.deer.ambient_baby")));
    public static final Supplier<SoundEvent> DEER_HURT_BABY = CommonPlatformHelper.registerSoundEvent("deer_hurt_baby", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.deer.hurt_baby")));



    // SAVANNA SOUNDS

    public static final Supplier<SoundEvent> RHINO_SCRAPE = CommonPlatformHelper.registerSoundEvent("rhino_scrape", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.rhino.scrape")));
    public static final Supplier<SoundEvent> RHINO_AMBIENT = CommonPlatformHelper.registerSoundEvent("rhino_ambient", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.rhino.ambient")));
    public static final Supplier<SoundEvent> RHINO_AMBIENT_BABY = CommonPlatformHelper.registerSoundEvent("rhino_ambient_baby", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.rhino.ambient_baby")));
    public static final Supplier<SoundEvent> LION_HURT = CommonPlatformHelper.registerSoundEvent("lion_hurt", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.lion.hurt")));
    public static final Supplier<SoundEvent> LION_AMBIENT = CommonPlatformHelper.registerSoundEvent("lion_ambient", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.lion.ambient")));
    public static final Supplier<SoundEvent> LION_ROAR = CommonPlatformHelper.registerSoundEvent("lion_roar", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.lion.roar")));
    public static final Supplier<SoundEvent> LION_SLEEP = CommonPlatformHelper.registerSoundEvent("lion_sleep", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.lion.sleep")));
    public static final Supplier<SoundEvent> LION_STEP = CommonPlatformHelper.registerSoundEvent("lion_step", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.lion.step")));
    public static final Supplier<SoundEvent> LION_ATTACK = CommonPlatformHelper.registerSoundEvent("lion_attack", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.lion.attack")));
    public static final Supplier<SoundEvent> ELEPHANT_HURT = CommonPlatformHelper.registerSoundEvent("elephant_hurt", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.elephant.hurt")));
    public static final Supplier<SoundEvent> ELEPHANT_AMBIENT = CommonPlatformHelper.registerSoundEvent("elephant_ambient", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.elephant.ambient")));
    public static final Supplier<SoundEvent> ZEBRA_AMBIENT = CommonPlatformHelper.registerSoundEvent("zebra_ambient", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.zebra.ambient")));
    public static final Supplier<SoundEvent> ZEBRA_HURT = CommonPlatformHelper.registerSoundEvent("zebra_hurt", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.zebra.hurt")));
    public static final Supplier<SoundEvent> ZEBRA_DEATH = CommonPlatformHelper.registerSoundEvent("zebra_death", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.zebra.death")));
    public static final Supplier<SoundEvent> ZEBRA_EAT = CommonPlatformHelper.registerSoundEvent("zebra_eat", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.zebra.eat")));
    public static final Supplier<SoundEvent> ZEBRA_BREATHE = CommonPlatformHelper.registerSoundEvent("zebra_breathe", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.zebra.breathe")));
    public static final Supplier<SoundEvent> ZEBRA_ANGRY = CommonPlatformHelper.registerSoundEvent("zebra_angry", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.zebra.angry")));
    public static final Supplier<SoundEvent> ZEBRA_JUMP = CommonPlatformHelper.registerSoundEvent("zebra_jump", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.zebra.jump")));
    public static final Supplier<SoundEvent> VULTURE_AMBIENT = CommonPlatformHelper.registerSoundEvent("vulture_ambient", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.vulture.ambient")));
    public static final Supplier<SoundEvent> VULTURE_HURT = CommonPlatformHelper.registerSoundEvent("vulture_hurt", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.vulture.hurt")));
    public static final Supplier<SoundEvent> VULTURE_DEATH = CommonPlatformHelper.registerSoundEvent("vulture_death", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.vulture.death")));
    public static final Supplier<SoundEvent> GIRAFFE_AMBIENT = CommonPlatformHelper.registerSoundEvent("giraffe_ambient", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.giraffe.ambient")));
    public static final Supplier<SoundEvent> HIPPO_AMBIENT = CommonPlatformHelper.registerSoundEvent("hippo_ambient", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.hippo.ambient")));
    public static final Supplier<SoundEvent> HIPPO_HURT = CommonPlatformHelper.registerSoundEvent("hippo_hurt", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.hippo.hurt")));
    public static final Supplier<SoundEvent> BOAR_AMBIENT = CommonPlatformHelper.registerSoundEvent("boar_ambient", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.boar.ambient")));

    public static final Supplier<SoundEvent> BOAR_AMBIENT_BABY = CommonPlatformHelper.registerSoundEvent("boar_ambient_baby", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.boar.ambient_baby")));
    public static final Supplier<SoundEvent> BOAR_HURT = CommonPlatformHelper.registerSoundEvent("boar_hurt", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.boar.hurt")));
    public static final Supplier<SoundEvent> BOAR_HURT_BABY = CommonPlatformHelper.registerSoundEvent("boar_hurt_baby", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.boar.hurt_baby")));
    public static final Supplier<SoundEvent> BOAR_DEATH = CommonPlatformHelper.registerSoundEvent("boar_death", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.boar.death")));
    public static final Supplier<SoundEvent> BOAR_DEATH_BABY = CommonPlatformHelper.registerSoundEvent("boar_death_baby", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.boar.death_baby")));
    public static final Supplier<SoundEvent> BOAR_ATTACK = CommonPlatformHelper.registerSoundEvent("boar_attack", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.boar.attack")));

    // SWAMP SOUNDS

    // Gator Eggs
    public static final Supplier<SoundEvent> GATOR_EGG_BREAK = CommonPlatformHelper.registerSoundEvent("alligator_egg_break", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.alligator.egg_break")));
    public static final Supplier<SoundEvent> GATOR_EGG_CRACK = CommonPlatformHelper.registerSoundEvent("alligator_egg_crack", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.alligator.egg_crack")));
    public static final Supplier<SoundEvent> GATOR_EGG_HATCH = CommonPlatformHelper.registerSoundEvent("alligator_egg_hatch", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.alligator.egg_hatch")));

    // Tortoise Eggs
    public static final Supplier<SoundEvent> TORTOISE_EGG_BREAK = CommonPlatformHelper.registerSoundEvent("tortoise_egg_break", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.tortoise.egg_break")));
    public static final Supplier<SoundEvent> TORTOISE_EGG_CRACK = CommonPlatformHelper.registerSoundEvent("tortoise_egg_crack", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.tortoise.egg_crack")));
    public static final Supplier<SoundEvent> TORTOISE_EGG_HATCH = CommonPlatformHelper.registerSoundEvent("tortoise_egg_hatch", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.tortoise.egg_hatch")));

    public static final Supplier<SoundEvent> GATOR_AMBIENT = CommonPlatformHelper.registerSoundEvent("alligator_ambient", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.alligator.ambient")));
    public static final Supplier<SoundEvent> GATOR_AMBIENT_BABY = CommonPlatformHelper.registerSoundEvent("alligator_ambient_baby", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.alligator.ambient_baby")));
    public static final Supplier<SoundEvent> GATOR_HURT = CommonPlatformHelper.registerSoundEvent("alligator_hurt", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.alligator.hurt")));
    public static final Supplier<SoundEvent> GATOR_DEATH = CommonPlatformHelper.registerSoundEvent("alligator_death", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.alligator.death")));
    public static final Supplier<SoundEvent> CATFISH_FLOP = CommonPlatformHelper.registerSoundEvent("catfish_flop", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.catfish.flop")));
    public static final Supplier<SoundEvent> BASS_FLOP = CommonPlatformHelper.registerSoundEvent("bass_flop", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.bass.flop")));

    public static final Supplier<SoundEvent> DRAGONFLY_HURT = CommonPlatformHelper.registerSoundEvent("dragonfly_hurt", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.dragonfly.hurt")));
    public static final Supplier<SoundEvent> DRAGONFLY_DEATH = CommonPlatformHelper.registerSoundEvent("dragonfly_death", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.dragonfly.death")));
    public static final Supplier<SoundEvent> DRAGONFLY_LOOP = CommonPlatformHelper.registerSoundEvent("dragonfly_loop", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.dragonfly.loop")));
    // DUCK SOUNDS
    public static final Supplier<SoundEvent> DUCK_AMBIENT = CommonPlatformHelper.registerSoundEvent("duck_ambient", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.duck.ambient")));
    public static final Supplier<SoundEvent> DUCK_HURT = CommonPlatformHelper.registerSoundEvent("duck_hurt", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.duck.hurt")));
    public static final Supplier<SoundEvent> DUCK_DEATH = CommonPlatformHelper.registerSoundEvent("duck_death", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.duck.death")));
    public static final Supplier<SoundEvent> DUCK_STEP = CommonPlatformHelper.registerSoundEvent("duck_step", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.duck.step")));
    // RUBBER DUCKY SOUNDS
    public static final Supplier<SoundEvent> RUBBER_DUCKY_AMBIENT = CommonPlatformHelper.registerSoundEvent("rubber_ducky_ambient", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.rubber_ducky.ambient")));
    public static final Supplier<SoundEvent> RUBBER_DUCKY_HURT = CommonPlatformHelper.registerSoundEvent("rubber_ducky_hurt", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.rubber_ducky.hurt")));
    public static final Supplier<SoundEvent> RUBBER_DUCKY_DEATH = CommonPlatformHelper.registerSoundEvent("rubber_ducky_death", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.rubber_ducky.death")));


    public static void init() {}
}
