package net.xuyifei.lolipickaxe.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.NeoForge;
import net.xuyifei.lolipickaxe.client.gui.assembly.GUILoliList;
import net.xuyifei.lolipickaxe.common.network.ServerboundPlayerAddEffectPacket;
import net.xuyifei.lolipickaxe.common.network.ServerboundPlayerRemoveEffectPacket;
import net.xuyifei.lolipickaxe.common.util.RomanNumberUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class GUIPotionEffect extends Screen {
    private final ItemStack stack;
    private EditBox levelInput;
    private GUILoliList potionList;
    private GUILoliList activeEffectList;
    private ResourceLocation[] allPotions;
    private List<MobEffectInstance> activeEffects;
    private Button add;
    private Button remove;
    private Button done;

    public GUIPotionEffect(Component title, ItemStack stack) {
        super(title);
        this.stack = stack;
    }

    @Override
    protected void init() {
        super.init();

        this.allPotions = BuiltInRegistries.MOB_EFFECT.keySet().toArray(new ResourceLocation[0]);

        refreshActiveEffects();

        int centerX = width / 2;
        int centerY = height / 2;

        this.potionList = new GUILoliList(
                Minecraft.getInstance(),
                centerX - 160, centerY - 115,
                100, 200,
                allPotions.length, 75, 15
        ) {
            @Override
            public String getElementName(int index) {
                MobEffect effect = BuiltInRegistries.MOB_EFFECT.get(allPotions[index]);
                return effect != null ? I18n.get(effect.getDescriptionId()) : "Unknown";
            }

            @Override
            public int getElementColor(int index) {
                return 0xFFFFFF;
            }

            @Override
            public void moveElement(int from, int to) {
            }
        };
        potionList.selected = 0;

        this.activeEffectList = new GUILoliList(
                Minecraft.getInstance(),
                centerX - 50, centerY - 115,
                100, 200,
                activeEffects.size(), 75, 15
        ) {
            @Override
            public String getElementName(int index) {
                MobEffectInstance effect = activeEffects.get(index);
                MobEffect mobEffect = effect.getEffect().value();
                String name = I18n.get(mobEffect.getDescriptionId());
                String level = RomanNumberUtil.toRoman(effect.getAmplifier() + 1);
                if (effect.getDuration() == -1) {
                    return name + " " + level + " (∞)";
                } else {
                    return name + " " + level + " ("+ effect.getDuration() / 20 + ")";
                }

            }

            @Override
            public int getElementColor(int index) {
                MobEffectInstance effect = activeEffects.get(index);
                MobEffect mobEffect = effect.getEffect().value();
                return mobEffect.getColor();
            }

            @Override
            public void moveElement(int from, int to) {
            }
        };
        activeEffectList.selected = 0;

        this.levelInput = new EditBox(font, centerX + 60, centerY - 95, 100, 20, Component.literal("等级"));
        levelInput.setValue("1");
        levelInput.setFilter(s -> s.matches("\\d*"));
        this.levelInput = addWidget(levelInput);

        this.add = addWidget(Button.builder(Component.literal("添加效果"), button -> addEffect())
                .bounds(centerX + 60, centerY - 35, 100, 20)
                .build());

        this.remove = addWidget(Button.builder(Component.literal("移除选中"), button -> removeEffect())
                .bounds(centerX + 60, centerY - 5, 100, 20)
                .build());

        this.done = addWidget(Button.builder(Component.literal("完成"), button -> onClose())
                .bounds(centerX - 160, centerY + 95, 320, 20)
                .build());
    }

    private void refreshActiveEffects() {
        List<MobEffectInstance> newEffects = new ArrayList<>(minecraft.player.getActiveEffects());

        this.activeEffects = new ArrayList<>(newEffects);

        if (activeEffectList != null) {
            activeEffectList.updateListSize(activeEffects.size());

            activeEffectList.numElements = activeEffects.size();
            if (activeEffectList.selected >= activeEffects.size()) {
                activeEffectList.selected = Math.max(0, activeEffects.size() - 1);
            }
        }
    }

    private void addEffect() {
        try {
            int level = Integer.parseInt(levelInput.getValue());

            if (level < 0) level = 0;
            if (level > 255) level = 255;

            ResourceLocation selectedPotion = allPotions[potionList.selected];
            MobEffect effect = BuiltInRegistries.MOB_EFFECT.get(selectedPotion);

            if (effect != null) {

                MobEffectInstance mobEffectInstance = new MobEffectInstance(Holder.direct(minecraft.player.level().registryAccess().lookup(Registries.MOB_EFFECT)
                        .flatMap(lookup -> lookup.get(ResourceKey.create(Registries.MOB_EFFECT, ResourceLocation.parse(selectedPotion.toString()))))
                        .map(Holder::value)
                        .get())
                        , -1, level - 1, false, false);
                minecraft.player.connection.send(new ServerboundPlayerAddEffectPacket(mobEffectInstance));

                activeEffects.add(mobEffectInstance);
                activeEffectList.add();
            }
        } catch (NumberFormatException e) {
            levelInput.setValue("1");
        }
    }

    private void removeEffect() {
        if (activeEffectList.selected >= 0 && activeEffectList.selected < activeEffects.size()) {
            MobEffectInstance selectedEffect = activeEffects.get(activeEffectList.selected);
            MobEffect effect = selectedEffect.getEffect().value();

            minecraft.player.connection.send(new ServerboundPlayerRemoveEffectPacket(Holder.direct(minecraft.player.level().registryAccess().lookup(Registries.MOB_EFFECT)
                    .flatMap(lookup -> lookup.get(ResourceKey.create(Registries.MOB_EFFECT, BuiltInRegistries.MOB_EFFECT.getKey(effect))))
                    .map(Holder::value)
                    .get())));

            activeEffects.remove(activeEffectList.selected);
            activeEffectList.remove();
        }
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        renderBackground(guiGraphics, mouseX, mouseY, partialTicks);
        refreshActiveEffects();

        guiGraphics.drawCenteredString(font, title, width / 2, 20, 0xFFFFFF);
        guiGraphics.drawString(font, "可用效果", width / 2 - 160, height / 2 - 130, 0xFFFFFF, false);
        guiGraphics.drawString(font, "激活效果", width / 2 - 50, height / 2 - 130, 0xFFFFFF, false);
        guiGraphics.drawString(font, "注：操作后可能更新不及时，可以退出界面后重新进入查看", width / 2 + 60, height / 2 + 60, 0xFFFFFF, false);

        potionList.draw(guiGraphics, mouseX, mouseY);
        activeEffectList.draw(guiGraphics, mouseX, mouseY);
        add.render(guiGraphics, mouseX, mouseY, partialTicks);
        remove.render(guiGraphics, mouseX, mouseY, partialTicks);
        done.render(guiGraphics, mouseX, mouseY, partialTicks);
        levelInput.render(guiGraphics, mouseX, mouseY, partialTicks);
    }

    @Override
    public void onClose() {
        NeoForge.EVENT_BUS.unregister(this);
        super.onClose();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (potionList.mouseClick((int) mouseX, (int) mouseY, button)) return true;
        if (activeEffectList.mouseClick((int) mouseX, (int) mouseY, button)) return true;

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (potionList.mouseScrolled(mouseX, mouseY, scrollY)) return true;
        if (activeEffectList.mouseScrolled(mouseX, mouseY, scrollY)) return true;
        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (potionList.mouseReleased(mouseX, mouseY, button)) return true;
        if (activeEffectList.mouseReleased(mouseX, mouseY, button)) return true;
        return super.mouseReleased(mouseX, mouseY, button);
    }
}