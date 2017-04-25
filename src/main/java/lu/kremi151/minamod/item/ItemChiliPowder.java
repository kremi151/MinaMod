package lu.kremi151.minamod.item;

import java.util.List;

import lu.kremi151.minamod.enums.EnumPlayerStat;
import lu.kremi151.minamod.interfaces.IMixtureApplicator;
import lu.kremi151.minamod.interfaces.IMixtureIngredient;
import lu.kremi151.minamod.util.MinaUtils;
import net.minecraft.item.ItemStack;

public class ItemChiliPowder extends ItemChili implements IMixtureIngredient, IMixtureApplicator{

	@Override
	public int getStatEffect(EnumPlayerStat stat) {
		return (stat == EnumPlayerStat.SPEED)?4:-4;
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
