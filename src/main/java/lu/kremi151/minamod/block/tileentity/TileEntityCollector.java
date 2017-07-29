package lu.kremi151.minamod.block.tileentity;

import java.util.List;

import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.enums.EnumParticleEffect;
import lu.kremi151.minamod.util.MinaUtils;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class TileEntityCollector extends TileEntitySidedInventory implements ITickable{
	
	private AxisAlignedBB rangeBox = null;
	
	private int ticksLeft = 0;
	private int maxTicks = 1000;
	private double range = 4.5;

	public TileEntityCollector() {
		super(9, "tile.collector.name");
	}

	@Override
	public void update() {
		if(!world.isRemote){
			if(rangeBox == null){
				BlockPos pos = getPos();
				double cx = pos.getX() + 0.5;
				double cy = pos.getY() + 0.5;
				double cz = pos.getZ() + 0.5;
				rangeBox = new AxisAlignedBB(cx - range, cy - range, cz - range, cx + range, cy + range, cz + range);
			}
			
			if(!isFull()){
				if(--ticksLeft <= 0){
					List<EntityItem> items = this.world.getEntitiesWithinAABB(EntityItem.class, rangeBox);
					for(EntityItem item : items){
						ItemStack res = tryInsert(item.getItem());
						if(res.isEmpty()){
							item.setDead();
						}else{
							item.setItem(res);
						}
						MinaMod.getProxy().spawnParticleEffectToAllAround(EnumParticleEffect.COLLECT, world, item.posX, item.posY + 0.25, item.posZ, 0f, 0f, 0f);
					}
					ticksLeft = maxTicks;
				}
			}else{
				this.ticksLeft = maxTicks;
		        markDirty();
			}
		}
	}
	
	private ItemStack tryInsert(ItemStack stack){
        for (int i = 0 ; i < this.getSizeInventory() ; i++)
        {
        	ItemStack itemstack = this.getStackInSlot(i);
        	if(itemstack.isEmpty()){
        		this.setInventorySlotContents(i, stack);
        		return ItemStack.EMPTY;
        	}else if(MinaUtils.areItemsStackable(itemstack, stack, true)){//TODO: too much overhead
        		//this.setInventorySlotContents(i, MinaUtils.mergeItemStack(stack, itemstack, stack.getCount()));
        		return MinaUtils.addItemStackToInventory(stack, this);
        		//return stack;
        	}
        }
        return stack;
	}
	
    private boolean isFull()
    {
        for (int i = 0 ; i < this.getSizeInventory() ; i++)
        {
        	ItemStack itemstack = this.getStackInSlot(i);
            if (itemstack.isEmpty() || itemstack.getCount() != itemstack.getMaxStackSize())
            {
                return false;
            }
        }

        return true;
    }

	@Override
	public void onCraftMatrixChanged() {
        markDirty();
    }
	
	@Override
	public void readFromNBT(NBTTagCompound nbt){
		super.readFromNBT(nbt);
		if(nbt.hasKey("ticksLeft", 3)){
			this.ticksLeft = nbt.getInteger("ticksLeft");
		}
		if(nbt.hasKey("maxTicks", 3)){
			this.maxTicks = nbt.getInteger("maxTicks");
		}
		if(nbt.hasKey("range", 6)){
			this.range = nbt.getDouble("range");
		}
		/*if(nbt.hasKey("inv", 9)){
			NBTTagList inv_nbt = nbt.getTagList("inv", 10);
			final int length = inv_nbt.tagCount();
			for(int i = 0 ; i < length ; i++){
				NBTTagCompound inbt = inv_nbt.getCompoundTagAt(i);
				ItemStack stack = new ItemStack(inbt);
				this.setInventorySlotContents(inbt.getByte("Slot"), stack);
			}
		}*/
		//TODO: Remove sooner or later
		if(nbt.hasKey("inv", 9)){
			NBTTagList old = nbt.getTagList("inv", 10);
			nbt.removeTag("inv");
			nbt.setTag("Items", old);
			MinaMod.println("Converting old Collector inventory format to new one at %s", this.pos.toString());
		}
		loadItems(nbt);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt){
		super.writeToNBT(nbt);
		nbt.setInteger("ticksLeft", ticksLeft);
		nbt.setInteger("maxTicks", maxTicks);
		nbt.setDouble("range", range);
		
		saveItems(nbt);
		
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
	
	public int getTicksLeft(){
		return ticksLeft;
	}
	
	public int getMaxTicks(){
		return maxTicks;
	}

}
