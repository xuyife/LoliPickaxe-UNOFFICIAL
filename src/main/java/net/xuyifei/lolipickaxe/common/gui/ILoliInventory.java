package net.xuyifei.lolipickaxe.common.gui;

import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface ILoliInventory extends Container {
    int getMaxPage();

    int getAllHasItemPageIndex();

    NonNullList<ItemStack> getPage(int index);

    boolean cancelStackLimit();

    int getCurrentPage();

    void setCurrentPage(int page);

    @Override
    default boolean canPlaceItem(int index, @NotNull ItemStack stack) {
        return true;
    }

    @Override
    default int getMaxStackSize() {
        return 64;
    }

    @Override
    default void setChanged() {
    }

    @Override
    default boolean stillValid(@NotNull Player player) {
        return true;
    }

    @Override
    default void clearContent() {
    }

    @Override
    default void startOpen(@NotNull Player player) {
    }

    @Override
    default void stopOpen(@NotNull Player player) {
    }
}
