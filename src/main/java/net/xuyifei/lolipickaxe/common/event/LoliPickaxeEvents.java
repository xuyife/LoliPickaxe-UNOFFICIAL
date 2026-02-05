package net.xuyifei.lolipickaxe.common.event;

import com.google.common.collect.Sets;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderLivingEvent;
import net.neoforged.neoforge.common.util.TriState;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.item.ItemTossEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingKnockBackEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.xuyifei.lolipickaxe.common.config.ConfigLoader;
import net.xuyifei.lolipickaxe.common.network.ServerboundLoliKillFacingPacket;
import net.xuyifei.lolipickaxe.common.network.ServerboundLoliLeftClickPacket;
import net.xuyifei.lolipickaxe.common.registry.ModConfigs;
import net.xuyifei.lolipickaxe.common.registry.ModDamageSources;
import net.xuyifei.lolipickaxe.common.registry.ModItems;
import net.xuyifei.lolipickaxe.common.registry.custom.LoliPickaxe;
import net.xuyifei.lolipickaxe.common.registry.custom.SmallLoliPickaxe;
import net.xuyifei.lolipickaxe.common.util.EventUtil;
import net.xuyifei.lolipickaxe.common.util.LoliPickaxeUtil;
import net.xuyifei.lolipickaxe.common.util.RomanNumberUtil;

import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@EventBusSubscriber(modid = net.xuyifei.lolipickaxe.LoliPickaxe.MODID)
public class LoliPickaxeEvents {

    private static final Set<Holder<MobEffect>> NEGATIVE_EFFECTS = Set.of(
            MobEffects.POISON,
            MobEffects.WITHER,
            MobEffects.BLINDNESS,
            MobEffects.CONFUSION,
            MobEffects.HUNGER,
            MobEffects.WEAKNESS,
            MobEffects.MOVEMENT_SLOWDOWN,
            MobEffects.DIG_SLOWDOWN,
            MobEffects.UNLUCK,
            MobEffects.BAD_OMEN,
            MobEffects.LEVITATION,
            MobEffects.DARKNESS
    );
    public static Set<Class<? extends Entity>> antiEntity = Sets.newHashSet();
    private static final Pattern STRING_ENCHANTMENT_PATTERN = Pattern.compile("enchantment\\.level\\.(\\d+)$");
    private static int tick = 0;
    private static int curColor = 0;
    private static final ChatFormatting[] colors = { ChatFormatting.GOLD, ChatFormatting.BLUE, ChatFormatting.GREEN, ChatFormatting.AQUA, ChatFormatting.RED, ChatFormatting.LIGHT_PURPLE, ChatFormatting.YELLOW };

    @SubscribeEvent
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        Player player = event.getEntity();
        ItemStack heldItem = player.getMainHandItem();

        if (heldItem.getItem() instanceof LoliPickaxe loliItem) {
            event.setCanceled(true);
            event.setUseItem(TriState.FALSE);

            if (ConfigLoader.getBoolean(heldItem, "loliPickaxeStopOnLiquid")) {
                HitResult hitResult = EventUtil.rayTrace(player, 32.0F, 1.0F);
                if (hitResult.getType() == HitResult.Type.BLOCK) {
                    BlockHitResult blockHitResult = (BlockHitResult) hitResult;
                    BlockPos blockPos = blockHitResult.getBlockPos();
                    FluidState fluidState = player.level().getFluidState(blockPos);

                    if (!fluidState.isEmpty()) {
                        loliItem.onLeftClick(player.level(), player, blockPos);
                        return;
                    }
                }
            }
            loliItem.onLeftClick(player.level(), player, event.getPos());
        }
    }

    @SubscribeEvent
    public static void onLeftClickEmpty(PlayerInteractEvent.LeftClickEmpty event) {
        Player player = event.getEntity();
        Level level = event.getLevel();
        if (ConfigLoader.getBoolean(player.getMainHandItem(), "loliPickaxeKillFacing") && !player.isSpectator() && !player.getMainHandItem().isEmpty() && player.getMainHandItem().getItem() instanceof LoliPickaxe) {
            ((LocalPlayer) player).connection.send(new ServerboundLoliKillFacingPacket());
        }

        HitResult hitResult = EventUtil.rayTrace(player, 32.0, 1.0F);

        if (hitResult.getType() == HitResult.Type.BLOCK) {
            BlockHitResult blockHitResult = (BlockHitResult) hitResult;
            BlockPos blockPos = blockHitResult.getBlockPos();
            FluidState fluidState = level.getFluidState(blockPos);

            if (!fluidState.isEmpty() && ConfigLoader.getBoolean(player.getMainHandItem(), "loliPickaxeStopOnLiquid")) {
                ((LocalPlayer) player).connection.send(new ServerboundLoliLeftClickPacket(blockPos, player.getMainHandItem()));
            }
        }

    }

    @SubscribeEvent
    public static void onLivingKnockBack(LivingKnockBackEvent event) {
        if (event.getEntity() instanceof Player player) {
            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                ItemStack stack = player.getInventory().getItem(i);
                if (stack.getItem() instanceof LoliPickaxe) {
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerHurt(LivingIncomingDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                ItemStack stack = player.getInventory().getItem(i);
                if (stack.getItem() instanceof LoliPickaxe) {
                    event.setCanceled(true);
                    if (ConfigLoader.getBoolean(player.getMainHandItem(), "loliPickaxeThorns")) {
                        if (event.getSource().getEntity() != null) {
                            LoliPickaxeUtil.kill(event.getSource().getEntity(), player);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if(event.getEntity().level().isClientSide()) return;
        Player player = event.getEntity();
        boolean hasPickaxe = false;
        boolean hasSmall = false;

        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.getItem() instanceof LoliPickaxe) {
                hasPickaxe = true;
                break;
            } else if (stack.getItem() instanceof SmallLoliPickaxe) {
                hasSmall = true;
                break;
            }
        }

        boolean currentlyCanFly = player.mayFly();

        if (hasPickaxe) {
            if (!currentlyCanFly) {
                player.getAbilities().mayfly = true;
                player.onUpdateAbilities();
            }
            for (Holder<MobEffect> effectHolder : NEGATIVE_EFFECTS) {
                if (player.hasEffect(effectHolder)) {
                    player.removeEffect(effectHolder);
                }
            }
        } else {
            if (currentlyCanFly && !hasSmall) {
                if (!player.isCreative() && !player.isSpectator()) {
                    player.getAbilities().mayfly = false;
                    player.getAbilities().flying = false;
                    player.onUpdateAbilities();
                }
            }
        }

        if (ModConfigs.getList("loliPickaxeBeyondRedemptionPlayerList").contains(player.getUUID().toString())) {
            if (!hasPickaxe) player.kill(); player.setHealth(0.0F);
        }

        if (ConfigLoader.getBoolean(LoliPickaxeUtil.getLoliPickaxe(player), "loliPickaxeAutoKillRangeEntity")) {
            int range = ConfigLoader.getInt(LoliPickaxeUtil.getLoliPickaxe(player), "loliPickaxeAutoKillRange");
            LoliPickaxeUtil.killRangeEntity(player.level(), player, range);
        }
    }

    @SubscribeEvent
    public static void onPlayerDead(LivingDeathEvent event) {
        if (event.getEntity().level().isClientSide()) return;

        if (event.getEntity() instanceof Player player) {
            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                ItemStack stack = player.getInventory().getItem(i);
                if (stack.getItem() instanceof LoliPickaxe) {
                    event.setCanceled(true);
                    break;
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLoliTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if (!stack.isEmpty() && stack.getItem() instanceof LoliPickaxe) {
            List<Component> tooltip = event.getToolTip();

            for (int i = 0; i < tooltip.size(); i++) {
                Component tip =  tooltip.get(i);
                if (tip.getString().endsWith(Component.translatable("attribute.name.generic.attack_damage").getString())) {
                    Component component = Component.translatable("loliPickaxe.damage");
                    StringBuilder sb = new StringBuilder();
                    for (int j = 0; j < component.getString().length(); j++) {
                        sb.append(colors[(curColor + j) % colors.length]);
                        sb.append(component.getString().charAt(j));
                    }
                    tooltip.set(i, Component.literal(" ").append(Component.translatable("attribute.modifier.equals.0", sb.toString() + ChatFormatting.GRAY, Component.translatable("attribute.name.generic.attack_damage").withStyle(ChatFormatting.DARK_GREEN))));
                } else if (tip.getString().endsWith(Component.translatable("attribute.name.generic.attack_speed").getString())) {
                    Component component = Component.translatable("loliPickaxe.speed");
                    StringBuilder sb = new StringBuilder();
                    for (int j = 0; j < component.getString().length(); j++) {
                        sb.append(colors[(curColor + j) % colors.length]);
                        sb.append(component.getString().charAt(j));
                    }
                    tooltip.set(i, Component.literal(" ").append(Component.translatable("attribute.modifier.equals.0", sb.toString() + ChatFormatting.GRAY, Component.translatable("attribute.name.generic.attack_speed").withStyle(ChatFormatting.DARK_GREEN))));
                } else {
                    Matcher matcher = STRING_ENCHANTMENT_PATTERN.matcher(tip.getString());
                    if (matcher.find()) {
                        tooltip.set(i, Component.literal(tip.getString().substring(0, matcher.start()) + RomanNumberUtil.toRoman(Integer.parseInt(matcher.group(1)))));
                    }
                }
            }
        }
        if (!stack.isEmpty() && BuiltInRegistries.ITEM.getKey(stack.getItem()).getNamespace().equals(net.xuyifei.lolipickaxe.LoliPickaxe.MODID)) {
            event.getToolTip().add(1, Component.literal("注：本mod为非官方重置版本，非原作者作品！").withStyle(ChatFormatting.DARK_RED).withStyle(ChatFormatting.BOLD));
            event.getToolTip().add(2, Component.literal("官方仓库：https://github.com/IslenautsGK/LoliPickaxe").withStyle(ChatFormatting.DARK_RED).withStyle(ChatFormatting.BOLD));
        }
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Pre event) {
        if (++tick >= 3) {
            tick = 0;
            if(--curColor < 0) {
                curColor = colors.length - 1;
            }
        }
    }

    @SubscribeEvent
    public static void onItemToss(ItemTossEvent event) {
        Player player = event.getPlayer();
        if (event.getEntity().getItem().getItem() instanceof LoliPickaxe) {
            if (ConfigLoader.getBoolean(event.getEntity().getItem(), "loliPickaxeFindOwner")) {
                event.getEntity().setPickUpDelay(0);
                event.getEntity().playerTouch(player);
            }
        }
    }

    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();
        for (Class<? extends Entity> clazz : antiEntity) {
            if (clazz.isInstance(entity)) {
                event.setCanceled(true);
                return;
            }
        }
    }

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.registerItem(
                Capabilities.EnergyStorage.ITEM,
                (stack, unused) -> new LoliPickaxe.EnergyStorage(),
                ModItems.LOLI_PICKAXE.get()
        );
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onRenderLivingPre(RenderLivingEvent.Pre<?, ?> event) {
        LivingEntity entity = event.getEntity();

        Entity cameraEntity = Minecraft.getInstance().getCameraEntity();

        if (cameraEntity instanceof Player player) {
            boolean shouldBeVisible = EventUtil.isInvisibleToPlayer(entity, player);

            if (entity.isInvisibleTo(player) && shouldBeVisible) {
                RenderSystem.enableBlend();
                RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA,
                        GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            }
        }
    }
}
