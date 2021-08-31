package yuelengm.exmachina.gui.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.list.ExtendedList;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.MathHelper;
import yuelengm.exmachina.gui.ScriptBrowser;

public class ScriptListWidget extends ExtendedList<ScriptListWidget.ScriptEntry> {
    private int listWidth;
    private ScriptBrowser parent;
    private double scale;

    public ScriptListWidget(ScriptBrowser parent, int listWidth, int top, int bottom) {
        super(Minecraft.getInstance(), listWidth, parent.height, top, bottom, 17);
        this.parent = parent;
        this.listWidth = listWidth;
        this.scale = Minecraft.getInstance().getWindow().getGuiScale();
        this.refreshList();
    }

    @Override
    protected int getScrollbarPosition() {
        return this.listWidth + this.x0 - 6;
    }

    @Override
    public int getRowWidth() {
        return this.listWidth;
    }

    public void refreshList() {
        this.clearEntries();
        this.parent.buildScriptList(this::addEntry, (name) -> new ScriptEntry(this.parent, name));
    }

    @Override
    public void render(MatrixStack mStack, int mouseX, int mouseY, float partialTicks) {
        int i = this.getScrollbarPosition();
        int j = i + 6;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuilder();

        int j1 = this.getRowLeft();
        int k = this.y0 + 4 - (int) this.getScrollAmount();

        RenderSystem.enableScissor((int) (getLeft() * scale), (int) ((parent.height - getBottom()) * scale), (int) (listWidth * scale), (int) ((getBottom() - getTop()) * scale));
        this.renderList(mStack, j1, k, mouseX, mouseY, partialTicks);
        RenderSystem.disableScissor();

        int k1 = this.getMaxScroll();
        if (k1 > 0) {
            RenderSystem.disableTexture();
            int l1 = (int) ((float) ((this.y1 - this.y0) * (this.y1 - this.y0)) / (float) this.getMaxPosition());
            l1 = MathHelper.clamp(l1, 32, this.y1 - this.y0 - 8);
            int i2 = (int) this.getScrollAmount() * (this.y1 - this.y0 - l1) / k1 + this.y0;
            if (i2 < this.y0) {
                i2 = this.y0;
            }

            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            bufferbuilder.vertex((double) i, (double) this.y1, 0.0D).uv(0.0F, 1.0F).color(0, 0, 0, 255).endVertex();
            bufferbuilder.vertex((double) j, (double) this.y1, 0.0D).uv(1.0F, 1.0F).color(0, 0, 0, 255).endVertex();
            bufferbuilder.vertex((double) j, (double) this.y0, 0.0D).uv(1.0F, 0.0F).color(0, 0, 0, 255).endVertex();
            bufferbuilder.vertex((double) i, (double) this.y0, 0.0D).uv(0.0F, 0.0F).color(0, 0, 0, 255).endVertex();
            bufferbuilder.vertex((double) i, (double) (i2 + l1), 0.0D).uv(0.0F, 1.0F).color(128, 128, 128, 255).endVertex();
            bufferbuilder.vertex((double) j, (double) (i2 + l1), 0.0D).uv(1.0F, 1.0F).color(128, 128, 128, 255).endVertex();
            bufferbuilder.vertex((double) j, (double) i2, 0.0D).uv(1.0F, 0.0F).color(128, 128, 128, 255).endVertex();
            bufferbuilder.vertex((double) i, (double) i2, 0.0D).uv(0.0F, 0.0F).color(128, 128, 128, 255).endVertex();
            bufferbuilder.vertex((double) i, (double) (i2 + l1 - 1), 0.0D).uv(0.0F, 1.0F).color(192, 192, 192, 255).endVertex();
            bufferbuilder.vertex((double) (j - 1), (double) (i2 + l1 - 1), 0.0D).uv(1.0F, 1.0F).color(192, 192, 192, 255).endVertex();
            bufferbuilder.vertex((double) (j - 1), (double) i2, 0.0D).uv(1.0F, 0.0F).color(192, 192, 192, 255).endVertex();
            bufferbuilder.vertex((double) i, (double) i2, 0.0D).uv(0.0F, 0.0F).color(192, 192, 192, 255).endVertex();
            tessellator.end();
        }

        this.renderDecorations(mStack, mouseX, mouseY);
        RenderSystem.enableTexture();
        RenderSystem.shadeModel(7424);
        RenderSystem.enableAlphaTest();
        RenderSystem.disableBlend();
    }


    public class ScriptEntry extends ExtendedList.AbstractListEntry<ScriptListWidget.ScriptEntry> {
        ScriptBrowser parent;
        public String scriptName;

        public ScriptEntry(ScriptBrowser parentIn, String name) {
            parent = parentIn;
            scriptName = name;
        }

        @Override
        public void render(MatrixStack mStack, int entryIdx, int top, int left, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean p_194999_5_, float partialTicks) {
            Minecraft mc = Minecraft.getInstance();
            FontRenderer font = mc.font;
            font.draw(mStack, scriptName, left + 3, top + 2, 0xFFFFFF);
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
            this.parent.setSelected(this);
            ScriptListWidget.this.setSelected(this);
            return false;
        }

    }
}
