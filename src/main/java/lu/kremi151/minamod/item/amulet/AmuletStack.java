package lu.kremi151.minamod.item.amulet;

import lu.kremi151.minamod.MinaItems;
import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.enums.EnumParticleEffect;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class AmuletStack {
	
	public static final AmuletStack EMPTY = new AmuletStack(null);

	private final AmuletBase amulet;
	private final NBTTagCompound data;

	private long cooldown_stamp = 0L;
	
	public AmuletStack(AmuletBase amulet){
		this(amulet, new NBTTagCompound());
	}
	
	public AmuletStack(AmuletBase amulet, NBTTagCompound data){
		this.amulet = amulet;
		this.data = data;
	}
	
	public AmuletBase getAmulet(){
		return amulet;
	}
	
	public NBTTagCompound getData(){
		return data;
	}
	
	public boolean isEmpty(){
		return amulet == null;
	}
	
	public boolean tryUse(World world, EntityPlayer player){
		if(!isEmpty() && cooldown_stamp < System.currentTimeMillis() && amulet.onUse(world, player, this)){
			MinaMod.getProxy().spawnParticleEffect(EnumParticleEffect.AMULET_AURA, player.world, player.posX, player.posY + 1.5, player.posZ, 0.0f, 0.0f, 0.0f);
			cooldown_stamp = System.currentTimeMillis() + amulet.getUseCooldown();
			return true;
		}
		return false;
	}
	
	public static AmuletStack fromItemStack(ItemStack stack){
		if(!stack.isEmpty() && stack.getItem() == MinaItems.AMULET){
			return new AmuletStack(AmuletRegistry.getById(stack.getMetadata()), stack.getOrCreateSubCompound("amulet"));
		}else{
			throw new IllegalArgumentException("Supplied item stack is not an amulet");
		}
	}
	
	public ItemStack toItemStack(){
		ItemStack stack = new ItemStack(MinaItems.AMULET, 1, amulet.getId());
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setTag("amulet", data.copy());
		stack.setTagCompound(nbt);
		return stack;
	}
}
