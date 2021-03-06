package lu.kremi151.minamod.capabilities.energynetwork;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class NetworkImpl implements IEnergyNetwork{
	
	final LinkedList<NetworkPointer> pointers = new LinkedList<>();
	final HashMap<BlockPos, ClientReference> clients = new HashMap<>();
	final HashSet<BlockPos> networkBlocks = new HashSet<>();
	final IBlockAccess world;
	private int energyBuffer = 0;
	
	NetworkImpl(IBlockAccess world){
		this.world = world;
	}

	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) {
		if(maxReceive > 0 && clients.size() > 0) {
			final int initialEnergyBuffer = energyBuffer;
			final int initialMaxReceive = maxReceive;
			maxReceive += energyBuffer;
			energyBuffer = 0;
			int consumed = 0;
			boolean _cont = true;
			while(_cont && maxReceive > 0) {
				_cont = false;
				final int portions = maxReceive / clients.size();
				if(portions == 0) {
					energyBuffer += maxReceive;
					return initialMaxReceive;
				}
				Iterator<Map.Entry<BlockPos, ClientReference>> it = clients.entrySet().iterator();
				while(it.hasNext()) {
					ClientReference ref = it.next().getValue();
					if(!ref.isEmpty()) {
						int accepted = ref.receiveEnergy(portions, simulate);
						if(accepted > 0) {
							_cont = true;
							maxReceive -= accepted;
							consumed += accepted;
						}
					}else {
						it.remove();
					}
				}
			}
			return Math.max(0, consumed - initialEnergyBuffer);
		}else {
			return 0;
		}
	}

	@Override
	public int extractEnergy(int maxExtract, boolean simulate) {
		if(clients.size() > 0) {
			int extracted = 0;
			Iterator<Map.Entry<BlockPos, ClientReference>> it = clients.entrySet().iterator();
			while(maxExtract > 0 && it.hasNext()) {
				ClientReference ref = it.next().getValue();
				if(!ref.isEmpty()) {
					int taken = ref.extractEnergy(maxExtract, simulate);
					if(taken > 0) {
						maxExtract -= taken;
						extracted += taken;
					}
				}else {
					it.remove();
				}
			}
			return extracted;
		}else {
			return 0;
		}
	}

	@Override
	public int getEnergyStored() {
		if(clients.size() > 0) {
			int energy = 0;
			Iterator<Map.Entry<BlockPos, ClientReference>> it = clients.entrySet().iterator();
			while(it.hasNext()) {
				ClientReference ref = it.next().getValue();
				if(!ref.isEmpty()) {
					int _en = ref.getEnergyStored();
					if(_en <= Integer.MAX_VALUE - energy) {
						energy += _en;
					}else {
						return Integer.MAX_VALUE;
					}
				}else {
					it.remove();
				}
			}
			return energy;
		}else {
			return 0;
		}
	}

	@Override
	public int getMaxEnergyStored() {
		if(clients.size() > 0) {
			int max_energy = 0;
			Iterator<Map.Entry<BlockPos, ClientReference>> it = clients.entrySet().iterator();
			while(it.hasNext()) {
				ClientReference ref = it.next().getValue();
				if(!ref.isEmpty()) {
					int _en = ref.getMaxEnergyStored();
					if(_en <= Integer.MAX_VALUE - max_energy) {
						max_energy += _en;
					}else {
						return Integer.MAX_VALUE;
					}
				}else {
					it.remove();
				}
			}
			return max_energy;
		}else {
			return 0;
		}
	}

	@Override
	public boolean canExtract() {
		Iterator<Map.Entry<BlockPos, ClientReference>> it = clients.entrySet().iterator();
		while(it.hasNext()) {
			ClientReference ref = it.next().getValue();
			if(!ref.isEmpty()) {
				if(ref.canExtract())return true;
			}else {
				it.remove();
			}
		}
		return false;
	}

	@Override
	public boolean canReceive() {
		Iterator<Map.Entry<BlockPos, ClientReference>> it = clients.entrySet().iterator();
		while(it.hasNext()) {
			ClientReference ref = it.next().getValue();
			if(!ref.isEmpty()) {
				if(ref.canReceive())return true;
			}else {
				it.remove();
			}
		}
		return false;
	}

	@Override
	public void registerClient(BlockPos pos, EnumFacing face) {
		ClientReference ref = clients.get(pos);
		if(ref == null) {
			ref = new ClientReference(pos);
			clients.put(pos, ref);
		}
		ref.addFace(face);
	}

	@Override
	public boolean unregisterClient(BlockPos pos, EnumFacing face) {
		ClientReference ref = clients.get(pos);
		if(ref != null) {
			boolean r = ref.faces.remove(face);
			if(ref.faces.size() == 0) {
				clients.remove(pos);
			}
			return r;
		}
		return false;
	}

	@Override
	public void registerNetworkBlock(BlockPos pos) {
		networkBlocks.add(pos);
	}

	@Override
	public boolean unregisterNetworkBlock(BlockPos pos) {
		return networkBlocks.remove(pos);
	}

	@Override
	public IEnergyNetwork copy() {
		NetworkImpl n = new NetworkImpl(world);
		n.clients.putAll(clients);
		n.networkBlocks.addAll(networkBlocks);
		return n;
	}

	@Override
	public void reset() {
		for(BlockPos pos : networkBlocks) {
			TileEntity te = world.getTileEntity(pos);
			if(te != null && te.hasCapability(IEnergyNetworkProvider.CAPABILITY, null)) {
				te.getCapability(IEnergyNetworkProvider.CAPABILITY, null).reset();
			}
		}
		networkBlocks.clear();
		clients.clear();
	}

	@Override
	public int clientCount() {
		return clients.size();
	}
	
	class ClientReference implements IEnergyStorage{
		
		private final BlockPos pos;
		private final HashSet<EnumFacing> faces = new HashSet<>();
		private int energyBuffer = 0;
		
		private ClientReference(BlockPos pos) {
			this.pos = pos;
		}
		
		private ClientReference addFace(EnumFacing face) {
			this.faces.add(face);
			return this;
		}
		
		Set<EnumFacing> getFaces(){
			return Collections.unmodifiableSet(faces);
		}
		
		private boolean isEmpty() {
			return faces.size() == 0;
		}
		
		@Override
		public boolean equals(Object obj) {
			if(obj == null || obj.getClass() != ClientReference.class) {
				return false;
			}else if(obj == this) {
				return true;
			}else {
				return ((ClientReference)obj).pos.equals(this.pos);
			}
		}
		
		@Override
		public int hashCode() {
			return pos.hashCode();
		}

		@Override
		public int receiveEnergy(int maxReceive, boolean simulate) {
			if(maxReceive > 0 && faces.size() > 0) {
				final int initialEnergyBuffer = energyBuffer;
				final int initialMaxReceive = maxReceive;
				maxReceive += energyBuffer;
				energyBuffer = 0;
				TileEntity te = world.getTileEntity(pos);
				if(te != null) {
					int consumed = 0;
					boolean _cont = true;
					while(_cont && maxReceive > 0) {
						_cont = false;
						final int portions = maxReceive / faces.size();
						if(portions == 0) {
							energyBuffer += maxReceive;
							return initialMaxReceive;
						}
						Iterator<EnumFacing> it = faces.iterator();
						while(it.hasNext()) {
							IEnergyStorage nrj = te.getCapability(CapabilityEnergy.ENERGY, it.next());
							if(nrj != null && !(nrj instanceof IEnergyNetworkProvider)) {
								int accepted = nrj.receiveEnergy(portions, simulate);
								if(accepted > 0) {
									_cont = true;
									maxReceive -= accepted;
									consumed += accepted;
								}
							}else {
								it.remove();
							}
						}
					}
					return Math.max(0, consumed - initialEnergyBuffer);
				}
			}
			return 0;
		}

		@Override
		public int extractEnergy(int maxExtract, boolean simulate) {
			if(faces.size() > 0) {
				TileEntity te = world.getTileEntity(pos);
				if(te != null) {
					int extracted = 0;
					Iterator<EnumFacing> it = faces.iterator();
					while(maxExtract > 0 && it.hasNext()) {
						IEnergyStorage nrj = te.getCapability(CapabilityEnergy.ENERGY, it.next());
						if(nrj != null && !(nrj instanceof IEnergyNetworkProvider)) {
							int taken = nrj.extractEnergy(maxExtract, simulate);
							if(taken > 0) {
								maxExtract -= taken;
								extracted += taken;
							}
						}else {
							it.remove();
						}
					}
					return extracted;
				}
			}
			return 0;
		}

		@Override
		public int getEnergyStored() {
			int energy = 0;
			if(faces.size() > 0) {
				TileEntity te = world.getTileEntity(pos);
				if(te != null) {
					Iterator<EnumFacing> it = faces.iterator();
					while(it.hasNext()) {
						IEnergyStorage nrj = te.getCapability(CapabilityEnergy.ENERGY, it.next());
						if(nrj != null && !(nrj instanceof IEnergyNetworkProvider)) {
							int _en = nrj.getEnergyStored();
							if(_en <= Integer.MAX_VALUE - energy) {
								energy += _en;
							}else {
								return Integer.MAX_VALUE;
							}
						}else {
							it.remove();
						}
					}
				}
			}
			return energy;
		}

		@Override
		public int getMaxEnergyStored() {
			int max_energy = 0;
			if(faces.size() > 0) {
				TileEntity te = world.getTileEntity(pos);
				if(te != null) {
					Iterator<EnumFacing> it = faces.iterator();
					while(it.hasNext()) {
						IEnergyStorage nrj = te.getCapability(CapabilityEnergy.ENERGY, it.next());
						if(nrj != null && !(nrj instanceof IEnergyNetworkProvider)) {
							int _en = nrj.getMaxEnergyStored();
							if(_en <= Integer.MAX_VALUE - max_energy) {
								max_energy += _en;
							}else {
								return Integer.MAX_VALUE;
							}
						}else {
							it.remove();
						}
					}
				}
			}
			return max_energy;
		}

		@Override
		public boolean canExtract() {
			if(faces.size() > 0) {
				TileEntity te = world.getTileEntity(pos);
				if(te != null) {
					Iterator<EnumFacing> it = faces.iterator();
					while(it.hasNext()) {
						IEnergyStorage nrj = te.getCapability(CapabilityEnergy.ENERGY, it.next());
						if(nrj != null && !(nrj instanceof IEnergyNetworkProvider)) {
							if(nrj.canExtract())return true;
						}else {
							it.remove();
						}
					}
				}
			}
			return false;
		}

		@Override
		public boolean canReceive() {
			if(faces.size() > 0) {
				TileEntity te = world.getTileEntity(pos);
				if(te != null) {
					Iterator<EnumFacing> it = faces.iterator();
					while(it.hasNext()) {
						IEnergyStorage nrj = te.getCapability(CapabilityEnergy.ENERGY, it.next());
						if(nrj != null && !(nrj instanceof IEnergyNetworkProvider)) {
							if(nrj.canReceive())return true;
						}else {
							it.remove();
						}
					}
				}
			}
			return false;
		}
	}

}
