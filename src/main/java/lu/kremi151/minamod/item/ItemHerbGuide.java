package lu.kremi151.minamod.item;

import java.util.Arrays;
import java.util.List;

import lu.kremi151.minamod.MinaCreativeTabs;
import lu.kremi151.minamod.MinaItems;
import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.enums.EnumHerb;
import lu.kremi151.minamod.util.IDRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemHerbGuide extends Item{

	public ItemHerbGuide(){
		this.setCreativeTab(MinaCreativeTabs.MIXTURES);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
	{
		tooltip.add(I18n.translateToLocalFormatted("item.herb_guide.lore", MathHelper.floor(percentageCompleted(stack) * 100.0f)));
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public EnumRarity getRarity(ItemStack stack)
    {
		if(percentageCompleted(stack) >= 1.0f) {
			return EnumRarity.EPIC;
		}else if(percentageSeen(stack) >= 1.0f) {
			return EnumRarity.RARE;
		}else {
			return EnumRarity.COMMON;
		}
    }
	
	public static float percentageSeen(ItemStack stack){
		int seen = stack.getOrCreateSubCompound("Guide").getInteger("Seen");
		float count = 0;
		for(int i = 0 ; i < 27 ; i++){
			if(((seen >> i) & 1) == 1)count += 1.0f;
		}
		return count / 27.0f;
	}
	
	public static float percentageCompleted(ItemStack stack){
		float perc = percentageSeen(stack);
		if(!includeStats(stack))perc *= 0.85f;
		if(!includeCrossing(stack))perc *= 0.85f;
		if(!includeMutability(stack))perc *= 0.90f;
		return perc;
	}
	
	public static boolean hasSeen(ItemStack stack, EnumHerb herb){
		return hasSeen(stack, herb.getHerbId());
	}
	
	public static boolean hasSeen(ItemStack stack, byte herbId){
		if(!stack.isEmpty() && stack.getItem() == MinaItems.HERB_GUIDE){
			NBTTagCompound nbt = stack.getOrCreateSubCompound("Guide");
			return ((nbt.getLong("Seen") >> (int)herbId) & 1) == 1;
		}
		return false;
	}
	
	public static boolean includeStats(ItemStack stack){
		if(!stack.isEmpty() && stack.getItem() == MinaItems.HERB_GUIDE){
			NBTTagCompound nbt = stack.getOrCreateSubCompound("Guide");
			return nbt.getBoolean("Stats");
		}
		return false;
	}
	
	public static boolean includeCrossing(ItemStack stack){
		if(!stack.isEmpty() && stack.getItem() == MinaItems.HERB_GUIDE){
			NBTTagCompound nbt = stack.getOrCreateSubCompound("Guide");
			return nbt.getBoolean("Crossing");
		}
		return false;
	}
	
	public static boolean includeMutability(ItemStack stack){
		if(!stack.isEmpty() && stack.getItem() == MinaItems.HERB_GUIDE){
			NBTTagCompound nbt = stack.getOrCreateSubCompound("Guide");
			return nbt.getBoolean("Mutability");
		}
		return false;
	}
	
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand)
    {
    	if(!worldIn.isRemote){
    		ItemStack stack = playerIn.getHeldItem(hand);
    		NBTTagCompound nbt = stack.getOrCreateSubCompound("Guide");
    		long seen = nbt.getLong("Seen");
    		for(int i = 0 ; i < playerIn.inventory.getSizeInventory() ; i++){
    			ItemStack inv_stack = playerIn.inventory.getStackInSlot(i);
    			if(!inv_stack.isEmpty()){
    				if(inv_stack.getItem() == MinaItems.HERB){
    					EnumHerb herb = EnumHerb.getByHerbId((byte)inv_stack.getMetadata());
    					seen |= (1L << inv_stack.getMetadata());
    				}else if(inv_stack.getItem() == MinaItems.MIXTURE){
    					nbt.setBoolean("Stats", true);
    				}
    			}
    		}
    		nbt.setLong("Seen", seen);
    		float perc = percentageSeen(stack);
    		if(perc >= 0.2f)nbt.setBoolean("Crossing", true);
    		if(perc >= 0.6f)nbt.setBoolean("Mutability", true);
    	}
    	playerIn.openGui(MinaMod.getMinaMod(), IDRegistry.guiIdHerbGuide, worldIn, hand==EnumHand.MAIN_HAND?1:0, 0, 0);
		return new ActionResult(EnumActionResult.SUCCESS, playerIn.getHeldItem(hand));
    }
}
