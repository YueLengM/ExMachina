package yuelengm.exmachina.gui;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yuelengm.exmachina.ExMachina;

@Mod.EventBusSubscriber(modid = ExMachina.MOD_ID, value = Dist.CLIENT)
public class TaskHudEvent {
    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onOverlayRender(RenderGameOverlayEvent event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) {
            return;
        }
        if (Minecraft.getInstance().player == null) {
            return;
        }
        TaskHud taskHud = new TaskHud(event.getMatrixStack());
        taskHud.render();
    }
}