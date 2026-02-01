package com.starfish_studios.naturalist.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GlowGoopItem extends BlockItem {

    public GlowGoopItem(Block block, @NotNull Item.Properties properties) {
        super(block, properties);
    }

    // @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip,
            TooltipFlag flag) {
        /*
         * if (Screen.hasShiftDown()) {
         * tooltip.add(Component.literal("Place up to 3").withStyle(ChatFormatting.GRAY)
         * );
         * tooltip.add(Component.literal("in one space!").withStyle(ChatFormatting.GRAY)
         * );
         * } else {
         * tooltip.add(Component.literal("Hold ").withStyle(ChatFormatting.GRAY)
         * .append(Component.literal("Shift").withStyle(ChatFormatting.YELLOW,
         * ChatFormatting.ITALIC))
         * .append(Component.literal(" for info!").withStyle(ChatFormatting.GRAY)));
         * }
         */
        tooltip.add(Component.literal("Place up to 3 in one space!").withStyle(ChatFormatting.GRAY));
    }
}
