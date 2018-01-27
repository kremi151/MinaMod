package lu.kremi151.minamod.item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

import javax.annotation.Nullable;

import lu.kremi151.minamod.advancements.triggers.MinaTriggers;
import lu.kremi151.minamod.capabilities.stats.ICapabilityStats;
import lu.kremi151.minamod.capabilities.stats.snack.ISnack;
import lu.kremi151.minamod.capabilities.stats.types.StatType;
import lu.kremi151.minamod.capabilities.stats.util.Stat;
import lu.kremi151.minamod.interfaces.ISyncCapabilitiesToClient;
import lu.kremi151.minamod.util.MinaUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemHerbMixture extends ItemFood implements ISyncCapabilitiesToClient{

	public ItemHerbMixture() {
		super(0, false);
		this.setContainerItem(Items.GLASS_BOTTLE);
		this.setAlwaysEdible();
	}
	
	@Override
	protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player)
    {
		super.onFoodEaten(stack, worldIn, player);
        if (!worldIn.isRemote)
        {
    		ISnack mixture = stack.getCapability(ISnack.CAPABILITY, null);
    		
    		if(mixture != null && mixture instanceof HerbMixtureSnack) {
            	MinaTriggers.TRIGGER_MODIFY_STATS.trigger((EntityPlayerMP) player);
            	
            	ICapabilityStats<EntityPlayer> stats = player.getCapability(ICapabilityStats.CAPABILITY, null);
            	
            	stats.listSupportedStatTypes().forEach(type -> {
            		int amount = mixture.getModification(type);
            		Stat stat = stats.getStat(type);
            		if(stat.getTraining().remaining() > amount){
            			stat.getTraining().increment(amount);
            		}else{
            			stat.getTraining().increment(stat.getTraining().remaining());
            		}
            	});
            	
        		player.inventory.addItemStackToInventory(new ItemStack(Items.GLASS_BOTTLE,1));
    		}
        }
    }
	
	@Override
	public EnumAction getItemUseAction(ItemStack par1ItemStack)
    {
        return EnumAction.DRINK;
    }
	
	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
    {
		ISnack cap = stack.getCapability(ISnack.CAPABILITY, null);
		
		if(cap != null && cap instanceof HerbMixtureSnack) {
			HerbMixtureSnack cap_ = (HerbMixtureSnack) cap;
			cap_.forEachType((type, val) -> {
				int amnt = val.intValue();
				if(amnt != 0)tooltip.add(I18n.translateToLocalFormatted("item.mixture." + type.getUnlocalizedName() + ".info", amnt));
			});
		}
		
		if(!hasEffect(stack, playerIn.getCapability(ICapabilityStats.CAPABILITY, null))){
			tooltip.add("");
			tooltip.add(TextFormatting.RED + I18n.translateToLocal("item.mixture.no_effect.info"));
		}
    }

	@Override
	public NBTTagCompound writeSyncableData(ItemStack stack, NBTTagCompound nbt) {
		return serializeCapability((HerbMixtureSnack) stack.getCapability(ISnack.CAPABILITY, null), nbt);
	}

	@Override
	public void readSyncableData(ItemStack stack, NBTTagCompound nbt) {
		deserializeCapability((HerbMixtureSnack) stack.getCapability(ISnack.CAPABILITY, null), nbt);
	}
	
	private static NBTTagCompound serializeCapability(HerbMixtureSnack cap, NBTTagCompound nbt) {
		NBTTagList list = new NBTTagList();
		cap.forEachType((type, val) -> {
			NBTTagCompound entry = new NBTTagCompound();
			entry.setString("Id", type.getId());
			entry.setInteger("Value", val.intValue());
			list.appendTag(entry);
		});
		nbt.setTag("List", list);
		nbt.setInteger("Color", cap.getColor());
		return nbt;
	}
	
	private static void deserializeCapability(HerbMixtureSnack cap, NBTTagCompound nbt) {
		cap.stats.clear();
		NBTTagCompound map = nbt.getCompoundTag("Map");
		NBTTagList list = nbt.getTagList("List", 10);
		for(int i = 0 ; i < list.tagCount() ; i++) {
			NBTTagCompound entry = list.getCompoundTagAt(i);
			if(entry.hasKey("Id", 8)) {
				StatType.findStatType(entry.getString("Id")).ifPresent(type -> cap.stats.put(type, entry.getInteger("Value")));
			}
		}
		cap.color = nbt.getInteger("Color");
	}

	@Override
	public net.minecraftforge.common.capabilities.ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt)
    {
        return new HerbMixtureProvider();
    }
	
	public static boolean hasEffect(ItemStack stack, ICapabilityStats<? extends EntityLivingBase> stats){
		ISnack cap = stack.getCapability(ISnack.CAPABILITY, null);
		
		if(cap != null && cap instanceof HerbMixtureSnack) {
			int sum = 0;
			
			for(StatType type : stats.listSupportedStatTypes()){
				Stat stat = stats.getStat(type);
				int training = cap.getModification(type);
				int nval = stat.getTraining().get() + training;
				if(!(stat.getMinTrainingValue() <= nval && nval <= stat.getMaxTrainingValue())){
					return false;
				}
				sum += training;
			}
			
			return sum <= stats.pointsLeft();
		}else {
			return false;
		}
	}
	
	private static class HerbMixtureProvider implements ICapabilityProvider, INBTSerializable<NBTTagCompound>{
		
		private final HerbMixtureSnack cap;
		
		private HerbMixtureProvider() {
			this(MinaUtils.COLOR_WHITE);
		}
		
		private HerbMixtureProvider(int color) {
			this.cap = new HerbMixtureSnack(color);
		}

		@Override
		public NBTTagCompound serializeNBT() {
			return serializeCapability(cap, new NBTTagCompound());
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt) {
			deserializeCapability(cap, nbt);
		}

		@Override
		public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
			return capability == ISnack.CAPABILITY;
		}

		@Override
		public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
			return (T) ((capability == ISnack.CAPABILITY) ? cap : null);
		}
		
	}
	
	public static class HerbMixtureSnack implements ISnack{
		
		private final HashMap<StatType, Integer> stats = new HashMap<>();
		private int color;
		
		private HerbMixtureSnack(int color) {
			this.color = color;
		}

		@Override
		public Set<StatType> getSupportedTypes() {
			return stats.keySet();
		}

		@Override
		public int getModification(StatType type) {
			Integer res = stats.get(type);
			if(res != null) {
				return res.intValue();
			}else {
				return 0;
			}
		}
		
		@Override
		public void forEachType(BiConsumer<StatType, Integer> consumer) {
			for(Map.Entry<StatType, Integer> e : stats.entrySet()) {
				consumer.accept(e.getKey(), e.getValue());
			}
		}
		
		public int getColor() {
			return color;
		}
		
		public void setColor(int color) {
			this.color = color;
		}

		@Override
		public boolean offer(StatType type, int value) {
			stats.put(type, value);
			return true;
		}
	}

}
