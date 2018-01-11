package lu.kremi151.minamod.item.amulet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class AmuletRegeneration extends AmuletBase{
	
	private static final byte MAX_DURATION = 10;

	@Override
	public boolean onUse(World world, EntityPlayer player, ItemStack stack) {
		byte duration = (byte)(stack.getOrCreateSubCompound("regenData").getByte("duration") - 1);
		player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 200));
		spawnAmuletAura(player, 0.0f, 0.5f, 0.5f);
		if(duration <= 0) {
			//TODO: Destroy
		}
		return true;
	}
	
	@Override
	public boolean hasDurability(NBTTagCompound data) {
		return true;
	}
	
	@Override
	public double getDurability(NBTTagCompound data) {
		int duration = MathHelper.clamp(data.getByte("duration"), 0, MAX_DURATION);
		return 1.0 - ((double)duration / (double)MAX_DURATION);
	}

}
