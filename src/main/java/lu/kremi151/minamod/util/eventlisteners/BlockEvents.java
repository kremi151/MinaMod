package lu.kremi151.minamod.util.eventlisteners;

import java.util.HashMap;

import lu.kremi151.minamod.MinaBlocks;
import lu.kremi151.minamod.MinaEnchantments;
import lu.kremi151.minamod.MinaItems;
import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.MinaPermissions;
import lu.kremi151.minamod.advancements.triggers.MinaTriggers;
import lu.kremi151.minamod.block.BlockBook;
import lu.kremi151.minamod.block.BlockIceAltar;
import lu.kremi151.minamod.block.BlockLetterbox;
import lu.kremi151.minamod.block.tileentity.TileEntityBook;
import lu.kremi151.minamod.block.tileentity.TileEntityLetterbox;
import lu.kremi151.minamod.capabilities.energynetwork.IEnergyNetworkProvider;
import lu.kremi151.minamod.entity.EntityIceSentinel;
import lu.kremi151.minamod.util.IDRegistry;
import lu.kremi151.minamod.util.MinaUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemWrittenBook;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;
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
		final ItemStack stackInMainhand = e.getEntityPlayer().getHeldItemMainhand();
		final IBlockState state = e.getWorld().getBlockState(e.getPos());
		final boolean creative = e.getEntityPlayer().capabilities.isCreativeMode;
		if(!e.getEntityPlayer().getHeldItemMainhand().isEmpty()){
			if(!e.getWorld().isRemote && state.getBlock() == MinaBlocks.ICE_ALTAR && state.getValue(BlockIceAltar.CAN_SPAWN_BOSS) && e.getEntityPlayer().getHeldItemMainhand().getItem() == MinaItems.CITRIN){//TODO: Citrin ersetzen
				boolean bp = (Boolean) state.getValue(BlockIceAltar.BLACK_PEARL);
				boolean wp = (Boolean) state.getValue(BlockIceAltar.WHITE_PEARL);
				if(bp && wp){
					e.getWorld().playSound(e.getPos().getX(), e.getPos().getY(), e.getPos().getZ(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.MASTER, 4.0f, (1.0F + (e.getWorld().rand.nextFloat() - e.getWorld().rand.nextFloat()) * 0.2F) * 0.7F, true);
					e.getWorld().setBlockState(e.getPos(), state.withProperty(BlockIceAltar.BLACK_PEARL, false).withProperty(BlockIceAltar.WHITE_PEARL, false));
					int ty = MinaUtils.getHeightValue(e.getWorld(), e.getPos().getX(), e.getPos().getZ());
					EntityIceSentinel eis = new EntityIceSentinel(e.getWorld());
					eis.setPosition((double)e.getPos().getX() + 0.5d, (double)ty + 0.1d, (double)e.getPos().getZ() + 0.5d);
					e.getWorld().spawnEntity(eis);
				}
			}
		}
	}

	@SubscribeEvent
	public void onBlockRightClicked(PlayerInteractEvent.RightClickBlock event) { // NO_UCD (unused code)
		if(!event.getWorld().isRemote && onBlockRightClickedLocal(event)) {
			return;
		}
		onBlockRightClickedCommon(event);
	}
	
	private boolean onBlockRightClickedLocal(PlayerInteractEvent.RightClickBlock event) {
		final IBlockState state = event.getWorld().getBlockState(event.getPos());
		final boolean creative = event.getEntityPlayer().capabilities.isCreativeMode;
		if(state.getBlock() == Blocks.CRAFTING_TABLE) {
			event.getEntityPlayer().openGui(MinaMod.getMinaMod(), IDRegistry.guiIdExtendedCrafting, event.getWorld(), event.getPos().getX(), event.getPos().getY(), event.getPos().getZ());
			event.setCanceled(true);
		}else if(!event.getEntityPlayer().getHeldItemMainhand().isEmpty()){
			if(state.getBlock() == MinaBlocks.ICE_ALTAR){
				if(event.getEntityPlayer().getHeldItemMainhand().getItem() == MinaItems.BLACK_PEARL && !((Boolean)state.getValue(BlockIceAltar.BLACK_PEARL))){
					event.getWorld().setBlockState(event.getPos(), state.withProperty(BlockIceAltar.BLACK_PEARL, true));
					if(!creative)event.getEntityPlayer().getHeldItemMainhand().shrink(1);
					return true;
				}
				if(event.getEntityPlayer().getHeldItemMainhand().getItem() == MinaItems.WHITE_PEARL && !((Boolean)state.getValue(BlockIceAltar.WHITE_PEARL))){
					event.getWorld().setBlockState(event.getPos(), state.withProperty(BlockIceAltar.WHITE_PEARL, true));
					if(!creative)event.getEntityPlayer().getHeldItemMainhand().shrink(1);
					return true;
				}
			}
			
			if(event.getEntityPlayer().getHeldItemMainhand().getItem() == Items.BLAZE_ROD && PermissionAPI.hasPermission(event.getEntityPlayer(), MinaPermissions.TICK_BLOCK_MANUALLY)){
				IBlockState ibs = event.getWorld().getBlockState(event.getPos());
				Block btu = ibs.getBlock();
				btu.updateTick(event.getWorld(), event.getPos(), ibs, event.getWorld().rand);
				return true;
			}
		}else{
			if(state.getBlock() == MinaBlocks.ICE_ALTAR){
				boolean bp = (Boolean) state.getValue(BlockIceAltar.BLACK_PEARL);
				boolean wp = (Boolean) state.getValue(BlockIceAltar.WHITE_PEARL);
				if(bp)event.getEntityPlayer().dropItem(MinaItems.BLACK_PEARL, 1);
				if(wp)event.getEntityPlayer().dropItem(MinaItems.WHITE_PEARL, 1);
				event.getWorld().setBlockState(event.getPos(), state.withProperty(BlockIceAltar.BLACK_PEARL, false).withProperty(BlockIceAltar.WHITE_PEARL, false));
				return true;
			}
		}
		return false;
	}
	
	private void onBlockRightClickedCommon(PlayerInteractEvent.RightClickBlock event) {
		final ItemStack stackInMainhand = event.getItemStack();
		if(!stackInMainhand.isEmpty() && !event.getEntityPlayer().isSneaking() && stackInMainhand.getItem() instanceof ItemWrittenBook) {
			TileEntityBook teBook = new TileEntityBook();
			teBook.setBookItem(stackInMainhand.copy());
			stackInMainhand.shrink(1);
			BlockPos pos = event.getPos().offset(event.getFace());
			event.getWorld().setBlockState(pos, MinaBlocks.BOOK.getDefaultState().withProperty(BlockBook.FACING, event.getEntityPlayer().getAdjustedHorizontalFacing()));
			event.getWorld().setTileEntity(pos, teBook);
			event.setCanceled(true);
			return;
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
		}else if(event.getState().getBlock() == MinaBlocks.GRAVESTONE && !event.getWorld().isRemote && !event.getPlayer().capabilities.isCreativeMode) {
			MinaTriggers.TRIGGER_BREAK_GRAVE.trigger((EntityPlayerMP) event.getPlayer());
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
	
	@SubscribeEvent
	public void onBlockPlaced(PlaceEvent event) {
		final TileEntity te = event.getWorld().getTileEntity(event.getPos());
		if(te != null) {
			for(EnumFacing dir : EnumFacing.VALUES) {
				if(te.hasCapability(CapabilityEnergy.ENERGY, dir)) {
					TileEntity te1 = event.getWorld().getTileEntity(event.getPos().offset(dir));
					if(te1 != null && te1.hasCapability(IEnergyNetworkProvider.CAPABILITY, dir.getOpposite())) {
						te1.getCapability(IEnergyNetworkProvider.CAPABILITY, dir.getOpposite()).getNetwork().registerClient(event.getPos(), dir);
					}
				}
			}
		}
	}
	
}
