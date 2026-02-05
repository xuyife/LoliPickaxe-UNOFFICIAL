package net.xuyifei.lolipickaxe.common.util;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.platform.TextureUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.xuyifei.lolipickaxe.LoliPickaxe;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.Set;

public class LoliCardOnlineUtil {
    private static Set<String> loading = Sets.newHashSet();
    private static Map<String, ResourceLocation> urlToTexture = Maps.newHashMap();
    private static Map<String, Integer> urlToWidth = Maps.newHashMap();
    private static Map<String, Integer> urlToHeight = Maps.newHashMap();

    public static boolean isLoad(String url) {
        return urlToTexture.containsKey(url);
    }

    public static ResourceLocation getResourceLocation(String url) {
        return urlToTexture.get(url);
    }

    public static void load(String url) {
        if (!loading.contains(url)) {
            add(url);
            new Thread(() -> {
                try {
                    URLConnection connection = new URL(url).openConnection();
                    connection.setDoOutput(true);
                    BufferedImage src = ImageIO.read(connection.getInputStream());
                    Minecraft.getInstance().execute(() -> {
                        try {
                            // 将BufferedImage转换为NativeImage
                            NativeImage nativeImage = bufferedImageToNativeImage(src);
                            int textureId = TextureUtil.generateTextureId();
                            TextureUtil.prepareImage(textureId, nativeImage.getWidth(), nativeImage.getHeight());
                            nativeImage.upload(0, 0, 0, false);

                            // 创建ResourceLocation
                            ResourceLocation resourceLocation = ResourceLocation.fromNamespaceAndPath(LoliPickaxe.MODID, "texture_" + url.hashCode());
                            urlToTexture.put(url, resourceLocation);
                            urlToWidth.put(url, nativeImage.getWidth());
                            urlToHeight.put(url, nativeImage.getHeight());

                            // 注册纹理
                            Minecraft.getInstance().getTextureManager().register(resourceLocation, new AbstractTexture() {
                                @Override
                                public void load(@NotNull ResourceManager resourceManager) throws IOException {
                                    // 纹理已经在上面上传了
                                }

                                @Override
                                public int getId() {
                                    return textureId;
                                }
                            });

                            remove(url);
                            nativeImage.close();
                        } catch (Exception ignored) {
                        }
                    });
                } catch (IOException ignored) {
                }
            }).start();
        }
    }

    private static NativeImage bufferedImageToNativeImage(BufferedImage bufferedImage) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        NativeImage nativeImage = new NativeImage(NativeImage.Format.RGBA, width, height, false);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int argb = bufferedImage.getRGB(x, y);
                int a = (argb >> 24) & 0xFF;
                int r = (argb >> 16) & 0xFF;
                int g = (argb >> 8) & 0xFF;
                int b = argb & 0xFF;
                nativeImage.setPixelRGBA(x, y, (a << 24) | (b << 16) | (g << 8) | r);
            }
        }

        return nativeImage;
    }

    public synchronized static void add(String url) {
        loading.add(url);
    }

    public synchronized static void remove(String url) {
        loading.remove(url);
    }

    public static void unload(String url) {
        if (urlToTexture.containsKey(url)) {
            ResourceLocation resourceLocation = urlToTexture.get(url);
            Minecraft.getInstance().getTextureManager().release(resourceLocation);
            urlToTexture.remove(url);
            urlToWidth.remove(url);
            urlToHeight.remove(url);
        }
    }

    public static void bind(String url) {
        if (urlToTexture.containsKey(url)) {
            ResourceLocation resourceLocation = urlToTexture.get(url);
            RenderSystem.setShaderTexture(0, resourceLocation);
        }
    }

    public static int getWidth(String url) {
        if (urlToWidth.containsKey(url)) {
            return urlToWidth.get(url);
        }
        return 0;
    }

    public static int getHeight(String url) {
        if (urlToHeight.containsKey(url)) {
            return urlToHeight.get(url);
        }
        return 0;
    }
}
