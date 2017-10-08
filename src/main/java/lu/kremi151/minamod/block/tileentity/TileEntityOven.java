package lu.kremi151.minamod.block.tileentity;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityOven extends BaseTileEntity implements ITickable{
	
	public static final int MAX_BURNING_COUNTDOWN = 24, MAX_ENERGY = 200;
	private static final int ENERGY_CONSUMING = 25;
	
	private ItemStack tmpResult = null;
	private int burningCountdown = MAX_BURNING_COUNTDOWN;
	private int energy = 0;
	private boolean acceptingEnergy = false;

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
    {
        return capability == CapabilityEnergy.ENERGY || capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
    	if(capability == CapabilityEnergy.ENERGY) {
    		return (T) nrj;
    	}else if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
    		return (T) ((facing == EnumFacing.DOWN) ? outputInv : inputInv);
    	}else {
    		return super.getCapability(capability, facing);
    	}
    }
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		nbt = super.writeToNBT(nbt);
		nbt.setInteger("Energy", energy);
		if(!inputInv.getStackInSlot(0).isEmpty()) {
			NBTTagCompound inbt = new NBTTagCompound();
			inputInv.getStackInSlot(0).writeToNBT(inbt);
			nbt.setTag("InputItem", inbt);
		}
		if(!outputInv.getStackInSlot(0).isEmpty()) {
			NBTTagCompound inbt = new NBTTagCompound();
			outputInv.getStackInSlot(0).writeToNBT(inbt);
			nbt.setTag("OutputItem", inbt);
		}
		return nbt;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		if(nbt.hasKey("Energy", 99)) {
			energy = MathHelper.clamp(nbt.getInteger("Energy"), 0, MAX_ENERGY);
		}
		if(nbt.hasKey("InputItem", 10)) {
			ItemStack stack = new ItemStack(nbt.getCompoundTag("InputItem"));
			inputInv.setStackInSlot(0, stack);
		}
		if(nbt.hasKey("OutputItem", 10)) {
			ItemStack stack = new ItemStack(nbt.getCompoundTag("OutputItem"));
			outputInv.setStackInSlot(0, stack);
		}
	}

	@Override
	public void update() {
		if(!world.isRemote && world.getWorldTime() % 10 == 0) {
			if(energy > 0)energy = Math.max(energy - 5, 0);
			acceptingEnergy = false;
			if(!inputInv.getStackInSlot(0).isEmpty()) {
				if(tmpResult == null)tmpResult = FurnaceRecipes.instance().getSmeltingResult(inputInv.getStackInSlot(0));
				final ItemStack outstack = outputInv.getStackInSlot(0);
				if(!tmpResult.isEmpty() && (outstack.isEmpty() || (outstack.getCount() < outstack.getMaxStackSize() && ItemHandlerHelper.canItemStacksStack(outstack, tmpResult)))) {
					acceptingEnergy = true;
					if(energy >= ENERGY_CONSUMING) {
						energy -= ENERGY_CONSUMING;
						if(burningCountdown-- <= 0) {
							inputInv.getStackInSlot(0).shrink(1);
							if(outstack.isEmpty()) {
								outputInv.setStackInSlot(0, tmpResult.copy());
							}else {
								outstack.grow(1);
							}
							burningCountdown = MAX_BURNING_COUNTDOWN;
						}
					}else {
						burningCountdown = MAX_BURNING_COUNTDOWN;
					}
				}else {
					burningCountdown = MAX_BURNING_COUNTDOWN;
				}
			}else {
				burningCountdown = MAX_BURNING_COUNTDOWN;
			}
			sync();
		}
	}
	
	public IItemHandler getInputInventory() {
		return inputInv;
	}
	
	public IItemHandler getOutputInventory() {
		return outputInv;
	}
	
	/*public float getBurningProgress() {
		return 1.0f - ((float)burningCountdown / (float)MAX_BURNING_COUNTDOWN);
	}*/
	
	public int getCookProgress() {
		return MAX_BURNING_COUNTDOWN - burningCountdown;
	}
	
	public boolean isCooking() {
		return this.acceptingEnergy;
	}
	
	public int getEnergy() {
		return this.energy;
	}
	
	private final IEnergyStorage nrj = new IEnergyStorage() {

		@Override
		public int receiveEnergy(int maxReceive, boolean simulate) {
			if(maxReceive > 0 && acceptingEnergy) {
				final int nval = energy + maxReceive;
				if(nval > MAX_ENERGY) {
					int consumed = MAX_ENERGY - energy;
					if(!simulate)energy = MAX_ENERGY;
					return consumed;
				}else {
					if(!simulate)energy = nval;
					return maxReceive;
				}
			}else {
				return 0;
			}
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
			return MAX_ENERGY;
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
	
	private final ItemStackHandler inputInv = new ItemStackHandler(1) {
		@Override
		protected void onContentsChanged(int slot)
	    {
			if(!this.stacks.get(0).isEmpty()) {
				tmpResult = FurnaceRecipes.instance().getSmeltingResult(this.stacks.get(0));
			}else {
				tmpResult = null;
			}
	    }
	}, outputInv = new ItemStackHandler(1);
	
	@Override
	public NBTTagCompound getUpdateTag()
    {
		NBTTagCompound nbt = super.getUpdateTag();
		nbt.setBoolean("Cooking", this.acceptingEnergy);
		return nbt;
    }

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(this.pos, 0, getUpdateTag());
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
		final NBTTagCompound nbt = packet.getNbtCompound();
		this.acceptingEnergy = nbt.getBoolean("Cooking");
		sync();
        world.updateComparatorOutputLevel(pos, blockType);
	}
	
}
