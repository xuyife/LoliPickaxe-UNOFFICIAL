package net.xuyifei.lolipickaxe.common.registry.tool;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.xuyifei.lolipickaxe.common.gui.ILoliInventory;

public interface IContainer {
    boolean hasInventory(ItemStack stack);

    ILoliInventory getInventory(ItemStack stack, HolderLookup.Provider registryAccess);
}
