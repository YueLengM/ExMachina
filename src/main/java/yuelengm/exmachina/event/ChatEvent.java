package yuelengm.exmachina.event;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yuelengm.exmachina.ExMachina;
import yuelengm.exmachina.task.TaskQueue;
import yuelengm.exmachina.util.MessageUtil;

@Mod.EventBusSubscriber(modid = ExMachina.MOD_ID, value = Dist.CLIENT)
public class ChatEvent {
    private static Minecraft mc = Minecraft.getInstance();

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onChatMessage(ClientChatEvent event) {
        String msg = event.getOriginalMessage();
        String[] s = msg.split(" ");
        boolean triggered = false;
        switch (s[0]) {
            case "$sleep":
            case "$keydown":
            case "$keyup":
            case "$click":
                if (s.length == 2) {
                    TaskQueue.addTask(s);
                    triggered = true;
                }
                break;
            case "$goto":
            case "$move":
            case "$look":
                if (s.length == 3) {
                    TaskQueue.addTask(s);
                    triggered = true;

                }
                break;
            case "$start":
            case "$s":
                if (s.length == 1) {
                    TaskQueue.start();
                    triggered = true;
                }
                break;
            case "$clear":
            case "$c":
                if (s.length == 1) {
                    TaskQueue.clear();
                    triggered = true;
                }
                break;
            case "$stop":
                if (s.length == 1) {
                    TaskQueue.stop();
                    triggered = true;
                }
                break;
            case "$pause":
            case "$p":
                if (s.length == 1) {
                    TaskQueue.pause();
                    triggered = true;
                }
                break;
            case "$resume":
            case "$r":
                if (s.length == 1) {
                    TaskQueue.resume();
                    triggered = true;
                }
                break;
            case "$auto":
                if (s.length == 1) {
                    TaskQueue.autoStart = !TaskQueue.autoStart;
                    MessageUtil.rawMessage(String.format("Auto start set to %s", TaskQueue.autoStart));
                    triggered = true;
                }
                break;
            case "$loop":
                if (s.length == 1) {
                    TaskQueue.loop = !TaskQueue.loop;
                    if (TaskQueue.idx >= TaskQueue.queue.size()) {
                        TaskQueue.idx = 0;
                    }
                    MessageUtil.rawMessage(String.format("Loop mode set to %s", TaskQueue.loop));
                    triggered = true;
                }
                break;
            case "$step":
                if (s.length == 1) {
                    TaskQueue.step = !TaskQueue.step;
                    MessageUtil.rawMessage(String.format("Step mode set to %s", TaskQueue.step));
                    triggered = true;
                }
                break;
            default:
                break;
        }

        if (triggered) {
            mc.gui.getChat().addRecentChat(msg);
            event.setCanceled(true);
        }
    }
}
