package lu.kremi151.minamod.capabilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

public class CapabilityItemKey implements IKey{
	
	private final ItemStack stack;
	
	public CapabilityItemKey(ItemStack stack){
		this.stack = stack;
	}
	
	protected NBTTagCompound nbt(){
		return stack.getOrCreateSubCompound("Key");
	}
	
	private void addToNBT(UUID uuid){
		NBTTagList list;
		NBTTagCompound nbt = nbt();
		if(nbt.hasKey("Ids", 9)){
			list = nbt.getTagList("Ids", 8);
		}else{
			list = new NBTTagList();
			nbt.setTag("Ids", list);
		}
		list.appendTag(new NBTTagString(uuid.toString()));
	}
	
	protected NBTTagList getUUIDs(){
		NBTTagCompound nbt = nbt();
		if(nbt.hasKey("Ids", 9)){
			return nbt.getTagList("Ids", 8);
		}else{
			NBTTagList list = new NBTTagList();
			nbt.setTag("Ids", list);
			return list;
		}
	}

	@Override
	public boolean canUnlock(UUID uuid){
		NBTTagList list = getUUIDs();
		if(list != null){
			int length = list.tagCount();
			for(int i = 0 ; i < length ; i++){
				try{
					UUID _uuid = UUID.fromString(list.getStringTagAt(i));
					if(_uuid.equals(uuid)){
						return true;
					}
				}catch(IllegalArgumentException e){
					System.out.println("Skipping erroneous uuid \"" + list.getStringTagAt(i) + "\"");
					list.removeTag(i);
				}
			}
		}
		return false;
	}

	@Override
	public void registerUnlockable(UUID uuid) {
		if(uuid != null){
			addToNBT(uuid);
		}
	}
	
	protected void clearUnlockables(){
		nbt().removeTag("Ids");
	}
	
	protected List<UUID> getUnlockables(){
		NBTTagList list = getUUIDs();
		ArrayList<UUID> uuids = new ArrayList<UUID>();
		if(list != null){
			int length = list.tagCount();
			for(int i = 0 ; i < length ; i++){
				uuids.add(UUID.fromString(list.getStringTagAt(i)));
			}
		}
		return Collections.unmodifiableList(uuids);
	}

	@Override
	public boolean empty() {
		return getUUIDs().tagCount() == 0;
	}

	@Override
	public Optional<UUID> getPrimaryKey() {
		if(!empty()){
			return Optional.of(UUID.fromString(getUUIDs().getStringTagAt(0)));
		}else{
			return Optional.empty();
		}
	}

}
