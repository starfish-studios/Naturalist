package com.starfish_studios.naturalist.core.registry;

import com.starfish_studios.naturalist.common.entity.Alligator;
import com.starfish_studios.naturalist.common.entity.Bird;
import com.starfish_studios.naturalist.common.entity.Butterfly;
import com.starfish_studios.naturalist.common.entity.Dragonfly;
import com.starfish_studios.naturalist.common.entity.Duck;
import com.starfish_studios.naturalist.common.entity.Firefly;
import com.starfish_studios.naturalist.common.entity.Hippo;
import com.starfish_studios.naturalist.common.entity.Snake;
import com.starfish_studios.naturalist.common.entity.Vulture;
import com.starfish_studios.naturalist.core.platform.CommonPlatformHelper;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.level.levelgen.Heightmap;

public class NaturalistSpawnPlacements {
    public static void register() {
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.SNAIL.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules);
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.BEAR.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.BUTTERFLY.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, Butterfly::checkButterflySpawnRules);
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.FIREFLY.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, Firefly::checkFireflySpawnRules);

        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.SNAKE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Snake::checkSnakeSpawnRules);
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.CORAL_SNAKE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Snake::checkSnakeSpawnRules);
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.RATTLESNAKE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Snake::checkSnakeSpawnRules);

        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.DEER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.BLUEJAY.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, Bird::checkBirdSpawnRules);
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.CANARY.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, Bird::checkBirdSpawnRules);
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.CARDINAL.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, Bird::checkBirdSpawnRules);
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.ROBIN.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, Bird::checkBirdSpawnRules);
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.FINCH.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, Bird::checkBirdSpawnRules);
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.SPARROW.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, Bird::checkBirdSpawnRules);

        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.RHINO.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.LION.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.ELEPHANT.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.ZEBRA.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.GIRAFFE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.HIPPO.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Hippo::checkHippoSpawnRules);
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.VULTURE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, Vulture::checkVultureSpawnRules);
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.BOAR.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.DRAGONFLY.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, Dragonfly::checkDragonflySpawnRules);

        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.CATFISH.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules);
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.ALLIGATOR.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Alligator::checkAlligatorSpawnRules);
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.BASS.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules);

        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.LIZARD.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules);
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.TORTOISE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules);
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.DUCK.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Duck::checkDuckSpawnRules);
    }
}

