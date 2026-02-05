package net.xuyifei.lolipickaxe.common.util.obj;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector3f;

import java.nio.FloatBuffer;
import java.util.function.Function;

public class Texture {

    public String name;
    public ResourceLocation texture;
    public FloatBuffer ka;
    public FloatBuffer kd;
    public FloatBuffer ks;
    public FloatBuffer ns;

    private Vector3f ambientColor = new Vector3f(0.2f, 0.2f, 0.2f);
    private Vector3f diffuseColor = new Vector3f(0.8f, 0.8f, 0.8f);
    private Vector3f specularColor = new Vector3f(0.0f, 0.0f, 0.0f);
    private float shininess = 32.0f;
    private float alpha = 1.0f;

    private boolean initialized = false;

    public Texture(String name) {
        this(name, null, null, null, null);
    }

    public Texture(String name, ResourceLocation texture) {
        this(name, texture, null, null, null);
    }

    public Texture(String name, ResourceLocation texture, float[] ka, float[] kd, float[] ks) {
        this.name = name;
        this.texture = texture;
        updateMaterialProperties(ka, kd, ks, 32.0f);
    }

    public Texture(String name, ResourceLocation texture, float[] ka, float[] kd, float[] ks, float ns) {
        this(name, texture, ka, kd, ks);
        updateMaterialProperties(ka, kd, ks, ns);
    }

    public Texture(String name, String textureNamespace, String texturePath) {
        this(name, ResourceLocation.fromNamespaceAndPath(textureNamespace, texturePath),
                null, null, null);
    }

    public Texture(String name, String textureNamespace, String texturePath,
                   float[] ka, float[] kd, float[] ks, float ns) {
        this(name, ResourceLocation.fromNamespaceAndPath(textureNamespace, texturePath),
                ka, kd, ks, ns);
    }

    public void updateMaterialProperties(float[] ka, float[] kd, float[] ks, float ns) {
        if (ka != null && ka.length >= 3) {
            this.ambientColor.set(ka[0], ka[1], ka[2]);
            this.ka = FloatBuffer.wrap(ka);
        }
        if (kd != null && kd.length >= 3) {
            this.diffuseColor.set(kd[0], kd[1], kd[2]);
            this.kd = FloatBuffer.wrap(kd);
        }
        if (ks != null && ks.length >= 3) {
            this.specularColor.set(ks[0], ks[1], ks[2]);
            this.ks = FloatBuffer.wrap(ks);
        }
        this.shininess = ns;
        if (ns >= 0) {
            this.ns = FloatBuffer.wrap(new float[]{ns});
        }
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public RenderType getRenderType(Function<ResourceLocation, RenderType> renderTypeFunction) {
        if (texture == null) {
            return RenderType.entityCutoutNoCull(ResourceLocation.fromNamespaceAndPath("minecraft", "missing"));
        }

        if (alpha < 1.0f) {
            return RenderType.entityTranslucent(texture);
        } else {
            return renderTypeFunction.apply(texture);
        }
    }

    public void applyMaterialProperties(ShaderInstance shader) {
        if (shader == null) {
            return;
        }

        try {
            if (shader.getUniform("AmbientColor") != null) {
                shader.getUniform("AmbientColor").set(ambientColor.x, ambientColor.y, ambientColor.z);
            }

            if (shader.getUniform("DiffuseColor") != null) {
                shader.getUniform("DiffuseColor").set(diffuseColor.x, diffuseColor.y, diffuseColor.z);
            }

            if (shader.getUniform("SpecularColor") != null) {
                shader.getUniform("SpecularColor").set(specularColor.x, specularColor.y, specularColor.z);
            }

            if (shader.getUniform("Shininess") != null) {
                shader.getUniform("Shininess").set(shininess);
            }

            if (shader.getUniform("Alpha") != null) {
                shader.getUniform("Alpha").set(alpha);
            }

            initialized = true;

        } catch (Exception ignored) {
        }
    }

    @Deprecated
    public void applyLegacyMaterialProperties() {

        if (texture != null) {
            RenderSystem.setShaderTexture(0, texture);
        }

        initialized = true;
    }

    public Vector3f getAmbientColor() {
        return ambientColor;
    }

    public Vector3f getDiffuseColor() {
        return diffuseColor;
    }

    public Vector3f getSpecularColor() {
        return specularColor;
    }

    public float getShininess() {
        return shininess;
    }

    public float getAlpha() {
        return alpha;
    }

    public ResourceLocation getTextureLocation() {
        return texture;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void resetInitialization() {
        initialized = false;
    }

    public static Texture createStandardMaterial(String name, String namespace, String path) {
        float[] ka = {0.2f, 0.2f, 0.2f};
        float[] kd = {0.8f, 0.8f, 0.8f};
        float[] ks = {0.0f, 0.0f, 0.0f};
        float ns = 32.0f;

        ResourceLocation textureLoc = ResourceLocation.fromNamespaceAndPath(namespace, path);
        return new Texture(name, textureLoc, ka, kd, ks, ns);
    }

    public static Texture createStandardMaterial(String name, ResourceLocation texture) {
        return createStandardMaterial(name, texture.getNamespace(), texture.getPath());
    }

    public static Texture createMetalMaterial(String name, String namespace, String path) {
        float[] ka = {0.25f, 0.25f, 0.25f};
        float[] kd = {0.4f, 0.4f, 0.4f};
        float[] ks = {0.774597f, 0.774597f, 0.774597f};
        float ns = 76.8f;

        ResourceLocation textureLoc = ResourceLocation.fromNamespaceAndPath(namespace, path);
        return new Texture(name, textureLoc, ka, kd, ks, ns);
    }

    public static Texture createPlasticMaterial(String name, String namespace, String path) {
        float[] ka = {0.0f, 0.0f, 0.0f};
        float[] kd = {0.55f, 0.55f, 0.55f};
        float[] ks = {0.70f, 0.70f, 0.70f};
        float ns = 32.0f;

        ResourceLocation textureLoc = ResourceLocation.fromNamespaceAndPath(namespace, path);
        return new Texture(name, textureLoc, ka, kd, ks, ns);
    }

    public static ResourceLocation createResourceLocation(String namespace, String path) {
        return ResourceLocation.fromNamespaceAndPath(namespace, path);
    }

    public static ResourceLocation parseResourceLocation(String location) {
        String[] parts = location.split(":", 2);
        if (parts.length == 2) {
            return ResourceLocation.fromNamespaceAndPath(parts[0], parts[1]);
        } else {
            return ResourceLocation.fromNamespaceAndPath("minecraft", location);
        }
    }
}
