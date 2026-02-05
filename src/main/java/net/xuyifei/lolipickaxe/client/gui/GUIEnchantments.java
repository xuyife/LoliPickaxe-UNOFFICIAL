package net.xuyifei.lolipickaxe.client.gui;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.xuyifei.lolipickaxe.client.gui.assembly.GUILoliList;
import net.xuyifei.lolipickaxe.common.network.ServerboundSetSlotPacket;
import net.xuyifei.lolipickaxe.common.util.RomanNumberUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class GUIEnchantments extends Screen {
    private ItemStack stack;
    private Button done;
    private Button add;
    private Button remove;
    private EditBox level;
    private GUILoliList enchantmentList;
    private GUILoliList selectEnchantmentList;
    private List<Holder.Reference<Enchantment>> enchantments;
    private List<LoliEntry> selectEnchantments;

    public GUIEnchantments(Component title, ItemStack stack) {
        super(title);
        this.stack = stack.copy();
    }

    @Override
    protected void init() {
        super.init();

        this.done = Button.builder(Component.translatable("gui.done"), button -> onDone())
                .bounds(this.width / 2 - 160, this.height / 2 + 95, 320, 20)
                .build();

        this.add = Button.builder(Component.literal("添加附魔"), button -> onAdd())
                .bounds(this.width / 2 + 60, this.height / 2 - 95, 100, 20)
                .build();

        this.remove = Button.builder(Component.literal("移除附魔"), button -> onRemove())
                .bounds(this.width / 2 + 60, this.height / 2 - 65, 100, 20)
                .build();

        this.addRenderableWidget(done);
        this.addRenderableWidget(add);
        this.addRenderableWidget(remove);

        this.level = new EditBox(this.font, this.width / 2 + 60, this.height / 2 - 35, 100, 20,
                Component.literal("附魔等级"));
        this.level.setValue("1");
        this.addRenderableWidget(level);

        if (this.minecraft != null && this.minecraft.level != null) {
            this.enchantments = this.minecraft.level.registryAccess()
                    .registryOrThrow(Registries.ENCHANTMENT)
                    .holders()
                    .toList();
        } else {
            this.enchantments = List.of();
        }

        this.enchantmentList = new GUILoliList(
                Minecraft.getInstance(), this.width / 2 - 160, this.height / 2 - 115,
                100, 200, enchantments.size(), 75, 15) {
            @Override
            public String getElementName(int index) {
                String name;
                Enchantment enchantment = minecraft.player.level().registryAccess().registryOrThrow(Registries.ENCHANTMENT).get(ResourceLocation.parse(enchantments.get(index).getRegisteredName()));
                name = enchantment != null ? I18n.get(enchantment.description().getString()) : "Unknow";
                if (ResourceLocation.parse(enchantments.get(index).getRegisteredName()).getPath().contains("curse")) {
                    name = "§c" + name;
                }
                return name;
            }

            @Override
            public int getElementColor(int index) {
                return 0xFFFFFF;
            }

            @Override
            public void moveElement(int from, int to) {
            }
        };
        this.enchantmentList.selected = 0;

        this.selectEnchantments = Lists.newArrayList();
        ItemEnchantments itemEnchantments = stack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);

        for (Map.Entry<Holder<Enchantment>, Integer> entry : itemEnchantments.entrySet()) {
            selectEnchantments.add(new LoliEntry(entry.getKey(), entry.getValue()));
        }

        this.selectEnchantmentList = new GUILoliList(
                Minecraft.getInstance(), this.width / 2 - 50, this.height / 2 - 115,
                100, 200, selectEnchantments.size(), 75, 15) {

            @Override
            public String getElementName(int index) {
                LoliEntry entry = selectEnchantments.get(index);
                Enchantment ench = entry.enchantment.value();
                int level = entry.level;

                String name = I18n.get(ench.description().getString()) + " " + RomanNumberUtil.toRoman(level);
                if (ResourceLocation.parse(selectEnchantments.get(index).enchantment.getRegisteredName()).getPath().contains("curse")) {
                    name = "§c" + name;
                }
                return name;
            }

            @Override
            public int getElementColor(int index) {
                return 0xFFFFFF;
            }

            @Override
            public void moveElement(int from, int to) {
            }
        };
        this.selectEnchantmentList.selected = 0;

        if (selectEnchantments.isEmpty()) {
            remove.active = false;
        }
    }

    private void onDone() {
        this.onClose();
    }

    private void onAdd() {
        if (enchantmentList.selected >= 0 && enchantmentList.selected < enchantments.size()) {
            Holder<Enchantment> selectedEnchantment = enchantments.get(enchantmentList.selected);

            boolean alreadyExists = selectEnchantments.stream()
                    .anyMatch(entry -> entry.enchantment.equals(selectedEnchantment));

            if (!alreadyExists) {
                int numberLevel;
                try {
                    numberLevel = Integer.parseInt(level.getValue());
                } catch (Exception e) {
                    numberLevel = 1;
                }

                numberLevel = Math.min(numberLevel, 255);

                stack.enchant(selectedEnchantment, numberLevel);
                minecraft.player.connection.send(new ServerboundSetSlotPacket(minecraft.player.getInventory().selected + Inventory.INVENTORY_SIZE, stack));

                selectEnchantments.add(new LoliEntry(selectedEnchantment, numberLevel));
                selectEnchantmentList.add();
                remove.active = true;
            }
        }
    }

    private void onRemove() {
        if (selectEnchantmentList.selected >= 0 && selectEnchantmentList.selected < selectEnchantments.size()) {
            EnchantmentHelper.updateEnchantments(stack, mutable -> mutable.set(selectEnchantments.get(selectEnchantmentList.selected).enchantment, 0));
            minecraft.player.connection.send(new ServerboundSetSlotPacket(minecraft.player.getInventory().selected + Inventory.INVENTORY_SIZE, stack));
            selectEnchantments.remove(selectEnchantmentList.selected);

            selectEnchantmentList.remove();

            if (selectEnchantmentList.selected >= selectEnchantments.size()) {
                selectEnchantmentList.selected = Math.max(0, selectEnchantments.size() - 1);
            }

            remove.active = !selectEnchantments.isEmpty();
        }
    }

    @Override
    public void onClose() {
        super.onClose();
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == InputConstants.KEY_ESCAPE) {
            this.onClose();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);

        guiGraphics.drawCenteredString(font, title, width / 2, 20, 0xFFFFFF);
        guiGraphics.drawString(font, "可用附魔", width / 2 - 160, height / 2 - 130, 0xFFFFFF, false);
        guiGraphics.drawString(font, "激活附魔", width / 2 - 50, height / 2 - 130, 0xFFFFFF, false);

        enchantmentList.draw(guiGraphics, mouseX, mouseY);
        selectEnchantmentList.draw(guiGraphics, mouseX, mouseY);
    }

    private record LoliEntry(Holder<Enchantment> enchantment, int level) {
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (enchantmentList.mouseClick((int) mouseX, (int) mouseY, button)) return true;
        if (selectEnchantmentList.mouseClick((int) mouseX, (int) mouseY, button)) return true;
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (enchantmentList.mouseScrolled(mouseX, mouseY, scrollY)) return true;
        if (selectEnchantmentList.mouseScrolled(mouseX, mouseY, scrollY)) return true;
        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }
}
