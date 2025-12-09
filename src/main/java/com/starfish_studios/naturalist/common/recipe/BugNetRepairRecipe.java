package com.starfish_studios.naturalist.common.recipe;

import com.google.gson.JsonObject;
import com.starfish_studios.naturalist.core.registry.NaturalistRecipes;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class BugNetRepairRecipe implements CraftingRecipe {
    private final ResourceLocation id;
    private final Ingredient tool;
    private final Ingredient repairMaterial;
    private final ItemStack result;

    public BugNetRepairRecipe(ResourceLocation id, Ingredient tool, Ingredient repairMaterial, ItemStack result) {
        this.id = id;
        this.tool = tool;
        this.repairMaterial = repairMaterial;
        this.result = result;
    }

    @Override
    public boolean matches(CraftingContainer container, @NotNull Level level) {
        List<ItemStack> items = new ArrayList<>();
        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            if (!stack.isEmpty()) {
                items.add(stack);
            }
        }
        
        if (items.size() < 2) {
            return false;
        }
        
        boolean hasTool = false;
        int repairMaterialCount = 0;
        
        for (ItemStack stack : items) {
            if (tool.test(stack)) {
                hasTool = true;
            } else if (repairMaterial.test(stack)) {
                repairMaterialCount++;
            } else {
                return false;
            }
        }
        
        return hasTool && repairMaterialCount > 0;
    }

    @Override
    public @NotNull ItemStack assemble(CraftingContainer container, @NotNull RegistryAccess registry) {
        ItemStack toolStack = ItemStack.EMPTY;
        int repairMaterialCount = 0;
        
        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            if (tool.test(stack)) {
                toolStack = stack;
            } else if (repairMaterial.test(stack)) {
                repairMaterialCount++;
            }
        }

        ItemStack resultStack = result.copy();
        if (!toolStack.isEmpty()) {
            if (toolStack.hasTag()) {
                CompoundTag toolTag = toolStack.getTag();
                if (toolTag != null && toolTag.contains("display", 10)) {
                    CompoundTag displayTag = toolTag.getCompound("display");
                    
                    CompoundTag preservedDisplay = new CompoundTag();
                    if (displayTag.contains("Name", 8)) {
                        preservedDisplay.putString("Name", displayTag.getString("Name"));
                    }
                    if (displayTag.contains("Lore", 9)) {
                        ListTag loreTag = displayTag.getList("Lore", 8);
                        preservedDisplay.put("Lore", loreTag.copy());
                    }
                    
                    if (!preservedDisplay.isEmpty()) {
                        resultStack.getOrCreateTag().put("display", preservedDisplay);
                    }
                }
            }
            
            int maxDurability = toolStack.getMaxDamage();
            int currentDamage = toolStack.getDamageValue();
            int repairPerString = maxDurability / 3;
            int totalRepair = repairPerString * repairMaterialCount;
            
            int newDamage = Math.max(0, currentDamage - totalRepair);
            resultStack.setDamageValue(newDamage);
        }
        
        return resultStack;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public @NotNull ItemStack getResultItem(@NotNull RegistryAccess registry) {
        return result;
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return id;
    }

    @Override
    public @NotNull CraftingBookCategory category() {
        return CraftingBookCategory.MISC;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return NaturalistRecipes.BUG_NET_REPAIR_SERIALIZER;
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return RecipeType.CRAFTING;
    }

    public static class Serializer implements RecipeSerializer<BugNetRepairRecipe> {
        @Override
        public @NotNull BugNetRepairRecipe fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject serializedRecipe) {
            JsonObject toolObj = GsonHelper.getAsJsonObject(serializedRecipe, "tool");
            Ingredient tool = Ingredient.fromJson(toolObj);
            
            JsonObject repairObj = GsonHelper.getAsJsonObject(serializedRecipe, "repair_material");
            Ingredient repairMaterial = Ingredient.fromJson(repairObj);
            
            ItemStack result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(serializedRecipe, "result"));
            
            return new BugNetRepairRecipe(recipeId, tool, repairMaterial, result);
        }

        @Override
        public BugNetRepairRecipe fromNetwork(@NotNull ResourceLocation recipeId, @NotNull FriendlyByteBuf buffer) {
            Ingredient tool = Ingredient.fromNetwork(buffer);
            Ingredient repairMaterial = Ingredient.fromNetwork(buffer);
            ItemStack result = buffer.readItem();
            
            return new BugNetRepairRecipe(recipeId, tool, repairMaterial, result);
        }

        @Override
        public void toNetwork(@NotNull FriendlyByteBuf buffer, @NotNull BugNetRepairRecipe recipe) {
            recipe.tool.toNetwork(buffer);
            recipe.repairMaterial.toNetwork(buffer);
            buffer.writeItem(recipe.result);
        }
    }
}

