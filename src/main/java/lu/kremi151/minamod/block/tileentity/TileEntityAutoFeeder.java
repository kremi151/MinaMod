package lu.kremi151.minamod.block.tileentity;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

public class TileEntityAutoFeeder extends TileEntitySidedInventory implements ITickable{
	
	public static final int POWER_TO_EXTRACT = 200;
	
	private ItemStack food = ItemStack.EMPTY;
	
	private AxisAlignedBB checkRange;
	
	private int ticksLeft = 0;
	private int maxTicks = 1000;

	private final IEnergyStorage nrj;
	
	public TileEntityAutoFeeder(){
		super(1, "tile.autofeeder.name");
		this.nrj = new EnergyStorage(9999);
	}
	
	public void setFood(@Nonnull ItemStack is){
		setInventorySlotContents(0, is);
		markDirty();
	}
	
	public ItemStack getFood(){
		return getStackInSlot(0);
	}
	
	public int getTicksLeft(){
		return ticksLeft;
	}
	
	public int getMaxTicks(){
		return maxTicks;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt){
		super.readFromNBT(nbt);
		if(nbt.hasKey("foodItem", 10)){
			NBTTagCompound inbt = nbt.getCompoundTag("foodItem");
			setInventorySlotContents(0, new ItemStack(inbt));
		}
		if(nbt.hasKey("ticksLeft", 99)){
			this.ticksLeft = nbt.getInteger("ticksLeft");
		}
		if(nbt.hasKey("maxTicks", 99)){
			this.maxTicks = nbt.getInteger("maxTicks");
		}
		if(nbt.hasKey("energy", 99)){
			nrj.extractEnergy(nrj.getEnergyStored(), false);
			nrj.receiveEnergy(nbt.getInteger("energy"), false);
		}
		
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt){
		super.writeToNBT(nbt);
		return writeToNBT(nbt, false);
	}
	
	private NBTTagCompound writeToNBT(NBTTagCompound nbt, boolean update){
		if(!update){
			ItemStack item = getStackInSlot(0);
			if(!item.isEmpty()){
				NBTTagCompound inbt = new NBTTagCompound();
				item.writeToNBT(inbt);
				nbt.setTag("foodItem", inbt);
			}
		}
		nbt.setInteger("ticksLeft", ticksLeft);
		nbt.setInteger("maxTicks", maxTicks);
		nbt.setInteger("energy", nrj.getEnergyStored());
		return nbt;
	}
	
	@Override
	public NBTTagCompound getUpdateTag()
    {
		return writeToNBT(super.getUpdateTag(), true);
    }
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket(){
		return new SPacketUpdateTileEntity(this.pos, 0, getUpdateTag());
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
        readFromNBT(packet.getNbtCompound());
	}
	
	public boolean hasEnoughPower(){
		return nrj.extractEnergy(POWER_TO_EXTRACT, true) >= POWER_TO_EXTRACT;
	}
	
	public int getEnergy(){
		return nrj.getEnergyStored();
	}
	
	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
		return (index == 1)?(!itemStackIn.isEmpty() && itemStackIn.hasCapability(CapabilityEnergy.ENERGY, null)):super.canInsertItem(index, itemStackIn, direction);
	}
	
	@Override
	public boolean canExtractItem(int index, ItemStack itemStackIn, EnumFacing direction) {
		return index != 1 && super.canExtractItem(index, itemStackIn, direction);
	}

	@Override
	public void update() {
		if(world.isRemote || !hasEnoughPower())return;
		
		if(checkRange == null){
			this.checkRange = new AxisAlignedBB((double)this.pos.getX() - 2.0, (double)this.pos.getY() - 2.0, (double)this.pos.getZ() - 2.0, (double)this.pos.getX() + 2.0, (double)this.pos.getY() + 2.0, (double)this.pos.getZ() + 2.0);
		}
		if(getFood().isEmpty()){
			this.ticksLeft = this.maxTicks;
		}else if(this.ticksLeft-- <= 0){
			List<EntityAnimal> l = this.world.getEntitiesWithinAABB(EntityAnimal.class, checkRange);
			int s = l.size();
			for(int i = 0 ; i < s ; i++){
				EntityAnimal a = l.get(i);
				if(a.isBreedingItem(getFood()) && (!a.isInLove() || a.isChild())){
					if(!hasEnoughPower()){
						break;
					}
					ItemStack is = getFood();
					is.shrink(1);
					a.setInLove(null);
					nrj.extractEnergy(POWER_TO_EXTRACT, false);
					if(is.getCount() == 0){
						setFood(ItemStack.EMPTY);
						break;
					}
				}
			}
			markDirty();
			this.ticksLeft = this.maxTicks;
		}
	}

	@Override
	public void onCraftMatrixChanged() {
        markDirty();
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
	
}
