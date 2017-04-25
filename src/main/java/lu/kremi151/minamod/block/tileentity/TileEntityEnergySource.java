package lu.kremi151.minamod.block.tileentity;

import javax.annotation.Nullable;

import lu.kremi151.minamod.block.BlockPipeBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

public class TileEntityEnergySource extends TileEntity implements ITickable{

	private final IEnergyStorage nrj;
	
	public TileEntityEnergySource(){
		super();
		nrj = new EnergyStorage(9999);
	}
	
	private BlockPipeBase getCable(){
		return (BlockPipeBase) this.blockType;
	}
	
	private IBlockState getActualState(){
		return ((BlockPipeBase) this.blockType).getActualState(world.getBlockState(pos), world, pos);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt){
		super.readFromNBT(nbt);
		if(nbt.hasKey("Energy", 99)){
			nrj.extractEnergy(nrj.getEnergyStored(), false);
			nrj.receiveEnergy(nbt.getInteger("Energy"), false);
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt){
		super.writeToNBT(nbt);
		nbt.setInteger("Energy", nrj.getEnergyStored());
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
        return (capability == CapabilityEnergy.ENERGY) || super.hasCapability(capability, facing);
    }

    @Override
    @Nullable
    public <T> T getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @Nullable net.minecraft.util.EnumFacing facing)
    {
        return (T) ((capability == CapabilityEnergy.ENERGY) ? nrj : super.getCapability(capability, facing));
    }

	@Override
	public void update() {
		for(EnumFacing face : EnumFacing.values()){
			BlockPos pos = this.pos.offset(face);
			TileEntity te = this.world.getTileEntity(pos);
			if(te != null && te.hasCapability(CapabilityEnergy.ENERGY, null)){
				((IEnergyStorage)te.getCapability(CapabilityEnergy.ENERGY, null)).receiveEnergy(9999, false);
			}
		}
	}

}
