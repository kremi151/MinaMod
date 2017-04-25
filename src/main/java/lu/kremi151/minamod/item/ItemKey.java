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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemKey extends Item{
	
	@Deprecated private static final String _UUID = "uuid";
	@Deprecated private static final String _COLOR = "tint";
	@Deprecated private static final String _DUPLICATE = "duplicate";
	@Deprecated private static final String _NOT_RAW = "encoded";
	@Deprecated private static final String ROOT_TAG = "keyProps";

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
		NBTTagCompound root = is.getSubCompound(ROOT_TAG);
		MinaKeyCapability cap = getMinaKey(is);
		if(root != null){
			if(root.hasKey(_UUID, 8)){
				cap.registerUnlockable(UUID.fromString(root.getString(_UUID)));
			}
			if(root.hasKey(_DUPLICATE, 99)){
				boolean v = root.getBoolean(_DUPLICATE);
				if(v){
					cap.setState(State.DUPLICATE);
				}
			}else if(root.hasKey(_NOT_RAW, 99)){
				boolean v = root.getBoolean(_NOT_RAW);
				if(v){
					cap.setState(State.NORMAL);
				}else{
					cap.setState(State.RAW);
				}
			}
			if(root.hasKey(_COLOR, 99)){
				cap.setTint(root.getInteger(_COLOR));
			}
			is.removeSubCompound(ROOT_TAG);
		}
		return cap;
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
	
	private static class KeyCapabilityProvider implements ICapabilityProvider{
		
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
		
	}
	
	public static class MinaKeyCapability extends CapabilityItemKey{

		private MinaKeyCapability(ItemStack stack) {
			super(stack);
		}
		
		public int getTint(){
			NBTTagCompound nbt = nbt();
			if(nbt.hasKey("Tint", 99)){
				return nbt.getInteger("Tint");
			}else{
				return MinaUtils.COLOR_WHITE;
			}
		}
		
		public void setTint(int tint){
			nbt().setInteger("Tint", tint);
		}
		
		public State getState(){
			NBTTagCompound nbt = nbt();
			if(nbt.hasKey("State", 99)){
				return State.getById(nbt.getByte("State"));
			}else{
				return State.RAW;
			}
		}
		
		public void setState(State state){
			nbt().setByte("State", (byte)state.id);
		}
		
		public void apply(MinaKeyCapability src){
			setTint(src.getTint());
			setState(src.getState());
			
			NBTTagList uuids = src.getUUIDs().copy();
			nbt().setTag("Ids", uuids);
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
