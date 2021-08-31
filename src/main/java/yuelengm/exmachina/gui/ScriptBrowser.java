package yuelengm.exmachina.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.list.ExtendedList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import yuelengm.exmachina.gui.widget.ScriptListWidget;
import yuelengm.exmachina.task.TaskQueue;
import yuelengm.exmachina.util.ScriptFileUtil;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class ScriptBrowser extends Screen {
    private ScriptListWidget scriptList;
    private StringTextComponent scriptListTitle;
    private Button addScriptBtn;
    private Button saveBtn;
    private Button loadBtn;
    private Button openFolderBtn;


    private List<String> files;
    private ScriptListWidget.ScriptEntry selected;
    private List<String> content;

    private int topMargin = 20;
    private int bottomMargin = 20;
    private int leftMargin = 20;
    private int rightMargin = 20;
    private int listWidth = 100;
    private int btnHeight = 20;


    public ScriptBrowser(ITextComponent title) {
        super(title);
    }

    public void setSelected(ScriptListWidget.ScriptEntry entry) {
        this.selected = entry == this.selected ? null : entry;
    }


    @Override
    public void tick() {
        this.scriptList.setSelected(this.selected);
    }

    public <T extends ExtendedList.AbstractListEntry<T>> void buildScriptList(Consumer<T> modListViewConsumer, Function<String, T> newEntry) {
        files.forEach((name) -> {
            modListViewConsumer.accept(newEntry.apply(name));
        });
    }

    @Override
    protected void init() {
        files = ScriptFileUtil.listNames();

        minecraft.keyboardHandler.setSendRepeatsToGui(true);

        scriptListTitle = new StringTextComponent("Script List");

        addScriptBtn = new Button(leftMargin + listWidth - btnHeight, topMargin, btnHeight, btnHeight, new StringTextComponent("+"), (button -> {
        }));
        addButton(addScriptBtn);

        openFolderBtn = new Button(leftMargin, this.height - bottomMargin - btnHeight, listWidth, btnHeight, new StringTextComponent("Script Folder"), (button) -> {
        });
        addButton(openFolderBtn);

        int btnWidth = (listWidth - 4) / 2;
        saveBtn = new Button(leftMargin, height - bottomMargin - btnHeight - 4 - btnHeight, btnWidth, btnHeight, new StringTextComponent("Save"), (button) -> {
        });
        addButton(saveBtn);
        loadBtn = new Button(leftMargin + btnWidth + 4, height - bottomMargin - btnHeight - 4 - btnHeight, btnWidth, btnHeight, new StringTextComponent("Load"), (button) -> {
            if (selected != null) {
                content = ScriptFileUtil.getScript(selected.scriptName);
                if (content != null) {
                    TaskQueue.load(content);
                }
            }
        });
        addButton(loadBtn);

        scriptList = new ScriptListWidget(this, listWidth, topMargin + btnHeight + 4, height - bottomMargin - btnHeight - 4 - btnHeight - 4);
        scriptList.setLeftPos(leftMargin);
        children.add(scriptList);
        // this.textFieldWidget = new TextFieldWidget(this.font, this.width / 2 - 100, 66, 200, 20,
        //                                            new StringTextComponent("qwe"));
        // this.children.add(this.textFieldWidget);
        //

    }

    @Override
    public void render(MatrixStack mStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(mStack);
        drawCenteredString(mStack, font, scriptListTitle, leftMargin + (listWidth - btnHeight - 4) / 2,
                topMargin + 6, 0xFFFFFF);
        if (content != null) {
            int hb = 0;
            for (String c : content) {
                drawString(mStack, font, c, leftMargin + listWidth + 4, topMargin + hb, 0xFFFFFF);
                hb += 10;
            }
        }
        this.addScriptBtn.render(mStack, mouseX, mouseY, partialTicks);
        this.scriptList.render(mStack, mouseX, mouseY, partialTicks);
        // this.textFieldWidget.render(mStack, mouseX, mouseY, partialTicks);
        this.openFolderBtn.render(mStack, mouseX, mouseY, partialTicks);
        this.saveBtn.render(mStack, mouseX, mouseY, partialTicks);
        this.loadBtn.render(mStack, mouseX, mouseY, partialTicks);
        super.render(mStack, mouseX, mouseY, partialTicks);
    }

}
