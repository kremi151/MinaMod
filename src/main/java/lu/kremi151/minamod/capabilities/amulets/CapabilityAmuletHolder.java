package lu.kremi151.minamod.capabilities.amulets;

import java.util.Optional;

import lu.kremi151.minamod.item.amulet.AmuletBase;
import lu.kremi151.minamod.item.amulet.AmuletRegistry;
import lu.kremi151.minamod.item.amulet.AmuletStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class CapabilityAmuletHolder implements IAmuletHolder{

	@CapabilityInject(IAmuletHolder.class)
	public static final Capability<IAmuletHolder> CAPABILITY_AMULET_HOLDER = null;
	
	private final NonNullList<AmuletStack> inv = NonNullList.withSize(3, AmuletStack.EMPTY);

	@Override
	public AmuletStack getAmuletAt(int slot) {
		return inv.get(slot);
	}

	@Override
	public void setAmuletAt(int slot, AmuletStack amulet) {
		inv.set(slot, amulet);
	}
	
	public static final Capability.IStorage<IAmuletHolder> STORAGE = new Capability.IStorage<IAmuletHolder>(){

		@Override
		public NBTBase writeNBT(Capability<IAmuletHolder> capability, IAmuletHolder instance, EnumFacing side) {
			NBTTagCompound nbt = new NBTTagCompound();
			NBTTagList inv = new NBTTagList();
			for(int n = 0 ; n <3 ; n++){
				AmuletStack amulet = instance.getAmuletAt(n);
				NBTTagCompound anbt = new NBTTagCompound();
				if(!amulet.isEmpty()){
					anbt.setInteger("id", amulet.getAmulet().getId());
					anbt.setTag("data", amulet.getData());
					inv.appendTag(anbt);
				}else{
					anbt.setInteger("id", -1);
					inv.appendTag(anbt);
				}
			}
			nbt.setTag("amulets", inv);
			return nbt;
		}

		@Override
		public void readNBT(Capability<IAmuletHolder> capability, IAmuletHolder instance, EnumFacing side, NBTBase nbt_) {
			if(nbt_ instanceof NBTTagCompound){
				NBTTagCompound nbt = (NBTTagCompound)nbt_;
				if(nbt.hasKey("amulets", 9)){
					NBTTagList inv = nbt.getTagList("amulets", 10);
					final int bound = Math.min(inv.tagCount(), 3);
					for(int i = 0 ; i < bound ; i++){
						NBTTagCompound anbt = inv.getCompoundTagAt(i);
						int id = anbt.getInteger("id");
						Optional<AmuletBase> op = AmuletRegistry.getByIdSafe(id);
						if(op.isPresent()){
							instance.setAmuletAt(i, new AmuletStack(op.get(), anbt.getCompoundTag("data")));
						}else{
							instance.setAmuletAt(i, AmuletStack.EMPTY);
						}
					}
				}
			}
		}
		
	};

	@Override
	public int amuletAmount() {
		return 3;
	}

}
