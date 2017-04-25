package lu.kremi151.minamod.item;

import java.util.List;

import lu.kremi151.minamod.MinaCreativeTabs;
import lu.kremi151.minamod.enums.EnumHerb;
import lu.kremi151.minamod.interfaces.IMixtureApplicator;
import lu.kremi151.minamod.interfaces.IMixtureIngredient;
import lu.kremi151.minamod.util.MinaUtils;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemPowder extends Item implements IMixtureIngredient{

	public ItemPowder() {
		super();
		this.setHasSubtypes(true);
		this.setCreativeTab(MinaCreativeTabs.MIXTURES);
	}
	
	@Override
	public int getMetadata(int damage){
		return MathHelper.clamp(damage, 0, EnumHerb.values().length);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
    {
        return "item.powder." + EnumHerb.getByHerbId((byte)stack.getMetadata()).getName();
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> subItems)
    {
		EnumHerb[] eh = EnumHerb.values();
		for(int i = 0 ; i < eh.length ; i++){
	        subItems.add(new ItemStack(itemIn, 1, eh[i].getHerbId()));
		}
    }

	@Override
	public int getIngredientColor(ItemStack stack) {
		if(stack.getMetadata() < 0 || stack.getMetadata() >= EnumHerb.values().length)return MinaUtils.COLOR_WHITE;
		return EnumHerb.getByHerbId((byte)stack.getMetadata()).getTint();
	}

	@Override
	public void getMixtureProperties(ItemStack stack, List<IMixtureApplicator> list) {
		list.add(EnumHerb.getByHerbId((byte)stack.getMetadata()));
	}
}
