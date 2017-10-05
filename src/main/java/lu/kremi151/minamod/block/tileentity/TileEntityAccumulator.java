package lu.kremi151.minamod.block.tileentity;

import javax.annotation.Nullable;

import lu.kremi151.minamod.block.BlockAccumulator;
import lu.kremi151.minamod.capabilities.AccessableEnergyStorage;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;

public class TileEntityAccumulator extends TileEntity implements ITickable{
	
	private static final int MAX_EXTRACT = 150;
	private final AccessableEnergyStorage nrj = new AccessableEnergyStorage(8000, 200, MAX_EXTRACT);

	@Override
	public void update() {
		if(!world.isRemote && world.getWorldTime() % 10 == 0 && !isPushingBlocked()) {
			EnumFacing dir = getDirection();
			TileEntity te = world.getTileEntity(pos.offset(dir));
			if(te != null && te.hasCapability(CapabilityEnergy.ENERGY, dir.getOpposite())) {
				int extract = nrj.extractEnergy(MAX_EXTRACT, false);
				int consumed = te.getCapability(CapabilityEnergy.ENERGY, dir.getOpposite()).receiveEnergy(extract, false);
				nrj.receiveEnergy(extract - consumed, false);
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
	}
}
