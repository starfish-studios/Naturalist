package com.starfish_studios.naturalist.common.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.starfish_studios.naturalist.core.registry.NaturalistRecipes;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public record BugNetInteractionRecipe(ResourceLocation id, EntityType<?> entityType, ItemStack dropStack, @Nullable List<ItemStack> inputItems, int priority, boolean shouldSwing, @Nullable String playSound) implements Recipe<Container> {

    @Override
    public boolean matches(@NotNull Container container, @NotNull Level level) {
        return false;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull Container container, @NotNull RegistryAccess registry) {
        return dropStack;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return false;
    }

    @Override
    public @NotNull ItemStack getResultItem(@NotNull RegistryAccess registry) {
        return dropStack;
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return id;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return NaturalistRecipes.BUG_NET_SERIALIZER;
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return NaturalistRecipes.BUG_NET;
    }

    @SuppressWarnings("deprecation")
    public static class Serializer implements RecipeSerializer<BugNetInteractionRecipe> {
        private static ItemStack itemStackFromJsonWithOptionalCount(JsonObject json) {
            ItemStack stack = ShapedRecipe.itemStackFromJson(json);
            if (!json.has("count")) {
                stack.setCount(1);
            }
            return stack;
        }

        @Override
        public @NotNull BugNetInteractionRecipe fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject serializedRecipe) {
            String path = recipeId.getPath();
            if (!path.startsWith("net/")) {
                throw new JsonSyntaxException("Bug net recipe must be located in recipes/net/ subdirectory. Recipe at " + recipeId + " is invalid. Expected path format: <namespace>:net/<recipe_name>");
            }

            JsonObject resultObject = GsonHelper.getAsJsonObject(serializedRecipe, "result");

            ItemStack output = itemStackFromJsonWithOptionalCount(resultObject);
            if (output.isEmpty()) {
                throw new IllegalArgumentException("Invalid or empty output ItemStack for recipe " + recipeId);
            }

            String entityTypeId;
            if (serializedRecipe.has("entity")) {
                entityTypeId = GsonHelper.getAsString(serializedRecipe, "entity");
            } else {
                entityTypeId = GsonHelper.getAsString(serializedRecipe, "entity_type");
            }
            if (entityTypeId.isEmpty()) {
                throw new JsonSyntaxException("Missing or invalid 'entity' or 'entity_type' in recipe: " + recipeId);
            }

            EntityType<?> entityType = BuiltInRegistries.ENTITY_TYPE.get(new ResourceLocation(entityTypeId));

            List<ItemStack> inputItems = null;
            JsonElement inputElement = null;
            if (serializedRecipe.has("ingredients")) {
                inputElement = serializedRecipe.get("ingredients");
            } else if (serializedRecipe.has("input")) {
                inputElement = serializedRecipe.get("input");
            }
            
            if (inputElement != null) {
                inputItems = new ArrayList<>();
                
                if (inputElement.isJsonArray()) {
                    JsonArray inputArray = inputElement.getAsJsonArray();
                    for (JsonElement element : inputArray) {
                        if (element.isJsonObject()) {
                            ItemStack input = itemStackFromJsonWithOptionalCount(element.getAsJsonObject());
                            if (!input.isEmpty()) {
                                inputItems.add(input);
                            }
                        }
                    }
                } else if (inputElement.isJsonObject()) {
                    ItemStack input = itemStackFromJsonWithOptionalCount(inputElement.getAsJsonObject());
                    if (!input.isEmpty()) {
                        inputItems.add(input);
                    }
                }
                
                if (inputItems.isEmpty()) {
                    inputItems = null;
                }
            }

            int priority = GsonHelper.getAsInt(serializedRecipe, "priority", 0);

            boolean shouldSwing = GsonHelper.getAsBoolean(serializedRecipe, "should_swing", true);

            String playSound = null;
            if (serializedRecipe.has("play_sound")) {
                JsonElement soundElement = serializedRecipe.get("play_sound");
                if (soundElement.isJsonPrimitive() && soundElement.getAsJsonPrimitive().isString()) {
                    String soundValue = soundElement.getAsString();
                    if (!soundValue.equals("false")) {
                        playSound = soundValue;
                    } else {
                        playSound = "false";
                    }
                }
            }

            return new BugNetInteractionRecipe(recipeId, entityType, output, inputItems, priority, shouldSwing, playSound);
        }

        @Override
        public BugNetInteractionRecipe fromNetwork(@NotNull ResourceLocation recipeId, FriendlyByteBuf buffer) {
            ItemStack output = buffer.readItem();
            EntityType<?> entityType = BuiltInRegistries.ENTITY_TYPE.get(buffer.readResourceLocation());
            int inputCount = buffer.readVarInt();
            List<ItemStack> inputItems = null;
            if (inputCount > 0) {
                inputItems = new ArrayList<>();
                for (int i = 0; i < inputCount; i++) {
                    inputItems.add(buffer.readItem());
                }
            }
            int priority = buffer.readVarInt();
            boolean shouldSwing = buffer.readBoolean();
            String playSound = buffer.readBoolean() ? buffer.readUtf() : null;

            return new BugNetInteractionRecipe(recipeId, entityType, output, inputItems, priority, shouldSwing, playSound);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, BugNetInteractionRecipe recipe) {
            buffer.writeItem(recipe.dropStack);
            buffer.writeResourceLocation(BuiltInRegistries.ENTITY_TYPE.getKey(recipe.entityType));
            if (recipe.inputItems != null) {
                buffer.writeVarInt(recipe.inputItems.size());
                for (ItemStack input : recipe.inputItems) {
                    buffer.writeItem(input);
                }
            } else {
                buffer.writeVarInt(0);
            }
            buffer.writeVarInt(recipe.priority);
            buffer.writeBoolean(recipe.shouldSwing);
            buffer.writeBoolean(recipe.playSound != null);
            if (recipe.playSound != null) {
                buffer.writeUtf(recipe.playSound);
            }
        }
    }
}