package lu.kremi151.minamod.item;

import java.util.List;

import javax.annotation.Nullable;

import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.MinaPermissions;
import lu.kremi151.minamod.entity.EntitySoulPearl;
import lu.kremi151.minamod.util.MinaUtils;
import lu.kremi151.minamod.util.TextHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityList.EntityEggInfo;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.server.permission.PermissionAPI;

public class ItemSoulPearl extends Item{

    public ItemSoulPearl()
    {
        this.maxStackSize = 1;
        this.setMaxDamage(20);
        this.setCreativeTab(CreativeTabs.MATERIALS);
        this.setHasSubtypes(true);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand)
    {
    	if(!MinaMod.getMinaConfig().enableSoulPearls()){
    		TextHelper.sendTranslateableErrorMessage(playerIn, "msg.soulpearl.disabled");
    	}else if(!PermissionAPI.hasPermission(playerIn, MinaPermissions.ALLOW_SOUL_PEARL_USE)){
    		TextHelper.sendTranslateableErrorMessage(playerIn, "msg.soulpearl.not_allowed");
    	}else{
    		ItemStack itemStackIn = playerIn.getHeldItem(hand);
    		
    		if (!playerIn.capabilities.isCreativeMode)
            {
                itemStackIn.shrink(1);
            }

            worldIn.playSound((EntityPlayer)null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
            
            if (!worldIn.isRemote)
            {
            	NBTTagCompound entity_nbt = (checkIfFull(itemStackIn))?itemStackIn.getTagCompound().getCompoundTag("entity"):null;
            	EntitySoulPearl sp = new EntitySoulPearl(worldIn, playerIn);
                sp.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 1.5F, 1.0F);
                if(itemStackIn.hasDisplayName()) sp.setCustomName(itemStackIn.getDisplayName());
                worldIn.spawnEntity(sp.setDamage(itemStackIn.getItemDamage()).setCatchedEntityNBT(entity_nbt).setCanDrop(!playerIn.capabilities.isCreativeMode));
            }

            playerIn.addStat(StatList.getObjectUseStats(this));
            return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
    	}        
        return super.onItemRightClick(worldIn, playerIn, hand);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
    	if(checkIfFull(stack)){
    		NBTTagCompound nbt = stack.getTagCompound().getCompoundTag("entity");
    		if(nbt.hasKey("id")){
//    			tooltip.add(I18n.translateToLocal("entity." + nbt.getString("id") + ".name"));
    			tooltip.add(I18n.translateToLocal("entity." + EntityList.getTranslationName(new ResourceLocation(nbt.getString("id"))) + ".name"));
    		}
    	}
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack)
    {
        return checkIfFull(stack);
    }
    
    public static boolean catchEntity(ItemStack is, EntityLivingBase entity){
    	if(entity == null)return false;
    	NBTTagCompound nbt = is.getOrCreateSubCompound("entity");
    	if(!entity.writeToNBTOptional(nbt)){
    		return false;
    	}
    	
    	EntityList.EntityEggInfo eei = (EntityEggInfo) EntityList.ENTITY_EGGS.get(EntityList.getKey(entity));
		if(eei != null){
			int r = (MinaUtils.extractRedFromColor(eei.primaryColor) + MinaUtils.extractRedFromColor(eei.primaryColor))/2;
			int g = (MinaUtils.extractGreenFromColor(eei.primaryColor) + MinaUtils.extractGreenFromColor(eei.primaryColor))/2;
			int b = (MinaUtils.extractBlueFromColor(eei.primaryColor) + MinaUtils.extractBlueFromColor(eei.primaryColor))/2;
			is.getTagCompound().setInteger("tint", MinaUtils.convertRGBToDecimal(r, g, b));
		}
    	return true;
    }
    
    public static boolean checkIfFull(ItemStack is){
    	if(is.getTagCompound()==null){
    		return false;
    	}else{
    		return is.getTagCompound().hasKey("entity", 10);
    	}
    	
    }
    
    public static boolean checkIfTinted(ItemStack is){
    	if(is.getTagCompound()==null){
    		return false;
    	}else{
    		return is.getTagCompound().hasKey("tint", 3);
    	}
    	
    }
    
    public static Class<? extends Entity> getEntityType(ItemStack is){ // NO_UCD (unused code)
    	if(checkIfFull(is)){
    		//TODO: Check if this works:
    		return EntityList.getClass(new ResourceLocation(is.getTagCompound().getCompoundTag("entity").getString("id")));
    	}
    	return null;
    }

}
