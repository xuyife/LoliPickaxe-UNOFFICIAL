package net.xuyifei.lolipickaxe.common.menu;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.xuyifei.lolipickaxe.LoliPickaxe;
import net.xuyifei.lolipickaxe.common.gui.ContainerBlackListLoliPickaxe;
import net.xuyifei.lolipickaxe.common.gui.ContainerLoliPickaxe;
import net.xuyifei.lolipickaxe.common.registry.custom.SmallLoliPickaxe;

import java.util.function.Supplier;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, LoliPickaxe.MODID);

    public static final Supplier<MenuType<ContainerLoliPickaxe>> LOLI_PICKAXE_CONTAINER = MENUS.register(
            "loli_pickaxe_container",
            () -> IMenuTypeExtension.create((containerId, playerInventory, buffer) -> {
                        Player player = playerInventory.player;
                        ItemStack pickaxeStack = ItemStack.EMPTY;
                        ItemStack mainHand = player.getMainHandItem();
                        ItemStack offHand = player.getOffhandItem();
                        int slotIndex = -1;

                        if (mainHand.getItem() instanceof net.xuyifei.lolipickaxe.common.registry.custom.LoliPickaxe && !mainHand.isEmpty()) {
                            pickaxeStack = mainHand;
                            slotIndex = playerInventory.player.getInventory().selected;
                        }
                        else if (offHand.getItem() instanceof net.xuyifei.lolipickaxe.common.registry.custom.LoliPickaxe && !offHand.isEmpty()) {
                            pickaxeStack = offHand;
                        }

                        return new ContainerLoliPickaxe(containerId, playerInventory, pickaxeStack, slotIndex);
                    }
            ));

    public static final Supplier<MenuType<ContainerBlackListLoliPickaxe>> LOLI_PICKAXE_BLACK_LIST = MENUS.register(
            "loli_pickaxe_black_list",
            () -> IMenuTypeExtension.create((containerId, playerInventory, buffer) -> {
                        Player player = playerInventory.player;
                        ItemStack pickaxeStack = ItemStack.EMPTY;
                        ItemStack mainHand = player.getMainHandItem();
                        ItemStack offHand = player.getOffhandItem();
                        int slotIndex = -1;

                        if (mainHand.getItem() instanceof net.xuyifei.lolipickaxe.common.registry.custom.LoliPickaxe && !mainHand.isEmpty()) {
                            pickaxeStack = mainHand;
                            slotIndex = playerInventory.player.getInventory().selected;
                        }
                        else if (offHand.getItem() instanceof net.xuyifei.lolipickaxe.common.registry.custom.LoliPickaxe && !offHand.isEmpty()) {
                            pickaxeStack = offHand;
                        }
                        return new ContainerBlackListLoliPickaxe(containerId, playerInventory, pickaxeStack, slotIndex);
                    }
            ));

    public static final Supplier<MenuType<ContainerLoliPickaxe>> SMALL_LOLI_PICKAXE_CONTAINER = MENUS.register(
            "small_loli_pickaxe_container",
            () -> IMenuTypeExtension.create((containerId, playerInventory, buffer) -> {
                        Player player = playerInventory.player;
                        ItemStack pickaxeStack = ItemStack.EMPTY;
                        ItemStack mainHand = player.getMainHandItem();
                        ItemStack offHand = player.getOffhandItem();
                        int slotIndex = -1;

                        if (mainHand.getItem() instanceof SmallLoliPickaxe && !mainHand.isEmpty()) {
                            pickaxeStack = mainHand;
                            slotIndex = playerInventory.player.getInventory().selected;
                        }
                        else if (offHand.getItem() instanceof SmallLoliPickaxe && !offHand.isEmpty()) {
                            pickaxeStack = offHand;
                        }

                        return new ContainerLoliPickaxe(containerId, playerInventory, pickaxeStack, slotIndex, true);
                    }
            ));

    public static void register(IEventBus bus) {
        MENUS.register(bus);
    }
}
