package lu.kremi151.minamod.util.registration;

import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;

public abstract class CommonRegistrationHandler<T extends IForgeRegistryEntry<T>> extends AbstractRegistrationHandler{

	protected final String name;
	protected final T obj;
	protected String variantNames[] = null;
	protected String oreName = null;
	
	public CommonRegistrationHandler(String name, T obj) {
		this.obj = obj;
		this.name = name;
	}
	
	public CommonRegistrationHandler variantNames(String... names) {
		this.variantNames = names;
		return this;
	}
	
	public CommonRegistrationHandler ore(String ore) {
		this.oreName = ore;
		return this;
	}
	
	public CommonRegistrationHandler ore() {
		this.oreName = name;
		return this;
	}

}
