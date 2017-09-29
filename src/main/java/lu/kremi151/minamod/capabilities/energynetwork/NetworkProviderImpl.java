package lu.kremi151.minamod.capabilities.energynetwork;

import java.util.ArrayList;
import java.util.Map;

import javax.annotation.Nonnull;

import lu.kremi151.minamod.util.TextHelper;
import net.minecraft.command.ICommandSender;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;

public abstract class NetworkProviderImpl implements IEnergyNetworkProvider{
	
	private IEnergyNetwork network = null;
	
	protected abstract World getWorld();
	protected abstract BlockPos getPos();
	
	private IEnergyNetwork checkNetwork() {
		if(network == null) {
			ArrayList<EnumFacing> potentialClients = new ArrayList(EnumFacing.VALUES.length);
			for(EnumFacing face : EnumFacing.VALUES) {
				BlockPos npos = getPos().offset(face);
				TileEntity te = getWorld().getTileEntity(npos);
				if(te != null) {
					if(te.hasCapability(IEnergyNetworkProvider.CAPABILITY, face.getOpposite())) {
						IEnergyNetworkProvider prov = te.getCapability(IEnergyNetworkProvider.CAPABILITY, face.getOpposite());
						if(network == null) {
							if(prov.hasNetwork()) {
								setNetwork(prov.getNetwork());
							}
						}else {
							setNetwork(EnergyNetworkHelper.combine(network, prov.getNetwork()));
						}
					}else if(te.hasCapability(CapabilityEnergy.ENERGY, face.getOpposite())) {
						potentialClients.add(face.getOpposite());
					}
					
				}
			}
			if(network == null)setNetwork(EnergyNetworkHelper.createNetwork(getWorld()));
			for(EnumFacing face : potentialClients) {
				BlockPos npos = getPos().offset(face);
				TileEntity te = getWorld().getTileEntity(npos);
				if(te != null && te.hasCapability(IEnergyNetworkProvider.CAPABILITY, face.getOpposite())) {
					network.registerClient(npos, face.getOpposite());
					System.out.println("Register client at " + npos + " {" + face.getOpposite() + "}");
				}
			}
		}
		return network;
	}

	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) {
		return checkNetwork().receiveEnergy(maxReceive, simulate);
	}

	@Override
	public int extractEnergy(int maxExtract, boolean simulate) {
		return checkNetwork().extractEnergy(maxExtract, simulate);
	}

	@Override
	public int getEnergyStored() {
		return checkNetwork().getEnergyStored();
	}

	@Override
	public int getMaxEnergyStored() {
		return checkNetwork().getMaxEnergyStored();
	}

	@Override
	public boolean canExtract() {
		return checkNetwork().canExtract();
	}

	@Override
	public boolean canReceive() {
		return checkNetwork().canReceive();
	}

	@Override
	public IEnergyNetwork getNetwork() {
		return checkNetwork();
	}
	
	@Override
	public boolean hasNetwork() {
		return network != null;
	}
	
	protected void setNetwork(@Nonnull IEnergyNetwork network) {
		if(network == null)throw new NullPointerException("Cannot set a network to null");
		boolean dispatch = (this.network == null || !this.network.equals(network));
		this.network = network;
		if(dispatch) {
			dispatchNetworkChangeToNeighbours(network);
		}
	}
	
	protected void dispatchNetworkChangeToNeighbours(IEnergyNetwork network) {
		World world = getWorld();
		for(EnumFacing face : EnumFacing.VALUES) {
			BlockPos npos = getPos().offset(face);
			TileEntity te = world.getTileEntity(npos);
			if(te != null && te.hasCapability(IEnergyNetworkProvider.CAPABILITY, face.getOpposite())) {
				te.getCapability(IEnergyNetworkProvider.CAPABILITY, face.getOpposite()).onNeighbourNetworkChanged(getPos(), network);
			}
		}
	}
	
	//TODO: Remove!!!
	public void printDebugInformation(ICommandSender sender) {
		TextHelper.sendChatMessage(sender, "Energy: " + this.getEnergyStored());
		TextHelper.sendChatMessage(sender, "Clients:");
		NetworkImpl nimpl = ((NetworkPointer)network).getPointingNetwork();
		for(Map.Entry<BlockPos, NetworkImpl.ClientReference> entry : nimpl.clients.entrySet()) {
			StringBuilder sb = new StringBuilder("* ");
			sb.append(entry.getKey().toString());
			sb.append(" [");
			for(EnumFacing f : entry.getValue().getFaces()) {
				sb.append(f.getName());
				sb.append(' ');
			}
			sb.append("]");
			TextHelper.sendChatMessage(sender, sb.toString());
		}
	}

}
