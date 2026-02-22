package net.xuyifei.lolipickaxe.common.util;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ambient.AmbientCreature;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.PlayerEnderChestContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.xuyifei.lolipickaxe.common.config.ConfigLoader;
import net.xuyifei.lolipickaxe.common.entity.EntityLoli;
import net.xuyifei.lolipickaxe.common.event.LoliPickaxeEvents;
import net.xuyifei.lolipickaxe.common.event.LoliTickEvent;
import net.xuyifei.lolipickaxe.common.network.ClientboundLoliDeadPacket;
import net.xuyifei.lolipickaxe.common.registry.ModConfigs;
import net.xuyifei.lolipickaxe.common.registry.ModDamageSources;
import net.xuyifei.lolipickaxe.common.registry.custom.LoliPickaxe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class LoliPickaxeUtil {
    public static void kill(Collection<Entity> entities, LivingEntity source) {
        for (Entity entity : entities) {
            kill(entity, source);
        }
    }

    public static void kill(Entity entity, LivingEntity source) {
        if (entity instanceof EntityLoli) {
            return;
        }
        if (entity instanceof Player) {
            killPlayer((Player) entity, source);
        } else if (entity instanceof LivingEntity) {
            killEntityLiving((LivingEntity) entity, source);
        } else if (ConfigLoader.getBoolean(getLoliPickaxe(source), "loliPickaxeValidToAllEntity")) {
            killEntity(entity);
        }
    }

    public static void killPlayer(Player player, Entity sourceEntity) {
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.getItem() instanceof LoliPickaxe || player.isDeadOrDying() || player instanceof FakePlayer) {
                return;
            }
        }

        if (sourceEntity instanceof Player p) {
            for (int i = 0; i < p.getInventory().getContainerSize(); i++) {
                ItemStack stack = p.getInventory().getItem(i);
                if (stack.getItem() instanceof LoliPickaxe) {
                    if (ConfigLoader.getBoolean(stack, "loliPickaxeClearInventory")) {
                        player.getInventory().clearContent();
                        PlayerEnderChestContainer enderChestContainer = player.getEnderChestInventory();
                        enderChestContainer.removeAllItems();
                    }
                    if (ConfigLoader.getBoolean(stack, "loliPickaxeDropItems")) {
                        player.getInventory().dropAll();
                    }
                    DamageSource ds = ModDamageSources.createLoliDamage(player.level(), p);
                    player.hurt(ds, Float.MAX_VALUE);
                    player.setHealth(0.0F);
                    if (player instanceof ServerPlayer serverPlayer) {
                        serverPlayer.connection.send(new ClientboundLoliDeadPacket(ConfigLoader.getBoolean(stack, "loliPickaxeCompulsoryRemove"), ConfigLoader.getBoolean(stack, "loliPickaxeBlueScreenAttack"), ConfigLoader.getBoolean(stack, "loliPickaxeExitAttack"), ConfigLoader.getBoolean(stack, "loliPickaxeFailRespondAttack")));
                        if (ConfigLoader.getBoolean(stack, "loliPickaxeBeyondRedemption")) {
                            List<String> loliPickaxeBeyondRedemptionPlayerList = ModConfigs.INSTANCE.loliPickaxeBeyondRedemptionPlayerList.get().stream().filter(String.class::isInstance).map(String.class::cast).collect(Collectors.toList());
                            if (!loliPickaxeBeyondRedemptionPlayerList.contains(player.getUUID().toString())) loliPickaxeBeyondRedemptionPlayerList.add(player.getUUID().toString());
                            ModConfigs.INSTANCE.loliPickaxeBeyondRedemptionPlayerList.set(loliPickaxeBeyondRedemptionPlayerList);
                            ModConfigs.INSTANCE.loliPickaxeBeyondRedemptionPlayerList.save();
                        }
                        if (ConfigLoader.getBoolean(stack, "loliPickaxeKickPlayer")) {
                            serverPlayer.connection.disconnect(Component.literal(Objects.requireNonNull(ConfigLoader.getString(stack, "loliPickaxeKickMessage"))));
                        }
                    }
                }
            }
        } else if (sourceEntity instanceof LivingEntity e) {
            DamageSource ds = ModDamageSources.createLoliDamage(player.level(), e);
            player.hurt(ds, Float.MAX_VALUE);
            player.setHealth(0.0F);
        }
    }

    public static void killEntityLiving(LivingEntity entity, LivingEntity source) {
        if (entity instanceof Player player) {
            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                ItemStack stack = player.getInventory().getItem(i);
                if (stack.getItem() instanceof LoliPickaxe) {
                    return;
                }
            }
        }

        if (!(entity.level().isClientSide() || entity.isDeadOrDying() || entity.getHealth() == 0.0F)) {
            entity.hurt(ModDamageSources.createLoliDamage(entity.level(), entity, source), Float.MAX_VALUE);
            entity.setHealth(0.0F);
            Class<? extends LivingEntity> clazz = entity.getClass();
            LoliPickaxeEvents.antiEntity.add(clazz);
            entity.die(ModDamageSources.createLoliDamage(entity.level(), entity, source));
            LoliPickaxeEvents.antiEntity.remove(clazz);
            if (ConfigLoader.getBoolean(getLoliPickaxe(source), "loliPickaxeCompulsoryRemove")) {
                for (Object string : ModConfigs.INSTANCE.loliPickaxeDelayRemoveList.get()) {
                    String[] parts = ((String) string).split(":::");
                    if (parts.length == 2) {
                        String before = parts[0];
                        String after = parts[1];
                        int tick;
                        if (BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString().equals(before)) {
                            tick = Integer.parseInt(after);
                            LoliTickEvent.schedule(entity::discard, tick);
                            return;
                        }
                    }
                }
                entity.discard();
            }
        }
    }

    public static void killEntity(Entity entity) {
        entity.discard();
    }

    public static void killFacing(LivingEntity source) {
        Level world = source.level();
        ItemStack stack = getLoliPickaxe(source);
        List<Entity> entities = new ArrayList<>();

        int range = ConfigLoader.getInt(stack, "loliPickaxeKillFacingRange");
        double slope = ConfigLoader.getDouble(stack, "loliPickaxeKillFacingSlope");
        boolean all = ConfigLoader.getBoolean(stack, "loliPickaxeValidToAllEntity");

        for (int dist = 0; dist <= range; dist += 2) {
            AABB bb = source.getBoundingBox();

            Vec3 lookVec = source.getLookAngle();
            lookVec = lookVec.normalize();

            bb = bb.inflate(slope * dist + 2.0, slope * dist + 0.25, slope * dist + 2.0);
            bb = bb.move(lookVec.x * dist, lookVec.y * dist, lookVec.z * dist);

            List<Entity> list;
            if (all) {
                list = world.getEntitiesOfClass(Entity.class, bb);
            } else {
                list = world.getEntitiesOfClass(LivingEntity.class, bb)
                        .stream()
                        .map(entity -> (Entity) entity)
                        .collect(Collectors.toList());
            }

            list.removeAll(entities);
            list.removeIf(entity -> entity.distanceTo(source) > range);
            entities.addAll(list);
        }

        entities.remove(source);

        if (!ConfigLoader.getBoolean(stack, "loliPickaxeValidToAmityEntity")) {
            entities.removeIf(en ->
                    en instanceof Player ||
                            en instanceof ArmorStand ||
                            en instanceof AmbientCreature ||
                            (en instanceof PathfinderMob && !(en instanceof Enemy))
            );
        }

        LoliPickaxeUtil.kill(entities, source);
    }

    public static ItemStack getLoliPickaxe(LivingEntity entity) {
        if (entity instanceof Player player) {
            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                ItemStack stack = player.getInventory().getItem(i);
                if (stack.getItem() instanceof LoliPickaxe) {
                    return stack;
                }
            }
        }
        return ItemStack.EMPTY;
    }

    @SuppressWarnings("unchecked")
    public static int killRangeEntity(Level level, Player player, int range) {
        ItemStack stack = player.getMainHandItem();
        if (stack.isEmpty() || !(stack.getItem() instanceof LoliPickaxe)) {
            stack = getLoliPickaxe(player);
        }
        List<? extends Entity> list;
        if (ConfigLoader.getBoolean(stack, "loliPickaxeValidToAllEntity")) {
            list = level.getEntitiesOfClass(Entity.class, new AABB(player.getX() - range, player.getY() - range, player.getZ() - range,
                                                                                player.getX() + range, player.getY() + range, player.getZ() + range));
        } else {
            list = level.getEntitiesOfClass(LivingEntity.class, new AABB(player.getX() - range, player.getY() - range, player.getZ() - range,
                                                                                player.getX() + range, player.getY() + range, player.getZ() + range));
        }
        if (!ConfigLoader.getBoolean(stack, "loliPickaxeValidToAmityEntity")) {
            list.removeIf(en -> en instanceof Player || en instanceof ArmorStand || en instanceof AmbientCreature || (en instanceof PathfinderMob && !(en instanceof Enemy)));
        }
        list.remove(player);
        kill((Collection<Entity>) list, player);
        return list.size();
    }
}
