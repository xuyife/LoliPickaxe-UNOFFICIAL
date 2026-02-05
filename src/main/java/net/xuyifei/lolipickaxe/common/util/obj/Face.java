package net.xuyifei.lolipickaxe.common.util.obj;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.util.Mth;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.nio.ByteBuffer;

public class Face {

    public static int defaultColor = 0xFFFFFFFF;

    public static void setColor(int color) {
        defaultColor = color;
    }

    public static void resetColor() {
        defaultColor = 0xFFFFFFFF;
    }

    public Vertex[] vertices;
    public Vertex[] vertexNormals;
    public Vertex faceNormal;
    public TextureCoordinate[] textureCoordinates;
    public int glDrawingMode;
    public String usemtl;

    private ByteBuffer cachedBuffer;

    public Vertex calculateFaceNormal() {
        if (vertices.length < 3) {
            return new Vertex(0, 0, 0);
        }

        float x1 = vertices[1].x - vertices[0].x;
        float y1 = vertices[1].y - vertices[0].y;
        float z1 = vertices[1].z - vertices[0].z;

        float x2 = vertices[2].x - vertices[0].x;
        float y2 = vertices[2].y - vertices[0].y;
        float z2 = vertices[2].z - vertices[0].z;

        float nx = y1 * z2 - z1 * y2;
        float ny = z1 * x2 - x1 * z2;
        float nz = x1 * y2 - y1 * x2;

        float length = Mth.sqrt(nx * nx + ny * ny + nz * nz);
        if (length > 0) {
            nx /= length;
            ny /= length;
            nz /= length;
        }

        return new Vertex(nx, ny, nz);
    }

    public void render(VertexConsumer vertexConsumer, PoseStack.Pose pose, int packedLight, int packedOverlay) {
        render(vertexConsumer, pose, 0.0005F, packedLight, packedOverlay);
    }

    public void render(VertexConsumer vertexConsumer, PoseStack.Pose pose, float textureOffset,
                       int packedLight, int packedOverlay) {

        if (faceNormal == null) {
            faceNormal = this.calculateFaceNormal();
        }

        float averageU = 0F;
        float averageV = 0F;
        int textureCoordinatesLength = 0;

        if (textureCoordinates != null && textureCoordinates.length > 0) {
            textureCoordinatesLength = textureCoordinates.length;
            for (TextureCoordinate tc : textureCoordinates) {
                averageU += tc.u;
                averageV += tc.v;
            }
            averageU /= textureCoordinatesLength;
            averageV /= textureCoordinatesLength;
        }

        int a = (defaultColor >> 24) & 0xFF;
        int r = (defaultColor >> 16) & 0xFF;
        int g = (defaultColor >> 8) & 0xFF;
        int b = defaultColor & 0xFF;

        float red = r / 255.0f;
        float green = g / 255.0f;
        float blue = b / 255.0f;
        float alpha = a / 255.0f;

        int overlayU = packedOverlay & 0xFFFF;
        int overlayV = (packedOverlay >> 16) & 0xFFFF;

        for (int i = 0; i < vertices.length; ++i) {
            Vertex vertex = vertices[i];

            Vector4f pos = new Vector4f(vertex.x, vertex.y, vertex.z, 1.0f);
            pos.mul(pose.pose());

            float texU = 0, texV = 0;
            if (textureCoordinates != null && i < textureCoordinatesLength) {
                TextureCoordinate texCoord = textureCoordinates[i];

                float offsetU = textureOffset;
                float offsetV = textureOffset;

                if (texCoord.u > averageU) offsetU = -offsetU;
                if (texCoord.v > averageV) offsetV = -offsetV;

                texU = texCoord.u + offsetU;
                texV = texCoord.v + offsetV;
            }

            float nx, ny, nz;
            if (vertexNormals != null && i < vertexNormals.length) {
                Vertex normal = vertexNormals[i];
                Vector3f norm = new Vector3f(normal.x, normal.y, normal.z);
                norm.mul(pose.normal());
                nx = norm.x * -1.05f;
                ny = norm.y * -1.05f;
                nz = norm.z * -1.05f;
            } else {
                Vector3f norm = new Vector3f(faceNormal.x, faceNormal.y, faceNormal.z);
                norm.mul(pose.normal());
                nx = norm.x;
                ny = norm.y;
                nz = norm.z;
            }

            float length = Mth.sqrt(nx * nx + ny * ny + nz * nz);
            if (length > 0) {
                nx /= length;
                ny /= length;
                nz /= length;
            }
            vertexConsumer
                    .addVertex(pos.x(), pos.y(), pos.z())
                    .setColor(red, green, blue, alpha)
                    .setUv(texU, texV)  // 这是 UV0（主纹理坐标）
                    .setUv1(overlayU, overlayV)  // 这是 UV1（光照/覆盖纹理坐标）
                    .setLight(packedLight)
                    .setNormal(nx, ny, nz);
        }
    }

    public static void renderFaces(Iterable<Face> faces, VertexConsumer vertexConsumer,
                                   PoseStack.Pose pose, int packedLight, int packedOverlay) {
        for (Face face : faces) {
            face.render(vertexConsumer, pose, packedLight, packedOverlay);
        }
    }

    public int getVertexCount() {
        return vertices != null ? vertices.length : 0;
    }

    public int[] getTriangleIndices() {
        if (vertices == null || vertices.length < 3) {
            return new int[0];
        }

        int[] indices = new int[(vertices.length - 2) * 3];
        int index = 0;

        for (int i = 1; i < vertices.length - 1; i++) {
            indices[index++] = 0;
            indices[index++] = i;
            indices[index++] = i + 1;
        }

        return indices;
    }

    public void clearCache() {
        cachedBuffer = null;
    }

}
