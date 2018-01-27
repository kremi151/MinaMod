package lu.kremi151.minamod.block.tileentity;

import java.util.Iterator;

import javax.annotation.Nullable;

import lu.kremi151.minamod.capabilities.AccessableEnergyStorage;
import lu.kremi151.minamod.capabilities.sketch.ISketch;
import lu.kremi151.minamod.inventory.BaseInventoryTileEntity;
import lu.kremi151.minamod.inventory.PseudoInventoryCrafting;
import lu.kremi151.minamod.util.MinaUtils;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.InvWrapper;

public class TileEntityAutoCrafter extends TileEntitySidedInventory implements ITickable{
	
	public final static int ENERGY_TRESHOLD = 150;
	
	public final IInventory resultsInv = new BaseInventoryTileEntity(this, "inventory.autocrafter.results", 5);
	public final IInventory sketchInv = new BaseInventoryTileEntity(this, "inventory.autocrafter.sketch", 1) {
		@Override
		public boolean isItemValidForSlot(int index, ItemStack stack) {
			return stack.isEmpty() || stack.hasCapability(ISketch.CAPABILITY, null);
		}
	};
	private final IItemHandler resultsItemHandler;
	private boolean isCrafting = false;
	private final AccessableEnergyStorage energy = new AccessableEnergyStorage(450, 200, 0);

	public TileEntityAutoCrafter() {
		super(9, "inventory.autocrafter");
		this.resultsItemHandler = new InvWrapper(resultsInv);
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
		return direction != EnumFacing.DOWN && super.canInsertItem(index, itemStackIn, direction);
	}
	
	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		return direction == EnumFacing.DOWN;
	}
	
	private NonNullList<ItemStack> getRequirements(){
		NonNullList<ItemStack> res = NonNullList.create();
		ItemStack sketch = sketchInv.getStackInSlot(0);
		if(!sketch.isEmpty() && sketch.hasCapability(ISketch.CAPABILITY, null)) {
			ISketch cap = sketch.getCapability(ISketch.CAPABILITY, null);
			for(ItemStack stack : cap.getOrder()) {
				if(!stack.isEmpty()) {
					ItemStack copy = stack.copy();
					copy.setCount(1);
					for(ItemStack rstack : res) {
						if(ItemHandlerHelper.canItemStacksStack(rstack, copy)) {
							rstack.grow(1);
							copy.setCount(0);
						}
					}
					if(!copy.isEmpty()) {
						res.add(copy);
					}
				}
			}
		}
		return res;
	}

	@Override
	public void update() {
		if(world != null && !world.isRemote && world.getWorldTime() % 10 == 0) {
			craft();
		}
	}
	
	private boolean craft() {
		if(isCrafting)return false;
		isCrafting = true;
		ItemStack sketch = sketchInv.getStackInSlot(0);
		if(energy.getEnergyStored() >= ENERGY_TRESHOLD && !sketch.isEmpty()) {
			ISketch cap = sketch.getCapability(ISketch.CAPABILITY, null);
			NonNullList<ItemStack> req = getRequirements();
			NonNullList<ItemStack> req_clone = MinaUtils.cloneItemList(req);
			if(req_clone.size() > 0) {
				Iterator<ItemStack> it = req_clone.iterator();
				while(it.hasNext()) {
					ItemStack ing = it.next();
					if(!ing.isEmpty()) {
						if(MinaUtils.countItemsInInventory(this, ing) >= ing.getCount()) {
							it.remove();
						}
					}else {
						it.remove();
					}
				}
				
				if(req_clone.size() == 0) {
					PseudoInventoryCrafting invCrafting = new PseudoInventoryCrafting(3, 3);
					for(int i = 0 ; i < cap.getOrder().size() ; i++) {
						invCrafting.setInventorySlotContents(i, cap.getOrder().get(i));
					}
					ItemStack result = CraftingManager.findMatchingResult(invCrafting, world);
					if(!result.isEmpty()) {
						if(ItemHandlerHelper.insertItem(resultsItemHandler, result, true).isEmpty()) {
							for(ItemStack stack : req) {
								if(!MinaUtils.consumeInventoryItems(this, stack)) {
									//throw new RuntimeException("This should not happen");
									System.err.println("Items could not be consumed while crafting in auto crafter at " + pos + ". This should not happen...");
								}
							}
							ItemHandlerHelper.insertItem(resultsItemHandler, result, false);
							energy.setEnergy(energy.getEnergyStored() - ENERGY_TRESHOLD);
							isCrafting = false;
							return true;
						}
					}
					
				}
			}
		}
		isCrafting = false;
		return false;
	}

	@Override
	public void onCraftMatrixChanged() {}
	
	@Override
    public boolean hasCapability(net.minecraftforge.common.capabilities.Capability<?> capability, @Nullable net.minecraft.util.EnumFacing facing)
    {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY 
        		|| capability == CapabilityEnergy.ENERGY
        		|| super.hasCapability(capability, facing);
    }

    @Override
    @Nullable
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
    	if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
        	if(facing == null || facing != EnumFacing.DOWN) {
        		return (T) itemHandler;
        	}else {
        		return (T) resultsItemHandler;
        	}
    	}else if(capability == CapabilityEnergy.ENERGY) {
    		return (T) energy;
        }else{
        	return super.getCapability(capability, facing);
        }
    }
    
	@Override
	public void readFromNBT(NBTTagCompound nbt){
		super.readFromNBT(nbt);
		loadItems(nbt);
		resultsInv.clear();
		if(nbt.hasKey("Results", 9)) {
			NBTTagList results = nbt.getTagList("Results", 10);
			for(int i = 0 ; i < Math.min(results.tagCount(), resultsInv.getSizeInventory()) ; i++) {
				resultsInv.setInventorySlotContents(i, new ItemStack(results.getCompoundTagAt(i)));
			}
		}
		sketchInv.clear();
		if(nbt.hasKey("Sketch", 10)) {
			sketchInv.setInventorySlotContents(0, new ItemStack(nbt.getCompoundTag("Sketch")));
		}
		energy.setEnergy(nbt.getInteger("Energy"));
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt){
		nbt = super.writeToNBT(nbt);
		saveItems(nbt);
		NBTTagList results = new NBTTagList();
		for(int i = 0 ; i < resultsInv.getSizeInventory() ; i++) {
			results.appendTag(resultsInv.getStackInSlot(i).writeToNBT(new NBTTagCompound()));
		}
		nbt.setTag("Results", results);
		if(!sketchInv.getStackInSlot(0).isEmpty()) {
			nbt.setTag("Sketch", sketchInv.getStackInSlot(0).writeToNBT(new NBTTagCompound()));
		}
		nbt.setInteger("Energy", energy.getEnergyStored());
		return nbt;
	}

}
