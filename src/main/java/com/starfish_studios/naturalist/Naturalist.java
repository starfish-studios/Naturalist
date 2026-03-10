package com.starfish_studios.naturalist;

import com.starfish_studios.naturalist.common.entity.*;
import com.starfish_studios.naturalist.common.entity.core.projectile.ThrownDuckEgg;
import com.starfish_studios.naturalist.core.platform.CommonPlatformHelper;
import com.starfish_studios.naturalist.core.registry.*;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.core.dispenser.ProjectileDispenseBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.DispensibleContainerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.levelgen.Heightmap;
import org.jetbrains.annotations.NotNull;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Naturalist {
    public static final String MOD_ID = "naturalist";
    public static final Logger LOGGER = LogManager.getLogger();

    public static void init() {
        NaturalistRegistry.init();
        NaturalistBlockEntities.init();
        NaturalistSoundEvents.init();
        NaturalistEntityTypes.init();
        NaturalistPotions.init();
        NaturalistRecipes.register();
    }

    public static void registerDispenserBehaviors() {
        DispenserBlock.registerProjectileBehavior(NaturalistRegistry.DUCK_EGG.get());

        // TODO: Re-implement bucket dispenser behaviors for 1.21 data components
        // The old NBT-based approach (stack.getOrCreateTag()) needs migration to data components
    }

    public static void registerBrewingRecipes() {
        // Brewing recipes are now registered via Fabric API's BrewingRecipeRegistryBuilder
        // Moved to NaturalistFabric.onInitialize()
    }

    public static void registerSpawnPlacements() {
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.SNAIL.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules);
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.BEAR.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.BUTTERFLY.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, Butterfly::checkButterflySpawnRules);
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.FIREFLY.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, Firefly::checkFireflySpawnRules);
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.SNAKE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Snake::checkSnakeSpawnRules);
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.CORAL_SNAKE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Snake::checkSnakeSpawnRules);
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.RATTLESNAKE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Snake::checkSnakeSpawnRules);
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.DEER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);

        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.BLUEJAY.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, Bird::checkBirdSpawnRules);
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.CANARY.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, Bird::checkBirdSpawnRules);
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.CARDINAL.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, Bird::checkBirdSpawnRules);
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.ROBIN.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, Bird::checkBirdSpawnRules);
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.FINCH.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, Bird::checkBirdSpawnRules);
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.SPARROW.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, Bird::checkBirdSpawnRules);

        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.RHINO.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.LION.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.ELEPHANT.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.ZEBRA.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.GIRAFFE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.HIPPO.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Hippo::checkHippoSpawnRules);
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.VULTURE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, Vulture::checkVultureSpawnRules);
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.BOAR.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.DRAGONFLY.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, Dragonfly::checkDragonflySpawnRules);
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.CATFISH.get(), SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules);
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.ALLIGATOR.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Alligator::checkAlligatorSpawnRules);
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.BASS.get(), SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules);
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.LIZARD.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules);
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.TORTOISE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules);
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.DUCK.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Duck::checkDuckSpawnRules);
    }


    public static void registerCompostables() {
        CommonPlatformHelper.registerCompostable(0.65F, NaturalistRegistry.SNAIL_SHELL.get());
    }
}
