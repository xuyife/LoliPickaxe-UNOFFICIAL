package net.xuyifei.lolipickaxe.common.util.obj;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

import java.util.ArrayList;
import java.util.Map;

public class GroupObject {
    public String name;
    public ArrayList<Face> faces = new ArrayList<>();
    public float rotationPointX;
    public float rotationPointY;
    public float rotationPointZ;
    public float rotateAngleX;
    public float rotateAngleY;
    public float rotateAngleZ;

    private Matrix4f transformMatrix;
    private Matrix3f normalMatrix;
    private boolean matrixDirty = true;

    public GroupObject() {
        this("");
    }

    public GroupObject(String name) {
        this.name = name;
    }

    public void setRotationPoint(float rotationPointXIn, float rotationPointYIn, float rotationPointZIn) {
        rotationPointX = rotationPointXIn;
        rotationPointY = rotationPointYIn;
        rotationPointZ = rotationPointZIn;
        matrixDirty = true;
    }

    public void setRotationAngles(float rotateAngleX, float rotateAngleY, float rotateAngleZ) {
        this.rotateAngleX = rotateAngleX;
        this.rotateAngleY = rotateAngleY;
        this.rotateAngleZ = rotateAngleZ;
        matrixDirty = true;
    }

    private void updateTransformMatrix() {
        if (transformMatrix == null) {
            transformMatrix = new Matrix4f();
        }
        if (normalMatrix == null) {
            normalMatrix = new Matrix3f();
        }

        transformMatrix.identity();

        Quaternionf rotation = new Quaternionf();
        if (rotateAngleZ != 0.0F) {
            rotation.rotateZ(rotateAngleZ);
        }
        if (rotateAngleY != 0.0F) {
            rotation.rotateY(rotateAngleY);
        }
        if (rotateAngleX != 0.0F) {
            rotation.rotateX(rotateAngleX);
        }

        transformMatrix.translation(rotationPointX, rotationPointY, rotationPointZ);
        transformMatrix.rotate(rotation);
        transformMatrix.translate(-rotationPointX, -rotationPointY, -rotationPointZ);

        transformMatrix.get3x3(normalMatrix);

        matrixDirty = false;
    }

    public Matrix4f getTransformMatrix() {
        if (matrixDirty) {
            updateTransformMatrix();
        }
        return new Matrix4f(transformMatrix); // 返回副本
    }

    public Matrix3f getNormalMatrix() {
        if (matrixDirty) {
            updateTransformMatrix();
        }
        return new Matrix3f(normalMatrix); // 返回副本
    }

    public void render(PoseStack poseStack, VertexConsumer vertexConsumer,
                       int packedLight, int packedOverlay,
                       float red, float green, float blue, float alpha) {

        if (faces.isEmpty()) {
            return;
        }

        poseStack.pushPose();

        applyTransformations(poseStack);

        PoseStack.Pose pose = poseStack.last();

        for (Face face : faces) {
            face.render(vertexConsumer, pose, packedLight, packedOverlay);
        }

        poseStack.popPose();
    }

    public void render(PoseStack poseStack, MultiBufferSource buffer, RenderType renderType,
                       Map<String, Texture> mtl, int packedLight, int packedOverlay,
                       float red, float green, float blue, float alpha) {

        if (faces.isEmpty()) {
            return;
        }

        poseStack.pushPose();

        applyTransformations(poseStack);

        PoseStack.Pose pose = poseStack.last();

        String currentMaterial = null;
        VertexConsumer vertexConsumer = null;

        for (Face face : faces) {
            if (face.usemtl == null || !face.usemtl.equals(currentMaterial)) {
                currentMaterial = face.usemtl;

                if (mtl != null && currentMaterial != null && mtl.containsKey(currentMaterial)) {
                    Texture texture = mtl.get(currentMaterial);
                    ResourceLocation textureLocation = texture.getTextureLocation();
                    vertexConsumer = buffer.getBuffer(RenderType.entityCutout(textureLocation));
                } else {
                    vertexConsumer = buffer.getBuffer(renderType);
                }
            }

            if (vertexConsumer != null) {
                face.render(vertexConsumer, pose, packedLight, packedOverlay);
            }
        }

        poseStack.popPose();
    }

    private void applyTransformations(PoseStack poseStack) {
        poseStack.translate(rotationPointX, rotationPointY, rotationPointZ);

        if (rotateAngleZ != 0.0F) {
            poseStack.mulPose(Axis.ZP.rotation(rotateAngleZ));
        }
        if (rotateAngleY != 0.0F) {
            poseStack.mulPose(Axis.YP.rotation(rotateAngleY));
        }
        if (rotateAngleX != 0.0F) {
            poseStack.mulPose(Axis.XP.rotation(rotateAngleX));
        }

        poseStack.translate(-rotationPointX, -rotationPointY, -rotationPointZ);
    }

    public void renderDirect(VertexConsumer vertexConsumer, PoseStack.Pose pose,
                             int packedLight, int packedOverlay,
                             float red, float green, float blue, float alpha) {

        if (faces.isEmpty()) {
            return;
        }

        Matrix4f groupTransform = getTransformMatrix();
        Matrix3f groupNormal = getNormalMatrix();

        Matrix4f combinedMatrix = new Matrix4f(pose.pose());
        combinedMatrix.mul(groupTransform);

        Matrix3f combinedNormal = new Matrix3f(pose.normal());
        combinedNormal.mul(groupNormal);

        renderWithCombinedMatrix(vertexConsumer, combinedMatrix, combinedNormal,
                packedLight, packedOverlay, red, green, blue, alpha);
    }

    private void renderWithCombinedMatrix(VertexConsumer vertexConsumer,
                                          Matrix4f combinedMatrix, Matrix3f combinedNormal,
                                          int packedLight, int packedOverlay,
                                          float red, float green, float blue, float alpha) {

        PoseStack tempPoseStack = new PoseStack();

        tempPoseStack.last().pose().set(combinedMatrix);
        tempPoseStack.last().normal().set(combinedNormal);

        for (Face face : faces) {
            face.render(vertexConsumer, tempPoseStack.last(), packedLight, packedOverlay);
        }
    }

    public void renderWithMatrix(VertexConsumer vertexConsumer,
                                 Matrix4f modelMatrix, Matrix3f normalMatrix,
                                 int packedLight, int packedOverlay,
                                 float red, float green, float blue, float alpha) {

        if (faces.isEmpty()) {
            return;
        }

        Matrix4f groupTransform = getTransformMatrix();
        Matrix3f groupNormal = getNormalMatrix();

        PoseStack tempPoseStack = new PoseStack();

        tempPoseStack.last().pose().set(modelMatrix);
        tempPoseStack.last().normal().set(normalMatrix);

        applyTransformations(tempPoseStack);

        for (Face face : faces) {
            face.render(vertexConsumer, tempPoseStack.last(), packedLight, packedOverlay);
        }
    }

    public int getFaceCount() {
        return faces.size();
    }

    public int getVertexCount() {
        int count = 0;
        for (Face face : faces) {
            count += face.getVertexCount();
        }
        return count;
    }

    public void addFace(Face face) {
        faces.add(face);
    }

    public void clearFaces() {
        faces.clear();
        matrixDirty = true;
    }

    public void markMatrixDirty() {
        matrixDirty = true;
    }

    public GroupObject copy() {
        GroupObject copy = new GroupObject(this.name);
        copy.rotationPointX = this.rotationPointX;
        copy.rotationPointY = this.rotationPointY;
        copy.rotationPointZ = this.rotationPointZ;
        copy.rotateAngleX = this.rotateAngleX;
        copy.rotateAngleY = this.rotateAngleY;
        copy.rotateAngleZ = this.rotateAngleZ;

        for (Face face : this.faces) {
            copy.faces.add(face);
        }

        copy.matrixDirty = true;
        return copy;
    }
}
