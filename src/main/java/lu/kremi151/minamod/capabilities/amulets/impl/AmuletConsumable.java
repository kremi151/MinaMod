package lu.kremi151.minamod.capabilities.amulets.impl;

import lu.kremi151.minamod.capabilities.amulets.IAmulet;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public abstract class AmuletConsumable implements IAmulet{
	
	private final int maxDuration;
	private int duration;
	protected final ItemStack itemStack;
	
	public AmuletConsumable(ItemStack itemStack, int maxDuration) {
		this.itemStack = itemStack;
		this.maxDuration = maxDuration;
		this.duration = maxDuration;
	}

	@Override
	public boolean onUse(World world, EntityPlayer player) {
		if(--duration <= 0) {
			itemStack.setCount(0);
		}
		return true;
	}
	
	@Override
	public boolean hasDurability() {
		return duration < maxDuration;
	}
	
	@Override
	public double getDurability() {
		int duration = MathHelper.clamp(this.duration, 0, maxDuration);
		return 1.0 - ((double)duration / (double)maxDuration);
	}
	
	@Override
	public NBTTagCompound saveData(NBTTagCompound nbt) {
		nbt.setInteger("Duration", duration);
		return nbt;
	}
	
	@Override
	public void loadData(NBTTagCompound nbt) {
		this.duration = MathHelper.clamp(nbt.getInteger("Duration"), 0, maxDuration);
	}
	
	@Override
	public NBTTagCompound saveSyncData(NBTTagCompound nbt) {
		nbt.setInteger("Duration", duration);
		return nbt;
	}
	
	@Override
	public void loadSyncData(NBTTagCompound nbt) {
		this.duration = MathHelper.clamp(nbt.getInteger("Duration"), 0, maxDuration);
	}

}
