package lu.kremi151.minamod.capabilities.amulets;

import java.util.List;

import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.enums.EnumParticleEffect;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IAmulet {
	
	@CapabilityInject(IAmulet.class)
	public static final Capability<IAmulet> CAPABILITY = null;
	
	boolean onUse(World world, EntityPlayer player);
	
	default void addInformation(EntityPlayer player, List<String> tooltip, boolean advanced){}
	
	default NBTTagCompound saveData(NBTTagCompound nbt) {
		return nbt;
	}
	default void loadData(NBTTagCompound nbt) {}

	default long getUseCooldown() {
		return 500l;
	}

	@SideOnly(Side.CLIENT)
	default boolean hasEffect() {
		return false;
	}
	
	default boolean hasDurability() {
		return false;
	}
	
	/**
	 * Remember: 0.0 equals 100% (full bar), 1.0 equals 0% (empty bar)
	 * @param data
	 * @return
	 */
	default double getDurability() {
		return 0.0;
	}
	
	public static void spawnAmuletAura(EntityPlayer player, float red, float green, float blue){
		MinaMod.getProxy().spawnParticleEffectToAllAround(EnumParticleEffect.AMULET_AURA, player.world, player.posX, player.posY + 1.5, player.posZ, red, green, blue);
	}
}
