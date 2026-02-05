package net.xuyifei.lolipickaxe.client.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.xuyifei.lolipickaxe.LoliPickaxe;
import net.xuyifei.lolipickaxe.common.network.ServerboundUpdateLoliCardOnlinePacket;
import org.jetbrains.annotations.NotNull;

public class GUILoliCardOnlineConfig extends Screen {
    private static final ResourceLocation LOLI_CARD_ONLINE_CONFIG_GUI_TEXTURE = ResourceLocation.fromNamespaceAndPath(LoliPickaxe.MODID, "textures/gui/loli_card_online_config.png");

    private String url;
    private EditBox urlField;
    private Button done;

    public GUILoliCardOnlineConfig(String url) {
        super(Component.literal("Loli Card Online Config"));
        this.url = url;
    }

    @Override
    public void init() {
        urlField = new EditBox(this.font, width / 2 - 80, height / 2 - 20, 160, 20, Component.literal("输入URL"));
        urlField.setMaxLength(500);
        if (this.url != null) {
            urlField.setValue(url);
        }
        urlField.setFocused(true);
        urlField.moveCursorToEnd(false);
        urlField = addWidget(urlField);

        done = addWidget(Button.builder(Component.translatable("gui.done"), button -> {
            minecraft.player.connection.send(new ServerboundUpdateLoliCardOnlinePacket(urlField.getValue()));
            minecraft.setScreen(null);
        }).bounds(width / 2 - 100, height / 2 + 10, 200, 20).build());
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
        if (super.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        }
        return urlField.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        if (super.mouseClicked(mouseX, mouseY, mouseButton)) {
            return true;
        }
        return urlField.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.renderBackground(guiGraphics, mouseX, mouseY, partialTicks);

        guiGraphics.blit(LOLI_CARD_ONLINE_CONFIG_GUI_TEXTURE, (width - 220) / 2, (height - 100) / 2, 0, 0, 220, 120);
        guiGraphics.drawCenteredString(this.font, Component.translatable("gui.lolipickaxe.loli_card_online_config.title"), this.width / 2, height / 2 - 40, 16777215);
        urlField.render(guiGraphics, mouseX, mouseY, partialTicks);
        done.render(guiGraphics, mouseX, mouseY, partialTicks);
    }
}
