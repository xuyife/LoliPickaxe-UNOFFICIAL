package net.xuyifei.lolipickaxe.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.xuyifei.lolipickaxe.common.registry.ModConfigs;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

import java.net.URI;
import java.net.URISyntaxException;

public class GUILoliCard extends Screen {
    private final String cardName;
    private final ResourceLocation resource;
    private final int imageWidth;
    private final int imageHeight;
    private final double ratio;
    private Button backButton;

    private int dcx;
    private int dcy;
    private double ds;
    private boolean clicked;
    private boolean moved;
    private int clickX;
    private int clickY;
    private int odcx;
    private int odcy;
    private URI clickedLinkURI;

    public GUILoliCard(String cardName, ResourceLocation resource, int imageWidth, int imageHeight) {
        super(Component.literal("Loli Card"));
        this.cardName = cardName;
        this.resource = resource;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.ratio = (double) imageWidth / (double) imageHeight;
        this.dcx = 0;
        this.dcy = 0;
        this.ds = 1;
        this.clicked = false;
        this.moved = false;
        this.clickX = 0;
        this.clickY = 0;
    }

    @Override
    protected void init() {
        super.init();
        backButton = addWidget(Button.builder(CommonComponents.GUI_BACK, button -> this.onClose())
                .bounds((this.width - 200) / 2, this.height - 20, 200, 20)
                .build());
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (scrollY != 0) {
            if (ds > 0.1 || scrollY > 0) {
                ds *= scrollY > 0 ? 1.28 : 0.78125;
                dcx += (int) (scrollY > 0 ? ((double) width / 2 + dcx - mouseX) * 0.28 : (mouseX - (double) width / 2 - dcx) * 0.21875);
                dcy += (int) (scrollY > 0 ? ((double) height / 2 + dcy - mouseY) * 0.28 : (mouseY - (double) height / 2 - dcy) * 0.21875);
            }
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (super.mouseClicked(mouseX, mouseY, button)) {
            return true;
        }

        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            clicked = true;
            clickX = (int) mouseX;
            clickY = (int) mouseY;
            odcx = dcx;
            odcy = dcy;
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (super.mouseReleased(mouseX, mouseY, button)) {
            return true;
        }

        if (clicked && button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            clicked = false;
            if (!moved) {
                for (int i = 0; i < ModConfigs.getList("loliCardURL").size(); i++) {
                    String[] parts = ((String) ModConfigs.getList("loliCardURL").get(i)).split(":::", 2);
                    if (parts.length == 2) {
                        String before = parts[0];
                        String after = parts[1];
                        if (before.contains(cardName)) {
                            try {
                                URI uri = new URI(after);
                                if (minecraft.options.chatLinks().get()) {
                                    clickedLinkURI = uri;
                                    minecraft.setScreen(new ConfirmLinkScreen(
                                            confirmed -> {
                                                if (confirmed) {
                                                    Util.getPlatform().openUri(uri);
                                                }
                                                clickedLinkURI = null;
                                                minecraft.setScreen(this);
                                            },
                                            Component.translatable("chat.link.confirm"),
                                            uri,
                                            false
                                    ));
                                } else {
                                    openWebLink(uri);
                                }
                            } catch (URISyntaxException ignored) {

                            }
                        }
                    }
                }
            }
            moved = false;
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (super.mouseDragged(mouseX, mouseY, button, dragX, dragY)) {
            return true;
        }

        if (clicked && button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            dcx = odcx + (int) (mouseX - clickX);
            dcy = odcy + (int) (mouseY - clickY);
            moved = true;
            return true;
        }
        return false;
    }

    private void openWebLink(URI url) {
        Util.getPlatform().openUri(url);
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.renderBackground(guiGraphics, mouseX, mouseY, partialTicks);
        RenderSystem.setShaderTexture(0, resource);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        int cx = width / 2 + dcx;
        int cy = height / 2 + dcy;
        double proportion;
        if (ratio < (double) width / (double) height) {
            proportion = (double) height / (double) imageHeight;
        } else {
            proportion = (double) width / (double) imageWidth;
        }
        int x = (int) (imageWidth * proportion / 2 * ds);
        int y = (int) (imageHeight * proportion / 2 * ds);

        guiGraphics.blit(
                resource,
                cx - x, cy - y,
                x * 2, y * 2,
                0, 0,
                imageWidth, imageHeight,
                imageWidth, imageHeight
        );

        backButton.render(guiGraphics, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(null);
    }
}
