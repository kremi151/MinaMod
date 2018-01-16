package lu.kremi151.minamod.block.tileentity;

import javax.annotation.Nullable;

import lu.kremi151.minamod.MinaBlocks;
import lu.kremi151.minamod.capabilities.AccessableEnergyStorage;
import lu.kremi151.minamod.inventory.BaseInventoryImpl;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.InvWrapper;

public class TileEntityCoalCompressor extends BaseTileEntity implements ITickable{
	
	private final AccessableEnergyStorage nrj = new AccessableEnergyStorage(200, 200, 0);
	private final IInventory invIn = new BaseInventoryImpl("input", 1);
	private final IInventory invOut = new BaseInventoryImpl("output", 1);
	private final IItemHandler invInHandler = new InvWrapper(invIn), invOutHandler = new InvWrapper(invOut);//TODO: Out should be read-only

	private static final int MAX_COMPRESSING_TICKS = 2000;//TODO: Adjust
	private int compressingTicks = 0;
	private boolean compressing = false;
	
    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
    {
        return capability == CapabilityEnergy.ENERGY || capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
    	if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
    		if(facing == EnumFacing.DOWN) {
    			return (T) invOutHandler;
    		}else {
    			return (T) invInHandler;
    		}
    	}else {
    		return (T) (capability == CapabilityEnergy.ENERGY ? nrj : super.getCapability(capability, facing));
    	}
    }

	@Override
	public void update() {
		if(!world.isRemote && world.getWorldTime() % 10 == 0) {
			ItemStack stack = invIn.getStackInSlot(0);
			if(!stack.isEmpty() && stack.getItem() == Items.COAL && stack.getCount() >= 8) {
				if(nrj.getEnergyStored() >= 200) {
					compressing = true;
					if(++compressingTicks >= MAX_COMPRESSING_TICKS) {
						ItemStack outstack = invOut.getStackInSlot(0);
						ItemStack result = new ItemStack(Items.DIAMOND);
						if(outstack.isEmpty()) {
							invOut.setInventorySlotContents(0, result);
							stack.shrink(8);
						}else if(outstack.getCount() < outstack.getMaxStackSize() && ItemHandlerHelper.canItemStacksStack(outstack, result)) {
							outstack.grow(1);
							stack.shrink(8);
						}
						compressingTicks = 0;
					}
					nrj.setEnergy(0);
				}else {
					compressing = false;
				}
			}else {
				compressingTicks = 0;
				compressing = false;
			}
			sync();
		}
	}
	
	public float getProgress() {
		return (float)compressingTicks / (float)MAX_COMPRESSING_TICKS;
	}
	
	public boolean isCompressing() {
		return compressing;
	}
	
	public IInventory getInputInventory() {
		return invIn;
	}
	
	public IInventory getOutputInventory() {
		return invOut;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt){
		super.readFromNBT(nbt);
		if(nbt.hasKey("ItemIn", 10)) {
			invIn.setInventorySlotContents(0, new ItemStack(nbt.getCompoundTag("ItemIn")));
		}
		if(nbt.hasKey("ItemOut", 10)) {
			invOut.setInventorySlotContents(0, new ItemStack(nbt.getCompoundTag("ItemOut")));
		}
		compressingTicks = nbt.getInteger("CompressTicks");
		compressing = nbt.getBoolean("IsCompressing");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt){
		super.writeToNBT(nbt);
		ItemStack item = invIn.getStackInSlot(0);
		if(!item.isEmpty()) {
			nbt.setTag("ItemIn", item.writeToNBT(new NBTTagCompound()));
		}
		item = invOut.getStackInSlot(0);
		if(!item.isEmpty()) {
			nbt.setTag("ItemOut", item.writeToNBT(new NBTTagCompound()));
		}
		nbt.setInteger("CompressTicks", compressingTicks);
		return nbt;
	}
	
	@Override
	public NBTTagCompound getUpdateTag()
    {
		NBTTagCompound nbt = super.getUpdateTag();
		nbt.setInteger("CompressTicks", compressingTicks);
		nbt.setBoolean("IsCompressing", compressing);
		return nbt;
    }

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(this.pos, 0, getUpdateTag());
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
		readFromNBT(packet.getNbtCompound());
		IBlockState state = world.getBlockState(getPos());
		world.notifyBlockUpdate(getPos(), state, MinaBlocks.COMPRESSOR.getActualState(state, world, pos), 3);
	}
}
