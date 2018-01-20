package lu.kremi151.minamod.capabilities.amulets.impl;

import lu.kremi151.minamod.capabilities.amulets.IAmulet;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class AmuletPotionEffect extends AmuletConsumable{
	
	private final Potion effect;
	private final int ticks;
	
	public AmuletPotionEffect(ItemStack stack, int maxDuration, Potion effect, int ticks) {
		super(stack, maxDuration);
		this.effect = effect;
		this.ticks = ticks;
	}
	
	public AmuletPotionEffect(ItemStack stack, Potion effect, int ticks) {
		this(stack, 10, effect, ticks);
	}

	@Override
	public boolean onUse(World world, EntityPlayer player) {
		player.addPotionEffect(new PotionEffect(effect, ticks));
		IAmulet.spawnAmuletAura(player, 0.0f, 0.5f, 0.5f);
		return super.onUse(world, player);
	}

}
