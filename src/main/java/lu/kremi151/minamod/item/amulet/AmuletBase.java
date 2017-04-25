package lu.kremi151.minamod.item.amulet;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class AmuletBase {
	
	private int id;

	public abstract boolean onUse(World world, EntityPlayer player, AmuletStack stack);
	public abstract String getUnlocalizedName();
	
	public void addInformation(NBTTagCompound data, EntityPlayer player, List<String> tooltip, boolean advanced){}
	
	public long getUseCooldown(){
		return 1000L;
	}
	
	@SideOnly(Side.CLIENT)
    public boolean hasEffect(NBTTagCompound data){
		return false;
	}
	
	final AmuletBase allocateId(int id){
		this.id = id;
		return this;
	}
	
	public final int getId(){
		return id;
	}
}
