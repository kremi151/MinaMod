package lu.kremi151.minamod.capabilities.amulets.impl;

import lu.kremi151.minamod.capabilities.amulets.IAmulet;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class AmuletRegeneration implements IAmulet{
	
	private static final byte MAX_DURATION = 10;
	private int duration = MAX_DURATION;
	
	private final ItemStack theStack;
	
	public AmuletRegeneration(ItemStack stack) {
		this.theStack = stack;
	}

	@Override
	public boolean onUse(World world, EntityPlayer player) {
		player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 200));
		IAmulet.spawnAmuletAura(player, 0.0f, 0.5f, 0.5f);
		if(--duration <= 0) {
			theStack.setCount(0);
		}
		return true;
	}
	
	@Override
	public boolean hasDurability() {
		return true;
	}
	
	@Override
	public double getDurability() {
		int duration = MathHelper.clamp(this.duration, 0, MAX_DURATION);
		return (double)duration / (double)MAX_DURATION;
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
