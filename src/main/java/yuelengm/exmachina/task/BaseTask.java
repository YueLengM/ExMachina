package yuelengm.exmachina.task;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import yuelengm.exmachina.ExMachina;
import yuelengm.exmachina.util.MessageUtil;

@Mod.EventBusSubscriber(modid = ExMachina.MOD_ID, value = Dist.CLIENT)
public class BaseTask {
    protected static Minecraft mc = Minecraft.getInstance();
    protected boolean isPaused = true;
    protected boolean inQueue;

    public BaseTask(boolean taskInQueue) {
        inQueue = taskInQueue;
    }

    public void pause() {
        isPaused = true;
    }

    public void resume() {
        isPaused = false;
    }

    public void finish() {
        isPaused = true;
        if (inQueue) {
            TaskQueue.toNext = true;
        }
    }

    public void finish(String name) {
        MessageUtil.rawMessage(name + " finished.");
        finish();
    }
}
