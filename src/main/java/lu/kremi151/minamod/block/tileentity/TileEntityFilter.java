package lu.kremi151.minamod.block.tileentity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.Validate;

import lu.kremi151.minamod.block.BlockFilter;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.IHopper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;

public class TileEntityFilter extends TileEntity implements IHopper, ITickable
{
    private NonNullList<ItemStack> inventory = NonNullList.<ItemStack>withSize(1, ItemStack.EMPTY);
    private int transferCooldown = -1;
    private long tickedGameTime;
    private String customName = null;

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.inventory = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);

        if (compound.hasKey("Item", 10)) {
        	this.inventory.add(new ItemStack(compound.getCompoundTag("Item")));
        }

        if (compound.hasKey("CustomName", 8))
        {
            this.customName = compound.getString("CustomName");
        }

        this.transferCooldown = compound.getInteger("TransferCooldown");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);

        //ItemStackHelper.saveAllItems(compound, this.inventory);
        if(!this.inventory.get(0).isEmpty()) {
        	compound.setTag("Item", this.inventory.get(0).writeToNBT(new NBTTagCompound()));
        }

        compound.setInteger("TransferCooldown", this.transferCooldown);

        if (this.hasCustomName())
        {
            compound.setString("CustomName", this.customName);
        }

        return compound;
    }
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
    {
        return oldState.getBlock() != newSate.getBlock();
    }

    @Override
    public void update()//Kinda modified
    {
        if (this.world != null && !this.world.isRemote)
        {
            --this.transferCooldown;
            this.tickedGameTime = this.world.getTotalWorldTime();

            if (!this.isOnTransferCooldown())
            {
                this.setTransferCooldown(0);
                this.updateFilter();
            }
        }
    }

    protected boolean updateFilter()//Kinda modified
    {
        if (this.world != null && !this.world.isRemote)
        {
            if (!this.isOnTransferCooldown() && BlockFilter.isEnabled(this.getBlockMetadata()))
            {
                boolean flag = false;

                if (!this.isInventoryEmpty())
                {
                    flag = this.transferItemsOut();
                }

                if (!this.isFull())
                {
                    flag = TileEntityHopper.captureDroppedItems(this) || flag;
                }

                if (flag)
                {
                    this.setTransferCooldown(8);
                    this.markDirty();
                    return true;
                }
            }

            return false;
        }
        else
        {
            return false;
        }
    }

    private boolean isInventoryEmpty()
    {
        for (ItemStack itemstack : this.inventory)
        {
            if (!itemstack.isEmpty())
            {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean isEmpty()
    {
        return this.isInventoryEmpty();
    }

    private boolean isFull()
    {
        for (ItemStack itemstack : this.inventory)
        {
            if (itemstack.isEmpty() || itemstack.getCount() != itemstack.getMaxStackSize())
            {
                return false;
            }
        }

        return true;
    }

    private boolean transferItemsOut()//Modified
    {
        IInventory iinventory = this.getInventoryForFilterTransfer();

        if (iinventory == null)
        {
            return false;
        }
        else
        {
            EnumFacing enumfacing = BlockFilter.getFacing(this.getBlockMetadata()).getOpposite();

            if (this.isInventoryFull(iinventory, enumfacing))
            {
                return false;
            }
            else
            {
            	if (!this.getStackInSlot(0).isEmpty() && this.getStackInSlot(0).getCount() > 1)
                {
                    ItemStack itemstack = this.getStackInSlot(0).copy();
                    ItemStack itemstack1 = TileEntityHopper.putStackInInventoryAllSlots(this, iinventory, this.decrStackSize(0, 1), enumfacing);

                    if (itemstack1.isEmpty())
                    {
                        iinventory.markDirty();
                        return true;
                    }

                    this.setInventorySlotContents(0, itemstack);
                }

                return false;
            }
        }
    }

    private boolean isInventoryFull(IInventory inventoryIn, EnumFacing side)
    {
        if (inventoryIn instanceof ISidedInventory)
        {
            ISidedInventory isidedinventory = (ISidedInventory)inventoryIn;
            int[] aint = isidedinventory.getSlotsForFace(side);

            for (int k : aint)
            {
                ItemStack itemstack1 = isidedinventory.getStackInSlot(k);

                if (itemstack1.isEmpty() || itemstack1.getCount() != itemstack1.getMaxStackSize())
                {
                    return false;
                }
            }
        }
        else
        {
            int i = inventoryIn.getSizeInventory();

            for (int j = 0; j < i; ++j)
            {
                ItemStack itemstack = inventoryIn.getStackInSlot(j);

                if (itemstack.isEmpty() || itemstack.getCount() != itemstack.getMaxStackSize())
                {
                    return false;
                }
            }
        }

        return true;
    }
    
	@Override
    public boolean hasCapability(net.minecraftforge.common.capabilities.Capability<?> capability, @Nullable net.minecraft.util.EnumFacing facing)
    {
        return (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) || super.hasCapability(capability, facing);
    }

	private net.minecraftforge.items.IItemHandler itemHandler;

    protected net.minecraftforge.items.IItemHandler createUnSidedHandler()
    {
        return new net.minecraftforge.items.wrapper.InvWrapper(this);
    }

    @Override
    @javax.annotation.Nullable
    public <T> T getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @Nullable net.minecraft.util.EnumFacing facing)
    {
        if (capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return (T) (itemHandler == null ? (itemHandler = createUnSidedHandler()) : itemHandler);
        return super.getCapability(capability, facing);
    }

    private IInventory getInventoryForFilterTransfer()
    {
        EnumFacing enumfacing = BlockFilter.getFacing(this.getBlockMetadata());
        return TileEntityHopper.getInventoryAtPosition(this.getWorld(), this.getXPos() + (double)enumfacing.getFrontOffsetX(), this.getYPos() + (double)enumfacing.getFrontOffsetY(), this.getZPos() + (double)enumfacing.getFrontOffsetZ());
    }

    @Override
    public double getXPos()
    {
        return (double)this.pos.getX() + 0.5D;
    }

    @Override
    public double getYPos()
    {
        return (double)this.pos.getY() + 0.5D;
    }

    @Override
    public double getZPos()
    {
        return (double)this.pos.getZ() + 0.5D;
    }

    public void setTransferCooldown(int ticks)
    {
        this.transferCooldown = ticks;
    }

    private boolean isOnTransferCooldown()
    {
        return this.transferCooldown > 0;
    }

    public boolean mayTransfer()
    {
        return this.transferCooldown > 8;
    }

    public long getLastUpdateTime() { return tickedGameTime; } // Forge

	@Override
	public ItemStack getStackInSlot(int index) {
		return (ItemStack)this.inventory.get(index);
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		return ItemStackHelper.getAndRemove(this.inventory, index);
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return true;
	}

	@Override
	public void openInventory(EntityPlayer player) {}

	@Override
	public void closeInventory(EntityPlayer player) {}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return true;
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void clear() {
		this.inventory.clear();
	}

	@Override
	public boolean hasCustomName() {
		return this.customName != null;
	}
	
	public void setCustomName(String name) {
		this.customName = name;
	}
	
	@Override
    public int getSizeInventory()
    {
        return this.inventory.size();
    }

    @Override
    public ItemStack decrStackSize(int index, int count)
    {
        ItemStack itemstack = ItemStackHelper.getAndSplit(this.inventory, index, count);
        return itemstack;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack)
    {
    	this.inventory.set(index, stack);

        if (stack.getCount() > this.getInventoryStackLimit())
        {
            stack.setCount(this.getInventoryStackLimit());
        }
    }

    @Override
    public String getName()
    {
        return this.hasCustomName() ? this.customName : "container.filter";
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

}
