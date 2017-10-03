package lu.kremi151.minamod.block.tileentity;

import javax.annotation.Nullable;

import lu.kremi151.minamod.block.BlockEnergyToRedstone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class TileEntityEnergyToRedstone extends TileEntity implements ITickable{
	
	private int energy = 0, output = 0;
	private final IEnergyStorage nrj = new IEnergyStorage() {

		@Override
		public int receiveEnergy(int maxReceive, boolean simulate) {
			int rest = MathHelper.clamp(15 - energy, 0, 15);
			int consume = Math.min(rest, maxReceive);
			energy += consume;
			return consume;
		}

		@Override
		public int extractEnergy(int maxExtract, boolean simulate) {
			return 0;
		}

		@Override
		public int getEnergyStored() {
			return energy;
		}

		@Override
		public int getMaxEnergyStored() {
			return 15;
		}

		@Override
		public boolean canExtract() {
			return false;
		}

		@Override
		public boolean canReceive() {
			return true;
		}
		
	};

	@Override
	public void update() {
        if (this.world != null && !this.world.isRemote && this.world.getTotalWorldTime() % 20L == 0L)
        {
        	updateOutput();
        }
	}
	
	private EnumFacing getFacing() {
		return world.getBlockState(pos).getValue(BlockEnergyToRedstone.FACING);
	}
	
    @Override
    public boolean hasCapability(net.minecraftforge.common.capabilities.Capability<?> capability, @Nullable net.minecraft.util.EnumFacing facing)
    {
        return (capability == CapabilityEnergy.ENERGY && facing == getFacing().getOpposite()) || super.hasCapability(capability, facing);
    }

    @Override
    @Nullable
    public <T> T getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @Nullable net.minecraft.util.EnumFacing facing)
    {
        return (T) ((capability == CapabilityEnergy.ENERGY && facing == getFacing().getOpposite()) ? nrj : super.getCapability(capability, facing));
    }
	
	private void sync() {
		IBlockState state = world.getBlockState(pos);
        world.notifyBlockUpdate(pos, state, state, 3);
	}
	
	private void updateOutput() {
		if(output != energy) {
			output = energy;
			sync();
		}
		energy = 0;
		world.notifyNeighborsOfStateChange(pos, blockType, true);
	}
	
	public int getOutput() {
		return output;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt){
		super.readFromNBT(nbt);
		if(nbt.hasKey("Energy", 99)) {
        	this.energy = MathHelper.clamp(nbt.getInteger("Energy"), 0, 15);
        }
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt){
		nbt = super.writeToNBT(nbt);
		nbt.setInteger("Energy", energy);
		return nbt;
	}
	
	@Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound nbt = super.getUpdateTag();
		nbt.setInteger("Energy", energy);
		return nbt;
	}
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket(){
		return new SPacketUpdateTileEntity(this.pos, 0, getUpdateTag());
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
        readFromNBT(packet.getNbtCompound());
        if(packet.getNbtCompound().hasKey("Energy", 99)) {
        	this.energy = MathHelper.clamp(packet.getNbtCompound().getInteger("Energy"), 0, 15);
            sync();
        }
	}

}
