package net.xuyifei.lolipickaxe.client.event;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.xuyifei.lolipickaxe.LoliPickaxe;
import net.xuyifei.lolipickaxe.client.key.KeyBinding;
import net.xuyifei.lolipickaxe.common.gui.GUIOpenWrapper;
import net.xuyifei.lolipickaxe.common.network.ServerboundLoliPickaxeDropAllPacket;
import net.xuyifei.lolipickaxe.common.network.ServerboundOpenLoliPickaxeContainerGuiPacket;
import net.xuyifei.lolipickaxe.common.network.ServerboundOpenLoliPickaxeContainerBlackListGuiPacket;
import net.xuyifei.lolipickaxe.common.network.ServerboundOpenSmallLoliPickaxeContainerGuiPacket;
import net.xuyifei.lolipickaxe.common.registry.ModItems;
import net.xuyifei.lolipickaxe.common.registry.custom.SmallLoliPickaxe;

@EventBusSubscriber(modid = LoliPickaxe.MODID, value = Dist.CLIENT)
public class LoliKeyEvents {
    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.player == null || minecraft.screen != null) {
            return;
        }
        
        if (KeyBinding.LOLI_CONFIG.consumeClick()) {
            ItemStack stack = minecraft.player.getInventory().getItem(minecraft.player.getInventory().selected);
            if (!stack.isEmpty() && stack.getItem() instanceof net.xuyifei.lolipickaxe.common.registry.custom.LoliPickaxe) {
                GUIOpenWrapper.OpenLoliConfigGui(Component.translatable("gui.lolipickaxe.loli_config.title"), stack);
            }
        } else if (KeyBinding.LOLI_POTION.consumeClick()) {
            ItemStack stack = minecraft.player.getInventory().getItem(minecraft.player.getInventory().selected);
            if (!stack.isEmpty() && stack.is(ModItems.LOLI_PICKAXE.get())) {
                GUIOpenWrapper.OpenPotionEffectGui(Component.translatable("gui.lolipickaxe.potion_effect.title"), stack);
            }
        } else if (KeyBinding.LOLI_ENCHANTMENT.consumeClick()) {
            ItemStack stack = minecraft.player.getInventory().getItem(minecraft.player.getInventory().selected);
            if (!stack.isEmpty() && stack.is(ModItems.LOLI_PICKAXE.get())) {
                GUIOpenWrapper.OpenEnchantmentGui(Component.translatable("gui.lolipickaxe.enchantment.title"), stack);
            }
        } else if (KeyBinding.LOLI_PICKAXE_CONTAINER.consumeClick()) {
            ItemStack stack = minecraft.player.getInventory().getItem(minecraft.player.getInventory().selected);
            if (!stack.isEmpty()) {
                if (stack.getItem() instanceof net.xuyifei.lolipickaxe.common.registry.custom.LoliPickaxe) {
                    if (!minecraft.player.isShiftKeyDown()) {
                        minecraft.player.connection.send(new ServerboundOpenLoliPickaxeContainerGuiPacket());
                    } else {
                        minecraft.player.connection.send(new ServerboundLoliPickaxeDropAllPacket());
                    }
                } else if (stack.getItem() instanceof SmallLoliPickaxe) {
                    if (!minecraft.player.isShiftKeyDown()) {
                        minecraft.player.connection.send(new ServerboundOpenSmallLoliPickaxeContainerGuiPacket());
                    } else {
                        minecraft.player.connection.send(new ServerboundLoliPickaxeDropAllPacket());
                    }
                }
            }
        } else if (KeyBinding.LOLI_PICKAXE_CONTAINER_BLACKLIST.consumeClick()) {
            ItemStack stack = minecraft.player.getInventory().getItem(minecraft.player.getInventory().selected);
            if (!stack.isEmpty() && stack.is(ModItems.LOLI_PICKAXE.get())) {
                minecraft.player.connection.send(new ServerboundOpenLoliPickaxeContainerBlackListGuiPacket());
            }
        }
    }
}
