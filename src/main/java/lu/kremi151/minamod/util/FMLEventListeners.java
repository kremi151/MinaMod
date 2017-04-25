package lu.kremi151.minamod.util;

import lu.kremi151.minamod.MinaMod;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FMLEventListeners {
	
	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs) { // NO_UCD (unused code)
        if(eventArgs.getModID().equals(MinaMod.MODID))
            MinaMod.getMinaConfig().reload();
    }
}
