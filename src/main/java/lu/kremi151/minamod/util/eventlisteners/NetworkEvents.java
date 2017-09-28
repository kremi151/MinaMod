package lu.kremi151.minamod.util.eventlisteners;

import lu.kremi151.minamod.MinaMod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;

public class NetworkEvents {

	@SubscribeEvent
	public void onPlayerConnect(ClientConnectedToServerEvent event) {
		if(MinaMod.getProxy().isMaintenanceFlagSet()) {
			event.setCanceled(true);
		}
	}
}
