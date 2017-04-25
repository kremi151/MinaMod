package lu.kremi151.minamod.item;

import java.util.List;

import lu.kremi151.minamod.MinaItems;
import lu.kremi151.minamod.MinaPotions;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class ItemHoneyPot extends ItemFood{

	public ItemHoneyPot() {
		super(1, false);
		this.setHasSubtypes(true);
		this.setMaxStackSize(1);
	}
	
	@Override
	public void addInformation(ItemStack is, EntityPlayer par2EntityPlayer, List list, boolean par4) {
		String i = null;
		switch(is.getItemDamage()){
		case 1:
			i = "item.honey_pot.info.saturation";
			break;
		}
		if(i!=null){
			list.add(I18n.translateToLocal(i));
		}
	}
	
	@Override
	public void onFoodEaten(ItemStack is, World w, EntityPlayer p){
		super.onFoodEaten(is, w, p);
		switch(is.getItemDamage()){
		case 1:
			p.addPotionEffect(new PotionEffect(MinaPotions.SATURATION, 3600));
			break;
		}
		p.inventory.addItemStackToInventory(new ItemStack(MinaItems.EMPTY_POT,1));
	}
	
	@Override
	public void getSubItems(Item a1, CreativeTabs a2, NonNullList<ItemStack> a3){
		for(int i = 0 ; i < 2 ; i++)a3.add(new ItemStack(a1,1,i));
	}
	
	@Override
	public boolean hasEffect(ItemStack is){
		return is.getItemDamage() > 0;
	}

}
