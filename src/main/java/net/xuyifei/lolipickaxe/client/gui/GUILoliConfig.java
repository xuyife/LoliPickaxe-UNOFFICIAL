package net.xuyifei.lolipickaxe.client.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.xuyifei.lolipickaxe.LoliPickaxe;
import net.xuyifei.lolipickaxe.common.config.ConfigLoader;
import net.xuyifei.lolipickaxe.common.config.annotation.ConfigField;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@OnlyIn(Dist.CLIENT)
public class GUILoliConfig extends Screen {
    private static final ResourceLocation LOLI_PICKAXE_CONFIG_GUI_TEXTURE = ResourceLocation.fromNamespaceAndPath(LoliPickaxe.MODID, "textures/gui/loli_pickaxe_config.png");

    private ItemStack stack;
    private Button done;
    private Button pre;
    private Button next;
    private Button booleanValue;
    private EditBox otherValue;
    private int curPage;
    private Map<String, Object> pendingChanges = new HashMap<>();

    public GUILoliConfig(Component title, ItemStack stack) {
        super(title);
        this.stack = stack.copy();
        curPage = 0;
    }

    @Override
    protected void init() {
        super.init();

        done = addWidget(Button.builder(CommonComponents.GUI_DONE, button -> {
            savePendingChanges();
            Objects.requireNonNull(minecraft).setScreen(null);
        }).bounds(width / 2 - 100, height / 2 + 20, 200, 20).build());

        pre = addWidget(Button.builder(Component.literal("<"), button -> {
            saveCurrentPageToPending();
            if (--curPage < 0) {
                curPage = ConfigLoader.guiFlags.size() - 1;
            }
            changePage();
        }).bounds(width / 2 - 100, height / 2 - 40, 20, 20).build());

        next = addWidget(Button.builder(Component.literal(">"), button -> {
            saveCurrentPageToPending();
            if (++curPage >= ConfigLoader.guiFlags.size()) {
                curPage = 0;
            }
            changePage();
        }).bounds(width / 2 + 80, height / 2 - 40, 20, 20).build());

        booleanValue = addWidget(Button.builder(Component.literal("false"), button -> {
            button.setMessage(Component.literal(button.getMessage().getString().equals("false") ? "true" : "false"));
            if (curPage >= 0 && curPage < ConfigLoader.guiFlags.size()) {
                String flag = ConfigLoader.guiFlags.get(curPage);
                pendingChanges.put(flag, button.getMessage().getString().equals("true"));
            }
        }).bounds(width / 2 - 40, height / 2 - 10, 80, 20).build());

        otherValue = new EditBox(font, width / 2 - 80, height / 2 - 10, 160, 20, Component.empty());
        otherValue.setMaxLength(100);
        otherValue.setResponder(text -> {
            if (curPage >= 0 && curPage < ConfigLoader.guiFlags.size()) {
                String flag = ConfigLoader.guiFlags.get(curPage);
                ConfigField annotations = ConfigLoader.flagAnnotations.get(flag);
                recordPendingChange(flag, text, annotations.valueType());
            }
        });
        addWidget(otherValue);

        changePage();
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTicks);

        guiGraphics.blit(LOLI_PICKAXE_CONFIG_GUI_TEXTURE, (width - 220) / 2, (height - 140) / 2, 0, 0, 220, 120);

        if (curPage >= 0 && curPage < ConfigLoader.guiFlags.size()) {
            String flag = ConfigLoader.guiFlags.get(curPage);
            String comment = ConfigLoader.flagAnnotations.get(flag).comment();
            guiGraphics.drawCenteredString(font, comment, width / 2, height / 2 - 60, 0xFFFFFF);
        }

        done.render(guiGraphics, mouseX, mouseY, partialTicks);
        pre.render(guiGraphics, mouseX, mouseY, partialTicks);
        next.render(guiGraphics, mouseX, mouseY, partialTicks);
        booleanValue.render(guiGraphics, mouseX, mouseY, partialTicks);
        otherValue.render(guiGraphics, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (otherValue.keyPressed(keyCode, scanCode, modifiers) || otherValue.isFocused()) {
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        otherValue.mouseClicked(mouseX, mouseY, button);
        return super.mouseClicked(mouseX, mouseY, button);
    }

    private void changePage() {
        if (curPage >= 0 && curPage < ConfigLoader.guiFlags.size()) {
            String flag = ConfigLoader.guiFlags.get(curPage);
            ConfigField annotations = ConfigLoader.flagAnnotations.get(flag);

            if (pendingChanges.containsKey(flag)) {
                Object pendingValue = pendingChanges.get(flag);
                updateUIWithValue(annotations.valueType(), pendingValue);
            } else {
                switch (annotations.valueType()) {
                    case INT:
                        otherValue.setValue(String.valueOf(ConfigLoader.getInt(stack, flag)));
                        break;
                    case DOUBLE:
                        otherValue.setValue(String.valueOf(ConfigLoader.getDouble(stack, flag)));
                        break;
                    case STRING:
                        otherValue.setValue(ConfigLoader.getString(stack, flag));
                        break;
                    case BOOLEAN:
                        booleanValue.setMessage(ConfigLoader.getBoolean(stack, flag) ? Component.literal("true") : Component.literal("false"));
                        break;
                    default:
                        break;
                }
            }
            updateUIVisibility(annotations.valueType());
        }
    }

    private void saveCurrentPageToPending() {
        if (curPage >= 0 && curPage < ConfigLoader.guiFlags.size()) {
            String flag = ConfigLoader.guiFlags.get(curPage);
            ConfigField annotations = ConfigLoader.flagAnnotations.get(flag);

            if (annotations.valueType() != ConfigField.ValueType.BOOLEAN) {
                recordPendingChange(flag, otherValue.getValue(), annotations.valueType());
            }
        }
    }

    private void recordPendingChange(String flag, String value, ConfigField.ValueType valueType) {
        try {
            switch (valueType) {
                case INT:
                    pendingChanges.put(flag, Integer.parseInt(value));
                    break;
                case DOUBLE:
                    pendingChanges.put(flag, Double.parseDouble(value));
                    break;
                case STRING:
                    pendingChanges.put(flag, value);
                    break;
                default:
                    break;
            }
        } catch (NumberFormatException e) {
            pendingChanges.remove(flag);
        }
    }

    private void savePendingChanges() {
        if (!pendingChanges.isEmpty()) {
            for (Map.Entry<String, Object> entry : pendingChanges.entrySet()) {
                String flag = entry.getKey();
                Object value = entry.getValue();
                ConfigField annotations = ConfigLoader.flagAnnotations.get(flag);

                if (annotations != null) {
                    switch (annotations.valueType()) {
                        case INT:
                            if (!Objects.equals(annotations.intMaxValueField(), "")) {
                                if ((Integer) value > ConfigLoader.getDouble(stack, annotations.intMaxValueField())) {
                                    value = ConfigLoader.getInt(stack, annotations.intMaxValueField());
                                }
                            }
                            if (!Objects.equals(annotations.intMinValueField(), "")) {
                                if ((Integer) value < ConfigLoader.getDouble(stack, annotations.intMinValueField())) {
                                    value = ConfigLoader.getInt(stack, annotations.intMinValueField());
                                }
                            }
                            ConfigLoader.setInt(stack, flag, (Integer) value);
                            break;
                        case DOUBLE:
                            if (!Objects.equals(annotations.doubleMaxValueField(), "")) {
                                if ((Double) value > ConfigLoader.getDouble(stack, annotations.doubleMaxValueField())) {
                                    value = ConfigLoader.getDouble(stack, annotations.doubleMaxValueField());
                                }
                            }
                            if (!Objects.equals(annotations.doubleMinValueField(), "")) {
                                if ((Double) value < ConfigLoader.getDouble(stack, annotations.doubleMinValueField())) {
                                    value = ConfigLoader.getDouble(stack, annotations.doubleMinValueField());
                                }
                            }
                            ConfigLoader.setDouble(stack, flag, (Double) value);
                            break;
                        case STRING:
                            ConfigLoader.setString(stack, flag, (String) value);
                            break;
                        case BOOLEAN:
                            ConfigLoader.setBoolean(stack, flag, (Boolean) value);
                            break;
                        default:
                            break;
                    }
                }
            }
            pendingChanges.clear();
        }
    }

    private void updateUIWithValue(ConfigField.ValueType valueType, Object value) {
        switch (valueType) {
            case INT, DOUBLE:
                otherValue.setValue(String.valueOf(value));
                break;
            case STRING:
                otherValue.setValue((String) value);
                break;
            case BOOLEAN:
                booleanValue.setMessage((Boolean) value ? Component.literal("true") : Component.literal("false"));
                break;
            default:
                break;
        }
    }

    private void updateUIVisibility(ConfigField.ValueType valueType) {
        if (valueType == ConfigField.ValueType.BOOLEAN) {
            booleanValue.visible = true;
            booleanValue.active = true;
            otherValue.visible = false;
            otherValue.setEditable(false);
        } else {
            booleanValue.visible = false;
            booleanValue.active = false;
            otherValue.visible = true;
            otherValue.setEditable(true);
        }
    }
}
