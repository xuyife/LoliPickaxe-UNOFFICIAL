package net.xuyifei.lolipickaxe.client.key;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.xuyifei.lolipickaxe.LoliPickaxe;

@EventBusSubscriber(modid = LoliPickaxe.MODID)
public class LoliKeyLoader {
    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event) {
        KeyBinding.register(event);
    }
}
