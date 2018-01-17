package lu.kremi151.minamod.capabilities.energynetwork;

import javax.annotation.Nullable;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class NetworkPointer implements IEnergyNetwork{
	
	private NetworkImpl network;
	
	NetworkPointer(NetworkImpl network){
		this.network = network;
		network.pointers.add(this);
	}
	
	void swapNetwork(NetworkImpl network) {
		if(network == null) {
			throw new NullPointerException();
		}
		this.network = network;
	}
	
	@Nullable NetworkImpl getPointingNetwork() {
		return network;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) {
			return false;
		}else if(obj == this) {
			return true;
		}else if(obj.getClass() == NetworkPointer.class) {
			return ((NetworkPointer)obj).network == this.network;
		}else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {//TODO: Remove
		return network.hashCode();
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

	@Override
	public boolean unregisterClient(BlockPos pos, EnumFacing face) {
		return network.unregisterClient(pos, face);
	}

	@Override
	public void registerNetworkBlock(BlockPos pos) {
		network.registerNetworkBlock(pos);
	}

	@Override
	public boolean unregisterNetworkBlock(BlockPos pos) {
		return network.unregisterNetworkBlock(pos);
	}

	@Override
	public IEnergyNetwork copy() {
		NetworkPointer n = new NetworkPointer((NetworkImpl) network.copy());
		n.getPointingNetwork().pointers.add(n);
		return n;
	}

	@Override
	public void reset() {
		network.reset();
	}

	@Override
	public int clientCount() {
		return network.clientCount();
	}

}
