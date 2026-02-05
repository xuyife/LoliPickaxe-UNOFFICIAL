package net.xuyifei.lolipickaxe.common.gui;

import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.xuyifei.lolipickaxe.common.registry.ModItems;
import net.xuyifei.lolipickaxe.common.registry.custom.SmallLoliPickaxe;

public class InventorySmallLoliPickaxe extends InventoryLoliBase {
    private ItemStack stack;

    public InventorySmallLoliPickaxe(ItemStack stack, HolderLookup.Provider registryAccess) {
        super(stack, Component.translatable("gui.lolipickaxe.small_container.title"), registryAccess);
        this.stack = stack;
    }

    @Override
    public int getMaxStackSize() {
        return cancelStackLimit() ? Integer.MAX_VALUE : getMaxPage() * 32;
    }

    @Override
    public int getMaxPage() {
        return ((SmallLoliPickaxe) ModItems.SMALL_LOLI_PICKAXE.get()).getMaxPage(stack);
    }

    @Override
    public boolean cancelStackLimit() {
        return false;
    }
}
