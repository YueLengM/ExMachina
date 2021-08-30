package yuelengm.exmachina.task;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.util.MovementInput;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class KeyTask extends BaseTask {
    protected ClientPlayerEntity player;
    public static Set<String> active = new HashSet<>();

    public static int rightClickDelay = 0;

    /**
     * 0~5  up down left right jump sneak
     * 6~11 click
     * 12 attack
     * 13 use
     */
    public static boolean[] key = new boolean[14];

    public static Map<String, Integer> map = new HashMap<String, Integer>() {{
        put("up", 0);
        put("down", 1);
        put("left", 2);
        put("right", 3);
        put("jump", 4);
        put("sneak", 5);
        put("isAttack", 12);
        put("isUse", 13);
    }};


    public static Method startUseItem = ObfuscationReflectionHelper.findMethod(Minecraft.class, "func_147121_ag");
    public static Method startAttack = ObfuscationReflectionHelper.findMethod(Minecraft.class, "func_147116_af");


    public KeyTask(boolean taskInQueue) {
        super(taskInQueue);
        player = mc.player;
    }

    public void clear() {
        pause();
        active.clear();
        Arrays.fill(key, false);
        mc.options.keyAttack.setDown(false);
    }

    public boolean parse(String type, String arg) {
        boolean finish = true;

        switch (arg) {
            case "up":
            case "down":
            case "left":
            case "right":
            case "jump":
            case "sneak":
                if (type.equals("click")) {
                    key[map.get(arg) + 6] = true;
                    finish = false;
                    break;
                }
                key[map.get(arg)] = type.equals("down");
                break;

            case "attack":
                switch (type) {
                    case "down":
                        key[12] = true;
                        try {
                            startAttack.invoke(mc);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "up":
                        key[12] = false;
                        mc.options.keyAttack.setDown(false);
                        break;
                    case "click":
                        try {
                            startAttack.invoke(mc);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                        break;
                    default:
                        break;
                }
                break;
            case "use":
                switch (type) {
                    case "down":
                        key[13] = true;
                        break;
                    case "up":
                        key[13] = false;
                        rightClickDelay = 0;
                        break;
                    case "click":
                        try {
                            startUseItem.invoke(mc);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                        rightClickDelay = 0;
                        break;
                    default:
                        break;
                }
            default:
                // todo
                break;
        }

        if (finish) {
            finish();
        }
        return true;
    }

    // private boolean parse2(String)

    @SubscribeEvent(priority = EventPriority.LOWEST)
    @OnlyIn(Dist.CLIENT)
    public void onTick(InputUpdateEvent event) {
        if (!isPaused) {
            MovementInput movementInput = event.getMovementInput();
            movementInput.jumping |= (key[4] || key[10]);
            movementInput.shiftKeyDown |= (key[5] || key[11]);

            if (key[0] || key[1] || key[2] || key[3] || key[6] || key[7] || key[8] || key[9]) {
                movementInput.forwardImpulse = ((key[0] || key[6]) == (key[1] || key[7]) ? 0.0F : ((key[0] || key[6]) ? 1.0F : -1.0F));
                movementInput.leftImpulse = ((key[2] || key[8]) == (key[3] || key[9]) ? 0.0F : ((key[2] || key[8]) ? 1.0F : -1.0F));
            }

            if (key[6] || key[7] || key[8] || key[9] || key[10] || key[11]) {
                Arrays.fill(key, 6, 11, false);
                finish();
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    @OnlyIn(Dist.CLIENT)
    public void onTick(TickEvent.PlayerTickEvent event) {
        if (!isPaused) {
            if (key[12]) {
                mc.options.keyAttack.setDown(true);
            }

            if (key[13]) {
                if (rightClickDelay > 0) {
                    --rightClickDelay;
                }
                if (rightClickDelay == 0 && !player.isUsingItem()) {
                    try {
                        startUseItem.invoke(mc);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    rightClickDelay = 16;
                }
            }
        }
    }
}
