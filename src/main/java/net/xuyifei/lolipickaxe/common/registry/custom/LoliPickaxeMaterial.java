package net.xuyifei.lolipickaxe.common.registry.custom;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.xuyifei.lolipickaxe.common.registry.ModDataComponents;
import net.xuyifei.lolipickaxe.common.registry.ModItems;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class LoliPickaxeMaterial extends Item {

    private final int subCount;
    private final int curLevel;
    private final String key;

    public LoliPickaxeMaterial(Properties properties, int subCount, int curLevel, String key) {
        super(properties.component(ModDataComponents.LOLI_ADDON_LEVEL.get(), curLevel));
        this.subCount = subCount;
        this.curLevel = curLevel;
        this.key = key;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        int level = stack.getOrDefault(ModDataComponents.LOLI_ADDON_LEVEL.get(), 0);
        if (level < subCount - 1) {
            String nextMaterial = (level == subCount - 2) ?
                    "item.lolipickaxe.loliMaterial.end" :
                    "item.lolipickaxe.loliMaterial." + (level + 1);

            tooltipComponents.add(Component.translatable("item.lolipickaxe.loliMaterial.recipe",
                    Component.translatable("item.lolipickaxe.loliMaterial." + level),
                    Component.translatable(nextMaterial),
                    Component.translatable("item.lolipickaxe.loliMaterial." + (subCount - 1))));
        }
        if (!this.key.equals("loli_entity_soul_addon")) {
            tooltipComponents.add(Component.literal("右键安装！"));
            tooltipComponents.add(Component.literal("默认安装给背包中第一个普通萝莉！"));
        } else if (this.curLevel == 6) {
            tooltipComponents.add(Component.literal("右键给背包中第一个装满升级的普通萝莉升级为氪金萝莉"));
        }
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        if (subCount > 1) {
            int level = stack.getOrDefault(ModDataComponents.LOLI_ADDON_LEVEL.get(), 0);
            if (level == subCount - 1) {
                return Component.translatable("item.lolipickaxe.loliMaterialFormat",
                        Component.translatable("item.lolipickaxe." + this.key),
                        Component.translatable("item.lolipickaxe.loliMaterial.end"));
            } else {
                return Component.translatable("item.lolipickaxe.loliMaterialFormat",
                        Component.translatable("item.lolipickaxe." + this.key),
                        Component.translatable("item.lolipickaxe.loliMaterial." + level));
            }
        } else {
            return super.getName(stack);
        }
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand usedHand) {
        ItemStack upgItem = player.getItemInHand(usedHand);
        if (!level.isClientSide()) {
            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                ItemStack stack = player.getInventory().getItem(i);
                if (!this.key.equals("loli_entity_soul_addon")) {
                    if (stack.getItem() instanceof SmallLoliPickaxe smallLoliPickaxe) {
                        var attrData = stack.get(ModDataComponents.SMALL_LOLI_ATTRIBUTE.get());
                        CompoundTag attrTag;

                        if (attrData != null) {
                            attrTag = attrData.copyTag();
                        } else {
                            attrTag = new CompoundTag();
                        }

                        String attrKey = switch (key) {
                            case "loli_coal_addon" -> "LoliDodge";
                            case "loli_iron_addon" -> "LoliDiggingSpeed";
                            case "loli_gold_addon" -> "LoliAttackDamage";
                            case "loli_redstone_addon" -> "LoliAttackSpeed";
                            case "loli_lapis_addon" -> "LoliFortuneLevel";
                            case "loli_diamond_addon" -> "LoliDiggingLevel";
                            case "loli_emerald_addon" -> "LoliDiggingRange";
                            case "loli_obsidian_addon" -> "LoliAntiInjury";
                            case "loli_glow_addon" -> "LoliBuff";
                            case "loli_quartz_addon" -> "LoliHitRange";
                            case "loli_nether_star_addon" -> "LoliBackpackPage";
                            case "loli_auto_furnace_addon" -> "LoliAutoFurnace";
                            case "loli_fly_addon" -> "LoliFly";
                            default -> "";
                        };

                        if (attrTag.isEmpty()) {
                            if (this.curLevel == 0) {
                                applyAddon(attrTag, attrKey, stack, smallLoliPickaxe, level, upgItem, player, usedHand);

                                return InteractionResultHolder.sidedSuccess(upgItem, false);
                            }
                        } else if (!attrTag.contains(attrKey)) {
                            if (this.curLevel == 0) {
                                applyAddon(attrTag, attrKey, stack, smallLoliPickaxe, level, upgItem, player, usedHand);

                                return InteractionResultHolder.sidedSuccess(upgItem, false);
                            } else if (this.curLevel > 0) {
                                player.displayClientMessage(Component.literal("请先安装上一级升级！"), true);

                                return InteractionResultHolder.pass(upgItem);
                            }
                        } else {
                            int currentLevel = attrTag.getInt(attrKey);
                            if (currentLevel == curLevel - 1) {
                                applyAddon(attrTag, attrKey, stack, smallLoliPickaxe, level, upgItem, player, usedHand);

                                return InteractionResultHolder.sidedSuccess(upgItem, false);
                            } else if (currentLevel < curLevel - 1) {
                                player.displayClientMessage(Component.literal("请先安装上一级升级！"), true);

                                return InteractionResultHolder.pass(upgItem);
                            } else if (currentLevel > curLevel - 1) {
                                player.displayClientMessage(Component.literal("已经安装了更高等级的升级或同等级的升级！"), true);

                                return InteractionResultHolder.pass(upgItem);
                            }
                        }
                    }
                } else if (this.curLevel == subCount - 1) {
                    if (stack.getItem() instanceof SmallLoliPickaxe) {
                        if (stack.getOrDefault(ModDataComponents.SMALL_LOLI_ATTRIBUTE.get(), CustomData.EMPTY).equals(SmallLoliPickaxe.getFull(level.registryAccess()).get(ModDataComponents.SMALL_LOLI_ATTRIBUTE.get()))) {
                            player.getInventory().setItem(i, new ItemStack(ModItems.LOLI_PICKAXE.get()));
                            upgItem.shrink(1);
                            player.swing(usedHand);

                            return InteractionResultHolder.sidedSuccess(upgItem, false);
                        }
                    }
                }
            }
        }
        return InteractionResultHolder.pass(upgItem);
    }

    public int getSubCount() {
        return subCount;
    }

    private void applyAddon(CompoundTag attrTag, String attrKey, ItemStack stack, SmallLoliPickaxe smallLoliPickaxe, Level level, ItemStack upgItem, Player player, InteractionHand usedHand) {
        attrTag.putInt(attrKey, curLevel);
        stack.set(ModDataComponents.SMALL_LOLI_ATTRIBUTE.get(), CustomData.of(attrTag));
        smallLoliPickaxe.updateEnchantment(stack, level.registryAccess());

        upgItem.shrink(1);
        player.swing(usedHand);
    }

}
