package net.xuyifei.lolipickaxe.common.registry;

import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.xuyifei.lolipickaxe.LoliPickaxe;
import net.xuyifei.lolipickaxe.common.registry.custom.SmallLoliPickaxe;

import java.util.function.Supplier;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, LoliPickaxe.MODID);

    public static final Supplier<CreativeModeTab> LOLI_TAB =
            CREATIVE_MODE_TABS.register("loli", () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModItems.LOLI_PICKAXE.get()))
                    .title(Component.translatable("itemGroup.loli_tab"))
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.LOLI_PICKAXE.get());
                        output.accept(ModItems.SMALL_LOLI_PICKAXE.get());
                        output.accept(SmallLoliPickaxe.getFull(Minecraft.getInstance().level.registryAccess()));
                        output.accept(ModItems.LOLI_DISPERSAL.get());
                        output.accept(ModItems.LOLI_CARD.get());
                        output.accept(ModItems.LOLI_CARD_ALBUM.get());
                        output.accept(ModItems.LOLI_CARD_ONLINE.get());
                        output.accept(ModItems.LOLI_RECORD.get());
                        output.accept(ModItems.LOLI_BLUE_SCREEN_TNT.get());
                        output.accept(ModItems.LOLI_EXIT_TNT.get());
                        output.accept(ModItems.LOLI_FAIL_RESPOND_TNT.get());
                        output.accept(ModItems.LOLI_ALTAR.get());
                        output.accept(ModItems.LOLI_SPAWN_EGG.get());
                    })
                    .build());

    public static final Supplier<CreativeModeTab> LOLI_RECIPE_TAB =
            CREATIVE_MODE_TABS.register("loli_recipe", () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModItems.LOLI_ENTITY_SOUL_ADDON_MAX.get()))
                    .title(Component.translatable("itemGroup.loli_recipe_tab"))
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.LOLI_COAL_ADDON_0.get());
                        output.accept(ModItems.LOLI_COAL_ADDON_1.get());
                        output.accept(ModItems.LOLI_COAL_ADDON_2.get());
                        output.accept(ModItems.LOLI_COAL_ADDON_3.get());
                        output.accept(ModItems.LOLI_COAL_ADDON_4.get());
                        output.accept(ModItems.LOLI_COAL_ADDON_5.get());
                        output.accept(ModItems.LOLI_COAL_ADDON_6.get());
                        output.accept(ModItems.LOLI_COAL_ADDON_7.get());
                        output.accept(ModItems.LOLI_COAL_ADDON_8.get());
                        output.accept(ModItems.LOLI_COAL_ADDON_MAX.get());

                        output.accept(ModItems.LOLI_IRON_ADDON_0.get());
                        output.accept(ModItems.LOLI_IRON_ADDON_1.get());
                        output.accept(ModItems.LOLI_IRON_ADDON_2.get());
                        output.accept(ModItems.LOLI_IRON_ADDON_3.get());
                        output.accept(ModItems.LOLI_IRON_ADDON_4.get());
                        output.accept(ModItems.LOLI_IRON_ADDON_5.get());
                        output.accept(ModItems.LOLI_IRON_ADDON_6.get());
                        output.accept(ModItems.LOLI_IRON_ADDON_7.get());
                        output.accept(ModItems.LOLI_IRON_ADDON_8.get());
                        output.accept(ModItems.LOLI_IRON_ADDON_MAX.get());

                        output.accept(ModItems.LOLI_GOLD_ADDON_0.get());
                        output.accept(ModItems.LOLI_GOLD_ADDON_1.get());
                        output.accept(ModItems.LOLI_GOLD_ADDON_2.get());
                        output.accept(ModItems.LOLI_GOLD_ADDON_3.get());
                        output.accept(ModItems.LOLI_GOLD_ADDON_4.get());
                        output.accept(ModItems.LOLI_GOLD_ADDON_5.get());
                        output.accept(ModItems.LOLI_GOLD_ADDON_MAX.get());

                        output.accept(ModItems.LOLI_REDSTONE_ADDON_0.get());
                        output.accept(ModItems.LOLI_REDSTONE_ADDON_1.get());
                        output.accept(ModItems.LOLI_REDSTONE_ADDON_2.get());
                        output.accept(ModItems.LOLI_REDSTONE_ADDON_MAX.get());

                        output.accept(ModItems.LOLI_LAPIS_ADDON_0.get());
                        output.accept(ModItems.LOLI_LAPIS_ADDON_1.get());
                        output.accept(ModItems.LOLI_LAPIS_ADDON_2.get());
                        output.accept(ModItems.LOLI_LAPIS_ADDON_3.get());
                        output.accept(ModItems.LOLI_LAPIS_ADDON_4.get());
                        output.accept(ModItems.LOLI_LAPIS_ADDON_MAX.get());

                        output.accept(ModItems.LOLI_DIAMOND_ADDON_0.get());
                        output.accept(ModItems.LOLI_DIAMOND_ADDON_1.get());
                        output.accept(ModItems.LOLI_DIAMOND_ADDON_2.get());
                        output.accept(ModItems.LOLI_DIAMOND_ADDON_3.get());
                        output.accept(ModItems.LOLI_DIAMOND_ADDON_4.get());
                        output.accept(ModItems.LOLI_DIAMOND_ADDON_MAX.get());

                        output.accept(ModItems.LOLI_EMERALD_ADDON_0.get());
                        output.accept(ModItems.LOLI_EMERALD_ADDON_1.get());
                        output.accept(ModItems.LOLI_EMERALD_ADDON_2.get());
                        output.accept(ModItems.LOLI_EMERALD_ADDON_3.get());
                        output.accept(ModItems.LOLI_EMERALD_ADDON_MAX.get());

                        output.accept(ModItems.LOLI_OBSIDIAN_ADDON_0.get());
                        output.accept(ModItems.LOLI_OBSIDIAN_ADDON_1.get());
                        output.accept(ModItems.LOLI_OBSIDIAN_ADDON_2.get());
                        output.accept(ModItems.LOLI_OBSIDIAN_ADDON_3.get());
                        output.accept(ModItems.LOLI_OBSIDIAN_ADDON_4.get());
                        output.accept(ModItems.LOLI_OBSIDIAN_ADDON_5.get());
                        output.accept(ModItems.LOLI_OBSIDIAN_ADDON_6.get());
                        output.accept(ModItems.LOLI_OBSIDIAN_ADDON_7.get());
                        output.accept(ModItems.LOLI_OBSIDIAN_ADDON_8.get());
                        output.accept(ModItems.LOLI_OBSIDIAN_ADDON_MAX.get());

                        output.accept(ModItems.LOLI_GLOW_ADDON_0.get());
                        output.accept(ModItems.LOLI_GLOW_ADDON_1.get());
                        output.accept(ModItems.LOLI_GLOW_ADDON_MAX.get());

                        output.accept(ModItems.LOLI_QUARTZ_ADDON_0.get());
                        output.accept(ModItems.LOLI_QUARTZ_ADDON_1.get());
                        output.accept(ModItems.LOLI_QUARTZ_ADDON_MAX.get());

                        output.accept(ModItems.LOLI_NETHER_STAR_ADDON_0.get());
                        output.accept(ModItems.LOLI_NETHER_STAR_ADDON_1.get());
                        output.accept(ModItems.LOLI_NETHER_STAR_ADDON_2.get());
                        output.accept(ModItems.LOLI_NETHER_STAR_ADDON_3.get());
                        output.accept(ModItems.LOLI_NETHER_STAR_ADDON_MAX.get());

                        output.accept(ModItems.LOLI_AUTO_FURNACE_ADDON.get());

                        output.accept(ModItems.LOLI_FLY_ADDON.get());

                        output.accept(ModItems.LOLI_GLOW_ADDON_MAX.get());

                        output.accept(ModItems.LOLI_ENTITY_SOUL_ADDON_0.get());
                        output.accept(ModItems.LOLI_ENTITY_SOUL_ADDON_1.get());
                        output.accept(ModItems.LOLI_ENTITY_SOUL_ADDON_2.get());
                        output.accept(ModItems.LOLI_ENTITY_SOUL_ADDON_3.get());
                        output.accept(ModItems.LOLI_ENTITY_SOUL_ADDON_4.get());
                        output.accept(ModItems.LOLI_ENTITY_SOUL_ADDON_5.get());
                        output.accept(ModItems.LOLI_ENTITY_SOUL_ADDON_MAX.get());
                    })
                    .build());

    public static void register(IEventBus bus) {
        CREATIVE_MODE_TABS.register(bus);
    }
}
