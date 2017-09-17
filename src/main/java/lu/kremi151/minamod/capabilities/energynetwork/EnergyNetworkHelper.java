package lu.kremi151.minamod.capabilities.energynetwork;

import net.minecraft.world.World;

public class EnergyNetworkHelper {

	public static IEnergyNetwork createNetwork(World world) {
		return new NetworkPointer(new NetworkImpl(world));
	}
	
	public static IEnergyNetwork combine(IEnergyNetwork a, IEnergyNetwork b) {
		throw new AbstractMethodError();
	}
	
}
