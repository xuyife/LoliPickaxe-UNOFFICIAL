package net.xuyifei.lolipickaxe.common.gui;

import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.xuyifei.lolipickaxe.common.registry.ModConfigs;

public class InventoryLoliPickaxe extends InventoryLoliBase {
    public InventoryLoliPickaxe(ItemStack stack, HolderLookup.Provider registryAccess) {
        super(stack, Component.translatable("gui.lolipickaxe.container.title"), registryAccess);
    }

    @Override
    public int getMaxStackSize() {
        return cancelStackLimit() ? Integer.MAX_VALUE : ModConfigs.getInt("loliPickaxeSlotStackLimit");
    }

    @Override
    public int getMaxPage() {
        return ModConfigs.getInt("loliPickaxeMaxPage");
    }

    @Override
    public boolean cancelStackLimit() {
        return ModConfigs.getBoolean("loliPickaxeCancelStackLimit");
    }
}
