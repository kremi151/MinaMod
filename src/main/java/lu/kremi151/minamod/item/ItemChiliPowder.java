package lu.kremi151.minamod.item;

import java.util.List;

import lu.kremi151.minamod.capabilities.stats.types.StatType;
import lu.kremi151.minamod.capabilities.stats.types.StatTypes;
import lu.kremi151.minamod.interfaces.IMixtureApplicator;
import lu.kremi151.minamod.interfaces.IMixtureIngredient;
import lu.kremi151.minamod.util.MinaUtils;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class ItemChiliPowder extends ItemChili implements IMixtureIngredient, IMixtureApplicator{
	
	public ItemChiliPowder() {
		super();
		this.setCreativeTab(CreativeTabs.MATERIALS);
	}

	@Override
	public int getStatEffect(StatType stat) {
		return (stat == StatTypes.SPEED)?4:-4;
	}

	@Override
	public StatType[] getSupportedStats() {
		return new StatType[]{StatTypes.SPEED};
	}

	@Override
	public int getIngredientColor(ItemStack stack) {
		return MinaUtils.convertRGBToDecimal(245, 150, 90);
	}

	@Override
	public void getMixtureProperties(ItemStack stack, List<IMixtureApplicator> list) {
		list.add(this);
	}

}
