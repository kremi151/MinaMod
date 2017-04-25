package lu.kremi151.minamod.capabilities;

import java.util.Optional;
import java.util.UUID;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public interface IKey {
	
	@CapabilityInject(IKey.class)
	public static final Capability<IKey> CAPABILITY_KEY = null;

	boolean canUnlock(UUID uuid);
	void registerUnlockable(UUID uuid);
	boolean empty();
	Optional<UUID> getPrimaryKey();
}
