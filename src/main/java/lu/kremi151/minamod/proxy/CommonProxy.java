package lu.kremi151.minamod.proxy;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.entity.EntityPenguin;
import lu.kremi151.minamod.enums.EnumParticleEffect;
import lu.kremi151.minamod.network.MessageShowCustomAchievement;
import lu.kremi151.minamod.network.MessageSpawnParticleEffect;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CommonProxy{
	
	private boolean flag_maintenance = false;

	public void registerRenderers() {}
	public void registerBuildInBlocks() {}
	public void registerStateMappings() {}
	public void registerKeyBindings(){}
	public void registerFluidModels() {}
	public void registerItemAndBlockColors() {}
	public void executeClientSide(Runnable r){}
	public void openBook(ItemStack book){}
	public void initClientEvents(){}
	
	public void showNotification(EntityPlayer player, String title, String message, long duration, ItemStack icon) {
		if(player instanceof EntityPlayerMP) {
			MessageShowCustomAchievement msg = new MessageShowCustomAchievement(title, message, duration, icon);
			MinaMod.getMinaMod().getPacketDispatcher().sendTo(msg, (EntityPlayerMP)player);
		}
	}
	
	public IThreadListener getThreadListener(MessageContext context) {
		if(context.side.isServer()) {
			return context.getServerHandler().player.mcServer;
		}else{
			throw new RuntimeException("Cannot get a thread listener from a client context on a dedicated server");
		}
	}
	
	public void addScreenLayer(int id, boolean force){}
	
	public <T> Optional<T> tryGetClientSideResult(Supplier<T> clientCode){
		return Optional.empty();
	}

	public void spawnParticleEffect(EnumParticleEffect effect, World worldObj, double posX, double posY, double posZ, float red, float green, float blue){}
	
	public void spawnParticleEffectToAllAround(EnumParticleEffect effect, World worldObj, double posX, double posY, double posZ, float red, float green, float blue){
		if(!worldObj.isRemote)MinaMod.getMinaMod().getPacketDispatcher().sendToAllAround(new MessageSpawnParticleEffect(effect, posX, posY, posZ, red, green, blue), new TargetPoint(worldObj.provider.getDimension(), posX, posY, posZ, 40.0));
	}

	private final HashMap<String, HashMap<UUID, Long>> cooldown_map = new HashMap<String, HashMap<UUID, Long>>();
	
	public void checkInitialPenguinState(EntityPenguin entity){}
	
	public void executeServerSide(Runnable r){
		r.run();
	}
	
	public boolean isServerSide(){
		return true;
	}
	
    /**
     * Returns a side-appropriate EntityPlayer for use during message handling
     */
    public EntityPlayer getPlayerEntity(MessageContext ctx) {
    	return ctx.getServerHandler().player;
    }
	
	private HashMap<UUID, Long> getCooldownCategory(String category){
		HashMap<UUID, Long> res = cooldown_map.get(category);
		if(res != null){
			return res;
		}else{
			res = new HashMap<UUID, Long>();
			cooldown_map.put(category, res);
			return res;
		}
	}
	
	public long getCooldownDate(String category, UUID player){
		HashMap<UUID, Long> cm = getCooldownCategory(category);
		Long res = cm.get(player);
		if(res != null){
			return res;
		}else{
			return 0;
		}
	}
	
	public void setCooldownDate(String category, UUID player, long cooldownDate){
		getCooldownCategory(category).put(player, cooldownDate);
	}
	
	public void clearCooldowns(){
		cooldown_map.clear();
	}
	
	public boolean isMaintenanceFlagSet() {
		return flag_maintenance;
	}
	
	public void setMaintenanceFlag(boolean flag) {
		this.flag_maintenance = flag;
	}
    
}
