//? if forge {
package com.starfish_studios.naturalist.core.platform.forge;

import com.starfish_studios.naturalist.core.registry.NaturalistMenus;
import com.starfish_studios.naturalist.common.item.forge.CaughtMobItem;
import com.starfish_studios.naturalist.common.item.forge.CaughtMobWithVariantsItem;
import com.starfish_studios.naturalist.common.item.forge.NoFluidMobBucketWithVariantsItem;
import com.starfish_studios.naturalist.util.forge.NaturalistBrewingRecipe;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.*;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

import static com.starfish_studios.naturalist.Naturalist.MOD_ID;

public class CommonPlatformHelperImpl {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MOD_ID);
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, MOD_ID);
    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTIONS, MOD_ID);
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, MOD_ID);

    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, MOD_ID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, MOD_ID);

    public static <T extends Block> Supplier<T> registerBlock(String name, Supplier<T> block) {
        return BLOCKS.register(name, block);
    }

    public static <T extends BlockEntity> Supplier<BlockEntityType<T>> registerBlockEntityType(String name, Supplier<BlockEntityType<T>> factory) {
        return BLOCK_ENTITY_TYPES.register(name, factory);
    }

    public static <T extends Item> Supplier<T> registerItem(String name, Supplier<T> item) {
        return ITEMS.register(name, item);
    }

    public static <T extends Mob> Supplier<SpawnEggItem> registerSpawnEggItem(String name, Supplier<EntityType<T>> entityType, int backgroundColor, int highlightColor) {
        return ITEMS.register(name, () -> new ForgeSpawnEggItem(entityType, backgroundColor, highlightColor, new Item.Properties()));
    }

    public static Supplier<Item> registerNoFluidMobBucketItem(String name, Supplier<? extends EntityType<?>> entitySupplier, Supplier<? extends Fluid> fluidSupplier, @NotNull Supplier<? extends SoundEvent> soundSupplier, int color) {
        return ITEMS.register(name, () -> new NoFluidMobBucketWithVariantsItem(entitySupplier, fluidSupplier, soundSupplier, new Item.Properties().stacksTo(1).craftRemainder(Items.BUCKET), color));
    }

    public static Supplier<Item> registerMobBucketItem(String name, Supplier<? extends EntityType<?>> entitySupplier, Supplier<? extends Fluid> fluidSupplier, Supplier<? extends SoundEvent> soundSupplier) {
        return ITEMS.register(name, () -> new MobBucketItem(entitySupplier, fluidSupplier, soundSupplier, new Item.Properties().stacksTo(1).craftRemainder(Items.BUCKET)));
    }

    public static Supplier<Item> registerCaughtMobItem(String name, Supplier<? extends EntityType<?>> entitySupplier, Supplier<? extends Fluid> fluidSupplier, Supplier<? extends SoundEvent> soundSupplier) {
        return ITEMS.register(name, () -> new CaughtMobItem(entitySupplier, fluidSupplier, soundSupplier, new Item.Properties().stacksTo(1)));
    }

    public static Supplier<Item> registerCaughtMobItem(String name, Supplier<? extends EntityType<?>> entitySupplier, Supplier<? extends Fluid> fluidSupplier, Supplier<? extends SoundEvent> soundSupplier, int variantAmount) {
        return ITEMS.register(name, () -> new CaughtMobWithVariantsItem(entitySupplier, fluidSupplier, soundSupplier, variantAmount, new Item.Properties().stacksTo(1)));
    }

    public static <T extends SoundEvent> Supplier<T> registerSoundEvent(String name, Supplier<T> soundEvent) {
        return SOUND_EVENTS.register(name, soundEvent);
    }

    public static <T extends Entity> Supplier<EntityType<T>> registerEntityType(String name, EntityType.EntityFactory<T> factory, MobCategory category, float width, float height, int clientTrackingRange) {
        return ENTITY_TYPES.register(name, () -> EntityType.Builder.of(factory, category).sized(width, height).clientTrackingRange(clientTrackingRange).build(name));
    }

    public static <T extends AbstractContainerMenu> Supplier<MenuType<T>> registerMenuType(String name, Supplier<MenuType<T>> supplier) {
        return MENU_TYPES.register(name, supplier);
    }

    public static <T extends AbstractContainerMenu> MenuType<T> createMenuType(NaturalistMenus.MenuFactory<T> factory) {
        return IForgeMenuType.create(factory::create);
    }

    public static void openMenu(ServerPlayer player, MenuProvider provider) {
        NetworkHooks.openScreen(player, provider);
    }

    public static <T extends Potion> Supplier<T> registerPotion(String name, Supplier<T> potion) {
        return POTIONS.register(name, potion);
    }

    public static void registerBrewingRecipe(Potion input, Item ingredient, Potion output) {
        BrewingRecipeRegistry.addRecipe(new NaturalistBrewingRecipe(input, ingredient, output));
    }

    public static <T extends Mob> void registerSpawnPlacement(EntityType<T> entityType, SpawnPlacements.Type decoratorType, Heightmap.Types heightMapType, SpawnPlacements.@NotNull SpawnPredicate<T> decoratorPredicate) {
        SpawnPlacements.register(entityType, decoratorType, heightMapType, decoratorPredicate);
    }

    public static void registerCompostable(float chance, ItemLike item) {
        ComposterBlock.COMPOSTABLES.put(item.asItem(), chance);
    }

    public static void registerRecipes(String name, Supplier<RecipeType<?>> type, Supplier<RecipeSerializer<?>> serializer) {
        RECIPE_TYPES.register(name, type);
        RECIPE_SERIALIZERS.register(name, serializer);
    }

    public static TagKey<Item> getShearsTag() {
        return Tags.Items.SHEARS;
    }
}
//?}
