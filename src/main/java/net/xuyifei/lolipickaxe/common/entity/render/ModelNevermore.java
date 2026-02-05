package net.xuyifei.lolipickaxe.common.entity.render;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.xuyifei.lolipickaxe.LoliPickaxe;
import net.xuyifei.lolipickaxe.common.util.obj.GroupObject;
import net.xuyifei.lolipickaxe.common.util.obj.ObjModelManager;
import net.xuyifei.lolipickaxe.common.util.obj.WavefrontObject;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class ModelNevermore extends EntityModel<Entity> {
    private final WavefrontObject nevermore;
    private GroupObject head;
    private GroupObject la;
    private GroupObject ra;

    private final Map<String, float[]> rotationCache = Maps.newHashMap();

    public ModelNevermore(EntityRendererProvider.Context context) {
        this.nevermore = ObjModelManager.getModel(
                ResourceLocation.fromNamespaceAndPath(LoliPickaxe.MODID, "models/entity/loli/loli.obj"));

        for (GroupObject group : this.nevermore.groupObjects) {
            if (group.name.equals("head")) {
                this.head = group;
            } else if (group.name.equals("la")) {
                this.la = group;
            } else if (group.name.equals("ra")) {
                this.ra = group;
            }
        }

        rotationCache.put("la", new float[]{0, 0, 0});
        rotationCache.put("ra", new float[]{0, 0, 0});
        rotationCache.put("head", new float[]{0, 0, 0});
    }

    @Override
    public void setupAnim(@NotNull Entity entity, float limbSwing, float limbSwingAmount,
                          float ageInTicks, float netHeadYaw, float headPitch) {

        if (la != null) {
            float laAngleX = Mth.cos(limbSwing + (float) Math.PI) * limbSwingAmount * 1.5F;
            la.setRotationAngles(laAngleX, 0, 0);
            rotationCache.put("la", new float[]{laAngleX, 0, 0});
        }

        if (ra != null) {
            float raAngleX = Mth.cos(limbSwing) * limbSwingAmount * 1.5F;
            ra.setRotationAngles(raAngleX, 0, 0);
            rotationCache.put("ra", new float[]{raAngleX, 0, 0});
        }

        if (head != null) {
            float headYaw = -netHeadYaw * Mth.DEG_TO_RAD;
            float headPitchRad = headPitch * Mth.DEG_TO_RAD;
            head.setRotationAngles(headPitchRad, headYaw, 0);
            rotationCache.put("head", new float[]{headPitchRad, headYaw, 0});
        }
    }

    @Override
    public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        float[] rgbaColor = fromColor(color);

        poseStack.pushPose();

        poseStack.mulPose(Axis.XP.rotationDegrees(180));
        poseStack.translate(0, -1.5, 0);

        nevermore.render(poseStack, vertexConsumer, packedLight, packedOverlay,
                rgbaColor[0], rgbaColor[1], rgbaColor[2], rgbaColor[3]);

        poseStack.popPose();
    }

    public void render(PoseStack poseStack, VertexConsumer vertexConsumer,
                       int packedLight, int packedOverlay,
                       float red, float green, float blue, float alpha,
                       float limbSwing, float limbSwingAmount,
                       float netHeadYaw, float headPitch) {

        updateAnimations(limbSwing, limbSwingAmount, netHeadYaw, headPitch);

        renderToBuffer(poseStack, vertexConsumer, packedLight, packedOverlay, toColor(red, green, blue, alpha));
    }

    public void renderBatch(PoseStack poseStack, VertexConsumer vertexConsumer,
                            int packedLight, int packedOverlay) {

        renderToBuffer(poseStack, vertexConsumer, packedLight, packedOverlay,
                toColor(1.0f, 1.0f, 1.0f, 1.0f));
    }

    private void updateAnimations(float limbSwing, float limbSwingAmount,
                                  float netHeadYaw, float headPitch) {

        if (la != null) {
            float laAngleX = Mth.cos(limbSwing + (float) Math.PI) * limbSwingAmount * 1.5F;
            la.setRotationAngles(laAngleX, 0, 0);
        }

        if (ra != null) {
            float raAngleX = Mth.cos(limbSwing) * limbSwingAmount * 1.5F;
            ra.setRotationAngles(raAngleX, 0, 0);
        }

        if (head != null) {
            float headYaw = -netHeadYaw * Mth.DEG_TO_RAD;
            float headPitchRad = headPitch * Mth.DEG_TO_RAD;
            head.setRotationAngles(headPitchRad, headYaw, 0);
        }
    }

    public GroupObject getHead() {
        return head;
    }

    public GroupObject getLeftArm() {
        return la;
    }

    public GroupObject getRightArm() {
        return ra;
    }

    public void setPartRotation(String partName, float x, float y, float z) {
        GroupObject part = getPartByName(partName);
        if (part != null) {
            part.setRotationAngles(x, y, z);
            rotationCache.put(partName, new float[]{x, y, z});
        }
    }

    public GroupObject getPartByName(String name) {
        for (GroupObject group : nevermore.groupObjects) {
            if (group.name.equals(name)) {
                return group;
            }
        }
        return null;
    }

    public void resetRotations() {
        for (GroupObject group : nevermore.groupObjects) {
            group.setRotationAngles(0, 0, 0);
        }
        rotationCache.clear();
    }

    public WavefrontObject getModel() {
        return nevermore;
    }

    public static float[] fromColor(int color) {
        float alpha = (color >> 24 & 0xFF) / 255.0f;
        float red = (color >> 16 & 0xFF) / 255.0f;
        float green = (color >> 8 & 0xFF) / 255.0f;
        float blue = (color & 0xFF) / 255.0f;
        return new float[]{red, green, blue, alpha};
    }

    private int toColor(float red, float green, float blue, float alpha) {
        int a = (int)(alpha * 255) & 0xFF;
        int r = (int)(red * 255) & 0xFF;
        int g = (int)(green * 255) & 0xFF;
        int b = (int)(blue * 255) & 0xFF;
        return (a << 24) | (r << 16) | (g << 8) | b;
    }
}
