package lu.kremi151.minamod.advancements.triggers;

import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.util.ReflectionLoader;

public class MinaTriggers {

	public static final TriggerOpenInventory TRIGGER_OPEN_AMULET_INVENTORY = new TriggerOpenInventory(MinaMod.MODID, "open_amulet_inventory");
	public static final TriggerOpenInventory TRIGGER_OPEN_STATS = new TriggerOpenInventory(MinaMod.MODID, "open_stats");

	public static void register() {
		ReflectionLoader.CriteriaTriggers_register(TRIGGER_OPEN_AMULET_INVENTORY);
		ReflectionLoader.CriteriaTriggers_register(TRIGGER_OPEN_STATS);
	}
}
