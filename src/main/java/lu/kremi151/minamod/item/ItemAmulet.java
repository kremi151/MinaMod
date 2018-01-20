package lu.kremi151.minamod.item;

import java.util.List;

import javax.annotation.Nullable;

import lu.kremi151.minamod.MinaCreativeTabs;
import lu.kremi151.minamod.MinaItems;
import lu.kremi151.minamod.capabilities.amulets.IAmulet;
import lu.kremi151.minamod.capabilities.amulets.ItemAmuletCapabilityProvider;
import lu.kremi151.minamod.capabilities.amulets.impl.AmuletEnder;
import lu.kremi151.minamod.capabilities.amulets.impl.AmuletExperience;
import lu.kremi151.minamod.capabilities.amulets.impl.AmuletHarmony;
import lu.kremi151.minamod.capabilities.amulets.impl.AmuletPotionEffect;
import lu.kremi151.minamod.capabilities.amulets.impl.AmuletReturn;
import lu.kremi151.minamod.interfaces.ISyncCapabilitiesToClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemAmulet extends Item{

	public ItemAmulet(){
		this.setCreativeTab(MinaCreativeTabs.AMULETS);
		this.setMaxStackSize(1);
	}
	
	@Override
	public net.minecraftforge.common.capabilities.ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt)
    {
		final IAmulet cap;
        if(stack.getItem() == MinaItems.AMULET_OF_EXPERIENCE) {
        	cap = new AmuletExperience();
        }else if(stack.getItem() == MinaItems.AMULET_OF_RETURN) {
        	cap = new AmuletReturn();
        }else if(stack.getItem() == MinaItems.AMULET_OF_REGENERATION) {
        	cap = new AmuletPotionEffect(stack, MobEffects.REGENERATION, 200);
        }else if(stack.getItem() == MinaItems.AMULET_OF_MERMAID) {
        	cap = new AmuletPotionEffect(stack, MobEffects.WATER_BREATHING, 200);
        }else if(stack.getItem() == MinaItems.AMULET_OF_HARMONY) {
        	cap = new AmuletHarmony();
        }else {
        	cap = new AmuletEnder(stack);
        }
        return new ItemAmuletCapabilityProvider(cap);
    }
	
	protected IAmulet getAmulet(ItemStack stack) {
		return stack.getCapability(IAmulet.CAPABILITY, null);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
    public boolean hasEffect(ItemStack stack){
		IAmulet amulet = getAmulet(stack);
		return amulet != null && amulet.hasEffect();
	}
	
	@Override
	public boolean showDurabilityBar(ItemStack stack)
    {
		IAmulet amulet = getAmulet(stack);
        return amulet != null && amulet.hasDurability();
    }
	
	@Override
	public double getDurabilityForDisplay(ItemStack stack)
    {
		IAmulet amulet = getAmulet(stack);
        return (amulet != null && amulet.hasDurability()) ? amulet.getDurability() : super.getDurabilityForDisplay(stack);
    }
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced)
    {
		IAmulet amulet = getAmulet(stack);
		if(amulet != null)amulet.addInformation(player, tooltip, advanced);
    }
	
	public static class Syncable extends ItemAmulet implements ISyncCapabilitiesToClient{

		@Override
		public NBTTagCompound writeSyncableData(ItemStack stack, NBTTagCompound nbt) {
			IAmulet amulet = getAmulet(stack);
			if(amulet != null) {
				return amulet.saveSyncData(nbt);
			}else {
				return nbt;
			}
		}

		@Override
		public void readSyncableData(ItemStack stack, NBTTagCompound nbt) {
			IAmulet amulet = getAmulet(stack);
			if(amulet != null)amulet.loadSyncData(nbt);
		}
		
	}
}
