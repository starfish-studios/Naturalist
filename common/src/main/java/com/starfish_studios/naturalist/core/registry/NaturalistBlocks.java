package com.starfish_studios.naturalist.core.registry;

import com.starfish_studios.naturalist.*;
import com.starfish_studios.naturalist.common.block.*;
import com.starfish_studios.naturalist.core.platform.CommonPlatformHelper;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.*;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.function.Supplier;

public class NaturalistBlocks {
    public static final Supplier<Block> CHRYSALIS = CommonPlatformHelper.registerBlock("chrysalis", () -> new ChrysalisBlock(BlockBehaviour.Properties.of().randomTicks().strength(0.2F, 3.0F).sound(SoundType.GRASS).noOcclusion().noCollission().pushReaction(PushReaction.DESTROY)));
    public static final Supplier<Block> DUCKWEED = CommonPlatformHelper.registerBlock("duckweed", () -> new WaterlilyBlock(BlockBehaviour.Properties.of().noCollission().randomTicks().instabreak().sound(SoundType.SMALL_DRIPLEAF).replaceable().ignitedByLava().pushReaction(PushReaction.DESTROY)));
    public static final Supplier<Block> GLOW_GOOP = CommonPlatformHelper.registerBlock("glow_goop", () -> new GlowGoopBlock(BlockBehaviour.Properties.of().strength(0.5F).replaceable().noOcclusion().noCollission().lightLevel(GlowGoopBlock.LIGHT_EMISSION).sound(SoundType.HONEY_BLOCK)));
    public static final Supplier<Block> AZURE_FROGLASS = registerBlock("azure_froglass", () -> new GlassBlock(BlockBehaviour.Properties.copy(Blocks.GLASS)));
    public static final Supplier<Block> VERDANT_FROGLASS = registerBlock("verdant_froglass", () -> new GlassBlock(BlockBehaviour.Properties.copy(Blocks.GLASS)));
    public static final Supplier<Block> CRIMSON_FROGLASS = registerBlock("crimson_froglass", () -> new GlassBlock(BlockBehaviour.Properties.copy(Blocks.GLASS)));
    public static final Supplier<Block> AZURE_FROGLASS_PANE = registerBlock("azure_froglass_pane", () -> new IronBarsBlock(BlockBehaviour.Properties.copy(Blocks.GLASS_PANE)));
    public static final Supplier<Block> VERDANT_FROGLASS_PANE = registerBlock("verdant_froglass_pane", () -> new IronBarsBlock(BlockBehaviour.Properties.copy(Blocks.GLASS_PANE)));
    public static final Supplier<Block> CRIMSON_FROGLASS_PANE = registerBlock("crimson_froglass_pane", () -> new IronBarsBlock(BlockBehaviour.Properties.copy(Blocks.GLASS_PANE)));
    public static final Supplier<Block> CATTAIL = registerBlock("cattail", () -> new CattailBlock(BlockBehaviour.Properties.of().noCollission().randomTicks().instabreak().sound(SoundType.SMALL_DRIPLEAF).offsetType(BlockBehaviour.OffsetType.XZ)));
    public static final Supplier<Block> TORTOISE_EGG = registerBlock("tortoise_egg", () -> new TortoiseEggBlock(BlockBehaviour.Properties.copy(Blocks.TURTLE_EGG)));
    public static final Supplier<Block> ALLIGATOR_EGG = registerBlock("alligator_egg", () -> new AlligatorEggBlock(BlockBehaviour.Properties.copy(Blocks.TURTLE_EGG)));
    public static final Supplier<Block> TEDDY_BEAR = registerBlock("teddy_bear", () -> new TeddyBearBlock(BlockBehaviour.Properties.of().strength(0.8f).sound(SoundType.WOOL).noOcclusion()));
    // public static final Supplier<Block> OSTRICH_EGG = registerBlock("ostrich_egg", () -> new OstrichEggBlock(BlockBehaviour.Properties.copy(Blocks.TURTLE_EGG)));

    public static final Supplier<Block> LIMESTONE = registerBlock("limestone", () -> new Block(BlockBehaviour.Properties.copy(Blocks.SANDSTONE)));
    public static final Supplier<Block> LIMESTONE_STAIRS = registerBlock("limestone_stairs", () -> new StairBlock(LIMESTONE.get().defaultBlockState(), BlockBehaviour.Properties.copy(Blocks.SANDSTONE)));
    public static final Supplier<Block> LIMESTONE_SLAB = registerBlock("limestone_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.SANDSTONE)));
    public static final Supplier<Block> LIMESTONE_WALL = registerBlock("limestone_wall", () -> new WallBlock(BlockBehaviour.Properties.copy(Blocks.SANDSTONE)));
    public static final Supplier<Block> LIMESTONE_BRICKS = registerBlock("limestone_bricks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.SANDSTONE)));
    public static final Supplier<Block> LIMESTONE_BRICK_STAIRS = registerBlock("limestone_brick_stairs", () -> new StairBlock(LIMESTONE_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.copy(Blocks.SANDSTONE)));
    public static final Supplier<Block> LIMESTONE_BRICK_SLAB = registerBlock("limestone_brick_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.SANDSTONE)));
    public static final Supplier<Block> LIMESTONE_BRICK_WALL = registerBlock("limestone_brick_wall", () -> new WallBlock(BlockBehaviour.Properties.copy(Blocks.SANDSTONE)));
    public static final Supplier<Block> CUT_LIMESTONE = registerBlock("cut_limestone", () -> new Block(BlockBehaviour.Properties.copy(Blocks.SANDSTONE)));
    public static final Supplier<Block> CUT_LIMESTONE_STAIRS = registerBlock("cut_limestone_stairs", () -> new StairBlock(CUT_LIMESTONE.get().defaultBlockState(), BlockBehaviour.Properties.copy(Blocks.SANDSTONE)));
    public static final Supplier<Block> CUT_LIMESTONE_SLAB = registerBlock("cut_limestone_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.SANDSTONE)));
    public static final Supplier<Block> CUT_LIMESTONE_WALL = registerBlock("cut_limestone_wall", () -> new WallBlock(BlockBehaviour.Properties.copy(Blocks.SANDSTONE)));
    public static final Supplier<Block> SMOOTH_LIMESTONE = registerBlock("smooth_limestone", () -> new Block(BlockBehaviour.Properties.copy(Blocks.SANDSTONE)));
    public static final Supplier<Block> SMOOTH_LIMESTONE_STAIRS = registerBlock("smooth_limestone_stairs", () -> new StairBlock(SMOOTH_LIMESTONE.get().defaultBlockState(), BlockBehaviour.Properties.copy(Blocks.SANDSTONE)));
    public static final Supplier<Block> SMOOTH_LIMESTONE_SLAB = registerBlock("smooth_limestone_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.SANDSTONE)));
    public static final Supplier<Block> SMOOTH_LIMESTONE_WALL = registerBlock("smooth_limestone_wall", () -> new WallBlock(BlockBehaviour.Properties.copy(Blocks.SANDSTONE)));


    public static void init() {
    }

    public static <T extends Block> Supplier<T> registerBlock(String name, Supplier<T> block) {
            Supplier<T> supplier = CommonPlatformHelper.registerBlock(name, block);
            CommonPlatformHelper.registerItem(name, () -> new BlockItem(supplier.get(), new Item.Properties()));
            return supplier;
    }

    public static void addAllToCreativeTab() {
        try {
            Field[] fields = NaturalistBlocks.class.getDeclaredFields();
            for (Field field : fields) {
                ParameterizedType type = (ParameterizedType) field.getGenericType();
                Type rawType = type.getRawType();
                Type[] typeArguments = type.getActualTypeArguments();

                if (rawType == Supplier.class) {
                    if (typeArguments.length == 1) {
                        Class<?> arg = (Class<?>) typeArguments[0];
                        if (Block.class.isAssignableFrom(arg)) {
                            Supplier<?> supplier = (Supplier<?>) field.get(null);
                            CommonPlatformHelper.acceptItemToCreativeTab(new ItemStack((Block) supplier.get()));
                        }
                    }
                }
            }
        } catch (IllegalAccessException ignored) {}
    }
}
