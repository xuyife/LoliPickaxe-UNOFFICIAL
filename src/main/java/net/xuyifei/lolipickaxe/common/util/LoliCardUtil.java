package net.xuyifei.lolipickaxe.common.util;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.xuyifei.lolipickaxe.LoliPickaxe;

import javax.imageio.ImageIO;
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

public class LoliCardUtil {

    private static List<String> customArtNames = null;
    private static List<Integer> customArtHeights = null;
    private static List<Integer> customArtWidths = null;
    private static List<ResourceLocation> customArtResources = null;

    public static void updateCustomArtDatas() {
        customArtNames = getListOfPaintings();
        if (customArtNames != null && !customArtNames.isEmpty()) {
            customArtResources = getPaintingsResourceLocations(customArtNames);
            customArtHeights = getPaintingSetHeights(customArtResources);
            customArtWidths = getPaintingSetWidths(customArtResources);
        }
    }

    public static List<String> getCustomArtNames() {
        return customArtNames;
    }

    public static List<Integer> getCustomArtHeights() {
        return customArtHeights;
    }

    public static List<Integer> getCustomArtWidths() {
        return customArtWidths;
    }

    public static List<ResourceLocation> getCustomArtResources() {
        return customArtResources;
    }

    private static List<ResourceLocation> getPaintingsResourceLocations(List<String> names) {
        List<ResourceLocation> paintings = new ArrayList<>();
        for (String name : names) {
            paintings.add(ResourceLocation.fromNamespaceAndPath(LoliPickaxe.MODID, "lolicards/" + name));
        }
        return paintings;
    }

    private static List<Integer> getPaintingSetHeights(List<ResourceLocation> resources) {
        List<Integer> heights = new ArrayList<>();
        ResourceManager resourceManager = Minecraft.getInstance().getResourceManager();

        for (ResourceLocation resource : resources) {
            try {
                Optional<Resource> theThing = resourceManager.getResource(resource);
                if (theThing.isPresent()) {
                    try (InputStream inputStream = theThing.get().open()) {
                        Image img = ImageIO.read(inputStream);
                        if (img != null) {
                            heights.add(img.getHeight(null));
                            continue;
                        }
                    }
                }
            } catch (IOException e) {
                LoliPickaxe.LOGGER.error("Failed to load painting height for: {}", resource, e);
            }
            heights.add(0); // Default height if failed to load
        }
        return heights;
    }

    private static List<Integer> getPaintingSetWidths(List<ResourceLocation> resources) {
        List<Integer> widths = new ArrayList<>();
        ResourceManager resourceManager = Minecraft.getInstance().getResourceManager();

        for (ResourceLocation resource : resources) {
            try {
                Optional<Resource> theThing = resourceManager.getResource(resource);
                if (theThing.isPresent()) {
                    try (InputStream inputStream = theThing.get().open()) {
                        Image img = ImageIO.read(inputStream);
                        if (img != null) {
                            widths.add(img.getWidth(null));
                            continue;
                        }
                    }
                }
            } catch (IOException e) {
                LoliPickaxe.LOGGER.error("Failed to load painting width for: {}", resource, e);
            }
            widths.add(0); // Default width if failed to load
        }
        return widths;
    }

    private static List<String> getListOfPaintings() {
        ResourceManager resourceManager = Minecraft.getInstance().getResourceManager();

        // 获取所有在 lolicards 命名空间下的 PNG 资源
        Set<ResourceLocation> paintingResources = resourceManager.listResources(
                        "lolicards",
                        res -> res.getPath().endsWith(".png")
                ).keySet().stream()
                .filter(resource -> resource.getNamespace().equals(LoliPickaxe.MODID))
                .collect(Collectors.toSet());

        // 提取文件名（去掉路径前缀）
        List<String> paintingNames = paintingResources.stream()
                .map(resource -> {
                    String path = resource.getPath();
                    // 移除 "lolicards/" 前缀
                    if (path.startsWith("lolicards/")) {
                        return path.substring("lolicards/".length());
                    }
                    return path;
                })
                .collect(Collectors.toList());

        return paintingNames.isEmpty() ? null : paintingNames;
    }

    // 异步加载版本（推荐使用）
    public static CompletableFuture<Void> updateCustomArtDatasAsync(Executor executor) {
        return CompletableFuture.supplyAsync(LoliCardUtil::getListOfPaintings, executor)
                .thenApply(names -> {
                    if (names != null && !names.isEmpty()) {
                        List<ResourceLocation> resources = getPaintingsResourceLocations(names);
                        List<Integer> heights = getPaintingSetHeights(resources);
                        List<Integer> widths = getPaintingSetWidths(resources);

                        customArtNames = names;
                        customArtResources = resources;
                        customArtHeights = heights;
                        customArtWidths = widths;
                    }
                    return null;
                });
    }

    // 获取指定索引的图片信息
    public static Optional<Image> getCustomArtImage(int index) {
        if (customArtResources == null || index < 0 || index >= customArtResources.size()) {
            return Optional.empty();
        }

        ResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
        ResourceLocation resource = customArtResources.get(index);

        try {
            Optional<Resource> resourceOpt = resourceManager.getResource(resource);
            if (resourceOpt.isPresent()) {
                try (InputStream inputStream = resourceOpt.get().open()) {
                    return Optional.ofNullable(ImageIO.read(inputStream));
                }
            }
        } catch (IOException e) {
            LoliPickaxe.LOGGER.error("Failed to load custom art image: {}", resource, e);
        }

        return Optional.empty();
    }
}