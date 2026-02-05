package net.xuyifei.lolipickaxe.common.registry;

import net.neoforged.neoforge.common.ModConfigSpec;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

public class ModConfigs {
    public static final ModConfigs INSTANCE;
    public static final ModConfigSpec SPEC;

    public final ModConfigSpec.ConfigValue<Double> entitySoulDropProbability;
    public final ModConfigSpec.ConfigValue<Boolean> loliAttack;
    public final ModConfigSpec.ConfigValue<Double> loliCardAlbumDropProbability;
    public final ModConfigSpec.ConfigValue<Integer> loliCardAlbumSwitchSpeed;
    public final ModConfigSpec.ConfigValue<Double> loliCardDropProbability;
    public final ModConfigSpec.ConfigValue<List<?>> loliCardOnlineDefURL;
    public final ModConfigSpec.ConfigValue<Boolean> loliCardRenderFrame;
    public final ModConfigSpec.ConfigValue<Double> loliCardScale;
    public final ModConfigSpec.ConfigValue<List<?>> loliCardURL;
    public final ModConfigSpec.ConfigValue<Boolean> loliEnableBuffAttackTNT;
    public final ModConfigSpec.ConfigValue<String> loliModelId;
    public final ModConfigSpec.ConfigValue<Integer> loliModelType;
    public final ModConfigSpec.ConfigValue<Boolean> loliPickaxeAutoAccept;
    public final ModConfigSpec.ConfigValue<Integer> loliPickaxeAutoKillRange;
    public final ModConfigSpec.ConfigValue<Boolean> loliPickaxeAutoKillRangeEntity;
    public final ModConfigSpec.ConfigValue<Boolean> loliPickaxeBeyondRedemption;
    public final ModConfigSpec.ConfigValue<List<?>> loliPickaxeBeyondRedemptionPlayerList;
    public final ModConfigSpec.ConfigValue<Double> loliPickaxeBlockReachDistance;
    public final ModConfigSpec.ConfigValue<Double> loliPickaxeBlockReachMaxDistance;
    public final ModConfigSpec.ConfigValue<Boolean> loliPickaxeBlueScreenAttack;
    public final ModConfigSpec.ConfigValue<Boolean> loliPickaxeCancelStackLimit;
    public final ModConfigSpec.ConfigValue<Integer> loliPickaxeMaxPage;
    public final ModConfigSpec.ConfigValue<Integer> loliPickaxeSlotStackLimit;
    public final ModConfigSpec.ConfigValue<Boolean> loliPickaxeClearInventory;
    public final ModConfigSpec.ConfigValue<Boolean> loliPickaxeCompulsoryRemove;
    public final ModConfigSpec.ConfigValue<List<?>> loliPickaxeDelayRemoveList;
    public final ModConfigSpec.ConfigValue<Boolean> loliPickaxeDropItems;
    public final ModConfigSpec.ConfigValue<Integer> loliPickaxeDropProtectTime;
    public final ModConfigSpec.ConfigValue<Integer> loliPickaxeDuration;
    public final ModConfigSpec.ConfigValue<Integer> loliPickaxeEnchantmentDefaultLimit;
    public final ModConfigSpec.ConfigValue<List<?>> loliPickaxeEnchantmentLimit;
    public final ModConfigSpec.ConfigValue<Boolean> loliPickaxeExitAttack;
    public final ModConfigSpec.ConfigValue<Boolean> loliPickaxeFailRespondAttack;
    public final ModConfigSpec.ConfigValue<Boolean> loliPickaxeFindOwner;
    public final ModConfigSpec.ConfigValue<Integer> loliPickaxeFindOwnerRange;
    public final ModConfigSpec.ConfigValue<Boolean> loliPickaxeForbidOnLivingUpdate;
    public final ModConfigSpec.ConfigValue<List<?>> loliPickaxeGuiChangeList;
    public final ModConfigSpec.ConfigValue<Boolean> loliPickaxeInfiniteBattery;
    public final ModConfigSpec.ConfigValue<Boolean> loliPickaxeInvisible;
    public final ModConfigSpec.ConfigValue<Boolean> loliPickaxeShowInvisible;
    public final ModConfigSpec.ConfigValue<Boolean> loliPickaxeKickPlayer;
    public final ModConfigSpec.ConfigValue<String> loliPickaxeKickMessage;
    public final ModConfigSpec.ConfigValue<Boolean> loliPickaxeKillFacing;
    public final ModConfigSpec.ConfigValue<Integer> loliPickaxeKillFacingRange;
    public final ModConfigSpec.ConfigValue<Double> loliPickaxeKillFacingSlope;
    public final ModConfigSpec.ConfigValue<Boolean> loliPickaxeKillRangeEntity;
    public final ModConfigSpec.ConfigValue<Integer> loliPickaxeKillRange;
    public final ModConfigSpec.ConfigValue<Integer> loliPickaxeMaxAutoKillRange;
    public final ModConfigSpec.ConfigValue<Integer> loliPickaxeMaxKillFacingRange;
    public final ModConfigSpec.ConfigValue<Double> loliPickaxeMaxKillFacingSlope;
    public final ModConfigSpec.ConfigValue<Integer> loliPickaxeMaxKillRange;
    public final ModConfigSpec.ConfigValue<Integer> loliPickaxeMaxRange;
    public final ModConfigSpec.ConfigValue<Double> loliPickaxeMaxTeleportDistance;
    public final ModConfigSpec.ConfigValue<Boolean> loliPickaxeMandatoryDrop;
    public final ModConfigSpec.ConfigValue<Integer> loliPickaxePotionDefaultLimit;
    public final ModConfigSpec.ConfigValue<List<?>> loliPickaxePotionLimit;
    public final ModConfigSpec.ConfigValue<Boolean> loliPickaxeReincarnation;
    public final ModConfigSpec.ConfigValue<List<?>> loliPickaxeReincarnationPlayerList;
    public final ModConfigSpec.ConfigValue<Boolean> loliPickaxeSpaceFolding;
    public final ModConfigSpec.ConfigValue<Boolean> loliPickaxeStopOnLiquid;
    public final ModConfigSpec.ConfigValue<Boolean> loliPickaxeThorns;
    public final ModConfigSpec.ConfigValue<Boolean> loliPickaxeTriggerBreakEvent;
    public final ModConfigSpec.ConfigValue<Boolean> loliPickaxeValidToAllEntity;
    public final ModConfigSpec.ConfigValue<Boolean> loliPickaxeValidToAmityEntity;
    public final ModConfigSpec.ConfigValue<List<?>> loliPickaxeWorldBlacklist;
    public final ModConfigSpec.ConfigValue<List<?>> loliRecordNames;
    public final ModConfigSpec.ConfigValue<Double> loliRecordDropProbability;
    public final ModConfigSpec.ConfigValue<Double> loliSpeed;
    public final ModConfigSpec.ConfigValue<Boolean> loliTeleport;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        INSTANCE = new ModConfigs(builder);
        SPEC = builder.build();
    }

    public ModConfigs(ModConfigSpec.Builder builder) {
        builder.push("general");
        this.entitySoulDropProbability = builder
                .comment("生物灵魂掉落概率")
                .defineInRange("entitySoulDropProbability", 0.01, 0.01, 1);
        this.loliAttack = builder
                .comment("萝莉主动攻击")
                .define("loliAttack", true);
        this.loliCardAlbumDropProbability = builder
                .comment("萝莉卡片掉落概率")
                .defineInRange("loliCardAlbumDropProbability", 0.01, 0.01, 1);
        this.loliCardAlbumSwitchSpeed = builder
                .comment("萝莉卡片册切换速度")
                .defineInRange("loliCardAlbumSwitchSpeed", 100, 0, 100);
        this.loliCardDropProbability = builder
                .comment("萝莉卡片掉落概率")
                .defineInRange("loliCardDropProbability", 0.1, 0.01, 1);
        this.loliCardOnlineDefURL = builder
                .comment("创造模式物品栏默认网络卡片")
                .defineList("loliCardOnlineDefURL",
                        List.of(
                                "https://bigimg.cheerfun.dev/get/https://i.pximg.net/img-original/img/2017/03/18/03/44/39/61965296_p0.png",
                                "https://bigimg.cheerfun.dev/get/https://i.pximg.net/img-original/img/2015/10/23/18/05/06/53170539_p0.jpg",
                                "https://bigimg.cheerfun.dev/get/https://i.pximg.net/img-original/img/2015/09/27/07/15/20/52735806_p0.jpg"
                        ),
                        () -> List.of(
                                "https://bigimg.cheerfun.dev/get/https://i.pximg.net/img-original/img/2017/03/18/03/44/39/61965296_p0.png",
                                "https://bigimg.cheerfun.dev/get/https://i.pximg.net/img-original/img/2015/10/23/18/05/06/53170539_p0.jpg",
                                "https://bigimg.cheerfun.dev/get/https://i.pximg.net/img-original/img/2015/09/27/07/15/20/52735806_p0.jpg"
                        ),
                        obj -> obj instanceof String
                );
        this.loliCardRenderFrame = builder
                .comment("萝莉卡片渲染展示框")
                .define("loliCardRenderFrame", false);
        this.loliCardScale = builder
                .comment("萝莉卡片展示框缩放比例")
                .defineInRange("loliCardScale", 1.0, 0.1, Double.MAX_VALUE);
        this.loliCardURL = builder
                .comment("萝莉卡片URL")
                .defineList("loliCardURL",
                        List.of(
                                "gk_head_portrait.png:::https://www.pixiv.net/artworks/61282195",
                                "xmnr:::https://www.pixiv.net/users/5776001"
                        ),
                        () -> List.of(
                                "gk_head_portrait.png:::https://www.pixiv.net/artworks/61282195",
                                "xmnr:::https://www.pixiv.net/users/5776001"
                        ),
                        obj -> obj instanceof String
                );
        this.loliEnableBuffAttackTNT = builder
                .comment("启用特效攻击炸弹")
                .define("loliEnableBuffAttackTNT", false);
        this.loliModelId = builder
                .comment("萝莉模型ID")
                .define("loliModelId", "touhou_little_maid:remilia_scarlet");
        this.loliModelType = builder
                .comment("萝莉模型(0:萝莉,1:纳文摩尔,2:纸片人,3:车万女仆)")
                .defineInRange("loliModelType", 0, 0, 3);
        this.loliPickaxeAutoAccept = builder
                .comment("自动收纳进储藏室")
                .define("loliPickaxeAutoAccept", true);
        this.loliPickaxeAutoKillRange = builder
                .comment("自动杀死周围实体的范围")
                .defineInRange("loliPickaxeAutoKillRange", 5, 0, Integer.MAX_VALUE);
        this.loliPickaxeAutoKillRangeEntity = builder
                .comment("自动杀死周围实体")
                .define("loliPickaxeAutoKillRangeEntity", false);
        this.loliPickaxeBeyondRedemption = builder
                .comment("灵魂超度")
                .define("loliPickaxeBeyondRedemption", false);
        this.loliPickaxeBeyondRedemptionPlayerList = builder
                .comment("灵魂超度玩家列表")
                .defineList("loliPickaxeBeyondRedemptionPlayerList", List.of(), List::of, obj -> obj instanceof String);
        this.loliPickaxeBlockReachDistance = builder
                .comment("挖掘距离")
                .defineInRange("loliPickaxeBlockReachDistance", 0.0, 0.0, Double.MAX_VALUE);
        this.loliPickaxeBlockReachMaxDistance = builder
                .comment("最大挖掘距离")
                .defineInRange("loliPickaxeBlockReachMaxDistance", 20.0, 0.0, Double.MAX_VALUE);
        this.loliPickaxeBlueScreenAttack = builder
                .comment("蓝屏打击")
                .define("loliPickaxeBlueScreenAttack", false);
        this.loliPickaxeCancelStackLimit = builder
                .comment("储藏室取消物品堆叠限制")
                .define("loliPickaxeCancelStackLimit", true);
        this.loliPickaxeClearInventory = builder
                .comment("清空玩家背包")
                .define("loliPickaxeClearInventory", false);
        this.loliPickaxeCompulsoryRemove = builder
                .comment("强制清除生物")
                .define("loliPickaxeCompulsoryRemove", true);
        this.loliPickaxeDelayRemoveList = builder
                .comment("强制死亡延迟特化列表(实体ID:Tick)")
                .defineList("loliPickaxeDelayRemoveList", List.of("minecraft:ender_dragon:::201"), () -> List.of("minecraft:ender_dragon:::201"), obj -> obj instanceof String);
        this.loliPickaxeDropItems = builder
                .comment("缴械")
                .define("loliPickaxeDropItems", false);
        this.loliPickaxeDropProtectTime = builder
                .comment("丢弃保护时间(ms)")
                .defineInRange("loliPickaxeDropProtectTime", 200, 0, Integer.MAX_VALUE);
        this.loliPickaxeDuration = builder
                .comment("效果持续时间(Tick)")
                .defineInRange("loliPickaxeDuration", 200, 0, Integer.MAX_VALUE);
        this.loliPickaxeEnchantmentDefaultLimit = builder
                .comment("默认附魔最大等级")
                .defineInRange("loliPickaxeEnchantmentDefaultLimit", 32, 0, Integer.MAX_VALUE);
        this.loliPickaxeEnchantmentLimit = builder
                .comment("附魔最大等级列表")
                .defineList("loliPickaxeEnchantmentLimit", List.of(), List::of, obj -> obj instanceof String);
        this.loliPickaxeExitAttack = builder
                .comment("蹦溃打击")
                .define("loliPickaxeExitAttack", false);
        this.loliPickaxeFailRespondAttack = builder
                .comment("未响应打击")
                .define("loliPickaxeFailRespondAttack", false);
        this.loliPickaxeFindOwner = builder
                .comment("寻找所有者")
                .define("loliPickaxeFindOwner", true);
        this.loliPickaxeFindOwnerRange = builder
                .comment("寻找所有者范围")
                .defineInRange("loliPickaxeFindOwnerRange", 50, 0,  Integer.MAX_VALUE);
        this.loliPickaxeForbidOnLivingUpdate = builder
                .comment("禁止死亡实体触发实体更新事件")
                .define("loliPickaxeForbidOnLivingUpdate", false);
        this.loliPickaxeGuiChangeList = builder
                .comment("GUI可修改选项")
                .defineList("loliPickaxeGuiChangeList", List.of(
                        "loliPickaxeMandatoryDrop",
                        "loliPickaxeStopOnLiquid",
                        "loliPickaxeBlockReachDistance",
                        "loliPickaxeAutoAccept",
                        "loliPickaxeThorns",
                        "loliPickaxeKillRangeEntity",
                        "loliPickaxeKillRange",
                        "loliPickaxeAutoKillRangeEntity",
                        "loliPickaxeAutoKillRange",
                        "loliPickaxeCompulsoryRemove",
                        "loliPickaxeValidToAmityEntity",
                        "loliPickaxeValidToAllEntity",
                        "loliPickaxeClearInventory",
                        "loliPickaxeDropItems",
                        "loliPickaxeKickPlayer",
                        "loliPickaxeKickMessage",
                        "loliPickaxeReincarnation",
                        "loliPickaxeBeyondRedemption",
                        "loliPickaxeBlueScreenAttack",
                        "loliPickaxeExitAttack",
                        "loliPickaxeFailRespondAttack",
                        "loliPickaxeKillFacing",
                        "loliPickaxeKillFacingRange",
                        "loliPickaxeKillFacingSlope",
                        "loliPickaxeInfiniteBattery",
                        "loliPickaxeInvisible",
                        "loliPickaxeShowInvisible"
                ), () -> List.of(
                        "loliPickaxeMandatoryDrop",
                        "loliPickaxeStopOnLiquid",
                        "loliPickaxeBlockReachDistance",
                        "loliPickaxeAutoAccept",
                        "loliPickaxeThorns",
                        "loliPickaxeKillRangeEntity",
                        "loliPickaxeKillRange",
                        "loliPickaxeAutoKillRangeEntity",
                        "loliPickaxeAutoKillRange",
                        "loliPickaxeCompulsoryRemove",
                        "loliPickaxeValidToAmityEntity",
                        "loliPickaxeValidToAllEntity",
                        "loliPickaxeClearInventory",
                        "loliPickaxeDropItems",
                        "loliPickaxeKickPlayer",
                        "loliPickaxeKickMessage",
                        "loliPickaxeReincarnation",
                        "loliPickaxeBeyondRedemption",
                        "loliPickaxeBlueScreenAttack",
                        "loliPickaxeExitAttack",
                        "loliPickaxeFailRespondAttack",
                        "loliPickaxeKillFacing",
                        "loliPickaxeKillFacingRange",
                        "loliPickaxeKillFacingSlope",
                        "loliPickaxeInfiniteBattery",
                        "loliPickaxeInvisible",
                        "loliPickaxeShowInvisible"
                ), obj -> obj instanceof String);
        this.loliPickaxeInfiniteBattery = builder
                .comment("超级电池")
                .define("loliPickaxeInfiniteBattery", true);
        this.loliPickaxeInvisible = builder
                .comment("视觉迷惑")
                .define("loliPickaxeInvisible", false);
        this.loliPickaxeKickPlayer = builder
                .comment("踢出玩家")
                .define("loliPickaxeKickPlayer", false);
        this.loliPickaxeKickMessage = builder
                .comment("踢出玩家消息")
                .define("loliPickaxeKickMessage", "你被氪金萝莉踢出了服务器");
        this.loliPickaxeKillFacing = builder
                .comment("左键范围攻击")
                .define("loliPickaxeKillFacing", true);
        this.loliPickaxeKillFacingRange = builder
                .comment("范围攻击范围")
                .defineInRange("loliPickaxeKillFacingRange", 50, 0, Integer.MAX_VALUE);
        this.loliPickaxeKillFacingSlope = builder
                .comment("范围攻击斜率")
                .defineInRange("loliPickaxeKillFacingSlope", 0.1, 0.0, Double.MAX_VALUE);
        this.loliPickaxeKillRangeEntity = builder
                .comment("潜行右键杀死周围实体")
                .define("loliPickaxeKillRangeEntity", true);
        this.loliPickaxeKillRange = builder
                .comment("潜行右键杀死周围实体的范围")
                .defineInRange("loliPickaxeKillRange", 50, 0, Integer.MAX_VALUE);
        this.loliPickaxeMandatoryDrop = builder
                .comment("强制掉落方块")
                .define("loliPickaxeMandatoryDrop", false);
        this.loliPickaxeMaxAutoKillRange = builder
                .comment("自动杀死周围实体的最大范围")
                .defineInRange("loliPickaxeMaxAutoKillRange", 10, 0, Integer.MAX_VALUE);
        this.loliPickaxeMaxKillFacingRange = builder
                .comment("范围攻击最大范围")
                .defineInRange("loliPickaxeMaxKillFacingRange", 200, 0, Integer.MAX_VALUE);
        this.loliPickaxeMaxKillFacingSlope = builder
                .comment("范围攻击最大斜率")
                .defineInRange("loliPickaxeMaxKillFacingSlope", 1.0, 0.0, Double.MAX_VALUE);
        this.loliPickaxeMaxKillRange = builder
                .comment("潜行右键杀死周围实体的最大范围")
                .defineInRange("loliPickaxeMaxKillRange", 100, 0, Integer.MAX_VALUE);
        this.loliPickaxeMaxPage = builder
                .comment("储藏室最大页数")
                .defineInRange("loliPickaxeMaxPage", 100, 1,  Integer.MAX_VALUE);
        this.loliPickaxeMaxRange = builder
                .comment("最大采掘范围")
                .defineInRange("loliPickaxeMaxRange", 5, 0, Integer.MAX_VALUE);
        this.loliPickaxeMaxTeleportDistance = builder
                .comment("传送最远距离")
                .defineInRange("loliPickaxeMaxTeleportDistance", 512.0, 0.0, Double.MAX_VALUE);
        this.loliPickaxePotionDefaultLimit = builder
                .comment("默认药水最大等级")
                .defineInRange("loliPickaxePotionDefaultLimit", 32, 0, Integer.MAX_VALUE);
        this.loliPickaxePotionLimit = builder
                .comment("药水最大等级列表")
                .defineList("loliPickaxePotionLimit", List.of(), List::of, obj -> obj instanceof String);
        this.loliPickaxeReincarnation = builder
                .comment("伊邪那美(需同时开启踢出玩家)")
                .define("loliPickaxeReincarnation", false);
        this.loliPickaxeReincarnationPlayerList = builder
                .comment("伊邪那美玩家列表")
                .defineList("loliPickaxeReincarnationPlayerList", List.of(), List::of, obj -> obj instanceof String);
        this.loliPickaxeShowInvisible = builder
                .comment("显示隐身生物")
                .define("loliPickaxeShowInvisible", true);
        this.loliPickaxeSlotStackLimit = builder
                .comment("储藏室最大堆叠数")
                .defineInRange("loliPickaxeSlotStackLimit", 2000000000, 1, Integer.MAX_VALUE);
        this.loliPickaxeSpaceFolding = builder
                .comment("跨世界传送")
                .define("loliPickaxeSpaceFolding", true);
        this.loliPickaxeStopOnLiquid = builder
                .comment("显示流体边框")
                .define("loliPickaxeStopOnLiquid", false);
        this.loliPickaxeThorns = builder
                .comment("反伤")
                .define("loliPickaxeThorns", true);
        this.loliPickaxeTriggerBreakEvent = builder
                .comment("触发方块破坏事件")
                .define("loliPickaxeTriggerBreakEvent", true);
        this.loliPickaxeValidToAllEntity = builder
                .comment("对全部实体有效")
                .define("loliPickaxeValidToAllEntity", false);
        this.loliPickaxeValidToAmityEntity = builder
                .comment("范围攻击对非怪物有效")
                .define("loliPickaxeValidToAmityEntity", true);
        this.loliPickaxeWorldBlacklist = builder
                .comment("跨世界传送黑名单")
                .defineList("loliPickaxeWorldBlacklist", List.of(), List::of,  obj -> obj instanceof String);
        this.loliRecordNames = builder
                .comment("额外唱片列表(声音:唱片名:唱片ID)")
                .defineList("loliRecordNames", List.of("lolirecord:loliRecord:loli_record"), () -> List.of("lolirecord:loliRecord:loli_record"), obj -> obj instanceof String);
        this.loliRecordDropProbability = builder
                .comment("萝莉唱片掉落概率")
                .defineInRange("loliRecordDropProbability", 0.001, 0.001, Double.MAX_VALUE);
        this.loliSpeed = builder
                .comment("萝莉行走速度")
                .defineInRange("loliSpeed", 1.0, 0.0, Double.MAX_VALUE);
        this.loliTeleport = builder
                .comment("萝莉瞬移")
                .define("loliTeleport", true);

        builder.pop();
    }

    public static Optional<Object> getConfigValue(String configId) {
        try {
            Field field = ModConfigs.class.getDeclaredField(configId);
            field.setAccessible(true);
            ModConfigSpec.ConfigValue<?> configValue = (ModConfigSpec.ConfigValue<?>) field.get(INSTANCE);
            return Optional.of(configValue.get());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return Optional.empty();
        }
    }

    public static Object getConfigValue(String configId, Object defaultValue) {
        return getConfigValue(configId).orElse(defaultValue);
    }

    @SuppressWarnings("unchecked")
    public static <T> Optional<T> getConfigValue(String configId, Class<T> type) {
        try {
            Field field = ModConfigs.class.getDeclaredField(configId);
            field.setAccessible(true);
            ModConfigSpec.ConfigValue<?> configValue = (ModConfigSpec.ConfigValue<?>) field.get(INSTANCE);
            Object value = configValue.get();

            if (type.isInstance(value)) {
                return Optional.of((T) value);
            }
            return Optional.empty();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return Optional.empty();
        }
    }

    public static boolean getBoolean(String configId) {
        return getConfigValue(configId, Boolean.class).orElse(false);
    }

    public static int getInt(String configId) {
        return getConfigValue(configId, Integer.class).orElse(0);
    }

    public static double getDouble(String configId) {
        return getConfigValue(configId, Double.class).orElse(0.0);
    }

    public static String getString(String configId) {
        return getConfigValue(configId, String.class).orElse("");
    }

    public static List<?> getList(String configId) {
        return getConfigValue(configId, List.class).orElse(List.of());
    }

    public static boolean hasConfig(String configId) {
        try {
            Field field = ModConfigs.class.getDeclaredField(configId);
            return field.getType().equals(ModConfigSpec.ConfigValue.class);
        } catch (NoSuchFieldException e) {
            return false;
        }
    }

    public static List<String> getAvailableConfigIds() {
        List<String> configIds = new java.util.ArrayList<>();
        Field[] fields = ModConfigs.class.getDeclaredFields();

        for (Field field : fields) {
            if (field.getType().equals(ModConfigSpec.ConfigValue.class)) {
                configIds.add(field.getName());
            }
        }

        return configIds;
    }
}
