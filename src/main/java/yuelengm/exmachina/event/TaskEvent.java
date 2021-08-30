package yuelengm.exmachina.event;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityLeaveWorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yuelengm.exmachina.ExMachina;
import yuelengm.exmachina.task.TaskQueue;

@Mod.EventBusSubscriber(modid = ExMachina.MOD_ID, value = Dist.CLIENT)
public class TaskEvent {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    @OnlyIn(Dist.CLIENT)
    public static void onPlayerJoin(EntityJoinWorldEvent event) {
        if ((event.getEntity() instanceof PlayerEntity)) {
            TaskQueue.release();
            TaskQueue.init();
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    @OnlyIn(Dist.CLIENT)
    public static void onPlayerLeave(EntityLeaveWorldEvent event) {
        if ((event.getEntity() instanceof PlayerEntity)) {
            TaskQueue.release();
        }
    }
}
