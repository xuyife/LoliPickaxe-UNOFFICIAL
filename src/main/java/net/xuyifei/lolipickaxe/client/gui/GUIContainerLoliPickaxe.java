package net.xuyifei.lolipickaxe.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.xuyifei.lolipickaxe.LoliPickaxe;
import net.xuyifei.lolipickaxe.common.gui.ContainerLoliPickaxe;
import net.xuyifei.lolipickaxe.common.network.ServerboundLoliPickaxeContainerPacket;
import org.jetbrains.annotations.NotNull;

public class GUIContainerLoliPickaxe extends AbstractContainerScreen<ContainerLoliPickaxe> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(LoliPickaxe.MODID, "textures/gui/container/loli_pickaxe_container.png");

    private Button previousButton;
    private Button nextButton;

    public GUIContainerLoliPickaxe(ContainerLoliPickaxe menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 240;
        this.imageHeight = 256;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void init() {
        super.init();

        previousButton = Button.builder(Component.literal("<"), button -> {
            menu.prePage();
            if (Minecraft.getInstance().player != null) {
                Minecraft.getInstance().player.connection.send(new ServerboundLoliPickaxeContainerPacket(false));
            }
        }).bounds(leftPos + 173, topPos + 22, 20, 20).build();

        nextButton = Button.builder(Component.literal(">"), button -> {
            menu.nextPage();
            if (Minecraft.getInstance().player != null) {
                Minecraft.getInstance().player.connection.send(new ServerboundLoliPickaxeContainerPacket(true));
            }
        }).bounds(leftPos + 213, topPos + 22, 20, 20).build();

        addRenderableWidget(previousButton);
        addRenderableWidget(nextButton);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        guiGraphics.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, 173, 8, 0x404040, false);
        String pageText = String.valueOf(menu.getCurrentPage() + 1);
        int textWidth = this.font.width(pageText);
        guiGraphics.drawString(this.font, pageText, 203 - textWidth / 2, 27, 0x404040, false);
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
