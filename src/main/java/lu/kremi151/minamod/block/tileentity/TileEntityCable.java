package lu.kremi151.minamod.block.tileentity;

import javax.annotation.Nullable;

import lu.kremi151.minamod.block.BlockPipeBase;
import lu.kremi151.minamod.capabilities.energynetwork.EnergyNetworkHelper;
import lu.kremi151.minamod.capabilities.energynetwork.IEnergyNetwork;
import lu.kremi151.minamod.capabilities.energynetwork.IEnergyNetworkProvider;
import lu.kremi151.minamod.capabilities.energynetwork.NetworkProviderImpl;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
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

		@Override
		public void onNeighbourNetworkChanged(BlockPos neighbor, IEnergyNetwork newNetwork) {
			if(this.hasNetwork()) {
				if(!this.getNetwork().equals(newNetwork)) {
					setNetwork(EnergyNetworkHelper.combine(this.getNetwork(), newNetwork));
				}
			}else {
				setNetwork(newNetwork);
				for(EnumFacing face : EnumFacing.VALUES) {
					BlockPos npos = TileEntityCable.this.pos.offset(face);
					TileEntity te = TileEntityCable.this.world.getTileEntity(npos);
					if(te != null && !te.hasCapability(IEnergyNetworkProvider.CAPABILITY, face.getOpposite()) && te.hasCapability(CapabilityEnergy.ENERGY, face.getOpposite())) {
						getNetwork().registerClient(npos, face.getOpposite());
					}
				}
			}
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

	@Override
	public void readFromNBT(NBTTagCompound nbt){
		super.readFromNBT(nbt);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt){
		super.writeToNBT(nbt);
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
