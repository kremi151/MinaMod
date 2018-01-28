package lu.kremi151.minamod.item;

import java.util.List;

import javax.annotation.Nullable;

import lu.kremi151.minamod.MinaItems;
import lu.kremi151.minamod.MinaPotions;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
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
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		String i = null;
		switch(stack.getItemDamage()){
		case 1:
			i = "item.honey_pot.info.saturation";
			break;
		}
		if(i!=null) tooltip.add(I18n.translateToLocal(i));
	}
	
	@Override
	public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.DRINK;
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
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items){
		if(isInCreativeTab(tab)) {
			for(int i = 0 ; i < 2 ; i++)items.add(new ItemStack(this,1,i));
		}
	}
	
	@Override
	public boolean hasEffect(ItemStack is){
		return is.getItemDamage() > 0;
	}

}
