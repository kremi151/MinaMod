package lu.kremi151.minamod.capabilities.stats.snack;

import java.util.Set;
import java.util.function.BiConsumer;

import lu.kremi151.minamod.capabilities.stats.types.StatType;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public interface ISnack {

	@CapabilityInject(ISnack.class)
	public static final Capability<ISnack> CAPABILITY = null;
	
	Set<StatType> getSupportedTypes();
	int getModification(StatType type);
	boolean offer(StatType type, int value);
	
	default void forEachType(BiConsumer<StatType, Integer> consumer) {
		for(StatType type : getSupportedTypes()) {
			consumer.accept(type, getModification(type));
		}
	}
}
