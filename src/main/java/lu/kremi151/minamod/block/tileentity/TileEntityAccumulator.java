package lu.kremi151.minamod.block.tileentity;

import javax.annotation.Nullable;

import lu.kremi151.minamod.block.BlockAccumulator;
import lu.kremi151.minamod.capabilities.AccessableEnergyStorage;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class TileEntityAccumulator extends BaseTileEntity implements ITickable{
	
	private static final int MAX_EXTRACT = 150;
	private final AccessableEnergyStorage nrj = new AccessableEnergyStorage(8000, 200, MAX_EXTRACT) {
		
		@Override
	    public int receiveEnergy(int maxReceive, boolean simulate)
	    {
			int res = super.receiveEnergy(maxReceive, simulate);
			if(!simulate && res != 0) sync();
			return res;
	    }
		
	};
	private int lastStateCharge = 0;

	@Override
	public void update() {
		if(!world.isRemote && world.getWorldTime() % 10 == 0 && !isPushingBlocked()) {
			EnumFacing dir = getDirection();
			TileEntity te = world.getTileEntity(pos.offset(dir));
			if(te != null && te.hasCapability(CapabilityEnergy.ENERGY, dir.getOpposite())) {
				int extract = nrj.extractEnergy(MAX_EXTRACT, false);
				int consumed = te.getCapability(CapabilityEnergy.ENERGY, dir.getOpposite()).receiveEnergy(extract, false);
				nrj.receiveEnergy(extract - consumed, false);
				int newStateCharge = getStateCharge();
				if(lastStateCharge != newStateCharge) {
					lastStateCharge = newStateCharge;
					sync();
				}
			}
		}
	}
	
	private IBlockState getState() {
		return world.getBlockState(pos);
	}
	
	private EnumFacing getDirection() {
		return getState().getValue(BlockAccumulator.FACING);
	}
	
	private boolean isPushingBlocked() {
		return world.isBlockPowered(pos);
	}
	
	public int getStateCharge() {
		return (4 * nrj.getEnergyStored()) / nrj.getMaxEnergyStored();
	}

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
    {
        return (capability == CapabilityEnergy.ENERGY && facing == getDirection()) || super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
        return (T) ((capability == CapabilityEnergy.ENERGY && facing == getDirection()) ? nrj : super.getCapability(capability, facing));
    }
    
    /**
     * @return Returns energy capability, bypassing facing check
     */
    public IEnergyStorage getEnergy() {
    	return nrj;
    }
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		nbt = super.writeToNBT(nbt);
		nbt.setInteger("Energy", nrj.getEnergyStored());
		return nbt;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		if(nbt.hasKey("Energy", 99)) {
			nrj.setEnergy(nbt.getInteger("Energy"));
		}
		if(nbt.hasKey("Capacity", 99)) {
			nrj.setCapacity(Math.max(nbt.getInteger("Capacity"), 0));
		}
	}
	
	@Override
	public NBTTagCompound getUpdateTag()
    {
		NBTTagCompound nbt = super.getUpdateTag();
		nbt.setInteger("Capacity", nrj.getMaxEnergyStored());
		nbt.setInteger("Energy", nrj.getEnergyStored());
		return nbt;
    }

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(this.pos, 0, getUpdateTag());
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
		readFromNBT(packet.getNbtCompound());
		sync();
	}
}
