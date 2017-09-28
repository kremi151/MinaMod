package lu.kremi151.minamod.capabilities.energynetwork;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.energy.IEnergyStorage;

public interface IEnergyNetworkProvider extends IEnergyStorage{
	
	@CapabilityInject(IEnergyNetworkProvider.class)
	public static final Capability<IEnergyNetworkProvider> CAPABILITY = null;
	
	IEnergyNetwork getNetwork();

}
