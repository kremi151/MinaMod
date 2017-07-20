package lu.kremi151.minamod.item.block;

import lu.kremi151.minamod.MinaItems;
import lu.kremi151.minamod.block.BlockSlotMachine;
import lu.kremi151.minamod.block.tileentity.TileEntitySlotMachine.SpinMode;
import lu.kremi151.minamod.util.SlotMachineBuilder;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBlockSlotMachine extends ItemBlock{
	
	private ItemStack otherGame = null;

	public ItemBlockSlotMachine(BlockSlotMachine block) {
		super(block);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
    public void getSubItems(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> subItems)
    {
        subItems.add(new ItemStack(itemIn, 1));
        
        if(otherGame == null) {        	
        	otherGame = new SlotMachineBuilder()
        			.addIcon(MinaItems.RUBY, 3, false)
        			.addIcon(MinaItems.SAPPHIRE, 3, false)
        			.addIcon(Items.EMERALD, 2, false)
        			.addIcon(Items.NETHER_STAR, 1, false)
        			.setPriceForSpinMode(SpinMode.ONE, 1)
        			.setPriceForSpinMode(SpinMode.THREE, 2)
        			.setPriceForSpinMode(SpinMode.FIVE, 3)
        			.setCustomName(TextFormatting.BLUE + "Shiny" + TextFormatting.RED + "Gem" + TextFormatting.GREEN + "Crush")
        			.buildItemStack();
        }
        
        subItems.add(otherGame.copy());        
    }

}
