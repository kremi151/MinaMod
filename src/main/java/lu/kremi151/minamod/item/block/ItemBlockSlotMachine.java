package lu.kremi151.minamod.item.block;

import lu.kremi151.minamod.MinaItems;
import lu.kremi151.minamod.block.BlockSlotMachine;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
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
        	otherGame = new ItemStack(block, 1);
            NBTTagCompound teTag = otherGame.getOrCreateSubCompound("BlockEntityTag");
            NBTTagList iconsTag = new NBTTagList();
            NBTTagCompound iconTag = new NBTTagCompound();
            iconTag.setString("Item", MinaItems.RUBY.getRegistryName().toString());
            iconTag.setInteger("Weight", 3);
            iconsTag.appendTag(iconTag.copy());
            iconTag.setString("Item", MinaItems.SAPPHIRE.getRegistryName().toString());
            iconsTag.appendTag(iconTag.copy());
            iconTag.setString("Item", MinaItems.CITRIN.getRegistryName().toString());
            iconTag.setInteger("Weight", 2);
            iconsTag.appendTag(iconTag.copy());
            iconTag.setString("Item", Items.NETHER_STAR.getRegistryName().toString());
            iconTag.setInteger("Weight", 1);
            iconsTag.appendTag(iconTag.copy());
            teTag.setTag("Icons", iconsTag);
        }
        
        subItems.add(otherGame.copy());        
    }

}
