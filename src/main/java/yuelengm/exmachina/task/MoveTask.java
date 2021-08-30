package yuelengm.exmachina.task;

import net.minecraft.util.math.vector.Vector3d;
import yuelengm.exmachina.ExMachina;

public class MoveTask extends GotoTask {
    public MoveTask(boolean taskInQueue) {
        super(taskInQueue);
    }

    public MoveTask(boolean taskInQueue, String f, String l) {
        this(taskInQueue);
        parse(f, l);
    }

    @Override
    public boolean parse(String f, String l) {
        try {
            Vector3d pos = player.position();
            oldX = pos.x + 1;
            oldZ = pos.z + 1;

            double numF = Double.parseDouble(f);
            double numL = Double.parseDouble(l);
            double alpha = Math.toRadians(player.getRotationVector().y);
            double cos = Math.cos(alpha);
            double sin = Math.sin(alpha);

            destX = pos.x + numL * cos - numF * sin;
            destZ = pos.z + numL * sin + numF * cos;

            return true;
        } catch (Exception e) {
            ExMachina.LOGGER.error("Task parse failed!");
            finish();
            return false;
        }
    }
}
