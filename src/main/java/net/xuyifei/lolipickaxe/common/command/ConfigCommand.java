package net.xuyifei.lolipickaxe.common.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.xuyifei.lolipickaxe.common.config.ConfigLoader;
import net.xuyifei.lolipickaxe.common.config.annotation.ConfigField;
import net.xuyifei.lolipickaxe.common.registry.ModConfigs;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ConfigCommand {
    private static final List<String> CONFIG_FIELDS = ConfigLoader.commandFlags;
    private static final SuggestionProvider<CommandSourceStack> CONFIG_SUGGESTIONS = (context, builder) -> SharedSuggestionProvider.suggest(CONFIG_FIELDS, builder);

    private static final SimpleCommandExceptionType CONFIG_FIELD_NOT_FOUND = new SimpleCommandExceptionType(
            Component.literal("配置字段未找到")
    );
    private static final SimpleCommandExceptionType CONFIG_SET_ERROR = new SimpleCommandExceptionType(
            Component.literal("设置配置时发生错误")
    );
    private static final SimpleCommandExceptionType INVALID_VALUE_TYPE = new SimpleCommandExceptionType(
            Component.literal("无效的值类型")
    );

    public static LiteralArgumentBuilder<CommandSourceStack> register() {
        return Commands.literal("loli")
                .then(Commands.literal("reload")
                        .executes(ConfigCommand::reloadConfig))
                .then(Commands.literal("listFlag")
                        .executes(context -> listFlags(context, 1))
                        .then(Commands.argument("page", IntegerArgumentType.integer(1))
                                .executes(context -> listFlags(context, IntegerArgumentType.getInteger(context, "page")))))
                .then(Commands.literal("listValue")
                        .executes(context -> listValues(context, 1))
                        .then(Commands.argument("page", IntegerArgumentType.integer(1))
                                .executes(context -> listValues(context, IntegerArgumentType.getInteger(context, "page")))))
                .then(Commands.argument("flag", StringArgumentType.string())
                        .suggests(CONFIG_SUGGESTIONS)
                        .executes(context -> getConfigValue(context, StringArgumentType.getString(context, "flag")))
                        .then(Commands.argument("value", StringArgumentType.greedyString())
                                .executes(context -> setConfigValue(
                                        context,
                                        StringArgumentType.getString(context, "flag"),
                                        StringArgumentType.getString(context, "value")
                                ))));
    }

    private static int reloadConfig(CommandContext<CommandSourceStack> context) {
        context.getSource().sendSuccess(() -> Component.translatable("commands.loli.reload"), true);
        return Command.SINGLE_SUCCESS;
    }

    private static int getConfigValue(CommandContext<CommandSourceStack> context, String flag) throws CommandSyntaxException {
        try {
            Field field = ModConfigs.class.getDeclaredField(flag);
            field.setAccessible(true);
            ModConfigSpec.ConfigValue<?> configValue = (ModConfigSpec.ConfigValue<?>) field.get(ModConfigs.INSTANCE);

            Object value = configValue.get();
            Component flagText = Component.literal(flag).withStyle(ChatFormatting.AQUA);
            Component valueText = Component.literal(String.valueOf(value)).withStyle(ChatFormatting.RED);

            context.getSource().sendSuccess(() -> Component.translatable("commands.loli.get", flagText, valueText), false);
            return Command.SINGLE_SUCCESS;
        } catch (NumberFormatException e) {
            throw INVALID_VALUE_TYPE.create();
        } catch (NoSuchFieldException e) {
            throw CONFIG_FIELD_NOT_FOUND.create();
        } catch (IllegalAccessException e) {
            throw CONFIG_SET_ERROR.create();
        }
    }

    private static int setConfigValue(CommandContext<CommandSourceStack> context, String flag, String value) throws CommandSyntaxException {
        try {
            Field field = ModConfigs.class.getDeclaredField(flag);
            field.setAccessible(true);
            ModConfigSpec.ConfigValue<?> configValue = (ModConfigSpec.ConfigValue<?>) field.get(ModConfigs.INSTANCE);

            Object currentValue = configValue.get();
            Object convertedValue = convertValue(value, currentValue);

            convertedValue = checkValue(convertedValue, flag, context);
            setConfigValueWithType(configValue, convertedValue);

            Component flagText = Component.literal(flag).withStyle(ChatFormatting.AQUA);
            Component valueText = Component.literal(String.valueOf(convertedValue)).withStyle(ChatFormatting.RED);

            context.getSource().sendSuccess(() -> Component.translatable("commands.loli.set", flagText, valueText), true);
            return Command.SINGLE_SUCCESS;
        } catch (NumberFormatException e) {
            throw INVALID_VALUE_TYPE.create();
        } catch (NoSuchFieldException e) {
            throw CONFIG_FIELD_NOT_FOUND.create();
        } catch (IllegalAccessException e) {
            throw CONFIG_SET_ERROR.create();
        }
    }

    @SuppressWarnings("unchecked")
    private static void setConfigValueWithType(ModConfigSpec.ConfigValue<?> configValue, Object value) {
        ModConfigSpec.ConfigValue<Object> typedConfigValue = (ModConfigSpec.ConfigValue<Object>) configValue;
        typedConfigValue.set(value);
        typedConfigValue.save();
    }

    private static Object checkValue(Object value, String flag, CommandContext<CommandSourceStack> context) {
        ConfigField annotations = ConfigLoader.flagAnnotations.get(flag);
        if (annotations != null) {
            if (value instanceof Integer) {
                if (!Objects.equals(annotations.intMaxValueField(), "")) {
                    if ((Integer) value > ModConfigs.getInt(annotations.intMaxValueField())) {
                        int oldValue = (Integer) value;
                        value = ModConfigs.getInt(annotations.intMaxValueField());
                        context.getSource().sendSystemMessage(Component.literal("值" + oldValue + "太大了，将设置为" + value).withStyle(ChatFormatting.RED));
                    }
                }
                if (!Objects.equals(annotations.intMinValueField(), "")) {
                    if ((Integer) value < ModConfigs.getInt(annotations.intMinValueField())) {
                        int oldValue = (Integer) value;
                        value = ModConfigs.getInt(annotations.intMinValueField());
                        context.getSource().sendSystemMessage(Component.literal("值" + oldValue + "太小了，将设置为" + value).withStyle(ChatFormatting.RED));
                    }
                }
                return value;
            } else if (value instanceof Double) {
                if (!Objects.equals(annotations.doubleMaxValueField(), "")) {
                    if ((Double) value > ModConfigs.getDouble(annotations.doubleMaxValueField())) {
                        double oldValue = (Double) value;
                        value = ModConfigs.getDouble(annotations.doubleMaxValueField());
                        context.getSource().sendSystemMessage(Component.literal("值" + oldValue + "太大了，将设置为" + value).withStyle(ChatFormatting.RED));
                    }
                }
                if (!Objects.equals(annotations.doubleMinValueField(), "")) {
                    if ((Double) value < ModConfigs.getDouble(annotations.doubleMinValueField())) {
                        double oldValue = (Double) value;
                        value = ModConfigs.getDouble(annotations.doubleMinValueField());
                        context.getSource().sendSystemMessage(Component.literal("值" + oldValue + "太小了，将设置为" + value).withStyle(ChatFormatting.RED));
                    }
                }
                return value;
            }
        }
        return value;
    }

    private static Object convertValue(String stringValue, Object currentValue) {
        if (currentValue instanceof Boolean) {
            return Boolean.parseBoolean(stringValue);
        } else if (currentValue instanceof Integer) {
            return Integer.parseInt(stringValue);
        } else if (currentValue instanceof Double) {
            return Double.parseDouble(stringValue);
        } else if (currentValue instanceof String) {
            return stringValue;
        } else if (currentValue instanceof List) {
            return Arrays.asList(stringValue.split(","));
        }
        return stringValue;
    }

    private static int listFlags(CommandContext<CommandSourceStack> context, int page) {
        int maxPage = (CONFIG_FIELDS.size() - 1) / 18 + 1;
        page = Mth.clamp(page, 1, maxPage);

        CommandSourceStack source = context.getSource();
        int finalPage = page;
        source.sendSuccess(() -> Component.translatable("------------------------%1$s/%2$s--------------------------", finalPage, maxPage).withStyle(ChatFormatting.GREEN), false);

        for (int i = (page - 1) * 18; i < page * 18; i++) {
            if (i < CONFIG_FIELDS.size()) {
                String flag = CONFIG_FIELDS.get(i);
                Component flagText = Component.literal(flag)
                        .withStyle(ChatFormatting.AQUA)
                        .withStyle(style -> style.withClickEvent(
                                new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/loli " + flag + " ")
                        ));
                Component commentText = Component.literal(ConfigLoader.flagAnnotations.get(flag).comment()).withStyle(ChatFormatting.LIGHT_PURPLE);
                source.sendSuccess(() -> Component.translatable("commands.loli.list", flagText, commentText), false);
            } else {
                source.sendSuccess(() -> Component.literal(""), false);
            }
        }

        sendPagination(source, page, maxPage, "listFlag");
        return Command.SINGLE_SUCCESS;
    }

    private static int listValues(CommandContext<CommandSourceStack> context, int page) {
        int maxPage = (CONFIG_FIELDS.size() - 1) / 18 + 1;
        page = Mth.clamp(page, 1, maxPage);

        CommandSourceStack source = context.getSource();
        int finalPage = page;
        source.sendSuccess(() -> Component.translatable("------------------------%1$s/%2$s--------------------------", finalPage, maxPage).withStyle(ChatFormatting.GREEN), false);

        for (int i = (page - 1) * 18; i < page * 18; i++) {
            if (i < CONFIG_FIELDS.size()) {
                String flag = CONFIG_FIELDS.get(i);
                try {
                    Field field = ModConfigs.class.getDeclaredField(flag);
                    field.setAccessible(true);
                    ModConfigSpec.ConfigValue<?> configValue = (ModConfigSpec.ConfigValue<?>) field.get(ModConfigs.INSTANCE);
                    Object value = configValue.get();

                    Component flagText = Component.literal(flag)
                            .withStyle(ChatFormatting.AQUA)
                            .withStyle(style -> style.withClickEvent(
                                    new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/loli " + flag + " " + value)
                            ));
                    Component valueText = Component.literal(String.valueOf(value)).withStyle(ChatFormatting.RED);

                    source.sendSuccess(() -> Component.translatable("commands.loli.get", flagText, valueText), false);
                } catch (Exception e) {
                    source.sendSuccess(() -> Component.literal("Error: " + flag).withStyle(ChatFormatting.RED), false);
                }
            } else {
                source.sendSuccess(() -> Component.literal(""), false);
            }
        }

        sendPagination(source, page, maxPage, "listValue");
        return Command.SINGLE_SUCCESS;
    }

    private static void sendPagination(CommandSourceStack source, int page, int maxPage, String command) {
        Component preButton = Component.translatable("commands.page.button.pre");
        if (page > 1) {
            preButton = preButton.copy().withStyle(style -> style
                    .withColor(ChatFormatting.GOLD)
                    .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/loli " + command + " " + (page - 1)))
            );
        } else {
            preButton = preButton.copy().withStyle(ChatFormatting.GRAY);
        }

        Component nextButton = Component.translatable("commands.page.button.next");
        if (page < maxPage) {
            nextButton = nextButton.copy().withStyle(style -> style
                    .withColor(ChatFormatting.GOLD)
                    .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/loli " + command + " " + (page + 1)))
            );
        } else {
            nextButton = nextButton.copy().withStyle(ChatFormatting.GRAY);
        }

        Component bottom = Component.translatable("----------------------%1$s/%2$s---------------------", preButton, nextButton)
                .withStyle(ChatFormatting.GREEN);

        source.sendSuccess(() -> bottom, false);
    }
}
