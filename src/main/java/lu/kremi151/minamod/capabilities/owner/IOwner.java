package lu.kremi151.minamod.capabilities.owner;

import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public interface IOwner {

	@CapabilityInject(IOwner.class)
	public static final Capability<IOwner> CAPABILITY = null;
	
	@Nullable UUID getOwner();
	void setOwner(@Nullable UUID owner);

}
