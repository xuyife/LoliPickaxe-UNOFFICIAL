package net.xuyifei.lolipickaxe.common.event;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.xuyifei.lolipickaxe.common.registry.ModConfigs;
import net.xuyifei.lolipickaxe.common.registry.ModItems;

@EventBusSubscriber
public class LoliDropEvent {
    @SubscribeEvent
    public static void onLivingDrop(LivingDropsEvent event) {
        Entity entity = event.getEntity();

        if (event.isRecentlyHit()) {
            if (entity.getRandom().nextDouble() < ModConfigs.INSTANCE.entitySoulDropProbability.get()) {
                event.getDrops().add(new ItemEntity(entity.level(), entity.getX(), entity.getY(), entity.getZ(), new ItemStack(ModItems.LOLI_ENTITY_SOUL_ADDON_0.get())));
            }
            if (entity.getRandom().nextDouble() < ModConfigs.INSTANCE.loliCardDropProbability.get()) {
                event.getDrops().add(new ItemEntity(entity.level(), entity.getX(), entity.getY(), entity.getZ(), new ItemStack(ModItems.LOLI_CARD.get())));
            }
            if (entity.getRandom().nextDouble() < ModConfigs.INSTANCE.loliCardAlbumDropProbability.get()) {
                event.getDrops().add(new ItemEntity(entity.level(), entity.getX(), entity.getY(), entity.getZ(), new ItemStack(ModItems.LOLI_CARD_ALBUM.get())));
            }
        }
    }
}
