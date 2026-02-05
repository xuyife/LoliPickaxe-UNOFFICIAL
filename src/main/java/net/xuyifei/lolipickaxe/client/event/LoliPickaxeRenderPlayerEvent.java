package net.xuyifei.lolipickaxe.client.event;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderPlayerEvent;
import net.xuyifei.lolipickaxe.common.config.ConfigLoader;
import net.xuyifei.lolipickaxe.common.util.LoliPickaxeUtil;

@EventBusSubscriber
public class LoliPickaxeRenderPlayerEvent {
    @SubscribeEvent
    public static void onPlayerRender(RenderPlayerEvent.Pre event) {
        Player player = event.getEntity();
        ItemStack stack = LoliPickaxeUtil.getLoliPickaxe(player);
        if (!stack.isEmpty() && ConfigLoader.getBoolean(stack, "loliPickaxeInvisible")) {
            event.setCanceled(true);
        }
    }
}
