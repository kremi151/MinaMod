package lu.kremi151.minamod.item;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import lu.kremi151.minamod.MinaItems;
import lu.kremi151.minamod.capabilities.CapabilityItemKey;
import lu.kremi151.minamod.capabilities.IKey;
import lu.kremi151.minamod.interfaces.ISyncCapabilitiesToClient;
import lu.kremi151.minamod.util.MinaUtils;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemKey extends Item implements ISyncCapabilitiesToClient{

	public ItemKey(){
		this.setCreativeTab(CreativeTabs.TOOLS);
	}
	
	@Override
    @SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		ExtendedKeyCapability key = (ExtendedKeyCapability) stack.getCapability(IKey.CAPABILITY_KEY, null);
		switch(key.getState()){
		case DUPLICATE:
			tooltip.add(I18n.translateToLocal("item.key.info.duplicate"));
			break;
		case NORMAL:
			tooltip.add(I18n.translateToLocal("item.key.info.no_raw"));
			break;
		case RAW:
			tooltip.add(I18n.translateToLocal("item.key.info.raw"));
			break;
		case MASTER:
			tooltip.add(I18n.translateToLocal("item.key.info.master"));
			break;
		default:
			tooltip.add(I18n.translateToLocal("item.key.info.unknown"));
			break;
		}
	}
	
	public static int getColor(ItemStack stack){
		if(stack == null)throw new NullPointerException();
		if(stack.getItem() != MinaItems.KEY)return MinaUtils.COLOR_WHITE;
		return ((ExtendedKeyCapability) stack.getCapability(IKey.CAPABILITY_KEY, null)).getTint();
	}

	public static State getState(ItemStack stack){
		return ((ExtendedKeyCapability)stack.getCapability(IKey.CAPABILITY_KEY, null)).getState();
	}
	
	public static ItemStack rawKey(){
		ItemStack stack = new ItemStack(MinaItems.KEY, 1);
		ExtendedKeyCapability key = (ExtendedKeyCapability) stack.getCapability(IKey.CAPABILITY_KEY, null);
		key.setState(State.RAW);
		key.setTint(MinaUtils.COLOR_WHITE);
		return stack;
	}
	
	@Override
	public final net.minecraftforge.common.capabilities.ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt)
    {
        return new KeyCapabilityProvider(stack);
    }

	@Override
	public NBTTagCompound writeSyncableData(ItemStack stack, NBTTagCompound nbt) {
		ExtendedKeyCapability key = (ExtendedKeyCapability) stack.getCapability(IKey.CAPABILITY_KEY, null);
		nbt.setInteger("Tint", key.getTint());
		nbt.setByte("State", (byte)key.getState().id);
		return nbt;
	}

	@Override
	public void readSyncableData(ItemStack stack, NBTTagCompound nbt) {
		ExtendedKeyCapability key = (ExtendedKeyCapability) stack.getCapability(IKey.CAPABILITY_KEY, null);
		key.setTint(nbt.getInteger("Tint"));
		key.setState(State.getById(nbt.getByte("State")));
	}
	
	public static class ExtendedKeyCapability extends CapabilityItemKey{
		
		private int tint = MinaUtils.COLOR_WHITE;
		private State state = State.RAW;

		public ExtendedKeyCapability(ItemStack stack) {
			super(stack);
		}
		
		public int getTint() {
			return tint;
		}
		
		public void setTint(int tint) {
			this.tint = tint;
		}
		
		public State getState() {
			return state;
		}
		
		public void setState(State state) {
			this.state = state;
		}
		
		public void applyFrom(ExtendedKeyCapability other) {
			this.uuids.clear();
			this.uuids.addAll(other.uuids);
			
			this.state = other.state;
			this.tint = other.tint;
		}
		
	}
	
	private static class KeyCapabilityProvider implements ICapabilityProvider, INBTSerializable<NBTTagCompound>{
		
		private final ExtendedKeyCapability cap;
		
		private KeyCapabilityProvider(ItemStack stack){
			this.cap = new ExtendedKeyCapability(stack);
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
			for(UUID uuid : cap.getKeyIds()) {
				uuids_nbt.appendTag(MinaUtils.convertUUIDToNBT(uuid));
			}
			nbt.setTag("Unlockables", uuids_nbt);
			nbt.setInteger("Tint", cap.getTint());
			nbt.setByte("State", (byte)cap.getState().id);
			
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
				cap.setTint(nbt.getInteger("Tint"));
			}
			if(nbt.hasKey("State", 99)) {
				cap.setState(State.getById(nbt.getByte("State")));
			}
		}
		
	}
	
	public static enum State{
		UNKNOWN(-1),
		RAW(0),
		NORMAL(1),
		DUPLICATE(2),
		MASTER(3);
		
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
