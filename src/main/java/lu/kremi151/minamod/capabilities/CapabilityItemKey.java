package lu.kremi151.minamod.capabilities;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;

public class CapabilityItemKey implements IKey{
	
	protected final ItemStack stack;
	protected final Set<UUID> uuids = new HashSet<>();
	
	public CapabilityItemKey(ItemStack stack){
		this.stack = stack;
	}

	@Override
	public Set<UUID> getKeyIds() {
		return Collections.unmodifiableSet(uuids);
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
	public void registerUnlockables(Collection<UUID> list) {
		uuids.addAll(list);
	}
	
	@Override
	public void registerUnlockables(UUID... uuids) {
		this.uuids.addAll(Arrays.asList(uuids));
	}

	@Override
	public boolean empty() {
		return uuids.size() == 0;
	}

}
