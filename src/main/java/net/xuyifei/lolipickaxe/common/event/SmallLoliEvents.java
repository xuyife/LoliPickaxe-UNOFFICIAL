package net.xuyifei.lolipickaxe.common.event;

import com.google.common.collect.Maps;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.xuyifei.lolipickaxe.LoliPickaxe;
import net.xuyifei.lolipickaxe.common.registry.ModDataComponents;
import net.xuyifei.lolipickaxe.common.registry.custom.SmallLoliPickaxe;

import java.util.Map;

@EventBusSubscriber(modid = LoliPickaxe.MODID)
public class SmallLoliEvents {
    private static Map<String, Double> dodgeMap = Maps.newHashMap();
    private static Map<String, Double> antiInjury = Maps.newHashMap();

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.getItem() instanceof SmallLoliPickaxe smallLoliPickaxe) {
                if (!player.level().isClientSide()) {
                    switch ((int) smallLoliPickaxe.buffLevel(stack)) {
                        case 3:
                            player.addEffect(new MobEffectInstance(MobEffects.SATURATION, 410, 0, false, false));
                        case 2:
                            player.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 410, 0, false, false));
                        case 1:
                            player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 410, 0, false, false));
                    }
                }
                dodgeMap.put(player.getGameProfile().getName(), smallLoliPickaxe.getDodge(stack));
                antiInjury.put(player.getGameProfile().getName(), smallLoliPickaxe.getAntiInjury(stack));
                return;
            }
        }
        if (dodgeMap.containsKey(player.getGameProfile().getName())) {
            dodgeMap.remove(player.getGameProfile().getName());
        }
        if (antiInjury.containsKey(player.getGameProfile().getName())) {
            antiInjury.remove(player.getGameProfile().getName());
        }
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingIncomingDamageEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player player) {
            if (dodgeMap.containsKey(player.getGameProfile().getName()) && player.getRandom().nextDouble() < dodgeMap.get(player.getGameProfile().getName())) {
                event.setCanceled(true);
            }
            if (antiInjury.containsKey(player.getGameProfile().getName()) && player.getRandom().nextDouble() < antiInjury.get(player.getGameProfile().getName())) {
                float damage = (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE) * 0.5f;
                if (event.getSource().getEntity() != null) {
                    player.attack(event.getSource().getEntity());
                }
                player.setHealth(Math.min(player.getHealth() + damage, player.getMaxHealth()));
            }
        }
    }

    @SubscribeEvent
    public static void onFly(PlayerTickEvent.Post event) {
        if (event.getEntity().level().isClientSide()) return;
        Player player = event.getEntity();
        boolean hasPickaxe = false;
        boolean hasLoliPickaxe = false;

        boolean currentlyCanFly = player.mayFly();
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (BuiltInRegistries.ITEM.getKey(stack.getItem()).toString().equals("lolipickaxe:small_loli_pickaxe")) {
                hasPickaxe = true;
                if (stack.getOrDefault(ModDataComponents.SMALL_LOLI_ATTRIBUTE.get(), CustomData.EMPTY).contains("LoliFly")) {
                    if (!currentlyCanFly) {
                        player.getAbilities().mayfly = true;
                        player.onUpdateAbilities();
                    }
                }
            } else if (BuiltInRegistries.ITEM.getKey(stack.getItem()).toString().equals("lolipickaxe:loli_pickaxe")) {
                hasLoliPickaxe = true;
            }
        }

        if (!hasPickaxe) {
            if (currentlyCanFly && !hasLoliPickaxe) {
                if (!player.isCreative() && !player.isSpectator()) {
                    player.getAbilities().mayfly = false;
                    player.getAbilities().flying = false;
                    player.onUpdateAbilities();
                }
            }
        }
    }
}
