package yuelengm.exmachina.task;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yuelengm.exmachina.ExMachina;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = ExMachina.MOD_ID, value = Dist.CLIENT)
public class TaskQueue {
    public static boolean isInitialized = false;
    public static boolean haveTask = false;
    public static boolean isPaused = true;
    public static boolean toNext = false;

    public static List<String[]> queue = new ArrayList<>();
    public static int idx = 0;
    public static BaseTask currTask;
    public static KeyTask keyTask;

    public static boolean autoStart = false;
    public static boolean step = false;
    public static boolean loop = true;

    public static void init() {
        if (!isInitialized) {
            keyTask = new KeyTask(true);
            MinecraftForge.EVENT_BUS.register(keyTask);
            isInitialized = true;
        }
    }

    public static void release() {
        if (isInitialized) {
            clear();
            MinecraftForge.EVENT_BUS.unregister(keyTask);
            keyTask = null;
            isInitialized = false;
        }
    }

    public static void addTask(String[] args) {
        if (isInitialized) {
            queue.add(args);
            if (autoStart) {
                start();
            }
        }
    }

    public static void start() {
        if (isInitialized && !haveTask && idx < queue.size()) {
            String[] args = queue.get(idx);

            boolean success = false;
            boolean keyEvent = false;
            switch (args[0]) {
                case "$sleep":
                    if (args.length == 2) {
                        currTask = new SleepTask(true);
                        success = ((SleepTask) currTask).parse(args[1]);
                    }
                    break;
                case "$goto":
                    if (args.length == 3) {
                        currTask = new GotoTask(true);
                        success = ((GotoTask) currTask).parse(args[1], args[2]);
                    }
                    break;
                case "$move":
                    if (args.length == 3) {
                        currTask = new MoveTask(true);
                        success = ((MoveTask) currTask).parse(args[1], args[2]);
                    }
                    break;
                case "$look":
                    if (args.length == 3) {
                        currTask = new LookTask(true);
                        success = ((LookTask) currTask).parse(args[1], args[2]);
                    }
                    break;
                case "$keydown":
                    if (args.length == 2) {
                        currTask = null;
                        keyEvent = true;
                        success = keyTask.parse("down", args[1]);
                    }
                    break;
                case "$keyup":
                    if (args.length == 2) {
                        currTask = null;
                        keyEvent = true;
                        success = keyTask.parse("up", args[1]);
                    }
                    break;
                case "$click":
                    if (args.length == 2) {
                        currTask = null;
                        keyEvent = true;
                        success = keyTask.parse("click", args[1]);
                    }
                    break;
                default:
                    break;
            }

            if (success) {
                if (!keyEvent) {
                    MinecraftForge.EVENT_BUS.register(currTask);
                    currTask.resume();
                } else {
                    keyTask.resume();
                }
                haveTask = true;
                isPaused = false;
            }
        }
    }

    public static void pause() {
        if (haveTask && !isPaused) {
            if (currTask != null) {
                currTask.pause();
            }
            keyTask.pause();
            isPaused = true;
        }
    }

    public static void resume() {
        if (haveTask && isPaused) {
            if (currTask != null) {
                currTask.resume();
            }
            keyTask.resume();
            isPaused = false;
        }
    }

    public static void stop() {
        if (isInitialized) {
            pause();
            if (currTask != null) {
                MinecraftForge.EVENT_BUS.unregister(currTask);
                currTask = null;
            }
            keyTask.clear();
            haveTask = false;
            idx = 0;
        }
    }

    public static void clear() {
        stop();
        queue.clear();
    }

    public static void next() {
        if (isInitialized) {
            if (currTask != null) {
                MinecraftForge.EVENT_BUS.unregister(currTask);
                currTask = null;
            }
            haveTask = false;
            idx = idx + 1;
            if (loop) {
                idx = idx % queue.size();
            }
            if (!step) {
                start();
            }
        }
    }

    @SubscribeEvent()
    @OnlyIn(Dist.CLIENT)
    public static void onTick(TickEvent.PlayerTickEvent event) {
        if (!isPaused) {
            if (toNext) {
                toNext = false;
                next();
            }
        }
    }
}
