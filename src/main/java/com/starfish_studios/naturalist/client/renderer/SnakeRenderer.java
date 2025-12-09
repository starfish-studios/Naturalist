package com.starfish_studios.naturalist.client.renderer;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.client.model.SnakeModel;
import com.starfish_studios.naturalist.client.renderer.layers.SleepLayer;
import com.starfish_studios.naturalist.common.entity.Snake;
//? if fabric {
/*import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
*///?} else if forge {
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
//?}
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

//? if fabric {
/*@Environment(EnvType.CLIENT)
*///?} else if forge {
@OnlyIn(Dist.CLIENT)
//?}
public class SnakeRenderer extends GeoEntityRenderer<Snake> {
    public SnakeRenderer(EntityRendererProvider.@NotNull Context renderManager) {
        super(renderManager, new SnakeModel());
        this.shadowRadius = 0.4F;
        this.addRenderLayer(new SleepLayer<>(this, new ResourceLocation(Naturalist.MOD_ID, "geo/entity/snake.geo.json"), new ResourceLocation(Naturalist.MOD_ID, "textures/entity/snake/snake_sleep.png")));
    }

    @Override
    public float getMotionAnimThreshold(Snake animatable) {
        return 0.000001f;
    }
}
