package lu.kremi151.minamod.capabilities;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;

public class CapabilityItemKey implements IKey{
	
	private final ItemStack stack;
	private final LinkedList<UUID> uuids = new LinkedList<>();
	
	public CapabilityItemKey(ItemStack stack){
		this.stack = stack;
	}
	
	public List<UUID> getUnlockableUUIDs(){
		return Collections.unmodifiableList(uuids);
	}
	
	public void clearUnlockables() {
		uuids.clear();
	}

	@Override
	public boolean canUnlock(@Nonnull UUID uuid){
		for(UUID _uuid : uuids) {
			if(_uuid.equals(uuid)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void registerUnlockable(@Nonnull UUID uuid) {
		if(uuid != null){
			uuids.add(uuid);
		}
	}

	@Override
	public boolean empty() {
		return uuids.size() == 0;
	}

	@Override
	public Optional<UUID> getPrimaryKey() {
		if(!empty()){
			return Optional.of(uuids.getFirst());
		}else{
			return Optional.empty();
		}
	}

}
