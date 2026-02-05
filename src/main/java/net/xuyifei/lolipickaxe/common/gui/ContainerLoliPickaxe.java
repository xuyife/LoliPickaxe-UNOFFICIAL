package net.xuyifei.lolipickaxe.common.gui;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.xuyifei.lolipickaxe.common.menu.ModMenuTypes;
import net.xuyifei.lolipickaxe.common.registry.tool.IContainer;
import org.jetbrains.annotations.NotNull;

public class ContainerLoliPickaxe extends AbstractContainerMenu {

    private final ILoliInventory inventory;
    private final ItemStack pickaxeStack;
    private final int slotIndex;

    public ContainerLoliPickaxe(int containerId, Inventory playerInventory, ItemStack pickaxeStack, int slotIndex) {
        super(ModMenuTypes.LOLI_PICKAXE_CONTAINER.get(), containerId);

        this.pickaxeStack = pickaxeStack;
        this.slotIndex = slotIndex;
        this.inventory = ((IContainer) pickaxeStack.getItem()).getInventory(pickaxeStack, playerInventory.player.level().registryAccess());
        this.inventory.startOpen(playerInventory.player);

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(inventory, row * 9 + col, col * 18 + 8, row * 18 + 8) {
                    @Override
                    public boolean mayPlace(@NotNull ItemStack stack) {
                        return inventory.canPlaceItem(getSlotIndex(), stack);
                    }

                    @Override
                    public int getMaxStackSize() {
                        return inventory.getMaxStackSize();
                    }

                    @Override
                    public int getMaxStackSize(@NotNull ItemStack stack) {
                        return inventory.getMaxStackSize();
                    }
                });
            }
        }

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 174 + row * 18));
            }
        }

        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 232));
        }
    }

    public ContainerLoliPickaxe(int containerId, Inventory playerInventory, ItemStack pickaxeStack, int slotIndex, boolean isSmall) {
        super(ModMenuTypes.SMALL_LOLI_PICKAXE_CONTAINER.get(), containerId);

        this.pickaxeStack = pickaxeStack;
        this.slotIndex = slotIndex;
        this.inventory = ((IContainer) pickaxeStack.getItem()).getInventory(pickaxeStack, playerInventory.player.level().registryAccess());
        this.inventory.startOpen(playerInventory.player);

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(inventory, row * 9 + col, col * 18 + 8, row * 18 + 8) {
                    @Override
                    public boolean mayPlace(@NotNull ItemStack stack) {
                        return inventory.canPlaceItem(getSlotIndex(), stack);
                    }

                    @Override
                    public int getMaxStackSize() {
                        return inventory.getMaxStackSize();
                    }

                    @Override
                    public int getMaxStackSize(@NotNull ItemStack stack) {
                        return inventory.getMaxStackSize();
                    }
                });
            }
        }

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 174 + row * 18));
            }
        }

        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 232));
        }
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return !pickaxeStack.isEmpty() &&
                (slotIndex == -1 ? pickaxeStack == player.getOffhandItem() :
                        slotIndex == player.getInventory().selected && pickaxeStack == player.getMainHandItem());
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            itemstack = slotStack.copy();

            if (index < 81) {
                if (!this.moveItemStackTo(slotStack, 81, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (!this.moveItemStackTo(slotStack, 0, 81, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (slotStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemstack;
    }

    public void prePage() {
        inventory.setCurrentPage(inventory.getCurrentPage() - 1);
        broadcastFullState();
    }

    public void nextPage() {
        inventory.setCurrentPage(inventory.getCurrentPage() + 1);
        broadcastFullState();
    }

    public int getCurrentPage() {
        return inventory.getCurrentPage();
    }

    public int getMaxPage() {
        return inventory.getMaxPage();
    }
}
