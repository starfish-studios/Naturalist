package com.starfish_studios.naturalist.core.registry;

import com.starfish_studios.naturalist.common.block.AlligatorEggBlock;
import com.starfish_studios.naturalist.common.block.ChrysalisBlock;
import com.starfish_studios.naturalist.common.block.GlowGoopBlock;
import com.starfish_studios.naturalist.common.block.SnailEggBlock;
import com.starfish_studios.naturalist.common.block.TeddyBearBlock;
import com.starfish_studios.naturalist.common.block.TortoiseEggBlock;
import com.starfish_studios.naturalist.core.platform.CommonPlatformHelper;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GlassBlock;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.Blocks;

import java.util.function.Supplier;

@SuppressWarnings("deprecation")
public class NaturalistBlocks {
    public static final Supplier<Block> ALLIGATOR_EGG = registerBlock("alligator_egg", () -> new AlligatorEggBlock(BlockBehaviour.Properties.copy(Blocks.TURTLE_EGG)));
    public static final Supplier<Block> TORTOISE_EGG = registerBlock("tortoise_egg", () -> new TortoiseEggBlock(BlockBehaviour.Properties.copy(Blocks.TURTLE_EGG)));
    public static final Supplier<Block> SNAIL_EGGS = registerBlock("snail_eggs", () -> new SnailEggBlock(BlockBehaviour.Properties.copy(Blocks.FROGSPAWN)));

    public static final Supplier<Block> GLOW_GOOP_BLOCK = registerBlockOnly("glow_goop", () -> new GlowGoopBlock(BlockBehaviour.Properties.of().strength(0.5F).replaceable().noOcclusion().noCollission().lightLevel(GlowGoopBlock.LIGHT_EMISSION).sound(SoundType.HONEY_BLOCK)));
    public static final Supplier<Block> TEDDY_BEAR = registerBlock("teddy_bear", () -> new TeddyBearBlock(BlockBehaviour.Properties.of().strength(0.8f).sound(SoundType.WOOL).noOcclusion()));
    public static final Supplier<Block> CHRYSALIS_BLOCK = registerBlockOnly("chrysalis", () -> new ChrysalisBlock(BlockBehaviour.Properties.of().randomTicks().strength(0.2F, 3.0F).sound(SoundType.GRASS).noOcclusion().noCollission().pushReaction(PushReaction.DESTROY)));

    public static final Supplier<Block> AZURE_FROGLASS = registerBlock("azure_froglass", () -> new GlassBlock(BlockBehaviour.Properties.copy(Blocks.GLASS)));
    public static final Supplier<Block> VERDANT_FROGLASS = registerBlock("verdant_froglass", () -> new GlassBlock(BlockBehaviour.Properties.copy(Blocks.GLASS)));
    public static final Supplier<Block> CRIMSON_FROGLASS = registerBlock("crimson_froglass", () -> new GlassBlock(BlockBehaviour.Properties.copy(Blocks.GLASS)));

    public static final Supplier<Block> AZURE_FROGLASS_PANE = registerBlock("azure_froglass_pane", () -> new IronBarsBlock(BlockBehaviour.Properties.copy(Blocks.GLASS_PANE)));
    public static final Supplier<Block> VERDANT_FROGLASS_PANE = registerBlock("verdant_froglass_pane", () -> new IronBarsBlock(BlockBehaviour.Properties.copy(Blocks.GLASS_PANE)));
    public static final Supplier<Block> CRIMSON_FROGLASS_PANE = registerBlock("crimson_froglass_pane", () -> new IronBarsBlock(BlockBehaviour.Properties.copy(Blocks.GLASS_PANE)));

    public static final Supplier<Block> SHELLSTONE = registerBlock("shellstone", () -> new Block(BlockBehaviour.Properties.copy(Blocks.SANDSTONE)));
    public static final Supplier<Block> SHELLSTONE_STAIRS = registerBlock("shellstone_stairs", () -> new StairBlock(SHELLSTONE.get().defaultBlockState(), BlockBehaviour.Properties.copy(Blocks.SANDSTONE)));
    public static final Supplier<Block> SHELLSTONE_SLAB = registerBlock("shellstone_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.SANDSTONE)));
    public static final Supplier<Block> SHELLSTONE_WALL = registerBlock("shellstone_wall", () -> new WallBlock(BlockBehaviour.Properties.copy(Blocks.SANDSTONE)));
    public static final Supplier<Block> SHELLSTONE_BRICKS = registerBlock("shellstone_bricks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.SANDSTONE)));
    public static final Supplier<Block> SHELLSTONE_BRICK_STAIRS = registerBlock("shellstone_brick_stairs", () -> new StairBlock(SHELLSTONE_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.copy(Blocks.SANDSTONE)));
    public static final Supplier<Block> SHELLSTONE_BRICK_SLAB = registerBlock("shellstone_brick_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.SANDSTONE)));
    public static final Supplier<Block> SHELLSTONE_BRICK_WALL = registerBlock("shellstone_brick_wall", () -> new WallBlock(BlockBehaviour.Properties.copy(Blocks.SANDSTONE)));

    public static final Supplier<Block> CUT_SHELLSTONE = registerBlock("cut_shellstone", () -> new Block(BlockBehaviour.Properties.copy(Blocks.SANDSTONE)));
    public static final Supplier<Block> CUT_SHELLSTONE_STAIRS = registerBlock("cut_shellstone_stairs", () -> new StairBlock(CUT_SHELLSTONE.get().defaultBlockState(), BlockBehaviour.Properties.copy(Blocks.SANDSTONE)));
    public static final Supplier<Block> CUT_SHELLSTONE_SLAB = registerBlock("cut_shellstone_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.SANDSTONE)));
    public static final Supplier<Block> CUT_SHELLSTONE_WALL = registerBlock("cut_shellstone_wall", () -> new WallBlock(BlockBehaviour.Properties.copy(Blocks.SANDSTONE)));

    public static final Supplier<Block> SMOOTH_SHELLSTONE = registerBlock("smooth_shellstone", () -> new Block(BlockBehaviour.Properties.copy(Blocks.SANDSTONE)));
    public static final Supplier<Block> SMOOTH_SHELLSTONE_STAIRS = registerBlock("smooth_shellstone_stairs", () -> new StairBlock(SMOOTH_SHELLSTONE.get().defaultBlockState(), BlockBehaviour.Properties.copy(Blocks.SANDSTONE)));
    public static final Supplier<Block> SMOOTH_SHELLSTONE_SLAB = registerBlock("smooth_shellstone_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.SANDSTONE)));
    public static final Supplier<Block> SMOOTH_SHELLSTONE_WALL = registerBlock("smooth_shellstone_wall", () -> new WallBlock(BlockBehaviour.Properties.copy(Blocks.SANDSTONE)));

    public static void init() {
    }

    private static <T extends Block> Supplier<T> registerBlock(String name, Supplier<T> block) {
        Supplier<T> supplier = CommonPlatformHelper.registerBlock(name, block);
        CommonPlatformHelper.registerItem(name, () -> new BlockItem(supplier.get(), new Item.Properties()));
        return supplier;
    }

    private static <T extends Block> Supplier<T> registerBlockOnly(String name, Supplier<T> block) {
        return CommonPlatformHelper.registerBlock(name, block);
    }
}

