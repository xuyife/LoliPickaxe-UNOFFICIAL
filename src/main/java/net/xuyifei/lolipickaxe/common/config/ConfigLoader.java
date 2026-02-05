package net.xuyifei.lolipickaxe.common.config;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.xuyifei.lolipickaxe.common.config.annotation.ConfigField;
import net.xuyifei.lolipickaxe.common.config.annotation.ConfigField.ConfigType;
import net.xuyifei.lolipickaxe.common.config.annotation.ConfigField.ValueType;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.xuyifei.lolipickaxe.common.registry.ModConfigs;
import net.xuyifei.lolipickaxe.common.registry.ModDataComponents;
import net.xuyifei.lolipickaxe.common.network.ServerboundSetSlotPacket;
import net.xuyifei.lolipickaxe.common.registry.custom.LoliPickaxe;

@OnlyIn(Dist.CLIENT)
public class ConfigLoader {
    private static final Minecraft minecraft = Minecraft.getInstance();
    public static final List<String> flags = Lists.newArrayList();
    public static final List<String> commandFlags = Lists.newArrayList();
    public static final List<String> guiFlags = Lists.newArrayList();
    public static final Map<String, ConfigField> flagAnnotations = Maps.newHashMap();
    public static final Map<String, Field> flagFields = Maps.newHashMap();

    @ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND }, comment = "最大采掘范围", valueType = ValueType.INT, intDefaultValue = 5)
    public static int loliPickaxeMaxRange;
    @ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND, ConfigType.GUI }, comment = "强制掉落方块", valueType = ValueType.BOOLEAN, booleanDefaultValue = false)
    public static boolean loliPickaxeMandatoryDrop;
    @ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND, ConfigType.GUI }, comment = "显示流体边框", valueType = ValueType.BOOLEAN, booleanDefaultValue = false)
    public static boolean loliPickaxeStopOnLiquid;
    @ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND, ConfigType.GUI }, comment = "挖掘距离", valueType = ValueType.DOUBLE, doubleDefaultValue = 0.0, doubleMinValue = 0, doubleMaxValueField = "loliPickaxeBlockReachMaxDistance")
    public static double loliPickaxeBlockReachDistance;
    @ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND }, comment = "最大挖掘距离", valueType = ValueType.DOUBLE, doubleDefaultValue = 20.0)
    public static double loliPickaxeBlockReachMaxDistance;
    @ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND }, comment = "储藏室最大页数", valueType = ValueType.INT, intDefaultValue = 100)
    public static int loliPickaxeMaxPage;
    @ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND }, comment = "储藏室取消物品堆叠限制", valueType = ValueType.BOOLEAN, booleanDefaultValue = true)
    public static boolean loliPickaxeCancelStackLimit;
    @ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND }, comment = "储藏室最大堆叠数", valueType = ValueType.INT, intDefaultValue = 2000000000)
    public static int loliPickaxeSlotStackLimit;
    @ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND, ConfigType.GUI }, comment = "自动收纳进储藏室", valueType = ValueType.BOOLEAN, booleanDefaultValue = true)
    public static boolean loliPickaxeAutoAccept;
    @ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND, ConfigType.GUI }, comment = "反伤", valueType = ValueType.BOOLEAN, booleanDefaultValue = true)
    public static boolean loliPickaxeThorns;
    @ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND, ConfigType.GUI }, comment = "潜行右键杀死周围实体", valueType = ValueType.BOOLEAN, booleanDefaultValue = true)
    public static boolean loliPickaxeKillRangeEntity;
    @ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND, ConfigType.GUI }, comment = "潜行右键杀死周围实体的范围", valueType = ValueType.INT, intDefaultValue = 50, intMinValue = 0, intMaxValueField = "loliPickaxeMaxKillRange")
    public static int loliPickaxeKillRange;
    @ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND }, comment = "潜行右键杀死周围实体的最大范围", valueType = ValueType.INT, intDefaultValue = 100)
    public static int loliPickaxeMaxKillRange;
    @ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND, ConfigType.GUI }, comment = "自动杀死周围实体", valueType = ValueType.BOOLEAN, booleanDefaultValue = false)
    public static boolean loliPickaxeAutoKillRangeEntity;
    @ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND, ConfigType.GUI }, comment = "自动杀死周围实体的范围", valueType = ValueType.INT, intDefaultValue = 5, intMinValue = 0, intMaxValueField = "loliPickaxeMaxAutoKillRange")
    public static int loliPickaxeAutoKillRange;
    @ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND }, comment = "自动杀死周围实体的最大范围", valueType = ValueType.INT, intDefaultValue = 10)
    public static int loliPickaxeMaxAutoKillRange;
    @ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND }, comment = "效果持续时间(Tick)", valueType = ValueType.INT, intDefaultValue = 200)
    public static int loliPickaxeDuration;
    @ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND }, comment = "丢弃保护时间(ms)", valueType = ValueType.INT, intDefaultValue = 200)
    public static int loliPickaxeDropProtectTime;
    @ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND, ConfigType.GUI }, comment = "强制清除生物", valueType = ValueType.BOOLEAN, booleanDefaultValue = true)
    public static boolean loliPickaxeCompulsoryRemove;
    @ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND, ConfigType.GUI }, comment = "范围攻击对非怪物有效", valueType = ValueType.BOOLEAN, booleanDefaultValue = true)
    public static boolean loliPickaxeValidToAmityEntity;
    @ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND, ConfigType.GUI }, comment = "对全部实体有效", valueType = ValueType.BOOLEAN, booleanDefaultValue = false)
    public static boolean loliPickaxeValidToAllEntity;
    @ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND, ConfigType.GUI }, comment = "清空玩家背包", valueType = ValueType.BOOLEAN, booleanDefaultValue = false)
    public static boolean loliPickaxeClearInventory;
    @ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND, ConfigType.GUI }, comment = "缴械", valueType = ValueType.BOOLEAN, booleanDefaultValue = false)
    public static boolean loliPickaxeDropItems;
    @ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND, ConfigType.GUI }, comment = "踢出玩家", valueType = ValueType.BOOLEAN, booleanDefaultValue = false)
    public static boolean loliPickaxeKickPlayer;
    @ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND, ConfigType.GUI }, comment = "踢出玩家消息", valueType = ValueType.STRING, stringDefaultValue = "你被氪金萝莉踢出了服务器")
    public static String loliPickaxeKickMessage;
    @ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND }, comment = "禁止死亡实体触发实体更新事件", valueType = ValueType.BOOLEAN, booleanDefaultValue = false)
    public static boolean loliPickaxeForbidOnLivingUpdate;
    @ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND, ConfigType.GUI }, comment = "伊邪那美(需同时开启踢出玩家)", valueType = ValueType.BOOLEAN, booleanDefaultValue = false, warning = true)
    public static boolean loliPickaxeReincarnation;
    @ConfigField(type = { ConfigType.CONFIG }, comment = "伊邪那美玩家列表", valueType = ValueType.LIST, listDefaultValue = {})
    public static List<String> loliPickaxeReincarnationPlayerList;
    @ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND, ConfigType.GUI }, comment = "灵魂超度", valueType = ValueType.BOOLEAN, booleanDefaultValue = false)
    public static boolean loliPickaxeBeyondRedemption;
    @ConfigField(type = { ConfigType.CONFIG }, comment = "灵魂超度玩家列表", valueType = ValueType.LIST, listDefaultValue = {})
    public static List<String> loliPickaxeBeyondRedemptionPlayerList;
    @ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND }, comment = "寻找所有者", valueType = ValueType.BOOLEAN, booleanDefaultValue = true)
    public static boolean loliPickaxeFindOwner;
    @ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND }, comment = "寻找所有者范围", valueType = ValueType.INT, intDefaultValue = 50)
    public static int loliPickaxeFindOwnerRange;
    @ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND, ConfigType.GUI }, comment = "蓝屏打击", valueType = ValueType.BOOLEAN, booleanDefaultValue = false, warning = true)
    public static boolean loliPickaxeBlueScreenAttack;
    @ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND, ConfigType.GUI }, comment = "蹦溃打击", valueType = ValueType.BOOLEAN, booleanDefaultValue = false, warning = true)
    public static boolean loliPickaxeExitAttack;
    @ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND, ConfigType.GUI }, comment = "未响应打击", valueType = ValueType.BOOLEAN, booleanDefaultValue = false, warning = true)
    public static boolean loliPickaxeFailRespondAttack;
    @ConfigField(type = { ConfigType.CONFIG }, comment = "强制死亡延迟特化列表(实体ID:Tick)", valueType = ValueType.MAP, mapDefaultValue = { "ender_dragon:::201" }, mapKeyType = ValueType.STRING, mapValueType = ValueType.INT)
    public static Map<String, Integer> loliPickaxeDelayRemoveList;
    @ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND, ConfigType.GUI }, comment = "左键范围攻击", valueType = ValueType.BOOLEAN, booleanDefaultValue = true)
    public static boolean loliPickaxeKillFacing;
    @ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND, ConfigType.GUI }, comment = "范围攻击范围", valueType = ValueType.INT, intDefaultValue = 50, intMinValue = 0, intMaxValueField = "loliPickaxeMaxKillFacingRange")
    public static int loliPickaxeKillFacingRange;
    @ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND }, comment = "范围攻击最大范围", valueType = ValueType.INT, intDefaultValue = 200)
    public static int loliPickaxeMaxKillFacingRange;
    @ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND, ConfigType.GUI }, comment = "范围攻击斜率", valueType = ValueType.DOUBLE, doubleDefaultValue = 0.1, doubleMinValue = 0, doubleMaxValueField = "loliPickaxeMaxKillFacingSlope")
    public static double loliPickaxeKillFacingSlope;
    @ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND }, comment = "范围攻击最大斜率", valueType = ValueType.DOUBLE, doubleDefaultValue = 1.0)
    public static double loliPickaxeMaxKillFacingSlope;
    @ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND, ConfigType.GUI }, comment = "视觉迷惑", valueType = ValueType.BOOLEAN, booleanDefaultValue = false)
    public static boolean loliPickaxeInvisible;
    @ConfigField(type = { ConfigType.CONFIG }, comment = "GUI可修改选项", valueType = ValueType.LIST, listType = ValueType.STRING, listDefaultValue = { "loliPickaxeMandatoryDrop", "loliPickaxeStopOnLiquid", "loliPickaxeBlockReachDistance", "loliPickaxeAutoAccept", "loliPickaxeThorns", "loliPickaxeKillRangeEntity", "loliPickaxeKillRange", "loliPickaxeAutoKillRangeEntity", "loliPickaxeAutoKillRange", "loliPickaxeCompulsoryRemove", "loliPickaxeValidToAmityEntity", "loliPickaxeValidToAllEntity", "loliPickaxeClearInventory", "loliPickaxeDropItems", "loliPickaxeKickPlayer", "loliPickaxeKickMessage", "loliPickaxeReincarnation", "loliPickaxeBeyondRedemption", "loliPickaxeBlueScreenAttack", "loliPickaxeExitAttack", "loliPickaxeFailRespondAttack", "loliPickaxeKillFacing", "loliPickaxeKillFacingRange", "loliPickaxeKillFacingSlope", "loliPickaxeInfiniteBattery", "loliPickaxeInvisible", "loliPickaxeShowInvisible" }, warning = true, warningMethod = "guiChangeListWarning")
    public static List<String> loliPickaxeGuiChangeList;
    @ConfigField(type = {}, comment = "额外唱片列表(声音:唱片名:唱片ID)", valueType = ValueType.LIST, listType = ValueType.STRING, listDefaultValue = { "lolirecord:loliRecord:loli_record" })
    public static List<String> loliRecodeNames;
    @ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND }, comment = "萝莉卡片掉落概率", valueType = ValueType.DOUBLE, doubleDefaultValue = 0.1)
    public static double loliCardDropProbability;
    @ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND }, comment = "萝莉卡片掉落概率", valueType = ValueType.DOUBLE, doubleDefaultValue = 0.01)
    public static double loliCardAlbumDropProbability;
    @ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND }, comment = "萝莉唱片掉落概率", valueType = ValueType.DOUBLE, doubleDefaultValue = 0.001)
    public static double loliRecordDropProbability;
    @ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND }, comment = "生物灵魂掉落概率", valueType = ValueType.DOUBLE, doubleDefaultValue = 0.01)
    public static double entitySoulDropProbability;
    @ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND }, comment = "萝莉行走速度", valueType = ValueType.DOUBLE, doubleDefaultValue = 1.0)
    public static double loliSpeed;
    @ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND }, comment = "萝莉主动攻击", valueType = ValueType.BOOLEAN, booleanDefaultValue = true)
    public static boolean loliAttack;
    @ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND }, comment = "萝莉瞬移", valueType = ValueType.BOOLEAN, booleanDefaultValue = true)
    public static boolean loliTeleport;
    @ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND }, comment = "萝莉模型(0:萝莉,1:纳文摩尔,2:纸片人,3:车万女仆)", valueType = ValueType.INT, intDefaultValue = 0)
    public static int loliModelType;
    @ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND }, comment = "萝莉模型ID", valueType = ValueType.STRING, stringDefaultValue = "touhou_little_maid:remilia_scarlet")
    public static String loliModelId;
    @ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND }, comment = "萝莉卡片展示框缩放比例", valueType = ValueType.DOUBLE, doubleDefaultValue = 1.0)
    public static double loliCardScale;
    @ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND }, comment = "萝莉卡片册切换速度", valueType = ValueType.INT, intDefaultValue = 100)
    public static int loliCardAlbumSwitchSpeed;
    @ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND }, comment = "萝莉卡片渲染展示框", valueType = ValueType.BOOLEAN, booleanDefaultValue = false)
    public static boolean loliCardRenderFrame;
    @ConfigField(type = { ConfigType.CONFIG }, comment = "附魔最大等级列表", valueType = ValueType.MAP, mapDefaultValue = {}, mapKeyType = ValueType.STRING, mapValueType = ValueType.INT)
    public static Map<String, Integer> loliPickaxeEnchantmentLimit;
    @ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND }, comment = "默认附魔最大等级", valueType = ValueType.INT, intDefaultValue = 32)
    public static int loliPickaxeEnchantmentDefaultLimit;
    @ConfigField(type = { ConfigType.CONFIG }, comment = "药水最大等级列表", valueType = ValueType.MAP, mapDefaultValue = {}, mapKeyType = ValueType.STRING, mapValueType = ValueType.INT)
    public static Map<String, Integer> loliPickaxePotionLimit;
    @ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND }, comment = "默认药水最大等级", valueType = ValueType.INT, intDefaultValue = 32)
    public static int loliPickaxePotionDefaultLimit;
    @ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND }, comment = "启用特效攻击炸弹", valueType = ValueType.BOOLEAN, booleanDefaultValue = false, warning = true)
    public static boolean loliEnableBuffAttackTNT;
    @ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND, ConfigType.GUI }, comment = "超级电池", valueType = ValueType.BOOLEAN, booleanDefaultValue = true)
    public static boolean loliPickaxeInfiniteBattery;
    @ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND }, comment = "跨世界传送", valueType = ValueType.BOOLEAN, booleanDefaultValue = true)
    public static boolean loliPickaxeSpaceFolding;
    @ConfigField(type = { ConfigType.CONFIG }, comment = "跨世界传送黑名单", valueType = ValueType.LIST, listType = ValueType.INT, listDefaultValue = {})
    public static List<Integer> loliPickaxeWorldBlacklist;
    @ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND }, comment = "传送最远距离", valueType = ValueType.DOUBLE, doubleDefaultValue = 512.0)
    public static double loliPickaxeMaxTeleportDistance;
    @ConfigField(type = { ConfigType.CONFIG }, comment = "萝莉卡片URL", valueType = ValueType.MAP, mapDefaultValue = { "gk_head_portrait.png:::https://www.pixiv.net/artworks/61282195", "小莫女儿:::https://www.pixiv.net/users/5776001" }, mapKeyType = ValueType.STRING, mapValueType = ValueType.STRING)
    public static Map<String, String> loliCardURL;
    @ConfigField(type = { ConfigType.CONFIG }, comment = "创造模式物品栏默认网络卡片", valueType = ValueType.LIST, listType = ValueType.STRING, listDefaultValue = { "https://bigimg.cheerfun.dev/get/https://i.pximg.net/img-original/img/2017/03/18/03/44/39/61965296_p0.png", "https://bigimg.cheerfun.dev/get/https://i.pximg.net/img-original/img/2015/10/23/18/05/06/53170539_p0.jpg", "https://bigimg.cheerfun.dev/get/https://i.pximg.net/img-original/img/2015/09/27/07/15/20/52735806_p0.jpg" })
    public static List<String> loliCardOnlineDefURL;
    @ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND, ConfigType.GUI }, comment = "显示隐身生物", valueType = ValueType.BOOLEAN, booleanDefaultValue = true)
    public static boolean loliPickaxeShowInvisible;
    @ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND }, comment = "触发方块破坏事件", valueType = ValueType.BOOLEAN, booleanDefaultValue = true)
    public static boolean loliPickaxeTriggerBreakEvent;

    static {
        try {
            Field[] fields = ConfigLoader.class.getFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(ConfigField.class)) {
                    ConfigField annotation = field.getAnnotation(ConfigField.class);
                    flags.add(field.getName());
                    flagAnnotations.put(field.getName(), annotation);
                    flagFields.put(field.getName(), field);
                    ConfigType[] types = annotation.type();
                    for (ConfigType type : types) {
                        switch (type) {
                            case COMMAND:
                                commandFlags.add(field.getName());
                                break;
                            case GUI:
                                guiFlags.add(field.getName());
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public static int getInt(ItemStack stack, String flag) {
        if (flags.contains(flag)) {
            ConfigField annotation = flagAnnotations.get(flag);
            if (annotation.valueType() == ValueType.INT) {
                CustomData configTag = stack.get(ModDataComponents.LOLI_CONFIG.get());
                if (configTag != null) {
                    if (configTag.contains(flag)) {
                        return configTag.copyTag().getInt(flag);
                    } else {
                        return ModConfigs.getInt(flag);
                    }
                }
            }
        }
        return 0;
    }

    public static void setInt(ItemStack stack, String flag, int value) {
        if (flags.contains(flag)) {
            ConfigField annotation = flagAnnotations.get(flag);
            if (annotation.valueType() == ValueType.INT) {
                if (guiFlags.contains(flag) && !stack.isEmpty() && stack.getItem() instanceof LoliPickaxe) {
                    CompoundTag tag = new CompoundTag();
                    tag.putInt(flag, value);
                    CustomData.update(ModDataComponents.LOLI_CONFIG.get(), stack, existingData -> existingData.merge(tag));

                    minecraft.player.setItemInHand(InteractionHand.MAIN_HAND, stack);
                    minecraft.player.connection.send(new ServerboundSetSlotPacket(minecraft.player.getInventory().selected + Inventory.INVENTORY_SIZE, stack));
                }
            }
        }
    }

    public static boolean getBoolean(ItemStack stack, String flag) {
        if (flags.contains(flag)) {
            ConfigField annotation = flagAnnotations.get(flag);
            if (annotation.valueType() == ValueType.BOOLEAN) {
                CustomData configTag = stack.get(ModDataComponents.LOLI_CONFIG.get());
                if (configTag != null) {
                    if (configTag.contains(flag)) {
                        return configTag.copyTag().getBoolean(flag);
                    } else {
                        return ModConfigs.getBoolean(flag);
                    }
                }
            }
        }
        return false;
    }

    public static void setBoolean(ItemStack stack, String flag, boolean value) {
        if (flags.contains(flag)) {
            ConfigField annotation = flagAnnotations.get(flag);
            if (annotation.valueType() == ValueType.BOOLEAN) {
                if (guiFlags.contains(flag) && !stack.isEmpty() && stack.getItem() instanceof LoliPickaxe) {
                    CompoundTag tag = new CompoundTag();
                    tag.putBoolean(flag, value);
                    CustomData.update(ModDataComponents.LOLI_CONFIG.get(), stack, existingData -> existingData.merge(tag));

                    minecraft.player.setItemInHand(InteractionHand.MAIN_HAND, stack);
                    minecraft.player.connection.send(new ServerboundSetSlotPacket(minecraft.player.getInventory().selected + Inventory.INVENTORY_SIZE, stack));
                }
            }
        }
    }

    public static double getDouble(ItemStack stack, String flag) {
        if (flags.contains(flag)) {
            ConfigField annotation = flagAnnotations.get(flag);
            if (annotation.valueType() == ValueType.DOUBLE) {
                CustomData configTag = stack.get(ModDataComponents.LOLI_CONFIG.get());
                if (configTag != null) {
                    if (configTag.contains(flag)) {
                        return configTag.copyTag().getDouble(flag);
                    } else {
                        return ModConfigs.getDouble(flag);
                    }
                }
            }
        }
        return 0;
    }

    public static void setDouble(ItemStack stack, String flag, double value) {
        if (flags.contains(flag)) {
            ConfigField annotation = flagAnnotations.get(flag);
            if (annotation.valueType() == ValueType.DOUBLE) {
                if (guiFlags.contains(flag) && !stack.isEmpty() && stack.getItem() instanceof LoliPickaxe) {
                    CompoundTag tag = new CompoundTag();
                    tag.putDouble(flag, value);
                    CustomData.update(ModDataComponents.LOLI_CONFIG.get(), stack, existingData -> existingData.merge(tag));

                    minecraft.player.setItemInHand(InteractionHand.MAIN_HAND, stack);
                    minecraft.player.connection.send(new ServerboundSetSlotPacket(minecraft.player.getInventory().selected + Inventory.INVENTORY_SIZE, stack));
                }
            }
        }
    }

    public static String getString(ItemStack stack, String flag) {
        if (flags.contains(flag)) {
            ConfigField annotation = flagAnnotations.get(flag);
            if (annotation.valueType() == ValueType.STRING) {
                CustomData configTag = stack.get(ModDataComponents.LOLI_CONFIG.get());
                if (configTag != null) {
                    if (configTag.contains(flag)) {
                        return configTag.copyTag().getString(flag);
                    } else {
                        return ModConfigs.getString(flag);
                    }
                }
            }
        }
        return null;
    }

    public static void setString(ItemStack stack, String flag, String value) {
        if (flags.contains(flag)) {
            ConfigField annotation = flagAnnotations.get(flag);
            if (annotation.valueType() == ValueType.STRING) {
                if (guiFlags.contains(flag) && !stack.isEmpty() && stack.getItem() instanceof LoliPickaxe) {
                    CompoundTag tag = new CompoundTag();
                    tag.putString(flag, value);
                    CustomData.update(ModDataComponents.LOLI_CONFIG.get(), stack, existingData -> existingData.merge(tag));

                    minecraft.player.setItemInHand(InteractionHand.MAIN_HAND, stack);
                    minecraft.player.connection.send(new ServerboundSetSlotPacket(minecraft.player.getInventory().selected + Inventory.INVENTORY_SIZE, stack));
                }
            }
        }
    }
}
