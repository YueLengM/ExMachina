package yuelengm.exmachina.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;
import yuelengm.exmachina.ExMachina;
import yuelengm.exmachina.gui.ScriptBrowser;

@Mod.EventBusSubscriber(modid = ExMachina.MOD_ID, value = Dist.CLIENT)
public class HotKeyEvent {
    public static KeyBinding MENU_KEY = new KeyBinding("key." + ExMachina.MOD_ID + ".menu", GLFW.GLFW_KEY_J, "key.categories." + ExMachina.MOD_ID);

    @SubscribeEvent
    public static void onKeyboardInput(InputEvent.KeyInputEvent event) {
        if (MENU_KEY.isDown()) {
            if (Minecraft.getInstance().player == null) {
                return;
            }

            Minecraft.getInstance().setScreen(new ScriptBrowser(new StringTextComponent(ExMachina.MOD_ID + ".menu")));

        }
    }
}
