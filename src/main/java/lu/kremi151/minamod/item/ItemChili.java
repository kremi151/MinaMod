package lu.kremi151.minamod.item;

import lu.kremi151.minamod.MinaBlocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemSeedFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class ItemChili extends ItemSeedFood{

	public ItemChili() {
		super(0, 0.5f, MinaBlocks.CHILI_CROP, Blocks.FARMLAND);
		this.setAlwaysEdible();
	}
	
	@Override
	protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player)
    {
		super.onFoodEaten(stack, worldIn, player);
		player.setFire(5);
		player.addPotionEffect(new PotionEffect(MobEffects.SPEED, 100, 4));
    }

}
