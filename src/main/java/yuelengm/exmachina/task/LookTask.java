package yuelengm.exmachina.task;

import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import yuelengm.exmachina.ExMachina;

public class LookTask extends BaseTask {
    private ClientPlayerEntity player;
    private float yaw = 0;
    private float pitch = 0;

    public LookTask(boolean taskInQueue) {
        super(taskInQueue);
        this.player = mc.player;
    }

    public LookTask(boolean taskInQueue, String yaw, String pitch) {
        this(taskInQueue);
        parse(yaw, pitch);
    }

    public boolean parse(String yaw, String pitch) {
        try {
            Vector2f rotationVector = player.getRotationVector();

            if (yaw.startsWith("~")) {
                this.yaw = rotationVector.y;
                if (!yaw.equals("~")) {
                    this.yaw += Float.parseFloat(yaw.substring(1));
                }
            } else {
                this.yaw = Float.parseFloat(yaw);
            }

            if (pitch.startsWith("~")) {
                this.pitch = rotationVector.x;
                if (!pitch.equals("~")) {
                    this.pitch += Float.parseFloat(pitch.substring(1));
                }
            } else {
                this.pitch = Float.parseFloat(pitch);
            }
            return true;
        } catch (Exception e) {
            ExMachina.LOGGER.error("Task parse failed!");
            finish();
            return false;
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    @OnlyIn(Dist.CLIENT)
    public void onTick(TickEvent.PlayerTickEvent event) {
        if (!isPaused) {
            if (player.yRot == yaw && player.xRot == pitch) {
                finish();
                return;
            }
            player.yRot = yaw;
            player.xRot = pitch;
        }
    }
}
