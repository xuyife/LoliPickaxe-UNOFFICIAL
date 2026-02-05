package net.xuyifei.lolipickaxe.common.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.xuyifei.lolipickaxe.LoliPickaxe;
import net.xuyifei.lolipickaxe.common.entity.EntityLoli;
import org.jetbrains.annotations.NotNull;

public class ModelLoli extends EntityModel<EntityLoli> {
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(LoliPickaxe.MODID, "loli"), "main");

    private final ModelPart leftLeg;
    private final ModelPart rightLeg;
    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart hair;
    private final ModelPart leftArm;
    private final ModelPart rightArm;

    public ModelLoli(ModelPart root) {
        this.leftLeg = root.getChild("left_leg");
        this.rightLeg = root.getChild("right_leg");
        this.body = root.getChild("body");
        this.head = root.getChild("head");
        this.hair = this.head.getChild("hair");
        this.leftArm = root.getChild("left_arm");
        this.rightArm = root.getChild("right_arm");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition leftLeg = partdefinition.addOrReplaceChild("left_leg",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-1.5F, -1.0F, -1.5F, 3, 8, 3),
                PartPose.offset(2.0F, 17.0F, 0.0F));

        PartDefinition rightLeg = partdefinition.addOrReplaceChild("right_leg",
                CubeListBuilder.create()
                        .texOffs(12, 0)
                        .addBox(-1.5F, -1.0F, -1.5F, 3, 8, 3),
                PartPose.offset(-2.0F, 17.0F, 0.0F));

        PartDefinition body = partdefinition.addOrReplaceChild("body",
                CubeListBuilder.create(),
                PartPose.offset(0.0F, 14.0F, 0.0F));

        body.addOrReplaceChild("body_part1",
                CubeListBuilder.create()
                        .texOffs(0, 41)
                        .addBox(-5.0F, 2.0F, -5.0F, 10, 2, 10),
                PartPose.ZERO);

        body.addOrReplaceChild("body_part2",
                CubeListBuilder.create()
                        .texOffs(4, 31)
                        .addBox(-4.0F, 0.0F, -4.0F, 8, 2, 8),
                PartPose.ZERO);

        body.addOrReplaceChild("body_part3",
                CubeListBuilder.create()
                        .texOffs(8, 20)
                        .addBox(-3.0F, -5.0F, -3.0F, 6, 5, 6),
                PartPose.ZERO);

        body.addOrReplaceChild("body_part4",
                CubeListBuilder.create()
                        .texOffs(12, 13)
                        .addBox(-2.0F, -8.0F, -2.0F, 4, 3, 4),
                PartPose.ZERO);

        PartDefinition head = partdefinition.addOrReplaceChild("head",
                CubeListBuilder.create()
                        .texOffs(48, 0)
                        .addBox(-4.0F, -4.5F, -4.0F, 8, 8, 8, new CubeDeformation(-0.5F)),
                PartPose.offset(0.0F, 5.0F, 0.0F));

        PartDefinition hair = head.addOrReplaceChild("hair",
                CubeListBuilder.create(),
                PartPose.ZERO);

        hair.addOrReplaceChild("hair_part1",
                CubeListBuilder.create()
                        .texOffs(80, 0)
                        .addBox(-4.0F, -4.5F, -4.0F, 8, 1, 8),
                PartPose.ZERO);

        hair.addOrReplaceChild("hair_part2",
                CubeListBuilder.create()
                        .texOffs(80, 9)
                        .addBox(-4.0F, -3.5F, 3.0F, 8, 9, 1),
                PartPose.ZERO);

        hair.addOrReplaceChild("hair_part3",
                CubeListBuilder.create()
                        .texOffs(112, 24)
                        .addBox(-3.0F, 5.5F, 3.0F, 6, 2, 1),
                PartPose.ZERO);

        hair.addOrReplaceChild("hair_part4",
                CubeListBuilder.create()
                        .texOffs(80, 19)
                        .addBox(-2.0F, 7.5F, 3.5F, 4, 1, 1),
                PartPose.ZERO);

        hair.addOrReplaceChild("hair_part5",
                CubeListBuilder.create()
                        .texOffs(90, 19)
                        .addBox(-2.0F, 8.5F, 4.0F, 4, 1, 1),
                PartPose.ZERO);

        hair.addOrReplaceChild("hair_part6",
                CubeListBuilder.create()
                        .texOffs(80, 21)
                        .addBox(-1.0F, 9.5F, 4.5F, 2, 1, 1),
                PartPose.ZERO);

        hair.addOrReplaceChild("hair_part7",
                CubeListBuilder.create()
                        .texOffs(86, 21)
                        .addBox(-1.0F, 10.5F, 5.0F, 2, 1, 1),
                PartPose.ZERO);

        hair.addOrReplaceChild("hair_part8",
                CubeListBuilder.create()
                        .texOffs(104, 14)
                        .addBox(0.0F, -1.5F, -4.0F, 1, 1, 1),
                PartPose.ZERO);

        hair.addOrReplaceChild("hair_part9",
                CubeListBuilder.create()
                        .texOffs(98, 15)
                        .addBox(1.0F, -3.5F, -4.0F, 1, 1, 1),
                PartPose.ZERO);

        hair.addOrReplaceChild("hair_part10",
                CubeListBuilder.create()
                        .texOffs(104, 12)
                        .addBox(-2.0F, -3.5F, -4.0F, 1, 1, 1),
                PartPose.ZERO);

        hair.addOrReplaceChild("hair_part11",
                CubeListBuilder.create()
                        .texOffs(98, 17)
                        .addBox(-4.0F, -1.5F, -4.0F, 1, 1, 1),
                PartPose.ZERO);

        hair.addOrReplaceChild("hair_part12",
                CubeListBuilder.create()
                        .texOffs(104, 16)
                        .addBox(3.0F, -1.5F, -4.0F, 1, 1, 1),
                PartPose.ZERO);

        hair.addOrReplaceChild("hair_part13",
                CubeListBuilder.create()
                        .texOffs(104, 9)
                        .addBox(-1.0F, -3.5F, -4.0F, 2, 2, 1),
                PartPose.ZERO);

        hair.addOrReplaceChild("hair_part14",
                CubeListBuilder.create()
                        .texOffs(98, 9)
                        .addBox(2.0F, -3.5F, -4.0F, 2, 2, 1),
                PartPose.ZERO);

        hair.addOrReplaceChild("hair_part15",
                CubeListBuilder.create()
                        .texOffs(98, 12)
                        .addBox(-4.0F, -3.5F, -4.0F, 2, 2, 1),
                PartPose.ZERO);

        hair.addOrReplaceChild("hair_part16",
                CubeListBuilder.create()
                        .texOffs(112, 0)
                        .addBox(-4.0F, -3.5F, -3.0F, 1, 4, 6),
                PartPose.ZERO);

        hair.addOrReplaceChild("hair_part17",
                CubeListBuilder.create()
                        .texOffs(102, 18)
                        .addBox(-4.0F, 0.5F, -1.0F, 1, 1, 4),
                PartPose.ZERO);

        hair.addOrReplaceChild("hair_part18",
                CubeListBuilder.create()
                        .texOffs(112, 20)
                        .addBox(-4.0F, 1.5F, 1.0F, 1, 1, 2),
                PartPose.ZERO);

        hair.addOrReplaceChild("hair_part19",
                CubeListBuilder.create()
                        .texOffs(124, 20)
                        .addBox(-4.0F, 2.5F, 2.0F, 1, 1, 1),
                PartPose.ZERO);

        hair.addOrReplaceChild("hair_part20",
                CubeListBuilder.create()
                        .texOffs(102, 23)
                        .addBox(3.0F, 0.5F, -1.0F, 1, 1, 4),
                PartPose.ZERO);

        hair.addOrReplaceChild("hair_part21",
                CubeListBuilder.create()
                        .texOffs(118, 20)
                        .addBox(3.0F, 1.5F, 1.0F, 1, 1, 2),
                PartPose.ZERO);

        hair.addOrReplaceChild("hair_part22",
                CubeListBuilder.create()
                        .texOffs(124, 22)
                        .addBox(3.0F, 2.5F, 2.0F, 1, 1, 1),
                PartPose.ZERO);

        hair.addOrReplaceChild("hair_part23",
                CubeListBuilder.create()
                        .texOffs(112, 10)
                        .addBox(3.0F, -3.5F, -3.0F, 1, 4, 6),
                PartPose.ZERO);

        PartDefinition leftArm = partdefinition.addOrReplaceChild("left_arm",
                CubeListBuilder.create()
                        .texOffs(26, 0)
                        .addBox(0.0F, 2.0F, -1.0F, 2, 5, 2)
                        .texOffs(24, 7)
                        .addBox(-0.5F, -1.0F, -1.5F, 3, 3, 3),
                PartPose.offsetAndRotation(3.0F, 10.0F, 0.0F, 0.0F, 0.0F, -0.3491F));

        PartDefinition rightArm = partdefinition.addOrReplaceChild("right_arm",
                CubeListBuilder.create()
                        .texOffs(38, 0)
                        .addBox(-2.0F, 2.0F, -1.0F, 2, 5, 2)
                        .texOffs(36, 7)
                        .addBox(-2.5F, -1.0F, -1.5F, 3, 3, 3),
                PartPose.offsetAndRotation(-3.0F, 10.0F, 0.0F, 0.0F, 0.0F, 0.3491F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(@NotNull EntityLoli entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.leftArm.xRot = Mth.cos(limbSwing + (float)Math.PI) * limbSwingAmount * 1.5F;
        this.rightArm.xRot = Mth.cos(limbSwing) * limbSwingAmount * 1.5F;
        this.leftLeg.xRot = Mth.cos(limbSwing) * limbSwingAmount * 1.5F;
        this.rightLeg.xRot = Mth.cos(limbSwing + (float)Math.PI) * limbSwingAmount * 1.5F;
        this.head.yRot = netHeadYaw * Mth.DEG_TO_RAD;
        this.head.xRot = headPitch * Mth.DEG_TO_RAD;
    }

    @Override
    public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        leftLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        rightLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        body.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        head.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        leftArm.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        rightArm.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }
}
