package com.starfish_studios.naturalist.core.registry;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.common.block.AlligatorEggBlock;
import com.starfish_studios.naturalist.common.block.ChrysalisBlock;
import com.starfish_studios.naturalist.common.block.GlowGoopBlock;
import com.starfish_studios.naturalist.common.block.SnailEggBlock;
import com.starfish_studios.naturalist.common.block.TeddyBearBlock;
import com.starfish_studios.naturalist.common.block.TortoiseEggBlock;
import com.starfish_studios.naturalist.common.item.GlowGoopItem;
import com.starfish_studios.naturalist.core.platform.CommonPlatformHelper;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.TransparentBlock;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.Blocks;

import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings("deprecation")
public class NaturalistBlocks {
        // Helper to create block resource key
        private static ResourceKey<Block> blockKey(String name) {
                return ResourceKey.create(Registries.BLOCK,
                                ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, name));
        }

        private static ResourceKey<Item> itemKey(String name) {
                return ResourceKey.create(Registries.ITEM,
                                ResourceLocation.fromNamespaceAndPath(Naturalist.MOD_ID, name));
        }

        public static final Supplier<Block> ALLIGATOR_EGG = registerBlock("alligator_egg",
                        props -> new AlligatorEggBlock(props),
                        BlockBehaviour.Properties.ofFullCopy(Blocks.TURTLE_EGG));
        public static final Supplier<Block> TORTOISE_EGG = registerBlock("tortoise_egg",
                        props -> new TortoiseEggBlock(props),
                        BlockBehaviour.Properties.ofFullCopy(Blocks.TURTLE_EGG));
        public static final Supplier<Block> SNAIL_EGGS = registerBlock("snail_eggs",
                        props -> new SnailEggBlock(props),
                        BlockBehaviour.Properties.ofFullCopy(Blocks.FROGSPAWN));

        public static final Supplier<Block> GLOW_GOOP_BLOCK = registerBlockOnly("glow_goop",
                        props -> new GlowGoopBlock(props),
                        BlockBehaviour.Properties.of().strength(0.5F).replaceable().noOcclusion()
                                        .noCollision().lightLevel(GlowGoopBlock.LIGHT_EMISSION)
                                        .sound(SoundType.HONEY_BLOCK));
        public static final Supplier<Block> TEDDY_BEAR = registerBlock("teddy_bear",
                        props -> new TeddyBearBlock(props),
                        BlockBehaviour.Properties.of().strength(0.8f).sound(SoundType.WOOL).noOcclusion());
        public static final Supplier<Block> CHRYSALIS_BLOCK = registerBlockOnly("chrysalis",
                        props -> new ChrysalisBlock(props),
                        BlockBehaviour.Properties.of().randomTicks().strength(0.2F, 3.0F)
                                        .sound(SoundType.GRASS).noOcclusion().noCollision()
                                        .pushReaction(PushReaction.DESTROY));

        public static final Supplier<Block> AZURE_FROGLASS = registerBlock("azure_froglass",
                        props -> new TransparentBlock(props),
                        BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS));
        public static final Supplier<Block> VERDANT_FROGLASS = registerBlock("verdant_froglass",
                        props -> new TransparentBlock(props),
                        BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS));
        public static final Supplier<Block> CRIMSON_FROGLASS = registerBlock("crimson_froglass",
                        props -> new TransparentBlock(props),
                        BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS));

        public static final Supplier<Block> AZURE_FROGLASS_PANE = registerBlock("azure_froglass_pane",
                        props -> new IronBarsBlock(props),
                        BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS_PANE));
        public static final Supplier<Block> VERDANT_FROGLASS_PANE = registerBlock("verdant_froglass_pane",
                        props -> new IronBarsBlock(props),
                        BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS_PANE));
        public static final Supplier<Block> CRIMSON_FROGLASS_PANE = registerBlock("crimson_froglass_pane",
                        props -> new IronBarsBlock(props),
                        BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS_PANE));

        public static final Supplier<Block> SHELLSTONE = registerBlock("shellstone",
                        Block::new,
                        BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE));
        public static final Supplier<Block> SHELLSTONE_STAIRS = registerBlock("shellstone_stairs",
                        props -> new StairBlock(SHELLSTONE.get().defaultBlockState(), props),
                        BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE));
        public static final Supplier<Block> SHELLSTONE_SLAB = registerBlock("shellstone_slab",
                        SlabBlock::new,
                        BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE));
        public static final Supplier<Block> SHELLSTONE_WALL = registerBlock("shellstone_wall",
                        WallBlock::new,
                        BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE));
        public static final Supplier<Block> SHELLSTONE_BRICKS = registerBlock("shellstone_bricks",
                        Block::new,
                        BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE));
        public static final Supplier<Block> SHELLSTONE_BRICK_STAIRS = registerBlock("shellstone_brick_stairs",
                        props -> new StairBlock(SHELLSTONE_BRICKS.get().defaultBlockState(), props),
                        BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE));
        public static final Supplier<Block> SHELLSTONE_BRICK_SLAB = registerBlock("shellstone_brick_slab",
                        SlabBlock::new,
                        BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE));
        public static final Supplier<Block> SHELLSTONE_BRICK_WALL = registerBlock("shellstone_brick_wall",
                        WallBlock::new,
                        BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE));

        public static final Supplier<Block> CUT_SHELLSTONE = registerBlock("cut_shellstone",
                        Block::new,
                        BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE));
        public static final Supplier<Block> CUT_SHELLSTONE_STAIRS = registerBlock("cut_shellstone_stairs",
                        props -> new StairBlock(CUT_SHELLSTONE.get().defaultBlockState(), props),
                        BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE));
        public static final Supplier<Block> CUT_SHELLSTONE_SLAB = registerBlock("cut_shellstone_slab",
                        SlabBlock::new,
                        BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE));
        public static final Supplier<Block> CUT_SHELLSTONE_WALL = registerBlock("cut_shellstone_wall",
                        WallBlock::new,
                        BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE));

        public static final Supplier<Block> SMOOTH_SHELLSTONE = registerBlock("smooth_shellstone",
                        Block::new,
                        BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE));
        public static final Supplier<Block> SMOOTH_SHELLSTONE_STAIRS = registerBlock("smooth_shellstone_stairs",
                        props -> new StairBlock(SMOOTH_SHELLSTONE.get().defaultBlockState(), props),
                        BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE));
        public static final Supplier<Block> SMOOTH_SHELLSTONE_SLAB = registerBlock("smooth_shellstone_slab",
                        SlabBlock::new,
                        BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE));
        public static final Supplier<Block> SMOOTH_SHELLSTONE_WALL = registerBlock("smooth_shellstone_wall",
                        WallBlock::new,
                        BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE));

        // Items for blocks that need to be registered separately (to avoid circular
        // dependency)
        public static Supplier<Item> GLOW_GOOP_ITEM;
        public static Supplier<Item> CHRYSALIS_ITEM;

        public static void init() {
                // Register items for blockOnly blocks AFTER all blocks are defined
                // This avoids circular dependencies with NaturalistItems

                // Register glow_goop item with proper key
                Item.Properties glowGoopProps = new Item.Properties().setId(itemKey("glow_goop"));
                GLOW_GOOP_ITEM = CommonPlatformHelper.registerItem("glow_goop",
                                () -> new GlowGoopItem(GLOW_GOOP_BLOCK.get(), glowGoopProps));

                // Register chrysalis item with proper key
                Item.Properties chrysalisProps = new Item.Properties().stacksTo(1).setId(itemKey("chrysalis"));
                CHRYSALIS_ITEM = CommonPlatformHelper.registerItem("chrysalis",
                                () -> new BlockItem(CHRYSALIS_BLOCK.get(), chrysalisProps));

                // Complete GlowGoopBlock initialization with item reference
                GlowGoopBlock.completeInit(() -> GLOW_GOOP_ITEM.get());
        }

        // MC 1.21 requires Block.Properties to have setId() called before creating the
        // Block
        private static <T extends Block> Supplier<T> registerBlock(String name,
                        Function<BlockBehaviour.Properties, T> factory,
                        BlockBehaviour.Properties properties) {
                // Set the block id on properties before creating the block
                properties = properties.setId(blockKey(name));
                BlockBehaviour.Properties finalProps = properties;
                Supplier<T> supplier = CommonPlatformHelper.registerBlock(name, () -> factory.apply(finalProps));
                // Also register the block item with proper item id
                Item.Properties itemProps = new Item.Properties().setId(itemKey(name));
                CommonPlatformHelper.registerItem(name, () -> new BlockItem(supplier.get(), itemProps));
                return supplier;
        }

        private static <T extends Block> Supplier<T> registerBlockOnly(String name,
                        Function<BlockBehaviour.Properties, T> factory,
                        BlockBehaviour.Properties properties) {
                properties = properties.setId(blockKey(name));
                BlockBehaviour.Properties finalProps = properties;
                return CommonPlatformHelper.registerBlock(name, () -> factory.apply(finalProps));
        }
}
