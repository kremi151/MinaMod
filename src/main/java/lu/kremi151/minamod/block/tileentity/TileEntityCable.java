package lu.kremi151.minamod.block.tileentity;

import javax.annotation.Nullable;

import lu.kremi151.minamod.block.BlockPipeBase;
import lu.kremi151.minamod.capabilities.energynetwork.IEnergyNetwork;
import lu.kremi151.minamod.capabilities.energynetwork.IEnergyNetworkProvider;
import lu.kremi151.minamod.capabilities.energynetwork.NetworkProviderImpl;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;

public class TileEntityCable extends TileEntity /*implements ITickable*/{

	@Nullable
	private final NetworkProviderImpl networkProvider = new NetworkProviderImpl() {

		@Override
		protected World getWorld() {
			return TileEntityCable.this.world;
		}

		@Override
		protected BlockPos getPos() {
			return TileEntityCable.this.pos;
		}
		
	};
	
	public TileEntityCable(){
		super();
	}
	
	private BlockPipeBase getCable(){
		return (BlockPipeBase) this.getBlockType();
	}
	
	private IBlockState getActualState(){
		return ((BlockPipeBase) this.getBlockType()).getActualState(world.getBlockState(pos), world, pos);
	}

	/*@Override
	public void update() {
		IBlockState as = getActualState();
		if(as != null){
			int count = 1;
			long energyTotal = nrj.getEnergyStored();
			for(EnumFacing ef : EnumFacing.VALUES){
				if(getCable().isConnected(as, ef)){
					TileEntity te = world.getTileEntity(pos.offset(ef));
					if(te != null && te.hasCapability(CapabilityEnergy.ENERGY, null)){//TODO:Face
						IEnergyStorage nrj = te.getCapability(CapabilityEnergy.ENERGY, null);//TODO:Face
						if(nrj.canReceive() && nrj.getEnergyStored() < this.nrj.getEnergyStored()){
							count ++;
							energyTotal += nrj.getEnergyStored();
						}
					}
				}
			}
			int partialEnergy = Math.max(0, (int)(energyTotal / count));
			for(EnumFacing ef : EnumFacing.VALUES){
				if(getCable().isConnected(as, ef)){
					TileEntity te = world.getTileEntity(pos.offset(ef));
					if(te != null && te.hasCapability(CapabilityEnergy.ENERGY, null)){//TODO:Face
						IEnergyStorage nrj = te.getCapability(CapabilityEnergy.ENERGY, null);//TODO:Face
						if(nrj.canReceive() && nrj.getEnergyStored() < partialEnergy){
							int toGive = partialEnergy - nrj.getEnergyStored();
							if(this.nrj.getEnergyStored() >= toGive)this.nrj.extractEnergy(nrj.receiveEnergy(toGive, false), false);
						}
					}
				}
			}
		}
	}*/

	@Override
	public void readFromNBT(NBTTagCompound nbt){
		super.readFromNBT(nbt);
		/*if(nbt.hasKey("Energy", 99)){
			nrj.extractEnergy(nrj.getEnergyStored(), false);
			nrj.receiveEnergy(nbt.getInteger("Energy"), false);
		}*/
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt){
		super.writeToNBT(nbt);
		//nbt.setInteger("Energy", nrj.getEnergyStored());
		return nbt;
	}
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket(){
		NBTTagCompound tagPacket = new NBTTagCompound();
		writeToNBT(tagPacket);
		return new SPacketUpdateTileEntity(this.pos, 0, tagPacket);
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
        readFromNBT(packet.getNbtCompound());
	}
	
	@Override
    public boolean hasCapability(net.minecraftforge.common.capabilities.Capability<?> capability, @Nullable net.minecraft.util.EnumFacing facing)
    {
        return (capability == IEnergyNetworkProvider.CAPABILITY || capability == CapabilityEnergy.ENERGY) || super.hasCapability(capability, facing);
    }

    @Override
    @Nullable
    public <T> T getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @Nullable net.minecraft.util.EnumFacing facing)
    {
        return (T) ((capability == IEnergyNetworkProvider.CAPABILITY || capability == CapabilityEnergy.ENERGY) ? networkProvider : super.getCapability(capability, facing));
    }
}
