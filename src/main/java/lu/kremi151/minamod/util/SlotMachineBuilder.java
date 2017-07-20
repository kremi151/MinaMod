package lu.kremi151.minamod.util;

import java.util.LinkedList;

import lu.kremi151.minamod.MinaBlocks;
import lu.kremi151.minamod.MinaItems;
import lu.kremi151.minamod.block.tileentity.TileEntitySlotMachine.SpinMode;
import lu.kremi151.minamod.util.nbtmath.SerializableFunction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

//TODO: Make common serializing with actual TileEntity
public class SlotMachineBuilder {
	
	private final LinkedList<Object[]> icons = new LinkedList<>();
	private SerializableFunction rowPriceFunction = null;
	private int prices[] = null;
	private String customName = null;

	public SlotMachineBuilder() {
	}
	
	public SlotMachineBuilder addIcon(Item icon, int weight, boolean isCherry) {
		icons.add(new Object[] {icon, weight, isCherry});
		return this;
	}
	
	public SlotMachineBuilder setRowPriceFunction(SerializableFunction<? extends NBTBase> function) {
		this.rowPriceFunction = function;
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
        	MinaUtils.applyLoresToItemStack(stack, new String[] {customName});
        }
        
        return stack;
	}
}
