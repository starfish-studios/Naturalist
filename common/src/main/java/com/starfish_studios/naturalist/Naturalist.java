package com.starfish_studios.naturalist;

import com.starfish_studios.naturalist.common.entity.*;
import com.starfish_studios.naturalist.common.entity.core.projectile.ThrownDuckEgg;
import com.starfish_studios.naturalist.core.registry.*;
import com.starfish_studios.naturalist.core.platform.CommonPlatformHelper;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.impl.biome.modification.BuiltInRegistryKeys;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.AbstractProjectileDispenseBehavior;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DispensibleContainerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.BlockHitResult;
import software.bernie.geckolib.GeckoLib;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Consumer;

public class Naturalist {
    public static final String MOD_ID = "naturalist";
    public static final CreativeModeTab TAB = CommonPlatformHelper.registerCreativeModeTab(new ResourceLocation(MOD_ID, "tab"), () -> new ItemStack(NaturalistBlocks.TEDDY_BEAR.get()));
    public static final ResourceKey<CreativeModeTab> TAB_KEY = ResourceKey.create(Registries.CREATIVE_MODE_TAB, new ResourceLocation(MOD_ID, "tab"));
    public static final Logger LOGGER = LogManager.getLogger();

    public static void init() {
        GeckoLib.initialize();
        NaturalistBlocks.init();
        NaturalistItems.init();
        NaturalistBlockEntities.init();
        NaturalistSoundEvents.init();
        NaturalistEntityTypes.init();
        NaturalistPotions.init();
    }

    public static void registerDispenserBehaviors() {
        DispenserBlock.registerBehavior(NaturalistItems.DUCK_EGG.get(), new AbstractProjectileDispenseBehavior() {
            protected Projectile getProjectile(Level level, Position position, ItemStack stack) {
                return Util.make(new ThrownDuckEgg(level, position.x(), position.y(), position.z()), (thrownDuckEgg) -> {
                    thrownDuckEgg.setItem(stack);
                });
            }
        });
        DispenseItemBehavior dispenseItemBehavior = new DefaultDispenseItemBehavior() {
            private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();

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
        DispenserBlock.registerBehavior(NaturalistItems.SNAIL_BUCKET.get(), dispenseItemBehavior);
        DispenserBlock.registerBehavior(NaturalistItems.BASS_BUCKET.get(), dispenseItemBehavior);
        DispenserBlock.registerBehavior(NaturalistItems.CATFISH_BUCKET.get(), dispenseItemBehavior);


        DispenserBlock.registerBehavior(NaturalistItems.SNAIL_BUCKET.get(), new DefaultDispenseItemBehavior() {
            public ItemStack execute(BlockSource source, ItemStack stack) {
                Direction direction = source.getBlockState().getValue(DispenserBlock.FACING);
                BlockPos blockPos = source.getPos().relative(direction);
                ServerLevel serverLevel = source.getLevel();
                Snail snail = NaturalistEntityTypes.SNAIL.get().spawn(serverLevel, stack, null, blockPos, MobSpawnType.DISPENSER, true, false);
                if (snail != null) {
                    snail.setSnailColor(Snail.Color.getTypeById(stack.getOrCreateTag().getInt("Color")));
                    stack.shrink(1);
                    return new ItemStack(Items.BUCKET);
                }
                return stack;
            }
        });
        DispenserBlock.registerBehavior(NaturalistItems.BUTTERFLY.get(), new DefaultDispenseItemBehavior() {
            public ItemStack execute(BlockSource source, ItemStack stack) {
                Direction direction = source.getBlockState().getValue(DispenserBlock.FACING);
                BlockPos blockPos = source.getPos().relative(direction);
                ServerLevel serverLevel = source.getLevel();
                Butterfly butterfly = NaturalistEntityTypes.BUTTERFLY.get().spawn(serverLevel, stack, null, blockPos, MobSpawnType.DISPENSER, true, false);
                if (butterfly != null) {
                    butterfly.setVariant(Butterfly.Variant.getTypeById(stack.getOrCreateTag().getInt("Variant")));
                    stack.shrink(1);
                }

                return stack;
            }
        });
    }
    
    public static void registerBrewingRecipes() {
        CommonPlatformHelper.registerBrewingRecipe(Potions.AWKWARD, NaturalistItems.ANTLER.get(), NaturalistPotions.FOREST_DASHER.get());
        CommonPlatformHelper.registerBrewingRecipe(NaturalistPotions.FOREST_DASHER.get(), Items.REDSTONE, NaturalistPotions.LONG_FOREST_DASHER.get());
        CommonPlatformHelper.registerBrewingRecipe(NaturalistPotions.FOREST_DASHER.get(), Items.GLOWSTONE_DUST, NaturalistPotions.STRONG_FOREST_DASHER.get());
    }

    public static void registerSpawnPlacements() {
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


    public static void registerCompostables() {
        CommonPlatformHelper.registerCompostable(0.65F, NaturalistItems.SNAIL_SHELL.get());
    }
}
