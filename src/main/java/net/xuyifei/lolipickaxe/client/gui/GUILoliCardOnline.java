package net.xuyifei.lolipickaxe.client.gui;

import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.xuyifei.lolipickaxe.common.util.LoliCardOnlineUtil;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.net.URISyntaxException;

public class GUILoliCardOnline extends Screen {
    private final String url;

    private Button back;
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

    public GUILoliCardOnline(String url) {
        super(Component.literal("Loli Card Online"));
        this.url = url;
        this.dcx = 0;
        this.dcy = 0;
        this.ds = 1;
        this.clicked = false;
        this.clickX = 0;
        this.clickY = 0;
    }

    @Override
    public void init() {
        super.init();
        back = addWidget(Button.builder(CommonComponents.GUI_BACK, button -> this.onClose())
                .bounds((this.width - 200) / 2, this.height - 20, 200, 20)
                .build());
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (scrollY == 0) {
            return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
        } else {
            if (ds > 0.1 || scrollY > 0) {
                ds *= scrollY > 0 ? 1.28 : 0.78125;
                dcx += (int) (scrollY > 0 ? ((double) width / 2 + dcx - (int) mouseX) * 0.28 : ((int) mouseX - (double) width / 2 - dcx) * 0.21875);
                dcy += (int) (scrollY > 0 ? ((double) height / 2 + dcy - (int) mouseY) * 0.28 : ((int) mouseY - (double) height / 2 - dcy) * 0.21875);
            }
            return true;
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        if (super.mouseClicked(mouseX, mouseY, mouseButton)) {
            return true;
        }
        if (mouseButton == 0) {
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
    public boolean mouseReleased(double mouseX, double mouseY, int state) {
        if (super.mouseReleased(mouseX, mouseY, state)) {
            return true;
        }
        if (clicked) {
            clicked = false;
            if (!moved) {
                try {
                    URI uri = new URI(url);
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
                                false));
                    } else {
                        openWebLink(uri);
                    }
                } catch (URISyntaxException ignored) {
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
        if (clicked) {
            dcx = odcx + (int) mouseX - clickX;
            dcy = odcy + (int) mouseY - clickY;
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
        if (LoliCardOnlineUtil.isLoad(url)) {
            LoliCardOnlineUtil.bind(url);
            int imageWidth = LoliCardOnlineUtil.getWidth(url);
            int imageHeight = LoliCardOnlineUtil.getHeight(url);
            double ratio = (double) imageWidth / (double) imageHeight;
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

            guiGraphics.blit(LoliCardOnlineUtil.getResourceLocation(url), cx - x, cy - y, 0, 0, x * 2, y * 2, x * 2, y * 2);
        } else {
            LoliCardOnlineUtil.load(url);
        }
        back.render(guiGraphics, mouseX, mouseY, partialTicks);
    }

    @Override
    public void onClose() {
        super.onClose();
        minecraft.setScreen(null);
    }
}
