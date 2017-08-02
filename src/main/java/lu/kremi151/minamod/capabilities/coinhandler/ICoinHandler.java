package lu.kremi151.minamod.capabilities.coinhandler;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public interface ICoinHandler {

	@CapabilityInject(ICoinHandler.class)
	public static final Capability<ICoinHandler> CAPABILITY = null;
	
	int getAmountCoins();
	boolean canPayCoins(int amount);
	boolean withdrawCoins(int amount, boolean simulate);
	boolean depositCoins(int amount, boolean simulate);
	
	@Deprecated
	default boolean withdrawCoins(int amount) {
		if(withdrawCoins(amount, true)) {
			return withdrawCoins(amount, false);
		}else {
			return false;
		}
	}
	
	@Deprecated
	default boolean depositCoins(int amount) {
		if(depositCoins(amount, true)) {
			return depositCoins(amount, false);
		}else {
			return false;
		}
	}
	
}
