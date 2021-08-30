package yuelengm.exmachina.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.util.text.StringTextComponent;

public class MessageUtil {
    public static Minecraft mc = Minecraft.getInstance();

    public static void rawMessage(String s) {
        ClientPlayerEntity player = mc.player;
        if (player == null) {
            return;
        }
        player.sendMessage(new StringTextComponent(s), player.getUUID());
    }
}
