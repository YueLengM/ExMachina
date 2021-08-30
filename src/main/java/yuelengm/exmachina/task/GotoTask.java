package yuelengm.exmachina.task;

import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import yuelengm.exmachina.ExMachina;

public class GotoTask extends BaseTask {
    protected ClientPlayerEntity player;
    protected double destX;
    protected double destZ;
    protected double oldX;
    protected double oldZ;
    protected double threshold = 1.0E-5F;

    public GotoTask(boolean taskInQueue) {
        super(taskInQueue);
        this.player = mc.player;
    }

    public GotoTask(boolean taskInQueue, String x, String z) {
        this(taskInQueue);
        parse(x, z);
    }

    public boolean parse(String x, String z) {
        try {
            Vector3d pos = player.position();
            oldX = pos.x + 1;
            oldZ = pos.z + 1;

            if (x.equals("@")) {
                destX = Math.floor(pos.x) + 0.5;
            } else if (x.startsWith("~")) {
                destX = pos.x;
                if (!x.equals("~")) {
                    destX += Double.parseDouble(x.substring(1));
                }
            } else {
                destX = Double.parseDouble(x);
            }

            if (z.equals("@")) {
                destZ = Math.floor(pos.z) + 0.5;
            } else if (z.startsWith("~")) {
                destZ = pos.z;
                if (!z.equals("~")) {
                    destZ += Double.parseDouble(z.substring(1));
                }
            } else {
                destZ = Double.parseDouble(z);
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
    public void onInput(InputUpdateEvent event) {
        if (!isPaused) {
            Vector3d pos = player.position();
            if (Math.abs(oldX - pos.x) < threshold && Math.abs(oldZ - pos.z) < threshold) {
                finish();
                return;
            }
            oldX = pos.x;
            oldZ = pos.z;

            double degF = Math.toRadians(player.getRotationVector().y);
            double degL = degF - Math.PI / 2;
            double dx = destX - pos.x;
            double dz = destZ - pos.z;
            float df = (float) (dz * Math.cos(degF) + dx * -Math.sin(degF));
            float dl = (float) (dz * Math.cos(degL) + dx * -Math.sin(degL));
            event.getMovementInput().forwardImpulse = df;
            event.getMovementInput().leftImpulse = dl;
        }
    }
}
