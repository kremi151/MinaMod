package lu.kremi151.minamod.capabilities;

import java.util.concurrent.Callable;

import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.capabilities.amulets.CapabilityAmuletHolder;
import lu.kremi151.minamod.capabilities.amulets.IAmuletHolder;
import lu.kremi151.minamod.capabilities.bliss.EntityBlissImpl;
import lu.kremi151.minamod.capabilities.bliss.ICapabilityBliss;
import lu.kremi151.minamod.capabilities.coinhandler.EntityCoinHandler;
import lu.kremi151.minamod.capabilities.coinhandler.ICoinHandler;
import lu.kremi151.minamod.capabilities.stats.CapabilityStatsImpl;
import lu.kremi151.minamod.capabilities.stats.CapabilityStatsPlayerImpl;
import lu.kremi151.minamod.capabilities.stats.ICapabilityStats;
import lu.kremi151.minamod.capabilities.stats.types.StatType;
import lu.kremi151.minamod.capabilities.stats.types.StatTypes;
import net.minecraft.entity.EntityLivingBase;
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
	public static final ResourceLocation MINA_ENTITY_STATS = new ResourceLocation(MinaMod.MODID, "entity_stats");

	public static void init(){
		CapabilityManager.INSTANCE.register(ICapabilityJetpack.class, new EmptyStorage(), new NoImplFactory());
		CapabilityManager.INSTANCE.register(ICoinHandler.class, new EmptyStorage(), new NoImplFactory());
		CapabilityManager.INSTANCE.register(IKey.class, new EmptyStorage(), new NoImplFactory());
		CapabilityManager.INSTANCE.register(IAmuletHolder.class, CapabilityAmuletHolder.STORAGE, CapabilityAmuletHolder::new);
		CapabilityManager.INSTANCE.register(ICapabilityBliss.class, ICapabilityBliss.STORAGE, new NoImplFactory());
		CapabilityManager.INSTANCE.register(ICapabilityStats.class, new CapabilityStatsImpl.Storage(), new NoImplFactory());
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
		private final ICapabilityStats<EntityPlayer> playerStats;
		private final IAmuletHolder amuletHolder;
		private final ICapabilityBliss bliss;
		
		public MinaPlayerCapabilityProvider(EntityPlayer player){
			coinHandler = new EntityCoinHandler(player);
			jetpack = new CapabilityPlayerJetpack(player);
			playerStats = new CapabilityStatsPlayerImpl(player, new StatType[]{StatTypes.ATTACK, StatTypes.DEFENSE, StatTypes.SPEED});
			amuletHolder = new CapabilityAmuletHolder();
			bliss = new EntityBlissImpl(player);
		}

		@Override
		public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
			return capability == ICoinHandler.CAPABILITY
					|| capability == ICapabilityJetpack.CAPABILITY_JETPACK
					|| capability == ICapabilityStats.CAPABILITY
					|| capability == CapabilityAmuletHolder.CAPABILITY_AMULET_HOLDER
					|| capability == ICapabilityBliss.CAPABILITY_BLISS;
		}

		@Override
		public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
			if(capability == ICoinHandler.CAPABILITY){
				return (T) this.coinHandler;
			}else if(capability == ICapabilityJetpack.CAPABILITY_JETPACK){
				return (T) this.jetpack;
			}else if(capability == ICapabilityStats.CAPABILITY){
				return (T) this.playerStats;
			}else if(capability == CapabilityAmuletHolder.CAPABILITY_AMULET_HOLDER){
				return (T) this.amuletHolder;
			}else if(capability == ICapabilityBliss.CAPABILITY_BLISS){
				return (T) this.bliss;
			}else{
				return null;
			}
		}

		@Override
		public NBTTagCompound serializeNBT() {
			NBTTagCompound root = new NBTTagCompound();
			
			NBTTagCompound stats_nbt = (NBTTagCompound) ICapabilityStats.CAPABILITY.getStorage().writeNBT(ICapabilityStats.CAPABILITY, this.playerStats, null);
			root.setTag("stats", stats_nbt);
			
			NBTTagCompound amulets_nbt = (NBTTagCompound) CapabilityAmuletHolder.CAPABILITY_AMULET_HOLDER.getStorage().writeNBT(CapabilityAmuletHolder.CAPABILITY_AMULET_HOLDER, this.amuletHolder, null);
			root.setTag("amulets", amulets_nbt);
			
			root.setInteger("bliss", bliss.getBliss());
			
			return root;
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt) {
			if(nbt.hasKey("stats", 10)){
				ICapabilityStats.CAPABILITY.getStorage().readNBT(ICapabilityStats.CAPABILITY, this.playerStats, null, nbt.getCompoundTag("stats"));
			}
			if(nbt.hasKey("amulets", 10)){
				CapabilityAmuletHolder.CAPABILITY_AMULET_HOLDER.getStorage().readNBT(CapabilityAmuletHolder.CAPABILITY_AMULET_HOLDER, this.amuletHolder, null, nbt.getCompoundTag("amulets"));
			}
			if(nbt.hasKey("bliss", 99)){
				try{
					bliss.setBliss(nbt.getInteger("bliss"));
				}catch(IllegalArgumentException e){
					System.err.println("Invalid bliss value detected, must be positive");
				}
			}
		}
		
	}
	
	public static class EntityStatsProvider<E extends EntityLivingBase> implements ICapabilitySerializable<NBTTagCompound>{
		
		private final ICapabilityStats<E> stats;
		
		public EntityStatsProvider(E entity, StatType statTypes[]){
			this.stats = new CapabilityStatsImpl<E>(entity, statTypes);
		}
		
		public ICapabilityStats<E> getStats(){
			return stats;
		}

		@Override
		public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
			return capability == ICapabilityStats.CAPABILITY;
		}

		@Override
		public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
			if(capability == ICapabilityStats.CAPABILITY){
				return (T) stats;
			}else{
				return null;
			}
		}

		@Override
		public NBTTagCompound serializeNBT() {
			return (NBTTagCompound) ICapabilityStats.CAPABILITY.getStorage().writeNBT(ICapabilityStats.CAPABILITY, this.stats, null);
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt) {
			ICapabilityStats.CAPABILITY.getStorage().readNBT(ICapabilityStats.CAPABILITY, this.stats, null, nbt);
		}
		
	}
}
