package lu.kremi151.minamod.capabilities;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public interface ICapabilityJetpack {

	@CapabilityInject(ICapabilityJetpack.class)
	public static final Capability<ICapabilityJetpack> CAPABILITY_JETPACK = null;
	
	void setUsingJetpack(boolean v);
	boolean isUsingJetpack();
}
