package lu.kremi151.minamod.util.registration;

import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;

@FunctionalInterface
public interface IRegistrationInterface<T extends IForgeRegistryEntry<T>, R extends AbstractRegistrationHandler> {

	R register(T obj, String name);
	
}
