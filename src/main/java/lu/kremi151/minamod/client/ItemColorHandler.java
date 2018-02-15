package lu.kremi151.minamod.client;

import lu.kremi151.minamod.MinaItems;
import lu.kremi151.minamod.capabilities.stats.snack.ISnack;
import lu.kremi151.minamod.enums.EnumHerb;
import lu.kremi151.minamod.item.ItemColoredWrittenBook;
import lu.kremi151.minamod.item.ItemHerbMixture;
import lu.kremi151.minamod.item.ItemKey;
import lu.kremi151.minamod.item.ItemSoulPearl;
import lu.kremi151.minamod.util.MinaUtils;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public enum ItemColorHandler implements IItemColor{
	INSTANCE;

	@Override
	public int colorMultiplier(ItemStack stack, int tintIndex) {
		if(stack.getItem() == MinaItems.HERB || stack.getItem() == MinaItems.POWDER){
			return EnumHerb.isValidId((byte)stack.getMetadata()) ? EnumHerb.getByHerbId((byte)stack.getMetadata(), EnumHerb.WHITE).getTint() : MinaUtils.COLOR_WHITE;
		}else if(stack.getItem() == MinaItems.MIXTURE && tintIndex == 1){
			return ((ItemHerbMixture.HerbMixtureSnack) stack.getCapability(ISnack.CAPABILITY, null)).getColor();
		}else if(stack.getItem() == MinaItems.SOUL_PEARL){
			if(ItemSoulPearl.checkIfTinted(stack)){
	        	return stack.getTagCompound().getInteger("tint");
	        }
		}else if(tintIndex == 1 && stack.getItem() == MinaItems.KEY){
			return ItemKey.getColor(stack);
		}else if(stack.getItem().getClass() == ItemColoredWrittenBook.class && tintIndex == 0){
			return MinaItems.COLORED_BOOK.getBookColor(stack);
		}
		
		return MinaUtils.COLOR_WHITE;
	}

}
