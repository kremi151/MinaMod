package lu.kremi151.minamod.util.eventlisteners;

import java.util.List;

import lu.kremi151.minamod.MinaBlocks;
import lu.kremi151.minamod.MinaEnchantments;
import lu.kremi151.minamod.MinaItems;
import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.block.BlockSieve;
import lu.kremi151.minamod.block.tileentity.TileEntitySieve;
import lu.kremi151.minamod.capabilities.stats.ICapabilityStats;
import lu.kremi151.minamod.interfaces.IFertilizable;
import lu.kremi151.minamod.util.MinaUtils;
import lu.kremi151.minamod.worlddata.MinaWorld;
import lu.kremi151.minamod.worlddata.data.FrostTemplePosition;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerPickupXpEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PlayerSpecificEvents {

	private MinaMod mod;
	
	public PlayerSpecificEvents(MinaMod mod){
		this.mod = mod;
	}
	
	@SubscribeEvent
	public void calculateBreakSpeed(PlayerEvent.BreakSpeed e){
		World world = e.getEntityPlayer().world;
		IBlockState state = world.getBlockState(e.getPos());
		if(state.getBlock() == MinaBlocks.SIEVE){
			TileEntitySieve te = (TileEntitySieve) world.getTileEntity(e.getPos());
			if(!te.isSieveEmpty()){
				e.setCanceled(true);
				if(!world.isRemote){
					if(!e.getEntityPlayer().getHeldItemMainhand().isEmpty() && e.getEntityPlayer().getHeldItemMainhand().getItem() instanceof ItemSpade){
						te.decrementLevel(0.15f);
						e.getEntityPlayer().getHeldItemMainhand().damageItem(1, e.getEntityPlayer());
					}else{
						te.decrementLevel(0.05f);
					}
					//world.setBlockState(e.getPos(), state.getActualState(world, e.getPos()));//TODO: optimize & fix
					//world.notifyBlockUpdate(e.getPos(), state, state.getActualState(world, e.getPos()), 3);

					BlockSieve.MaterialType material = te.getMaterial();
					if(te.getLevel() == 0.0f && material.getLootTable() != null){
						List<ItemStack> stacks = world.getLootTableManager().getLootTableFromLocation(material.getLootTable()).generateLootForPools(e.getEntityPlayer().getRNG(), new LootContext.Builder((WorldServer) world).build());
						if(stacks.size() > 0){
							MinaUtils.dropItem(stacks.get(e.getEntityPlayer().getRNG().nextInt(stacks.size())), world, e.getPos().getX() + 0.5, e.getPos().getY(), e.getPos().getZ() + 0.5);
						}
					}
				}
			}
			world.notifyBlockUpdate(e.getPos(), state, state.getActualState(world, e.getPos()), 3);
		}
	}
	
	@SubscribeEvent
	public void onItemDestroy(PlayerDestroyItemEvent event){ // NO_UCD (unused code)
		if(EnchantmentHelper.getEnchantmentLevel(MinaEnchantments.WOOD_BACKUP, event.getOriginal()) != 0){
			Item i = event.getOriginal().getItem();
			if(i instanceof ItemPickaxe){
				event.getEntityPlayer().inventory.addItemStackToInventory(new ItemStack(Items.WOODEN_PICKAXE));
			}else if(i instanceof ItemSpade){
				event.getEntityPlayer().inventory.addItemStackToInventory(new ItemStack(Items.WOODEN_SHOVEL));
			}else if(i instanceof ItemAxe){
				event.getEntityPlayer().inventory.addItemStackToInventory(new ItemStack(Items.WOODEN_AXE));
			}else if(i instanceof ItemHoe){
				event.getEntityPlayer().inventory.addItemStackToInventory(new ItemStack(Items.WOODEN_HOE));
			}else if(i instanceof ItemSword){
				event.getEntityPlayer().inventory.addItemStackToInventory(new ItemStack(Items.WOODEN_SWORD));
			}
		}
	}
	
	@SubscribeEvent
    public void bonemealUsed(BonemealEvent event) // NO_UCD (unused code)
    {
		if(event.getWorld().isRemote)return;
		Block b = event.getWorld().getBlockState(event.getPos()).getBlock();
		if(b == null)return;
		if(b instanceof IFertilizable){
			IFertilizable c = (IFertilizable)b;
			boolean r=c.onFertilize(event.getWorld(), event.getPos(), event.getWorld().rand);
			if(r)event.setResult(Result.ALLOW);
		}
    }
	
	@SubscribeEvent
	public void onEntityInteract(PlayerInteractEvent.EntityInteract event){ // NO_UCD (unused code)
		if(event.getEntityPlayer().world.isRemote)return;
		if(!event.getEntityPlayer().getHeldItemMainhand().isEmpty()){
			if(event.getEntityPlayer().getHeldItemMainhand().getItem() == Items.GLASS_BOTTLE
					&& event.getTarget() instanceof EntityCow){
				ItemStack is = event.getEntityPlayer().getHeldItemMainhand();
				if(is.getCount() == 1){
					event.getEntityPlayer().setHeldItem(EnumHand.MAIN_HAND, new ItemStack(MinaItems.MILK_BOTTLE,1));
				}else{
					is.shrink(1);
					event.getEntityPlayer().inventory.addItemStackToInventory(new ItemStack(MinaItems.MILK_BOTTLE,1));
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onPlayerInteract(PlayerInteractEvent event){ // NO_UCD (unused code)
		if(event.getEntityPlayer().world.isRemote)return;
		if(!event.getEntityPlayer().getHeldItemMainhand().isEmpty()){
			if(event.getEntityPlayer().getHeldItemMainhand().getItem() == MinaItems.HARMONY_PEARL){
				MinaMod.debugPrintln("harmony pearl used");
//				MinaMod.getMinaMod().getPacketDispatcher().sendTo(new MessageHarmonyPearl(0,0,0), (EntityPlayerMP) event.getEntityPlayer());//TODO
				FrostTemplePosition ftp = MinaWorld.forWorld(event.getEntityPlayer().world).getFrostTempleMeta().getNearest(event.getEntityPlayer().posX, event.getEntityPlayer().posY, event.getEntityPlayer().posZ);
				if(ftp != null){
					MinaMod.debugPrintln("found temple: " + ftp);
					MinaUtils.makeLookAt(ftp.getAltarPosition().getX(), ftp.getAltarPosition().getY(), ftp.getAltarPosition().getZ(), event.getEntityPlayer());
					event.getEntityPlayer().playSound(SoundEvents.ENTITY_FIREWORK_LAUNCH, 1f, 1f);
				}else{
					MinaMod.debugPrintln("no temple found");
					event.getEntityPlayer().playSound(SoundEvents.BLOCK_STONE_BUTTON_CLICK_ON, 1f, 1f);
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onPickupExp(PlayerPickupXpEvent event){
		/*float testExp = event.getEntityPlayer().experience;
		float newExp = (float)event.getOrb().getXpValue() / (float)event.getEntityPlayer().xpBarCap();
		
		if(event.getEntityPlayer().experience + newExp >= 1.0F){
			LEVEL UP
		}*/
		
		((ICapabilityStats)event.getEntityPlayer().getCapability(ICapabilityStats.CAPABILITY, null)).getEffort().increment(event.getOrb().xpValue);
	}
	
	@SubscribeEvent
	public void onPlayerClone(PlayerEvent.Clone event){
		if(event.isWasDeath()){
			event.getEntityPlayer().getCapability(ICapabilityStats.CAPABILITY, null)
				.applyFrom(event.getOriginal().getCapability(ICapabilityStats.CAPABILITY, null));
		}
	}
	
	//TODO: Oil
//	@SubscribeEvent
//    public void onBucketFill(FillBucketEvent event) {
//		MovingObjectPosition t = event.target;
//		IBlockState bs = event.world.getBlockState(t.getBlockPos());
//		Block b = bs.getBlock();
//		if(b == null)return;
//		if(!(b instanceof BlockLiquid))return;
//		int level = (Integer)bs.getValue(BlockLiquid.LEVEL);
//		ItemStack res = null;
//		if(level == 0){
//			if(b == MinaBlocks.blockOil){
//				event.world.setBlockToAir(t.getBlockPos());
//				res = new ItemStack(MinaItems.itemOilBucket, 1);
//			}
//		}
//		if(res != null){
//			event.result = res;
//            event.setResult(Result.ALLOW);
//		}
//    }

}
