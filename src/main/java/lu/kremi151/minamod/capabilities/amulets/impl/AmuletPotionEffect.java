package lu.kremi151.minamod.capabilities.amulets.impl;

import lu.kremi151.minamod.capabilities.amulets.IAmulet;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class AmuletPotionEffect implements IAmulet{
	
	private static final int MAX_DURATION = 10;
	private int duration = MAX_DURATION;
	
	private final ItemStack theStack;
	private final Potion effect;
	private final int ticks;
	
	public AmuletPotionEffect(ItemStack stack, Potion effect, int ticks) {
		this.theStack = stack;
		this.effect = effect;
		this.ticks = ticks;
	}

	@Override
	public boolean onUse(World world, EntityPlayer player) {
		player.addPotionEffect(new PotionEffect(effect, ticks));
		IAmulet.spawnAmuletAura(player, 0.0f, 0.5f, 0.5f);
		if(--duration <= 0) {
			theStack.setCount(0);
		}
		return true;
	}
	
	@Override
	public boolean hasDurability() {
		return duration < MAX_DURATION;
	}
	
	@Override
	public double getDurability() {
		int duration = MathHelper.clamp(this.duration, 0, MAX_DURATION);
		return 1.0 - ((double)duration / (double)MAX_DURATION);
	}
	
	@Override
	public NBTTagCompound saveData(NBTTagCompound nbt) {
		nbt.setInteger("Duration", duration);
		return nbt;
	}
	
	@Override
	public void loadData(NBTTagCompound nbt) {
		this.duration = MathHelper.clamp(nbt.getInteger("Duration"), 0, MAX_DURATION);
	}

}
