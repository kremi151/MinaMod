package lu.kremi151.minamod.capabilities.energynetwork;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class NetworkImpl implements IEnergyNetwork{
	
	private final LinkedList<NetworkPointer> pointers = new LinkedList<>();
	private final HashMap<BlockPos, ClientReference> clients = new HashMap<>();
	private final World world;
	
	NetworkImpl(World world){
		this.world = world;
		pointers.add(new NetworkPointer(this));
	}

	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) {
		int consumed = 0;
		boolean _cont = true;
		while(_cont && maxReceive > 0) {
			_cont = false;
			final int portions = maxReceive / clients.size();
			Iterator<Map.Entry<BlockPos, ClientReference>> it = clients.entrySet().iterator();
			while(it.hasNext()) {
				ClientReference ref = it.next().getValue();
				if(!ref.isEmpty()) {
					int accepted = ref.receiveEnergy(maxReceive, simulate);
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
		return consumed;
	}

	@Override
	public int extractEnergy(int maxExtract, boolean simulate) {
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
	}

	@Override
	public int getEnergyStored() {
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
	}

	@Override
	public int getMaxEnergyStored() {
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
	
	private class ClientReference implements IEnergyStorage{
		
		private final BlockPos pos;
		private final HashSet<EnumFacing> faces = new HashSet<>();
		
		private ClientReference(BlockPos pos) {
			this.pos = pos;
		}
		
		private ClientReference addFace(EnumFacing face) {
			this.faces.add(face);
			return this;
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
			if(faces.size() > 0) {
				TileEntity te = world.getTileEntity(pos);
				if(te != null) {
					int consumed = 0;
					boolean _cont = true;
					while(_cont && maxReceive > 0) {
						_cont = false;
						final int portions = maxReceive / faces.size();
						Iterator<EnumFacing> it = faces.iterator();
						while(it.hasNext()) {
							IEnergyStorage nrj = te.getCapability(CapabilityEnergy.ENERGY, it.next());
							if(nrj != null && !(nrj instanceof IEnergyNetworkProvider)) {
								int accepted = nrj.receiveEnergy(maxReceive, simulate);
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
					return consumed;
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
