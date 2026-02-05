package net.xuyifei.lolipickaxe.common.gui;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.client.Minecraft;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.xuyifei.lolipickaxe.LoliPickaxe;
import net.xuyifei.lolipickaxe.common.registry.ModDataComponents;
import net.xuyifei.lolipickaxe.common.registry.tool.IContainer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class InventoryLoliBase implements ILoliInventory {
    private final ItemStack stack;
    private final List<NonNullList<ItemStack>> pages;
    private int currentPage;
    private final Component displayName;
    private final HolderLookup.Provider registryAccess;

    public InventoryLoliBase(ItemStack stack, Component displayName, HolderLookup.Provider registryAccess) {
        this.stack = stack;
        this.pages = Lists.newArrayList();
        this.displayName = displayName;
        this.registryAccess = registryAccess;
        loadFromItem();
    }

    private void loadFromItem() {
        pages.clear();

        CompoundTag customData = stack.getOrDefault(ModDataComponents.LOLI_INVENTORY_DATA, CustomData.EMPTY).copyTag();

        this.currentPage = customData.getInt("CurrentPage");

        ListTag pageList = customData.getList("Pages", Tag.TAG_COMPOUND);
        if (pageList.isEmpty()) {
            pages.add(NonNullList.withSize(getContainerSize(), ItemStack.EMPTY));
            saveToItem();
        } else {
            for (int i = 0; i < pageList.size(); i++) {
                CompoundTag pageTag = pageList.getCompound(i);
                NonNullList<ItemStack> page = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);

                ListTag items = pageTag.getList("Items", Tag.TAG_COMPOUND);
                for (int j = 0; j < items.size(); j++) {
                    CompoundTag itemTag = items.getCompound(j);
                    int slot = itemTag.getInt("Slot");
                    if (slot >= 0 && slot < page.size()) {
                        ItemStack itemStack = ItemStack.parseOptional(registryAccess, itemTag);
                        itemStack.setCount(itemTag.getInt("count"));
                        page.set(slot, itemStack);
                    }
                }
                pages.add(page);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void saveToItem() {
        CompoundTag customData = new CompoundTag();
        customData.putInt("CurrentPage", currentPage);

        ListTag pageList = new ListTag();
        for (NonNullList<ItemStack> page : pages) {
            CompoundTag pageTag = new CompoundTag();
            ListTag items = new ListTag();

            for (int i = 0; i < page.size(); i++) {
                ItemStack itemStack = page.get(i);
                if (!itemStack.isEmpty()) {
                    CompoundTag itemTag = new CompoundTag();
                    itemTag.putInt("Slot", i);
                    itemTag.putString("id", itemStack.getItemHolder().getRegisteredName());
                    itemTag.putInt("count", itemStack.getCount());
                    if (!itemStack.getComponents().isEmpty()) {
                        CompoundTag components = new CompoundTag();
                        itemStack.getComponentsPatch().entrySet().forEach((entry) -> {
                            var type = entry.getKey();
                            var value = entry.getValue();
                            ResourceLocation id = BuiltInRegistries.DATA_COMPONENT_TYPE.getKey(type);
                            if (id != null) {
                                serializeComponent((DataComponentType<Object>) type, value.orElse(null), components, id.getNamespace() + ":" + id.getPath());
                            }
                        });
                        itemTag.put("components", components);
                    }
                    items.add(itemTag);
                }
            }

            pageTag.put("Items", items);
            pageList.add(pageTag);
        }

        customData.put("Pages", pageList);
        stack.set(ModDataComponents.LOLI_INVENTORY_DATA, CustomData.of(customData));
    }

    @Override
    public int getContainerSize() {
        return 81;
    }

    @Override
    public boolean isEmpty() {
        return pages.stream()
                .flatMap(List::stream)
                .allMatch(ItemStack::isEmpty);
    }

    @Override
    public @NotNull ItemStack getItem(int index) {
        if (index < 0 || index >= getContainerSize()) {
            return ItemStack.EMPTY;
        }
        return getPage(currentPage).get(index);
    }

    @Override
    public @NotNull ItemStack removeItem(int index, int count) {
        NonNullList<ItemStack> currentPageItems = getPage(currentPage);
        ItemStack stack = currentPageItems.get(index);
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }

        ItemStack result;
        if (stack.getCount() <= count) {
            result = stack;
            currentPageItems.set(index, ItemStack.EMPTY);
        } else {
            result = stack.split(count);
            if (stack.getCount() == 0) {
                currentPageItems.set(index, ItemStack.EMPTY);
            }
        }

        setChanged();
        return result;
    }

    @Override
    public @NotNull ItemStack removeItemNoUpdate(int index) {
        NonNullList<ItemStack> currentPageItems = getPage(currentPage);
        ItemStack stack = currentPageItems.get(index);
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }

        currentPageItems.set(index, ItemStack.EMPTY);
        setChanged();
        return stack;
    }

    @Override
    public void setItem(int index, @NotNull ItemStack stack) {
        NonNullList<ItemStack> currentPageItems = getPage(currentPage);
        currentPageItems.set(index, stack);

        if (!stack.isEmpty() && stack.getCount() > getMaxStackSize()) {
            stack.setCount(getMaxStackSize());
        }

        setChanged();
    }

    @Override
    public void setChanged() {
        saveToItem();
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return ILoliInventory.super.stillValid(player);
    }

    @Override
    public void clearContent() {
        pages.forEach(List::clear);
        pages.clear();
        setChanged();
    }

    @Override
    public NonNullList<ItemStack> getPage(int index) {
        if (index < 0 || index >= getMaxPage()) {
            return NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
        }

        while (index >= pages.size()) {
            pages.add(NonNullList.withSize(getContainerSize(), ItemStack.EMPTY));
        }

        return pages.get(index);
    }

    @Override
    public int getCurrentPage() {
        return currentPage;
    }

    @Override
    public void setCurrentPage(int page) {
        if (page < 0) {
            page = 0;
        } else if (page >= getMaxPage()) {
            page = getMaxPage() - 1;
        }

        this.currentPage = page;
        setChanged();
    }

    @Override
    public int getAllHasItemPageIndex() {
        if (!stack.isEmpty() && stack.has(ModDataComponents.LOLI_INVENTORY_DATA.get())) {
            ListTag listTag = stack.get(ModDataComponents.LOLI_INVENTORY_DATA.get()).copyTag().getList("Pages", Tag.TAG_COMPOUND);
            return listTag.size();
        }
        return -1;
    }

    @Override
    public boolean canPlaceItem(int index, @NotNull ItemStack stack) {
        if (!(stack.getItem() instanceof IContainer)) {
            return true;
        }
        return false;
    }

    @Override
    public void startOpen(@NotNull Player player) {
        ILoliInventory.super.startOpen(player);
    }

    @Override
    public void stopOpen(@NotNull Player player) {
        setChanged();
    }

    private void serializeComponent(DataComponentType<Object> type, Object value,
                                               CompoundTag tag, String key) {
        try {
            Codec<Object> codec = type.codecOrThrow();
            DataResult<Tag> result = codec.encodeStart(registryAccess.createSerializationContext(NbtOps.INSTANCE), value);

            result.ifSuccess(nbt -> tag.put(key, nbt)).ifError(error -> {
                // Ignored
            });

        } catch (Exception ignored) {
        }
    }

    public static Object convertTagToObject(Tag tag) {
        if (tag == null) {
            return null;
        }

        byte type = tag.getId();

        return switch (type) {
            case Tag.TAG_BYTE -> ((ByteTag) tag).getAsByte();
            case Tag.TAG_SHORT -> ((ShortTag) tag).getAsShort();
            case Tag.TAG_INT -> ((IntTag) tag).getAsInt();
            case Tag.TAG_LONG -> ((LongTag) tag).getAsLong();
            case Tag.TAG_FLOAT -> ((FloatTag) tag).getAsFloat();
            case Tag.TAG_DOUBLE -> ((DoubleTag) tag).getAsDouble();
            case Tag.TAG_STRING -> tag.getAsString();
            case Tag.TAG_BYTE_ARRAY -> ((ByteArrayTag) tag).getAsByteArray();
            case Tag.TAG_INT_ARRAY -> ((IntArrayTag) tag).getAsIntArray();
            case Tag.TAG_LONG_ARRAY -> ((LongArrayTag) tag).getAsLongArray();
            case Tag.TAG_LIST -> convertListTag((ListTag) tag);
            case Tag.TAG_COMPOUND -> tag;
            default -> tag;
        };
    }

    private static List<Object> convertListTag(ListTag listTag) {
        List<Object> list = new ArrayList<>();

        for (Tag element : listTag) {
            list.add(convertTagToObject(element));
        }

        return list;
    }
}
