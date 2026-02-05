package net.xuyifei.lolipickaxe.common.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.xuyifei.lolipickaxe.common.entity.ModEntities;

@EventBusSubscriber
public class LoliEvent {
    @SubscribeEvent
    public static void onLoliHurt(LivingIncomingDamageEvent event) {
        if (event.getEntity().getType() == ModEntities.LOLI_ENTITY.get()) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onLoliDead(LivingDeathEvent event) {
        if (event.getEntity().getType() == ModEntities.LOLI_ENTITY.get()) {
            event.setCanceled(true);
        }
    }
}
