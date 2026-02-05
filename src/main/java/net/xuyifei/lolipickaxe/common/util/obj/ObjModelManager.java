package net.xuyifei.lolipickaxe.common.util.obj;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import net.minecraft.resources.ResourceLocation;
import net.xuyifei.lolipickaxe.LoliPickaxe;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

public class ObjModelManager {
    private static WavefrontObject defaultModel;
    private static final ResourceLocation resourceDefaultModel =
            ResourceLocation.fromNamespaceAndPath(LoliPickaxe.MODID, "models/entity/loli/loli.obj");

    private static LoadingCache<ResourceLocation, WavefrontObject> cache;

    static {
        try {
            defaultModel = new WavefrontObject(resourceDefaultModel);
        } catch (Exception e) {
            defaultModel = createEmptyModel();
            LoliPickaxe.LOGGER.error("Failed to load default OBJ model", e);
        }

        cache = CacheBuilder.newBuilder()
                .build(CacheLoader.asyncReloading(
                        new CacheLoader<ResourceLocation, WavefrontObject>() {
                            @Override
                            public @NotNull WavefrontObject load(@NotNull ResourceLocation key) throws Exception {
                                try {
                                    return new WavefrontObject(key);
                                } catch (Exception e) {
                                    LoliPickaxe.LOGGER.error("Failed to load OBJ model: " + key, e);
                                    return defaultModel;
                                }
                            }
                        },
                        Executors.newCachedThreadPool()
                ));
    }

    public static void reload() {
        cache.invalidateAll();
        try {
            defaultModel = new WavefrontObject(resourceDefaultModel);
        } catch (Exception e) {
            defaultModel = createEmptyModel();
            LoliPickaxe.LOGGER.error("Failed to reload default OBJ model", e);
        }
    }

    public static WavefrontObject getModel(ResourceLocation loc) {
        try {
            return cache.get(loc);
        } catch (ExecutionException e) {
            LoliPickaxe.LOGGER.error("Failed to get model from cache: " + loc, e);
            return defaultModel;
        }
    }

    public static WavefrontObject getModel(String path) {
        return getModel(ResourceLocation.fromNamespaceAndPath(LoliPickaxe.MODID, path));
    }

    public static WavefrontObject getModel(String namespace, String path) {
        return getModel(ResourceLocation.fromNamespaceAndPath(namespace, path));
    }

    public static void preloadModel(ResourceLocation loc) {
        cache.refresh(loc);
    }

    public static boolean isModelCached(ResourceLocation loc) {
        return cache.asMap().containsKey(loc);
    }

    public static void invalidateModel(ResourceLocation loc) {
        cache.invalidate(loc);
    }

    public static String getCacheStats() {
        return cache.stats().toString();
    }

    private static WavefrontObject createEmptyModel() {
        try {
            return new WavefrontObject("empty", new java.io.ByteArrayInputStream(new byte[0]));
        } catch (Exception e) {
            throw new ModelFormatException("Failed to create empty model", e);
        }
    }

    public static WavefrontObject getDefaultModel() {
        return defaultModel;
    }

    public static LoadingCache<ResourceLocation, WavefrontObject> getCache() {
        return cache;
    }
}
