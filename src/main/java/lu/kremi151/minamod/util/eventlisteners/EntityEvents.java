package lu.kremi151.minamod.util.eventlisteners;

import java.util.Map;
import java.util.Random;

import lu.kremi151.minamod.MinaArmorMaterial;
import lu.kremi151.minamod.MinaBlocks;
import lu.kremi151.minamod.MinaEnchantments;
import lu.kremi151.minamod.MinaItems;
import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.MinaPotions;
import lu.kremi151.minamod.block.BlockElevatorFloor;
import lu.kremi151.minamod.block.BlockStool;
import lu.kremi151.minamod.capabilities.MinaCapabilities;
import lu.kremi151.minamod.capabilities.stats.CapabilityStatsPlayerImpl;
import lu.kremi151.minamod.capabilities.stats.ICapabilityStats;
import lu.kremi151.minamod.capabilities.stats.types.StatType;
import lu.kremi151.minamod.capabilities.stats.types.StatTypes;
import lu.kremi151.minamod.entity.EntityIceGolhem;
import lu.kremi151.minamod.packet.message.MessageSetScreenLayer;
import lu.kremi151.minamod.util.MinaUtils;
import lu.kremi151.minamod.util.ReflectionLoader;
import lu.kremi151.minamod.worlddata.MinaWorld;
import lu.kremi151.minamod.worldprovider.WorldProviderOverworldHook;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.ZombieEvent.SummonAidEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EntityEvents {

	private final MinaMod mod;
	private final Random RNG = new Random(System.currentTimeMillis());
	
	public EntityEvents(MinaMod mod){
		this.mod = mod;
	}
	
	@SubscribeEvent
	public void onEntityUpdate(LivingUpdateEvent event) { // NO_UCD (unused code)
		EntityLivingBase living = event.getEntityLiving();
		if(!(living instanceof EntityPlayer) &! MinaWorld.forWorld(living.world).hasMobsEnabled()){
			living.setDead();
			return;
		}
		iteratePotionEffects(living);

		IBlockState beneath = living.world.getBlockState(living.getPosition().down());
		
		if(BlockElevatorFloor.isValidJumpFloor(beneath)){
			if(living.isSneaking() && MinaBlocks.ELEVATOR_FLOOR.checkCooldown(living)){
				MinaBlocks.ELEVATOR_FLOOR.jumpElevate(living, false);
				MinaBlocks.ELEVATOR_FLOOR.setCooldown(living);
			}else if(isJumping(living) && MinaBlocks.ELEVATOR_FLOOR.checkCooldown(living)){
				MinaBlocks.ELEVATOR_FLOOR.jumpElevate(living, true);
				MinaBlocks.ELEVATOR_FLOOR.setCooldown(living);
			}
		}
	}
	
	private boolean isJumping(EntityLivingBase e){
		try {
			return ReflectionLoader.EntityLivingBase_isJumping(e);
		} catch (IllegalArgumentException e1) {
			e1.printStackTrace();
			throw new RuntimeException();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	private void iteratePotionEffects(EntityLivingBase living){
		boolean isPlayer = living instanceof EntityPlayer;
		if(isPlayer && getPotionDuration(living, MinaPotions.SATURATION) > 0){
			EntityPlayer p = (EntityPlayer) living;
			p.getFoodStats().setFoodLevel(20);
		}
		if(getPotionDuration(living, MinaPotions.FREEZE) > 0){
			living.setPositionAndUpdate(living.posX, living.posY, living.posZ);
			living.setJumping(false);
		}
		if(isPlayer && getPotionDuration(living, MinaPotions.DOGE) > 0){
			MinaMod.getMinaMod().getPacketDispatcher().sendTo(MessageSetScreenLayer.removeLayer(), (EntityPlayerMP) living);
		}
	}
	
	private int getPotionDuration(EntityLivingBase living, Potion potion){
		PotionEffect eff = living.getActivePotionEffect(potion);
		return (eff!=null)?eff.getDuration():-1;
	}
	
	@SubscribeEvent
	public void onEntityDeath(LivingDeathEvent event){ // NO_UCD (unused code)
		if(event.getSource().getTrueSource() instanceof EntityPlayer){
			EntityLivingBase killed = event.getEntityLiving();
			EntityPlayer killer = (EntityPlayer) event.getSource().getTrueSource();
			if(!killer.inventory.getCurrentItem().isEmpty()){
				Map<Enchantment, Integer> ench = EnchantmentHelper.getEnchantments(killer.inventory.getCurrentItem());
				if(ench.containsKey(MinaEnchantments.BEHEADER)){
					int meta = -1;
					if(event.getEntityLiving() instanceof EntitySkeleton){
						meta = 0;
					}else if(event.getEntityLiving() instanceof EntityWither){
						meta = 1;
					}else if(event.getEntityLiving() instanceof EntityZombie){
						meta = 2;
					}else if(event.getEntityLiving() instanceof EntityPlayer){
						meta = 3;
					}else if(event.getEntityLiving() instanceof EntityCreeper){
						meta = 4;
					}
					if(meta != -1){
						ItemStack is = new ItemStack(Items.SKULL, 1, meta);
						if(meta == 3){
							EntityPlayer p = (EntityPlayer) event.getEntityLiving();
							NBTTagCompound nbt = new NBTTagCompound();
							NBTTagCompound owner = new NBTTagCompound();
							NBTUtil.writeGameProfile(owner, p.getGameProfile());
							nbt.setTag("SkullOwner", owner);
							is.setTagCompound(nbt);
						}
						EntityItem ei = new EntityItem(event.getEntityLiving().world);
						ei.setItem(is);
						ei.setPosition(event.getEntityLiving().posX, event.getEntityLiving().posY, event.getEntityLiving().posZ);
						event.getEntityLiving().world.spawnEntity(ei);
					}
				}
			}
			int superMobLvl = MinaUtils.getSuperMobLevel(killed);
			if(superMobLvl > 0){
				((ICapabilityStats)killer.getCapability(ICapabilityStats.CAPABILITY, null)).offer(CapabilityStatsPlayerImpl.DISTRIBUTION_MULTIPLICATOR, 1 + superMobLvl);
			}
		}
		if(event.getEntityLiving() instanceof EntitySheep){
			if(event.getEntityLiving().getRNG().nextInt(18) == 0){
				ItemStack is = new ItemStack(MinaItems.BAMBUS, 1);
				EntityItem ei = new EntityItem(event.getEntityLiving().world);
				ei.setItem(is);
				ei.setPosition(event.getEntityLiving().posX, event.getEntityLiving().posY, event.getEntityLiving().posZ);
				event.getEntityLiving().world.spawnEntity(ei);
			}
		}
	}
	
	@SubscribeEvent
	public void onLivingDropsExp(LivingExperienceDropEvent event){
		event.setDroppedExperience((MinaUtils.getSuperMobLevel(event.getEntityLiving()) + 1) * event.getDroppedExperience());
	}
	
	@SubscribeEvent
	public void onEntitySpawn(EntityJoinWorldEvent event){ // NO_UCD (unused code)
		if(event.getEntity() instanceof EntitySkeleton || event.getEntity() instanceof EntityZombie){
			EntityMob s = (EntityMob)event.getEntity();
	        s.tasks.addTask(3, new EntityAIAvoidEntity((EntityCreature) event.getEntity(), EntityIceGolhem.class, 6.0F, 0.6D, 0.6D));
		}
		if(event.getEntity().hasCapability(ICapabilityStats.CAPABILITY, null)){
			((ICapabilityStats)event.getEntity().getCapability(ICapabilityStats.CAPABILITY, null)).initAttributes();//TODO:should this be here?
		}
	}
	
	//TODO:Not 100% trustworth
	private static boolean isCausedByArrow(DamageSource source){
		return /*source instanceof EntityDamageSourceIndirect && */
				source.getDamageType().equalsIgnoreCase("arrow");
	}
	
	@SubscribeEvent
	public void onEntityDamage(LivingHurtEvent event){ // NO_UCD (unused code)
		if(event.getEntityLiving() instanceof EntityPlayer){
			EntityPlayer player = (EntityPlayer) event.getEntityLiving();
			ItemStack tmp;
			if(isCausedByArrow(event.getSource())){
				if(!(tmp = player.inventory.armorInventory.get(0)).isEmpty() 
						&& tmp.getItem() instanceof ItemArmor 
						&& ((ItemArmor)tmp.getItem()).getArmorMaterial() == MinaArmorMaterial.TURTLE_SHELL){
					event.setAmount(event.getAmount() * 0.8f);
				}
				if(!(tmp = player.inventory.armorInventory.get(1)).isEmpty() 
						&& tmp.getItem() instanceof ItemArmor 
						&& ((ItemArmor)tmp.getItem()).getArmorMaterial() == MinaArmorMaterial.TURTLE_SHELL){
					event.setAmount(event.getAmount() * 0.7f);
				}
				if(!(tmp = player.inventory.armorInventory.get(2)).isEmpty() 
						&& tmp.getItem() instanceof ItemArmor 
						&& ((ItemArmor)tmp.getItem()).getArmorMaterial() == MinaArmorMaterial.TURTLE_SHELL){
					event.setAmount(event.getAmount() * 0.45f);
				}
				if(!(tmp = player.inventory.armorInventory.get(3)).isEmpty() 
						&& tmp.getItem() instanceof ItemArmor 
						&& ((ItemArmor)tmp.getItem()).getArmorMaterial() == MinaArmorMaterial.TURTLE_SHELL){
					event.setAmount(event.getAmount() * 0.55f);
				}
			}
		}
		
		if(event.getEntityLiving().hasCapability(ICapabilityStats.CAPABILITY, null)){
			ICapabilityStats pstats = event.getEntityLiving().getCapability(ICapabilityStats.CAPABILITY, null);
			if(pstats.supports(StatTypes.DEFENSE)){
				float defFactor = (float)pstats.getStat(StatTypes.DEFENSE).getActual().get() / 127.5f;
				event.setAmount(event.getAmount() / defFactor);
			}
		}
		
		Entity sourceEntity = event.getSource().getTrueSource();
		if(sourceEntity != null && sourceEntity.hasCapability(ICapabilityStats.CAPABILITY, null)){
			ICapabilityStats pstats = sourceEntity.getCapability(ICapabilityStats.CAPABILITY, null);
			if(pstats.supports(StatTypes.ATTACK)){
				float atkFactor = (float)pstats.getStat(StatTypes.ATTACK).getActual().get() / 127.5f;
				event.setAmount(event.getAmount() * atkFactor);
			}
		}
		
		if(event.getSource().getTrueSource() instanceof EntityPlayer){
			EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();
			if(!player.inventory.getCurrentItem().isEmpty()){
				ItemStack ci = player.inventory.getCurrentItem();
				Map<Enchantment, Integer> ench = EnchantmentHelper.getEnchantments(ci);
				if(ench.containsKey(MinaEnchantments.EMERGENCY)){
					float factor = Math.max(1f, Math.min(5f / player.getHealth(), 2.5f));
					float old_ammount = event.getAmount();
					event.setAmount(factor * old_ammount);
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onEntityMount(EntityMountEvent event){
		if(event.isDismounting() && event.getEntityBeingMounted() instanceof EntityArmorStand){
			if(MinaUtils.checkHasTag(event.getEntityBeingMounted(), BlockStool.DUMMY_TAG)){
				event.getEntityBeingMounted().setDead();
			}
		}
	}
	
	private float localDifficulty = 0f;
	private int localDifficultyRecalc = 0;
	
	private float getLocalDifficulty(World world, BlockPos pos){
		if(localDifficultyRecalc-- <= 0){
			localDifficulty = world.getDifficultyForLocation(pos).getAdditionalDifficulty();
			localDifficultyRecalc = 100;
		}
		return localDifficulty;
	}
	
	@SubscribeEvent
	public void onAttachEntityCapabilities(AttachCapabilitiesEvent.Entity e){
		if(e.getObject() instanceof EntityPlayer){
			e.addCapability(MinaCapabilities.MINA_PLAYER_CAPS_ID, new MinaCapabilities.MinaPlayerCapabilityProvider((EntityPlayer) e.getObject()));
		}
		if(e.getObject() instanceof EntityMob && e.getObject().world.getDifficulty() != EnumDifficulty.PEACEFUL){
			EntityMob entity = (EntityMob) e.getObject();
			MinaCapabilities.EntityStatsProvider<EntityMob> statsProv = new MinaCapabilities.EntityStatsProvider<EntityMob>((EntityMob)e.getObject(), new StatType[]{StatTypes.ATTACK, StatTypes.DEFENSE, StatTypes.SPEED});
			if(WorldProviderOverworldHook.isBloodMoon(entity.world)){
				statsProv.getStats().getStat(StatTypes.ATTACK).getActual().set(127 + entity.getRNG().nextInt(128));
				statsProv.getStats().getStat(StatTypes.DEFENSE).getActual().set(127 + entity.getRNG().nextInt(128));
				statsProv.getStats().getStat(StatTypes.SPEED).getActual().set(127 + entity.getRNG().nextInt(128));
			}else if(RNG.nextInt(10) == 0 && RNG.nextFloat() <= MathHelper.clamp(getLocalDifficulty(e.getObject().world, e.getObject().getPosition()), 0f, 1f) * 0.5f){
				statsProv.getStats().getStat(StatTypes.ATTACK).getActual().set(64 + entity.getRNG().nextInt(191));
				statsProv.getStats().getStat(StatTypes.DEFENSE).getActual().set(64 + entity.getRNG().nextInt(191));
				statsProv.getStats().getStat(StatTypes.SPEED).getActual().set(64 + entity.getRNG().nextInt(191));
			}
			e.addCapability(MinaCapabilities.MINA_ENTITY_STATS, statsProv);
		}
	}
	
	@SubscribeEvent
	public void onZombieSummonAid(SummonAidEvent event) {
		if(MinaMod.getMinaConfig().preventZombieSummonAid()) {
			event.setResult(Result.DENY);
		}
	}
}
