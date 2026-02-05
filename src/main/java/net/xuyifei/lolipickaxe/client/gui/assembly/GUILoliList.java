package net.xuyifei.lolipickaxe.client.gui.assembly;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;

public abstract class GUILoliList extends Gui {
    protected int xWidth;
    protected int yHeight;
    public int xPosition;
    public int yPosition;
    public double scroll = 0.0D;
    public int numElements = 0;
    public int elementWidth;
    public int elementHeight;
    public int elementsPerLine;
    public int selected = -1;
    public boolean scrolling;
    public int dragged = -1;
    public int dragYOffset = 0;
    public int dragXOffset = 0;
    public int dragDelay = 0;

    protected final Minecraft minecraft;

    public GUILoliList(Minecraft minecraft, int xPosition, int yPosition, int width, int height,
                       int numElements, int elementWidth, int elementHeight) {
        super(minecraft);
        this.minecraft = minecraft;
        this.xWidth = width;
        this.yHeight = height;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.numElements = numElements;
        this.elementWidth = elementWidth;
        this.elementHeight = elementHeight;
        this.elementsPerLine = Math.max(1, (this.xWidth - 20) / elementWidth);
    }

    public void draw(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.fill(xPosition, yPosition, xPosition + xWidth, yPosition + yHeight, 0x80000000);

        int totalHeight = (int) Math.ceil((double) numElements / elementsPerLine) * elementHeight;
        int scrollOffset = (int) (scroll * Math.max(0, totalHeight - yHeight));

        if (totalHeight > yHeight) {
            int scrollBarHeight = (int) ((float) yHeight * yHeight / totalHeight);
            scrollBarHeight = Math.max(10, Math.min(scrollBarHeight, yHeight - 10));
            int scrollBarY = yPosition + (int) (scroll * (yHeight - scrollBarHeight));
            guiGraphics.fill(xPosition + xWidth - 6, yPosition, xPosition + xWidth, yPosition + yHeight, 0x80000000);
            guiGraphics.fill(xPosition + xWidth - 6, scrollBarY, xPosition + xWidth, scrollBarY + scrollBarHeight, 0xFF808080);
        }

        int startLine = scrollOffset / elementHeight;
        int endLine = startLine + (yHeight / elementHeight) + 2;

        for (int i = startLine * elementsPerLine; i < Math.min(numElements, endLine * elementsPerLine); i++) {
            int line = i / elementsPerLine;
            int column = i % elementsPerLine;
            int x = xPosition + column * elementWidth;
            int y = yPosition + line * elementHeight - scrollOffset;

            if (y + elementHeight >= yPosition && y <= yPosition + yHeight) {
                if (i == selected) {
                    guiGraphics.fill(x, y, x + elementWidth, y + elementHeight, 0x40FFFFFF);
                }

                drawElement(guiGraphics, i, x, y);
            }
        }

        if (dragged >= 0 && dragged < numElements && dragDelay == 0) {
            int line = dragged / elementsPerLine;
            int column = dragged % elementsPerLine;
            int x = mouseX + dragXOffset;
            int y = mouseY + dragYOffset;
            drawElement(guiGraphics, dragged, x, y);
        }
    }

    public void update(int mouseX, int mouseY) {
        if (dragDelay > 0) {
            dragDelay++;
            if (dragDelay > 10) {
                dragDelay = 0;
            }
        }

        if (scrolling) {
            scroll = Math.max(0, Math.min(1, (mouseY - (yPosition + 10.0)) / (yHeight - 20.0)));
        }

        if (dragged >= 0 && dragDelay == 0) {
            if (mouseY < yPosition) {
                scroll = Math.max(0, scroll - 0.05);
            } else if (mouseY > yPosition + yHeight) {
                scroll = Math.min(1, scroll + 0.05);
            }
        }
    }

    public boolean mouseClick(int mouseX, int mouseY, int button) {
        if (button == 0) {
            if (mouseX >= xPosition + xWidth - 20 && mouseX < xPosition + xWidth &&
                    mouseY >= yPosition && mouseY < yPosition + yHeight) {
                scrolling = true;
                return true;
            }

            if (mouseX >= xPosition && mouseX < xPosition + xWidth - 20 &&
                    mouseY >= yPosition && mouseY < yPosition + yHeight) {

                int totalHeight = (int) Math.ceil((double) numElements / elementsPerLine) * elementHeight;
                int scrollOffset = (int) (scroll * Math.max(0, totalHeight - yHeight));

                int clickedIndex = ((mouseY - yPosition + scrollOffset) / elementHeight) * elementsPerLine +
                        ((mouseX - xPosition) / elementWidth);

                if (clickedIndex < numElements) {
                    selected = clickedIndex;
                    dragged = clickedIndex;
                    dragDelay = 1;
                    dragYOffset = -(mouseY - yPosition + scrollOffset) % elementHeight;
                    dragXOffset = -(mouseX - xPosition) % elementWidth;
                    selectElement();
                    return true;
                }
            }
        }
        return false;
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (mouseX >= xPosition && mouseX < xPosition + xWidth &&
                mouseY >= yPosition && mouseY < yPosition + yHeight) {

            scroll = Math.max(0, Math.min(1, scroll - delta * 0.1));
            return true;
        }
        return false;
    }

    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0) {
            scrolling = false;

            if (dragged >= 0 && dragDelay == 0 && dragged < numElements) {
                int totalHeight = (int) Math.ceil((double) numElements / elementsPerLine) * elementHeight;
                int scrollOffset = (int) (scroll * Math.max(0, totalHeight - yHeight));

                double targetIndex = ((mouseY - yPosition + scrollOffset) / elementHeight) * elementsPerLine +
                        ((mouseX - xPosition) / elementWidth);

                if (targetIndex < numElements && targetIndex != dragged) {
                    moveElement(dragged, (int) targetIndex);
                }
                dragged = -1;
                return true;
            }
        }
        return false;
    }

    protected void drawElement(GuiGraphics guiGraphics, int index, int x, int y) {
        String name = getElementName(index);
        int color = getElementColor(index);
        guiGraphics.drawString(minecraft.font, name, x + 2, y + 2, color, false);
    }

    public void selectElement() {
    }

    public abstract String getElementName(int index);
    public abstract int getElementColor(int index);
    public abstract void moveElement(int from, int to);

    public void add() {
        numElements++;
        if (selected == -1) {
            selected = 0;
        }
    }

    public void remove() {
        numElements--;
        if (selected >= numElements) {
            selected = Math.max(-1, numElements - 1);
        }
    }

    public void updateListSize(int newSize) {
        this.numElements = newSize;
        if (selected >= numElements) {
            selected = Math.max(-1, numElements - 1);
        }
    }
}
