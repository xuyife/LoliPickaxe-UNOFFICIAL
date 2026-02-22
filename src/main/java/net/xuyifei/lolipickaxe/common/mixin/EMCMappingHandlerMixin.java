package net.xuyifei.lolipickaxe.common.mixin;

import it.unimi.dsi.fastutil.objects.Object2LongMap;
import moze_intel.projecte.api.ItemInfo;
import moze_intel.projecte.emc.EMCMappingHandler;
import net.minecraft.core.Holder;
import net.xuyifei.lolipickaxe.common.registry.ModItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = EMCMappingHandler.class, remap = false)
public class EMCMappingHandlerMixin {
    @Inject(method = "updateEmcValues", at = @At("HEAD"))
    private static void onUpdateEmcValues(Object2LongMap<ItemInfo> data, CallbackInfoReturnable<ItemInfo> cir) {
        data.removeLong(ItemInfo.fromItem(Holder.direct(ModItems.LOLI_PICKAXE.get())));
        data.removeLong(ItemInfo.fromItem(Holder.direct(ModItems.SMALL_LOLI_PICKAXE.get())));

        data.removeLong(ItemInfo.fromItem(Holder.direct(ModItems.LOLI_COAL_ADDON_1.get())));
        data.removeLong(ItemInfo.fromItem(Holder.direct(ModItems.LOLI_COAL_ADDON_2.get())));
        data.removeLong(ItemInfo.fromItem(Holder.direct(ModItems.LOLI_COAL_ADDON_3.get())));
        data.removeLong(ItemInfo.fromItem(Holder.direct(ModItems.LOLI_COAL_ADDON_4.get())));
        data.removeLong(ItemInfo.fromItem(Holder.direct(ModItems.LOLI_COAL_ADDON_5.get())));
        data.removeLong(ItemInfo.fromItem(Holder.direct(ModItems.LOLI_COAL_ADDON_6.get())));
        data.removeLong(ItemInfo.fromItem(Holder.direct(ModItems.LOLI_COAL_ADDON_7.get())));
        data.removeLong(ItemInfo.fromItem(Holder.direct(ModItems.LOLI_COAL_ADDON_8.get())));
        data.removeLong(ItemInfo.fromItem(Holder.direct(ModItems.LOLI_COAL_ADDON_MAX.get())));

        data.removeLong(ItemInfo.fromItem(Holder.direct(ModItems.LOLI_IRON_ADDON_1.get())));
        data.removeLong(ItemInfo.fromItem(Holder.direct(ModItems.LOLI_IRON_ADDON_2.get())));
        data.removeLong(ItemInfo.fromItem(Holder.direct(ModItems.LOLI_IRON_ADDON_3.get())));
        data.removeLong(ItemInfo.fromItem(Holder.direct(ModItems.LOLI_IRON_ADDON_4.get())));
        data.removeLong(ItemInfo.fromItem(Holder.direct(ModItems.LOLI_IRON_ADDON_5.get())));
        data.removeLong(ItemInfo.fromItem(Holder.direct(ModItems.LOLI_IRON_ADDON_6.get())));
        data.removeLong(ItemInfo.fromItem(Holder.direct(ModItems.LOLI_IRON_ADDON_7.get())));
        data.removeLong(ItemInfo.fromItem(Holder.direct(ModItems.LOLI_IRON_ADDON_8.get())));
        data.removeLong(ItemInfo.fromItem(Holder.direct(ModItems.LOLI_IRON_ADDON_MAX.get())));

        data.removeLong(ItemInfo.fromItem(Holder.direct(ModItems.LOLI_GOLD_ADDON_1.get())));
        data.removeLong(ItemInfo.fromItem(Holder.direct(ModItems.LOLI_GOLD_ADDON_2.get())));
        data.removeLong(ItemInfo.fromItem(Holder.direct(ModItems.LOLI_GOLD_ADDON_3.get())));
        data.removeLong(ItemInfo.fromItem(Holder.direct(ModItems.LOLI_GOLD_ADDON_4.get())));
        data.removeLong(ItemInfo.fromItem(Holder.direct(ModItems.LOLI_GOLD_ADDON_5.get())));
        data.removeLong(ItemInfo.fromItem(Holder.direct(ModItems.LOLI_GOLD_ADDON_MAX.get())));

        data.removeLong(ItemInfo.fromItem(Holder.direct(ModItems.LOLI_REDSTONE_ADDON_1.get())));
        data.removeLong(ItemInfo.fromItem(Holder.direct(ModItems.LOLI_REDSTONE_ADDON_2.get())));
        data.removeLong(ItemInfo.fromItem(Holder.direct(ModItems.LOLI_REDSTONE_ADDON_MAX.get())));

        data.removeLong(ItemInfo.fromItem(Holder.direct(ModItems.LOLI_LAPIS_ADDON_1.get())));
        data.removeLong(ItemInfo.fromItem(Holder.direct(ModItems.LOLI_LAPIS_ADDON_2.get())));
        data.removeLong(ItemInfo.fromItem(Holder.direct(ModItems.LOLI_LAPIS_ADDON_3.get())));
        data.removeLong(ItemInfo.fromItem(Holder.direct(ModItems.LOLI_LAPIS_ADDON_4.get())));
        data.removeLong(ItemInfo.fromItem(Holder.direct(ModItems.LOLI_LAPIS_ADDON_MAX.get())));

        data.removeLong(ItemInfo.fromItem(Holder.direct(ModItems.LOLI_DIAMOND_ADDON_1.get())));
        data.removeLong(ItemInfo.fromItem(Holder.direct(ModItems.LOLI_DIAMOND_ADDON_2.get())));
        data.removeLong(ItemInfo.fromItem(Holder.direct(ModItems.LOLI_DIAMOND_ADDON_3.get())));
        data.removeLong(ItemInfo.fromItem(Holder.direct(ModItems.LOLI_DIAMOND_ADDON_4.get())));
        data.removeLong(ItemInfo.fromItem(Holder.direct(ModItems.LOLI_DIAMOND_ADDON_MAX.get())));

        data.removeLong(ItemInfo.fromItem(Holder.direct(ModItems.LOLI_EMERALD_ADDON_1.get())));
        data.removeLong(ItemInfo.fromItem(Holder.direct(ModItems.LOLI_EMERALD_ADDON_2.get())));
        data.removeLong(ItemInfo.fromItem(Holder.direct(ModItems.LOLI_EMERALD_ADDON_3.get())));
        data.removeLong(ItemInfo.fromItem(Holder.direct(ModItems.LOLI_EMERALD_ADDON_MAX.get())));

        data.removeLong(ItemInfo.fromItem(Holder.direct(ModItems.LOLI_OBSIDIAN_ADDON_1.get())));
        data.removeLong(ItemInfo.fromItem(Holder.direct(ModItems.LOLI_OBSIDIAN_ADDON_2.get())));
        data.removeLong(ItemInfo.fromItem(Holder.direct(ModItems.LOLI_OBSIDIAN_ADDON_3.get())));
        data.removeLong(ItemInfo.fromItem(Holder.direct(ModItems.LOLI_OBSIDIAN_ADDON_4.get())));
        data.removeLong(ItemInfo.fromItem(Holder.direct(ModItems.LOLI_OBSIDIAN_ADDON_5.get())));
        data.removeLong(ItemInfo.fromItem(Holder.direct(ModItems.LOLI_OBSIDIAN_ADDON_6.get())));
        data.removeLong(ItemInfo.fromItem(Holder.direct(ModItems.LOLI_OBSIDIAN_ADDON_7.get())));
        data.removeLong(ItemInfo.fromItem(Holder.direct(ModItems.LOLI_OBSIDIAN_ADDON_8.get())));
        data.removeLong(ItemInfo.fromItem(Holder.direct(ModItems.LOLI_OBSIDIAN_ADDON_MAX.get())));

        data.removeLong(ItemInfo.fromItem(Holder.direct(ModItems.LOLI_GLOW_ADDON_1.get())));
        data.removeLong(ItemInfo.fromItem(Holder.direct(ModItems.LOLI_GLOW_ADDON_MAX.get())));

        data.removeLong(ItemInfo.fromItem(Holder.direct(ModItems.LOLI_QUARTZ_ADDON_1.get())));
        data.removeLong(ItemInfo.fromItem(Holder.direct(ModItems.LOLI_QUARTZ_ADDON_MAX.get())));

        data.removeLong(ItemInfo.fromItem(Holder.direct(ModItems.LOLI_NETHER_STAR_ADDON_1.get())));
        data.removeLong(ItemInfo.fromItem(Holder.direct(ModItems.LOLI_NETHER_STAR_ADDON_2.get())));
        data.removeLong(ItemInfo.fromItem(Holder.direct(ModItems.LOLI_NETHER_STAR_ADDON_3.get())));
        data.removeLong(ItemInfo.fromItem(Holder.direct(ModItems.LOLI_NETHER_STAR_ADDON_MAX.get())));

        data.removeLong(ItemInfo.fromItem(Holder.direct(ModItems.LOLI_FLY_ADDON.get())));


        data.put(ItemInfo.fromItem(Holder.direct(ModItems.LOLI_ENTITY_SOUL_ADDON_0.get())), (long) Math.pow(2, 20));
    }
}
