package yuelengm.exmachina.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.util.math.MathHelper;
import yuelengm.exmachina.task.TaskQueue;

import java.util.List;

public class TaskHud extends AbstractGui {
    private static Minecraft mc = Minecraft.getInstance();
    private int width;
    private int height;
    private MatrixStack matrixStack;

    public static int showBefore = 2;
    public static int showAfter = 2;
    public static int heightBias = 20;

    public static int colorNormal = Integer.parseInt("FFFFFF", 16);
    public static int colorActive = Integer.parseInt("FFAA00", 16);


    public TaskHud(MatrixStack matrixStack) {
        this.width = mc.getWindow().getGuiScaledWidth();
        this.height = mc.getWindow().getGuiScaledHeight();
        this.matrixStack = matrixStack;
    }

    public void setMatrixStack(MatrixStack stack) {
        this.matrixStack = stack;
    }

    public void render() {
        List<String[]> queue = TaskQueue.queue;
        if (queue.isEmpty()) {
            return;
        }

        int idx = TaskQueue.idx;
        int startIdx = MathHelper.clamp(idx - showBefore, 0, Math.max(0, queue.size() - showBefore - showAfter - 1));
        int heightBefore = 0;
        int taskIdx;
        for (int i = 0; i < showBefore + showAfter + 1; ++i) {
            taskIdx = startIdx + i;
            if (taskIdx >= queue.size()) {
                return;
            }
            drawCenteredString(this.matrixStack, mc.font, String.join(" ", queue.get(taskIdx)), width / 2,
                               (height / 2) - 4 + heightBias + heightBefore,
                               taskIdx == idx ? colorActive : colorNormal);
            heightBefore += 10;
        }
    }
}