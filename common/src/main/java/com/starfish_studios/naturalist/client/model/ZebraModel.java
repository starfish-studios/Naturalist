package com.starfish_studios.naturalist.client.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.AbstractEquineModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.state.DonkeyRenderState;

@Environment(value= EnvType.CLIENT)
public class ZebraModel extends AbstractEquineModel<DonkeyRenderState> {
    private final ModelPart leftChest;
    private final ModelPart rightChest;

    public ZebraModel(ModelPart root) {
        super(root);
        this.leftChest = this.body.getChild("left_chest");
        this.rightChest = this.body.getChild("right_chest");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshDefinition = AbstractEquineModel.createBodyMesh(CubeDeformation.NONE);
        PartDefinition partDefinition = meshDefinition.getRoot();
        PartDefinition partDefinition2 = partDefinition.getChild("body");
        CubeListBuilder cubeListBuilder = CubeListBuilder.create().texOffs(26, 21).addBox(-4.0f, 0.0f, -2.0f, 8.0f, 8.0f, 3.0f);
        partDefinition2.addOrReplaceChild("left_chest", cubeListBuilder, PartPose.offsetAndRotation(6.0f, -8.0f, 0.0f, 0.0f, -1.5707964f, 0.0f));
        partDefinition2.addOrReplaceChild("right_chest", cubeListBuilder, PartPose.offsetAndRotation(-6.0f, -8.0f, 0.0f, 0.0f, 1.5707964f, 0.0f));
        return LayerDefinition.create(meshDefinition, 64, 64);
    }

    @Override
    public void setupAnim(DonkeyRenderState donkeyRenderState) {
        super.setupAnim(donkeyRenderState);
        this.leftChest.visible = donkeyRenderState.hasChest;
        this.rightChest.visible = donkeyRenderState.hasChest;
    }
}
