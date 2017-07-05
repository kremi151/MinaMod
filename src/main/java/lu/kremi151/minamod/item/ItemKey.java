package lu.kremi151.minamod.item;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import lu.kremi151.minamod.MinaItems;
import lu.kremi151.minamod.capabilities.CapabilityItemKey;
import lu.kremi151.minamod.capabilities.IKey;
import lu.kremi151.minamod.util.MinaUtils;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemKey extends Item{

	public ItemKey(){
		this.setCreativeTab(CreativeTabs.MATERIALS);
	}

	@Override
	public void onCreated(ItemStack is, World par2World, EntityPlayer par3EntityPlayer) {
		getData(is);
	}
	
	@Override
    @SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		State state = getData(stack).getState();
		switch(state){
		case DUPLICATE:
			tooltip.add(I18n.translateToLocal("item.key.info.duplicate"));
			break;
		case NORMAL:
			tooltip.add(I18n.translateToLocal("item.key.info.no_raw"));
			break;
		case RAW:
			tooltip.add(I18n.translateToLocal("item.key.info.raw"));
			break;
		default:
			tooltip.add(I18n.translateToLocal("item.key.info.unknown"));
			break;
		}
	}
	
	public static int getColor(ItemStack is){
		if(is == null)throw new NullPointerException();
		if(is.getItem() != MinaItems.KEY)return MinaUtils.COLOR_WHITE;
		return getData(is).getTint();
	}
	
	public static void setColor(ItemStack is, int color){
		if(is == null)throw new NullPointerException();
		if(is.getItem() != MinaItems.KEY)return;
		getData(is).setTint(color);
	}

	public static State getState(ItemStack is){
		if(is == null)throw new NullPointerException();
		if(is.getItem() != MinaItems.KEY)return State.UNKNOWN;
		return getData(is).getState();
	}
	
	public static void setState(ItemStack is, State s){
		if(is == null || s == null)throw new NullPointerException();
		if(is.getItem() != MinaItems.KEY)return;
		getData(is).setState(s);
	}
	
	public static MinaKeyCapability getData(ItemStack is){
		if(is == null)throw new NullPointerException();
		if(is.isEmpty() || !(is.getItem() instanceof ItemKey))throw new IllegalArgumentException("The supplied item stack is empty or not a key");
		return getMinaKey(is);
	}
	
	private static MinaKeyCapability getMinaKey(ItemStack stack){
		return (MinaKeyCapability) stack.getCapability(IKey.CAPABILITY_KEY, null);
	}
	
	public static ItemStack rawKey(){
		ItemStack is = new ItemStack(MinaItems.KEY, 1);
		getData(is).setState(State.RAW);
		return is;
	}
	
	@Override
	public final net.minecraftforge.common.capabilities.ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt)
    {
        return new KeyCapabilityProvider(stack);
    }
	
	private static class KeyCapabilityProvider implements ICapabilityProvider, INBTSerializable<NBTTagCompound>{
		
		private final MinaKeyCapability cap;
		
		private KeyCapabilityProvider(ItemStack stack){
			this.cap = new MinaKeyCapability(stack);
		}

		@Override
		public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
			return capability == IKey.CAPABILITY_KEY;
		}

		@Override
		public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
			return (T) ((capability == IKey.CAPABILITY_KEY)?cap:null);
		}

		@Override
		public NBTTagCompound serializeNBT() {
			NBTTagCompound nbt = new NBTTagCompound();
			
			NBTTagList uuids_nbt = new NBTTagList();
			for(UUID uuid : cap.getUnlockableUUIDs()) {
				uuids_nbt.appendTag(MinaUtils.convertUUIDToNBT(uuid));
			}
			nbt.setTag("Unlockables", uuids_nbt);
			nbt.setInteger("Tint", cap.tint);
			nbt.setByte("State", (byte)cap.state.id);
			
			return nbt;
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt) {
			cap.clearUnlockables();
			
			NBTTagList uuids_nbt = nbt.getTagList("Unlockables", 10);
			for(int i = 0 ; i < uuids_nbt.tagCount() ; i++) {
				cap.registerUnlockable(MinaUtils.convertNBTToUUID(uuids_nbt.getCompoundTagAt(i)));
			}
			if(nbt.hasKey("Tint", 99)) {
				cap.tint = nbt.getInteger("Tint");
			}else {
				cap.tint = MinaUtils.COLOR_WHITE;
			}
			if(nbt.hasKey("State", 99)) {
				cap.state = State.getById(nbt.getByte("State"));
			}else {
				cap.state = State.RAW;
			}
		}
		
	}
	
	public static class MinaKeyCapability extends CapabilityItemKey{
		
		private int tint = MinaUtils.COLOR_WHITE;
		private State state = State.RAW;

		private MinaKeyCapability(ItemStack stack) {
			super(stack);
		}
		
		public int getTint(){
			return tint;
		}
		
		public void setTint(int tint){
			this.tint = tint;
		}
		
		public State getState(){
			return state;
		}
		
		public void setState(State state){
			this.state = state;
		}
		
		public void apply(MinaKeyCapability src){
			setTint(src.getTint());
			setState(src.getState());
			
			clearUnlockables();
			registerUnlockables(src.getUnlockableUUIDs());
		}
		
	}
	
	public static enum State{
		UNKNOWN(-1),
		RAW(0),
		NORMAL(1),
		DUPLICATE(2);
		
		private int id;
		
		private State(int id){
			this.id = id;
		}
		
		public static State getById(int id){
			for(State s : values()){
				if(s.id == id)return s;
			}
			return UNKNOWN;
		}
		
	}
}
