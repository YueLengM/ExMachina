package yuelengm.exmachina.init;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import yuelengm.exmachina.ExMachina;
import yuelengm.exmachina.event.HotKeyEvent;

@Mod.EventBusSubscriber(modid = ExMachina.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class KeyRegistry {
    @SubscribeEvent
    public static void onClientSetupEvent(FMLClientSetupEvent event) {
        ClientRegistry.registerKeyBinding(HotKeyEvent.MENU_KEY);
    }
}
