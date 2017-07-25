package lu.kremi151.minamod.util.slotmachine;

import java.util.LinkedList;

import lu.kremi151.minamod.MinaBlocks;
import lu.kremi151.minamod.MinaItems;
import lu.kremi151.minamod.util.MinaUtils;
import lu.kremi151.minamod.util.nbtmath.SerializableFunctionBase;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

//TODO: Make common serializing with actual TileEntity
public class SlotMachineBuilder {
	
	private final LinkedList<Object[]> icons = new LinkedList<>();
	private SerializableFunctionBase rowPriceFunction = null, cherryRowPriceFunction = null;
	private int prices[] = null;
	private String customName = null;
	private double maxWin = -1.0, cherryWin = -1.0;

	public SlotMachineBuilder() {
	}
	
	public SlotMachineBuilder addIcon(Item icon, int weight, boolean isCherry) {
		icons.add(new Object[] {icon, weight, isCherry});
		return this;
	}
	
	public SlotMachineBuilder addIcon(Block icon, int weight, boolean isCherry) {
		icons.add(new Object[] {Item.getItemFromBlock(icon), weight, isCherry});
		return this;
	}
	
	public SlotMachineBuilder setRowPriceFunction(SerializableFunctionBase<? extends NBTBase> function) {
		this.rowPriceFunction = function;
		return this;
	}
	
	public SlotMachineBuilder setCherryRowPriceFunction(SerializableFunctionBase<? extends NBTBase> function) {
		this.cherryRowPriceFunction = function;
		return this;
	}
	
	public SlotMachineBuilder setMaxWin(double maxWin) {
		this.maxWin = maxWin;
		return this;
	}
	
	public SlotMachineBuilder setCherryWin(double cherryWin) {
		this.cherryWin = cherryWin;
		return this;
	}
	
	public SlotMachineBuilder setPriceForSpinMode(SpinMode mode, int coins) {
		if(prices == null) {
			prices = new int[SpinMode.values().length];
			for(SpinMode _mode : SpinMode.values()) {
				prices[_mode.ordinal()] = _mode.getDefaultPrice();
			}
		}
		prices[mode.ordinal()] = coins;
		return this;
	}
	
	public SlotMachineBuilder setCustomName(String name) {
		this.customName = name;
		return this;
	}
	
	public ItemStack buildItemStack() throws IllegalStateException{
		if(icons.size() < 2) {
			throw new IllegalStateException("The amount of icons should be at least 2");
		}
		ItemStack stack = new ItemStack(MinaBlocks.SLOT_MACHINE, 1);
        NBTTagCompound teTag = stack.getOrCreateSubCompound("BlockEntityTag");

        NBTTagList iconsTag = new NBTTagList();
        while(icons.size() > 0) {
        	Object[] o = icons.removeFirst();
        	NBTTagCompound iconTag = new NBTTagCompound();
            iconTag.setString("Item", ((Item)o[0]).getRegistryName().toString());
            iconTag.setInteger("Weight", ((Integer)o[1]).intValue());
            if(((Boolean)o[2]).booleanValue())iconTag.setBoolean("Cherry", true);
            iconsTag.appendTag(iconTag);
        }
        teTag.setTag("Icons", iconsTag);
        
        if(rowPriceFunction != null) {
        	teTag.setTag("RowPriceFunction", rowPriceFunction.serialize());
        }else if(maxWin > 0.0) {
        	teTag.setDouble("MaxWin", maxWin);
        }
        
        if(cherryRowPriceFunction != null) {
        	teTag.setTag("CherryRowPriceFunction", cherryRowPriceFunction.serialize());
        }else if(cherryWin > 0.0) {
        	teTag.setDouble("CherryWin", maxWin);
        }
        
        if(prices != null) {
        	NBTTagCompound pnbt = new NBTTagCompound();
        	for(SpinMode mode : SpinMode.values()) {
        		pnbt.setInteger(mode.name().toLowerCase(), prices[mode.ordinal()]);
        	}
        	teTag.setTag("Prices", pnbt);
        }
        
        if(customName != null) {
        	teTag.setString("CustomName", customName);
        	//MinaUtils.applyLoresToItemStack(stack, new String[] {customName});
        }
        
        return stack;
	}
}
