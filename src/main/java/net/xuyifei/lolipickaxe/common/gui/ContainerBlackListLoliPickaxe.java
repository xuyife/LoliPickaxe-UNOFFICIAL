package net.xuyifei.lolipickaxe.common.gui;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import net.xuyifei.lolipickaxe.common.menu.ModMenuTypes;
import net.xuyifei.lolipickaxe.common.registry.ModDataComponents;
import net.xuyifei.lolipickaxe.common.registry.tool.IContainer;
import org.jetbrains.annotations.NotNull;

public class ContainerBlackListLoliPickaxe extends AbstractContainerMenu {
    private ItemStackHandler items = new ItemStackHandler(81);
    private Player player;
    private ItemStack stack;
    private int slotIndex;

    public ContainerBlackListLoliPickaxe(int windowId, Inventory playerInventory, ItemStack stack, int slotIndex) {
        super(ModMenuTypes.LOLI_PICKAXE_BLACK_LIST.get(), windowId);
        if (!stack.isEmpty() && stack.getItem() instanceof IContainer) {
            this.stack = stack;
            this.player = playerInventory.player;
            this.slotIndex = slotIndex;

            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    this.addSlot(new SlotItemHandler(this.items, i * 9 + j, j * 18 + 8, i * 18 + 8));
                }
            }

            for (int i = 0; i < 3; ++i) {
                for (int j = 0; j < 9; ++j) {
                    this.addSlot(new Slot(playerInventory, i * 9 + j + 9, j * 18 + 8, i * 18 + 174));
                }
            }

            for (int i = 0; i < 9; ++i) {
                this.addSlot(new Slot(playerInventory, i, i * 18 + 8, 232));
            }

            if (stack.has(ModDataComponents.LOLI_BLACK_LIST_DATA.get())) {
                CompoundTag nbt = stack.get(ModDataComponents.LOLI_BLACK_LIST_DATA.get()).copyTag();
                if (nbt.contains("Blacklist")) {
                    ListTag blackList = nbt.getList("Blacklist", Tag.TAG_COMPOUND);
                    if (blackList.size() <= items.getSlots()) {
                        for (int i = 0; i < blackList.size(); i++) {
                            CompoundTag black = blackList.getCompound(i);
                            if (black.contains("Slot") && black.contains("Name") && black.contains("Damage")) {
                                Item blackItem = BuiltInRegistries.ITEM.get(ResourceLocation.tryParse(black.getString("Name")));
                                ItemStack blackStack = new ItemStack(blackItem, 1);
                                blackStack.set(DataComponents.DAMAGE, black.getInt("Damage"));
                                items.setStackInSlot(black.getInt("Slot"), blackStack);
                            }
                        }
                    }
                }
            }
        } else {
            this.stack = ItemStack.EMPTY;
        }
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return !stack.isEmpty() && (slotIndex == -1 || slotIndex == player.getInventory().selected);
    }

    @Override
    public void clicked(int slotId, int dragType, @NotNull ClickType clickType, @NotNull Player player) {
        if (slotIndex != -1 && slotId == 108 + slotIndex) {
            return;
        } else if (slotId >= 0 && slotId < items.getSlots()) {
            if (clickType == ClickType.PICKUP) {
                ItemStack stack = player.containerMenu.getCarried().copy();
                stack.setCount(1);
                items.setStackInSlot(slotId, stack);
            }
            return;
        }
        super.clicked(slotId, dragType, clickType, player);
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public void removed(@NotNull Player player) {
        super.removed(player);
        if (!stack.isEmpty()) {
            CompoundTag nbt = stack.getOrDefault(ModDataComponents.LOLI_BLACK_LIST_DATA.get(), CustomData.EMPTY).copyTag();

            ListTag blackList = new ListTag();
            for (int i = 0; i < items.getSlots(); i++) {
                ItemStack blackStack = items.getStackInSlot(i);
                if (!blackStack.isEmpty()) {
                    CompoundTag black = new CompoundTag();
                    black.putInt("Slot", i);
                    black.putString("Name", blackStack.getItemHolder().getRegisteredName());
                    black.putInt("Damage", blackStack.getDamageValue());
                    blackList.add(black);
                }
            }
            nbt.put("Blacklist", blackList);
            stack.set(ModDataComponents.LOLI_BLACK_LIST_DATA.get(), CustomData.of(nbt));
        }
    }
}
