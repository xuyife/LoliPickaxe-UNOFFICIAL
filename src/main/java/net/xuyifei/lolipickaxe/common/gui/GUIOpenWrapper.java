package net.xuyifei.lolipickaxe.common.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.xuyifei.lolipickaxe.client.gui.GUIEnchantments;
import net.xuyifei.lolipickaxe.client.gui.GUILoliConfig;
import net.xuyifei.lolipickaxe.client.gui.GUIPotionEffect;
import net.xuyifei.lolipickaxe.common.registry.custom.SmallLoliPickaxe;

public class GUIOpenWrapper {
    public static void OpenLoliConfigGui(Component title, ItemStack stack) {
        Minecraft.getInstance().setScreen(new GUILoliConfig(title, stack));
    }

    public static void OpenPotionEffectGui(Component title, ItemStack stack) {
        Minecraft.getInstance().setScreen(new GUIPotionEffect(title, stack));
    }

    public static void OpenEnchantmentGui(Component title, ItemStack stack) {
        Minecraft.getInstance().setScreen(new GUIEnchantments(title, stack));
    }

    public static void OpenContainerGUI(Player player, ItemStack stack, int slotIndex) {
        if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.openMenu(new SimpleMenuProvider(
                    (containerId, playerInventory, player1) ->
                        new ContainerLoliPickaxe(containerId, playerInventory, stack, slotIndex),
                    Component.translatable("gui.lolipickaxe.container.title")
            ));
        }
    }

    public static void OpenContainerBlackListGUI(Player player, ItemStack stack, int slotIndex) {
        if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.openMenu(new SimpleMenuProvider(
                    (containerId, playerInventory, player1) ->
                            new ContainerBlackListLoliPickaxe(containerId, playerInventory, stack, slotIndex),
                    Component.translatable("gui.lolipickaxe.container_blacklist.title")
            ));
        }
    }

    public static void OpenSmallContainerGUI(Player player, ItemStack stack, int slotIndex) {
        if (player instanceof ServerPlayer serverPlayer) {
            if (((SmallLoliPickaxe) stack.getItem()).getMaxPage(stack) > 0) {
                serverPlayer.openMenu(new SimpleMenuProvider(
                        (containerId, playerInventory, player1) ->
                                new ContainerLoliPickaxe(containerId, playerInventory, stack, slotIndex, true),
                        Component.translatable("gui.lolipickaxe.small_container.title")
                ));
            }
        }
    }
}
