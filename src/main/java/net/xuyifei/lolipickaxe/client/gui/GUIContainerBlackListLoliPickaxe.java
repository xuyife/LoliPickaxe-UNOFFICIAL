package net.xuyifei.lolipickaxe.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.xuyifei.lolipickaxe.LoliPickaxe;
import net.xuyifei.lolipickaxe.common.gui.ContainerBlackListLoliPickaxe;
import org.jetbrains.annotations.NotNull;

public class GUIContainerBlackListLoliPickaxe extends AbstractContainerScreen<ContainerBlackListLoliPickaxe> {
    private static final ResourceLocation LOLI_PICKAXE_CONTAINER_BLACKLIST_GUI_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(LoliPickaxe.MODID, "textures/gui/container/loli_pickaxe_container_blacklist.png");

    public GUIContainerBlackListLoliPickaxe(ContainerBlackListLoliPickaxe menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageHeight = 256;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        guiGraphics.blit(LOLI_PICKAXE_CONTAINER_BLACKLIST_GUI_TEXTURE, x, y, 0, 0, imageWidth, imageHeight);
    }

    @Override
    protected void renderLabels(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
    }
}
