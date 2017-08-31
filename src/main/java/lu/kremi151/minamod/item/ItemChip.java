package lu.kremi151.minamod.item;

import java.util.List;

import lu.kremi151.minamod.MinaCreativeTabs;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.translation.I18n;

public class ItemChip extends Item{
	
	public static final String[] variant_names = new String[]{
		"chip_a",
		"chip_b",
		"chip_c"
	};

	public ItemChip(){
		this.setHasSubtypes(true);
		this.setCreativeTab(MinaCreativeTabs.TECHNOLOGY);
	}
	
	@Override
	public void getSubItems(Item a1, CreativeTabs a2, NonNullList<ItemStack> a3){
		for(int i = 0 ; i < variant_names.length ; i++){
			a3.add(new ItemStack(a1,1,i));
		}
	}
	
	@Override
	public void addInformation(ItemStack is, EntityPlayer par2EntityPlayer, List list, boolean par4) {
		list.add(I18n.translateToLocal("item.chip.info." + is.getItemDamage()));
	}
	
	
}
