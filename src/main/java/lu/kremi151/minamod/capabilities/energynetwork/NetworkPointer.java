package lu.kremi151.minamod.capabilities.energynetwork;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class NetworkPointer implements IEnergyNetwork{
	
	private NetworkImpl network;
	
	NetworkPointer(NetworkImpl network){
		this.network = network;
	}
	
	void swapNetwork(NetworkImpl network) {
		if(network == null) {
			throw new NullPointerException();
		}
		this.network = network;
	}

	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) {
		return network.receiveEnergy(maxReceive, simulate);
	}

	@Override
	public int extractEnergy(int maxExtract, boolean simulate) {
		return network.extractEnergy(maxExtract, simulate);
	}

	@Override
	public int getEnergyStored() {
		return network.getEnergyStored();
	}

	@Override
	public int getMaxEnergyStored() {
		return network.getMaxEnergyStored();
	}

	@Override
	public boolean canExtract() {
		return network.canExtract();
	}

	@Override
	public boolean canReceive() {
		return network.canReceive();
	}

	@Override
	public void registerClient(BlockPos pos, EnumFacing face) {
		network.registerClient(pos, face);
	}

}
