package com.starfish_studios.naturalist;

import com.starfish_studios.naturalist.common.entity.*;
import com.starfish_studios.naturalist.platform.CommonPlatformHelper;
import com.starfish_studios.naturalist.registry.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.levelgen.Heightmap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Naturalist {
    public static final String MOD_ID = "naturalist";
    public static final Logger LOGGER = LogManager.getLogger();

    public static void init() {
        //GeckoLib.initialize();
        NaturalistRegistry.init();
        NaturalistCreativeModeTabs.registerCreativeModeTabs();
        NaturalistBlockEntities.init();
        NaturalistSoundEvents.init();
        NaturalistEntityTypes.init();
        NaturalistPotions.init();
    }

    public static void registerDispenserBehaviors() {
        // TODO: 1.21.4
        /*DispenserBlock.registerBehavior(NaturalistRegistry.DUCK_EGG, new AbstractProjectileDispenseBehavior() {
            protected Projectile getProjectile(Level level, Position position, ItemStack stack) {
                return Util.make(new ThrownDuckEgg(level, position.x(), position.y(), position.z()), (thrownDuckEgg) -> {
                    thrownDuckEgg.setItem(stack);
                });
            }
        });
        DispenseItemBehavior dispenseItemBehavior = new DefaultDispenseItemBehavior() {
            private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();

            @Override
            public ItemStack execute(BlockSource source, ItemStack stack) {
                DispensibleContainerItem dispensibleContainerItem = (DispensibleContainerItem)stack.getItem();
                BlockPos blockPos = source.getPos().relative(source.getBlockState().getValue(DispenserBlock.FACING));
                Level level = source.getLevel();
                if (dispensibleContainerItem.emptyContents(null, level, blockPos, null)) {
                    dispensibleContainerItem.checkExtraContent(null, level, stack, blockPos);
                    return new ItemStack(Items.BUCKET);
                } else {
                    return this.defaultDispenseItemBehavior.dispense(source, stack);
                }
            }
        };
        DispenserBlock.registerBehavior(NaturalistRegistry.SNAIL_BUCKET, dispenseItemBehavior);
        DispenserBlock.registerBehavior(NaturalistRegistry.BASS_BUCKET, dispenseItemBehavior);
        DispenserBlock.registerBehavior(NaturalistRegistry.CATFISH_BUCKET, dispenseItemBehavior);


        DispenserBlock.registerBehavior(NaturalistRegistry.SNAIL_BUCKET, new DefaultDispenseItemBehavior() {
            public ItemStack execute(BlockSource source, ItemStack stack) {
                Direction direction = source.getBlockState().getValue(DispenserBlock.FACING);
                BlockPos blockPos = source.getPos().relative(direction);
                ServerLevel serverLevel = source.getLevel();
                Snail snail = NaturalistEntityTypes.SNAIL.spawn(serverLevel, stack, null, blockPos, MobSpawnType.DISPENSER, true, false);
                if (snail != null) {
                    snail.setSnailColor(Snail.Color.getTypeById(stack.getOrCreateTag().getInt("Color")));
                    stack.shrink(1);
                    return new ItemStack(Items.BUCKET);
                }
                return stack;
            }
        });
        DispenserBlock.registerBehavior(NaturalistRegistry.BUTTERFLY, new DefaultDispenseItemBehavior() {
            public ItemStack execute(BlockSource source, ItemStack stack) {
                Direction direction = source.getBlockState().getValue(DispenserBlock.FACING);
                BlockPos blockPos = source.getPos().relative(direction);
                ServerLevel serverLevel = source.getLevel();
                Butterfly butterfly = NaturalistEntityTypes.BUTTERFLY.spawn(serverLevel, stack, null, blockPos, MobSpawnType.DISPENSER, true, false);
                if (butterfly != null) {
                    butterfly.setVariant(Butterfly.Variant.getTypeById(stack.getOrCreateTag().getInt("Variant")));
                    stack.shrink(1);
                }

                return stack;
            }
        });*/
    }
    
    public static void registerBrewingRecipes(PotionBrewing.Builder builder) {
        builder.addMix(Potions.AWKWARD, NaturalistRegistry.ANTLER, NaturalistPotions.FOREST_DASHER);
        builder.addMix(NaturalistPotions.FOREST_DASHER, Items.REDSTONE, NaturalistPotions.LONG_FOREST_DASHER);
        builder.addMix(NaturalistPotions.FOREST_DASHER, Items.GLOWSTONE_DUST, NaturalistPotions.STRONG_FOREST_DASHER);
    }

    public static void registerSpawnPlacements() {
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.SNAIL, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules);
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.BEAR, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.BUTTERFLY, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, Butterfly::checkButterflySpawnRules);
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.FIREFLY, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, Firefly::checkFireflySpawnRules);
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.SNAKE, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Snake::checkSnakeSpawnRules);
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.CORAL_SNAKE, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Snake::checkSnakeSpawnRules);
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.RATTLESNAKE, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Snake::checkSnakeSpawnRules);
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.DEER, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);


        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.BLUEJAY, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, Bird::checkBirdSpawnRules);
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.CANARY, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, Bird::checkBirdSpawnRules);
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.CARDINAL, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, Bird::checkBirdSpawnRules);
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.ROBIN, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, Bird::checkBirdSpawnRules);
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.FINCH, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, Bird::checkBirdSpawnRules);
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.SPARROW, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, Bird::checkBirdSpawnRules);


        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.RHINO, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.LION, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.ELEPHANT, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.ZEBRA, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.GIRAFFE, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.HIPPO, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Hippo::checkHippoSpawnRules);
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.VULTURE, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, Vulture::checkVultureSpawnRules);
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.BOAR, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.DRAGONFLY, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, Dragonfly::checkDragonflySpawnRules);
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.CATFISH, SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules);
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.ALLIGATOR, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Alligator::checkAlligatorSpawnRules);
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.BASS, SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules);
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.LIZARD, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules);
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.TORTOISE, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules);
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.DUCK, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Duck::checkDuckSpawnRules);
    }


    public static void registerCompostables() {
        CommonPlatformHelper.registerCompostable(0.65F, NaturalistRegistry.SNAIL_SHELL);
    }
}
