package lu.kremi151.minamod.capabilities.coinhandler;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public interface ICoinHandler {

	@CapabilityInject(ICoinHandler.class)
	public static final Capability<ICoinHandler> CAPABILITY_COIN_HANDLER = null;
	
	int getAmountCoins();
	boolean hasCoins(int amount);
	boolean withdrawCoins(int amount);
	boolean depositCoins(int amount);
	
}
