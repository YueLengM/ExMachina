package yuelengm.exmachina.task;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import yuelengm.exmachina.ExMachina;

public class SleepTask extends BaseTask {
    public long startTime;
    public long durations;

    public SleepTask(boolean taskInQueue) {
        super(taskInQueue);
        this.startTime = System.currentTimeMillis();
    }

    public SleepTask(boolean taskInQueue, String millSec) {
        this(taskInQueue);
        parse(millSec);
    }

    public boolean parse(String millSec) {
        try {
            durations = Long.parseLong(millSec);
            return true;
        } catch (Exception e) {
            ExMachina.LOGGER.error("Task parse failed!");
            finish();
            return false;
        }
    }

    @Override
    public void pause() {
        durations = durations - (System.currentTimeMillis() - startTime);
        super.pause();
    }

    @Override
    public void resume() {
        startTime = System.currentTimeMillis();
        super.resume();
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    @OnlyIn(Dist.CLIENT)
    public void onTick(TickEvent.PlayerTickEvent event) {
        if (!isPaused) {
            if (System.currentTimeMillis() - startTime >= durations) {
                finish();
            }
        }
    }
}
