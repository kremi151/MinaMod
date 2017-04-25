package lu.kremi151.minamod.capabilities;

import java.util.concurrent.Callable;

import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.capabilities.amulets.CapabilityAmuletHolder;
import lu.kremi151.minamod.capabilities.amulets.IAmuletHolder;
import lu.kremi151.minamod.capabilities.coinhandler.EntityCoinHandler;
import lu.kremi151.minamod.capabilities.coinhandler.ICoinHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class MinaCapabilities {

	public static final ResourceLocation MINA_PLAYER_CAPS_ID = new ResourceLocation(MinaMod.MODID, "player_caps");

	public static void init(){
		CapabilityManager.INSTANCE.register(ICapabilityJetpack.class, new EmptyStorage(), new NoImplFactory());
		CapabilityManager.INSTANCE.register(ICoinHandler.class, new EmptyStorage(), new NoImplFactory());
		CapabilityManager.INSTANCE.register(IKey.class, new EmptyStorage(), new NoImplFactory());
		CapabilityManager.INSTANCE.register(IPlayerStats.class, new CapabilityPlayerStats.Storage(), new NoImplFactory());
		CapabilityManager.INSTANCE.register(IAmuletHolder.class, CapabilityAmuletHolder.STORAGE, CapabilityAmuletHolder::new);
	}
	
	private static class EmptyStorage<A> implements Capability.IStorage<A>{

		@Override
		public NBTBase writeNBT(Capability<A> capability, A instance, EnumFacing side) {
			return new NBTTagCompound();
		}

		@Override
		public void readNBT(Capability<A> capability, A instance, EnumFacing side, NBTBase nbt) {}
		
	}
	
	private static class NoImplFactory<A> implements Callable<A> {

		  @Override
		  public A call() throws Exception {
			  throw new RuntimeException("No default implementation available. Please use a specific implementation of this capability.");
		  }
	}
	
	public static class MinaPlayerCapabilityProvider implements ICapabilitySerializable<NBTTagCompound>{
		
		private final ICoinHandler coinHandler;
		private final ICapabilityJetpack jetpack;
		private final IPlayerStats playerStats;
		private final IAmuletHolder amuletHolder;
		
		public MinaPlayerCapabilityProvider(EntityPlayer player){
			coinHandler = new EntityCoinHandler(player);
			jetpack = new CapabilityPlayerJetpack(player);
			playerStats = new CapabilityPlayerStats(player);
			amuletHolder = new CapabilityAmuletHolder();
		}

		@Override
		public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
			return capability == ICoinHandler.CAPABILITY_COIN_HANDLER
					|| capability == ICapabilityJetpack.CAPABILITY_JETPACK
					|| capability == CapabilityPlayerStats.CAPABILITY
					|| capability == CapabilityAmuletHolder.CAPABILITY_AMULET_HOLDER;
		}

		@Override
		public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
			if(capability == ICoinHandler.CAPABILITY_COIN_HANDLER){
				return (T) this.coinHandler;
			}else if(capability == ICapabilityJetpack.CAPABILITY_JETPACK){
				return (T) this.jetpack;
			}else if(capability == CapabilityPlayerStats.CAPABILITY){
				return (T) this.playerStats;
			}else if(capability == CapabilityAmuletHolder.CAPABILITY_AMULET_HOLDER){
				return (T) this.amuletHolder;
			}else{
				return null;
			}
		}

		@Override
		public NBTTagCompound serializeNBT() {
			NBTTagCompound root = new NBTTagCompound();
			
			NBTTagCompound stats_nbt = (NBTTagCompound) CapabilityPlayerStats.CAPABILITY.getStorage().writeNBT(CapabilityPlayerStats.CAPABILITY, this.playerStats, null);
			root.setTag("stats", stats_nbt);
			
			root.setInteger("eff", this.playerStats.getEffortBar());
			
			NBTTagCompound amulets_nbt = (NBTTagCompound) CapabilityAmuletHolder.CAPABILITY_AMULET_HOLDER.getStorage().writeNBT(CapabilityAmuletHolder.CAPABILITY_AMULET_HOLDER, this.amuletHolder, null);
			root.setTag("amulets", amulets_nbt);
			
			return root;
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt) {
			if(nbt.hasKey("stats", 10)){
				CapabilityPlayerStats.CAPABILITY.getStorage().readNBT(CapabilityPlayerStats.CAPABILITY, this.playerStats, null, nbt.getCompoundTag("stats"));
			}
			if(nbt.hasKey("eff", 99)){
				this.playerStats.setEffort(nbt.getInteger("eff"));
			}
			if(nbt.hasKey("amulets", 10)){
				CapabilityAmuletHolder.CAPABILITY_AMULET_HOLDER.getStorage().readNBT(CapabilityAmuletHolder.CAPABILITY_AMULET_HOLDER, this.amuletHolder, null, nbt.getCompoundTag("amulets"));
			}
		}
		
	}
}
