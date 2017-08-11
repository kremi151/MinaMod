package lu.kremi151.minamod.util.eventlisteners;

import java.util.HashMap;

import lu.kremi151.minamod.MinaBlocks;
import lu.kremi151.minamod.MinaEnchantments;
import lu.kremi151.minamod.MinaItems;
import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.MinaPermissions;
import lu.kremi151.minamod.block.BlockIceAltar;
import lu.kremi151.minamod.block.BlockLetterbox;
import lu.kremi151.minamod.block.tileentity.TileEntityLetterbox;
import lu.kremi151.minamod.block.tileentity.TileEntityPlate;
import lu.kremi151.minamod.entity.EntityIceSentinel;
import lu.kremi151.minamod.util.MinaUtils;
import lu.kremi151.minamod.worldgen.WorldGenPalm;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.server.permission.PermissionAPI;

public class BlockEvents {
	
	private MinaMod mod;
	
	private HashMap<String, Object> break_bypass = new HashMap<String, Object>();
	
	public BlockEvents(MinaMod mod){
		this.mod = mod;
	}
	
	@SubscribeEvent
	public void onBlockLeftClicked(PlayerInteractEvent.LeftClickBlock e) { // NO_UCD (unused code)
		if(e.getWorld().isRemote)return;
		IBlockState bs = e.getWorld().getBlockState(e.getPos());
		boolean creative = e.getEntityPlayer().capabilities.isCreativeMode;
		if(!e.getEntityPlayer().getHeldItemMainhand().isEmpty()){
			if(bs.getBlock() == MinaBlocks.ICE_ALTAR && bs.getValue(BlockIceAltar.CAN_SPAWN_BOSS) && e.getEntityPlayer().getHeldItemMainhand().getItem() == MinaItems.CITRIN){//TODO: Citrin ersetzen
				boolean bp = (Boolean) bs.getValue(BlockIceAltar.BLACK_PEARL);
				boolean wp = (Boolean) bs.getValue(BlockIceAltar.WHITE_PEARL);
				if(bp && wp){
					e.getWorld().playSound(e.getPos().getX(), e.getPos().getY(), e.getPos().getZ(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.MASTER, 4.0f, (1.0F + (e.getWorld().rand.nextFloat() - e.getWorld().rand.nextFloat()) * 0.2F) * 0.7F, true);
					e.getWorld().setBlockState(e.getPos(), bs.withProperty(BlockIceAltar.BLACK_PEARL, false).withProperty(BlockIceAltar.WHITE_PEARL, false));
					int ty = MinaUtils.getHeightValue(e.getWorld(), e.getPos().getX(), e.getPos().getZ());
					EntityIceSentinel eis = new EntityIceSentinel(e.getWorld());
					eis.setPosition((double)e.getPos().getX() + 0.5d, (double)ty + 0.1d, (double)e.getPos().getZ() + 0.5d);
					e.getWorld().spawnEntity(eis);
				}
			}
		}
	}

	@SubscribeEvent
	public void onBlockRightClicked(PlayerInteractEvent.RightClickBlock e) { // NO_UCD (unused code)
		if(e.getWorld().isRemote)return;
		IBlockState bs = e.getWorld().getBlockState(e.getPos());
		boolean creative = e.getEntityPlayer().capabilities.isCreativeMode;
		if(!e.getEntityPlayer().getHeldItemMainhand().isEmpty()){
			if(bs.getBlock() == MinaBlocks.ICE_ALTAR){
				if(e.getEntityPlayer().getHeldItemMainhand().getItem() == MinaItems.BLACK_PEARL && !((Boolean)bs.getValue(BlockIceAltar.BLACK_PEARL))){
					e.getWorld().setBlockState(e.getPos(), bs.withProperty(BlockIceAltar.BLACK_PEARL, true));
					if(!creative)e.getEntityPlayer().getHeldItemMainhand().shrink(1);
					return;
				}
				if(e.getEntityPlayer().getHeldItemMainhand().getItem() == MinaItems.WHITE_PEARL && !((Boolean)bs.getValue(BlockIceAltar.WHITE_PEARL))){
					e.getWorld().setBlockState(e.getPos(), bs.withProperty(BlockIceAltar.WHITE_PEARL, true));
					if(!creative)e.getEntityPlayer().getHeldItemMainhand().shrink(1);
					return;
				}
			}else if(bs.getBlock() == MinaBlocks.PLATE && e.getEntityPlayer().getHeldItemMainhand().getItem() instanceof ItemFood){
				TileEntityPlate te = (TileEntityPlate) e.getWorld().getTileEntity(e.getPos());
				if(e.getEntityPlayer().capabilities.isCreativeMode){
					te.setItem(e.getEntityPlayer().getHeldItemMainhand());
				}else{
					if(!te.getItem().isEmpty()){
						e.getEntityPlayer().dropItem(te.getItem(), true);
					}
					te.setItem(e.getEntityPlayer().getHeldItemMainhand());
					int i = e.getEntityPlayer().inventory.currentItem;
					e.getEntityPlayer().inventory.decrStackSize(i, 1);
				}
				e.getWorld().scheduleBlockUpdate(e.getPos(), bs.getBlock(), 1, 1);
				//TODO: 1.9
//				e.getWorld().markBlockForUpdate(e.getPos())
				return;
			}
			
			if(e.getEntityPlayer().getHeldItemMainhand().getItem() == Items.BLAZE_ROD && PermissionAPI.hasPermission(e.getEntityPlayer(), MinaPermissions.TICK_BLOCK_MANUALLY)){
				IBlockState ibs = e.getWorld().getBlockState(e.getPos());
				Block btu = ibs.getBlock();
				btu.updateTick(e.getWorld(), e.getPos(), ibs, e.getWorld().rand);
				return;
			}
		}else{
			if(bs.getBlock() == MinaBlocks.ICE_ALTAR){
				boolean bp = (Boolean) bs.getValue(BlockIceAltar.BLACK_PEARL);
				boolean wp = (Boolean) bs.getValue(BlockIceAltar.WHITE_PEARL);
				if(bp)e.getEntityPlayer().dropItem(MinaItems.BLACK_PEARL, 1);
				if(wp)e.getEntityPlayer().dropItem(MinaItems.WHITE_PEARL, 1);
				e.getWorld().setBlockState(e.getPos(), bs.withProperty(BlockIceAltar.BLACK_PEARL, false).withProperty(BlockIceAltar.WHITE_PEARL, false));
				return;
			}else if(bs.getBlock() == MinaBlocks.PLATE){
				TileEntityPlate te = (TileEntityPlate) e.getWorld().getTileEntity(e.getPos());
				if(!te.getItem().isEmpty()){
					if(e.getEntityPlayer().capabilities.isCreativeMode){
						te.setItem(null);
					}else{
						e.getEntityPlayer().dropItem(te.getItem(), true);
						te.setItem(null);
					}
					//TODO: 1.9
//					e.getWorld().markBlockForUpdate(e.getPos());
				}
				return;
			}
		}
	}
	
	@SubscribeEvent
	public void onBlockBreak(BreakEvent event){ // NO_UCD (unused code)
		if(event.getState().getBlock() == Blocks.SNOW_LAYER){
			if(event.getPlayer().getRNG().nextInt(100) == 0){
				if(!event.getPlayer().capabilities.isCreativeMode){
					ItemStack is = new ItemStack(MinaItems.EVER_SNOW, 1);
					EntityItem entityitem = new EntityItem(event.getWorld(), event.getPos().getX(), event.getPos().getY(), event.getPos().getZ(), is);
		            entityitem.setDefaultPickupDelay();
		            event.getWorld().spawnEntity(entityitem);
				}
			}
		}else if(event.getState().getBlock() instanceof BlockLetterbox){
			TileEntityLetterbox te = (TileEntityLetterbox) event.getWorld().getTileEntity(event.getPos());
			boolean c = (!event.getPlayer().capabilities.isCreativeMode) &! te.checkOwner(event.getPlayer().getGameProfile());
			if(c){
				event.setCanceled(true);
				ITextComponent msg = new TextComponentTranslation("msg.letterbox.blockbreak").setStyle(new Style().setColor(TextFormatting.RED));
				event.getPlayer().sendMessage(msg);
			}
		}else if(event.getState().getBlock() == Blocks.MOB_SPAWNER){
			if(!event.getPlayer().getHeldItemMainhand().isEmpty() && EnchantmentHelper.getEnchantmentLevel(MinaEnchantments.SPAWN_KEEPER, event.getPlayer().getHeldItemMainhand()) > 0){
				event.setExpToDrop(0);
				NBTTagCompound nbt = new NBTTagCompound();
				TileEntityMobSpawner te = (TileEntityMobSpawner) event.getWorld().getTileEntity(event.getPos());
				te.writeToNBT(nbt);
				
				break_bypass.put("br_" + event.getPos().toString(), nbt);
				
			}
		/*}else if(!event.getPlayer().capabilities.isCreativeMode && event.getState().getBlock() == MinaBlocks.SIEVE){
			ItemStack held = event.getPlayer().getHeldItem(EnumHand.MAIN_HAND);
			if(held.isEmpty() || held.getItem() instanceof ItemSpade || !(held.getItem() instanceof ItemTool)){
				int level = event.getState().getValue(BlockSieve.LEVEL);
				if(level > 0){
					BlockSieve.MaterialType material = MinaBlocks.SIEVE.getActualState(event.getState(), event.getWorld(), event.getPos()).getValue(BlockSieve.MATERIAL);
					
					if(material != BlockSieve.MaterialType.AIR && material.getLootTable() != null && event.getPlayer().getRNG().nextInt(3) == 0){
						List<ItemStack> stacks = event.getWorld().getLootTableManager().getLootTableFromLocation(material.getLootTable()).generateLootForPools(event.getPlayer().getRNG(), new LootContext.Builder((WorldServer) event.getWorld()).build());
						final int length = stacks.size();
						if(length > 0){
							MinaUtils.dropItem(stacks.get(event.getPlayer().getRNG().nextInt(length)), event.getWorld(), event.getPos().getX() + 0.5, event.getPos().getY(), event.getPos().getZ() + 0.5);
						}
					}
					
					IBlockState newState = event.getState().withProperty(BlockSieve.LEVEL, level - 1);
					if(level == 1){
						newState = newState.withProperty(BlockSieve.MATERIAL, BlockSieve.MaterialType.AIR);
					}
					event.getWorld().setBlockState(event.getPos(), newState);
					event.setCanceled(true);
				}
			}*/
		}
	}
	
	@SubscribeEvent
	public void onBlockDropItems(HarvestDropsEvent event){ // NO_UCD (unused code)
		if(event.getState().getBlock() == Blocks.OBSIDIAN){
			if(!event.isSilkTouching() && event.getDrops().size() != 0 && !event.getHarvester().inventory.getCurrentItem().isEmpty()){
				int el = EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, event.getHarvester().inventory.getCurrentItem());
				if(el == 5){
					event.getDrops().clear();
					ItemStack is = new ItemStack(MinaItems.OBSIDIAN_FRAGMENT,4);
					event.getDrops().add(is);
				}
			}
		}else if(event.getState().getBlock() == Blocks.MOB_SPAWNER){
			if(!event.getHarvester().getHeldItemMainhand().isEmpty() && EnchantmentHelper.getEnchantmentLevel(MinaEnchantments.SPAWN_KEEPER, event.getHarvester().getHeldItemMainhand()) > 0){
				event.getDrops().clear();
				
				String key = "br_" + event.getPos().toString();
				if(break_bypass.containsKey(key)){
					MinaMod.debugPrintln("Break bypass key found: " + key);
					NBTTagCompound nbt = (NBTTagCompound)break_bypass.get(key);
					break_bypass.remove(key);
					NBTTagCompound nbtr = new NBTTagCompound();
					nbtr.setTag("BlockEntityTag", nbt);
					ItemStack is = new ItemStack(Blocks.MOB_SPAWNER, 1);
					is.setTagCompound(nbtr);
					MinaUtils.applyLoresToItemStack(is, new String[]{I18n.translateToLocal("entity." + nbt.getString("EntityId") + ".name")});
					event.getDrops().add(is);
				}else{
					MinaMod.debugPrintln("Break bypass key not found...");
				}
			}
		}
	}
	
}
