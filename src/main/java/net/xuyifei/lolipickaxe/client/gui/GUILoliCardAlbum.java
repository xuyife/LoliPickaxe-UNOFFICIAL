package net.xuyifei.lolipickaxe.client.gui;

import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.xuyifei.lolipickaxe.common.registry.ModConfigs;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class GUILoliCardAlbum extends Screen {
    private final String groupName;
    private final int count;
    private final ResourceLocation[] resources;
    private final int[] imageWidths;
    private final int[] imageHeights;
    private final double[] ratios;
    private Button back;
    private Button pre;
    private Button next;

    private int dcx;
    private int dcy;
    private double ds;
    private boolean clicked;
    private boolean moved;
    private int clickX;
    private int clickY;
    private int odcx;
    private int odcy;
    private int page;
    private URI clickedLinkURI;

    public GUILoliCardAlbum(String groupName, List<ResourceLocation> resourceList, List<Integer> imageWidthList, List<Integer> imageHeightList) {
        super(Component.literal("Loli Card Album - " + groupName));
        this.groupName = groupName;
        this.count = resourceList.size();
        this.resources = new ResourceLocation[this.count];
        this.imageWidths = new int[this.count];
        this.imageHeights = new int[this.count];
        this.ratios = new double[this.count];
        for (int i = 0; i < this.count; i++) {
            this.resources[i] = resourceList.get(i);
            this.imageWidths[i] = imageWidthList.get(i);
            this.imageHeights[i] = imageHeightList.get(i);
            this.ratios[i] = (double) this.imageWidths[i] / (double) this.imageHeights[i];
        }
        this.dcx = 0;
        this.dcy = 0;
        this.ds = 1;
        this.clicked = false;
        this.clickX = 0;
        this.clickY = 0;
        this.page = 0;
    }

    @Override
    protected void init() {
        super.init();

        this.back = this.addRenderableWidget(Button.builder(CommonComponents.GUI_BACK, button -> this.onClose())
                .bounds((this.width - 200) / 2, this.height - 20, 200, 20)
                .build());

        this.pre = this.addRenderableWidget(Button.builder(Component.literal("<"), button -> previousPage())
                .bounds((this.width - 220) / 2, this.height - 20, 20, 20)
                .build());

        this.next = this.addRenderableWidget(Button.builder(Component.literal(">"), button -> nextPage())
                .bounds((this.width + 200) / 2, this.height - 20, 20, 20)
                .build());
    }

    private void previousPage() {
        if (--page < 0) {
            page = count - 1;
        }
    }

    private void nextPage() {
        if (++page >= count) {
            page = 0;
        }
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
                        if (before.contains(groupName)) {
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

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTicks);

        if (count == 0) {
            guiGraphics.drawCenteredString(this.font, Component.literal("No images found"), this.width / 2, this.height / 2, 0xFFFFFF);
        } else {
            renderCurrentImage(guiGraphics);

            String pageInfo = (page + 1) + " / " + count;
            guiGraphics.drawCenteredString(this.font, pageInfo, this.width / 2, 20, 0xFFFFFF);
        }
        back.render(guiGraphics, mouseX, mouseY, partialTicks);
        pre.render(guiGraphics, mouseX, mouseY, partialTicks);
        next.render(guiGraphics, mouseX, mouseY, partialTicks);
    }

    private void renderCurrentImage(GuiGraphics guiGraphics) {
        ResourceLocation currentResource = resources[page];
        int cx = width / 2 + dcx;
        int cy = height / 2 + dcy;

        double proportion;
        if (ratios[page] < (double) width / (double) height) {
            proportion = (double) height / (double) imageHeights[page];
        } else {
            proportion = (double) width / (double) imageWidths[page];
        }

        int x = (int) (imageWidths[page] * proportion / 2 * ds);
        int y = (int) (imageHeights[page] * proportion / 2 * ds);

        guiGraphics.blit(
                currentResource,
                cx - x, cy - y,
                x * 2, y * 2,
                0, 0,
                imageWidths[page], imageHeights[page],
                imageWidths[page], imageHeights[page]
        );
    }

    private void openWebLink(URI url) {
        Util.getPlatform().openUri(url);
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
