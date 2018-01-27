package lu.kremi151.minamod.advancements.triggers;

import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.util.ReflectionLoader;

public class MinaTriggers {

	public static final TriggerCustom TRIGGER_OPEN_AMULET_INVENTORY = new TriggerCustom(MinaMod.MODID, "open_amulet_inventory");
	public static final TriggerCustom TRIGGER_OPEN_STATS = new TriggerCustom(MinaMod.MODID, "open_stats");
	public static final TriggerCustom TRIGGER_MODIFY_STATS = new TriggerCustom(MinaMod.MODID, "modify_stats");
	public static final TriggerCustom TRIGGER_TRAIN_STATS = new TriggerCustom(MinaMod.MODID, "train_stats");

	public static void register() {
		ReflectionLoader.CriteriaTriggers_register(TRIGGER_OPEN_AMULET_INVENTORY);
		ReflectionLoader.CriteriaTriggers_register(TRIGGER_OPEN_STATS);
		ReflectionLoader.CriteriaTriggers_register(TRIGGER_MODIFY_STATS);//TODO: Use vanilla trigger?
		ReflectionLoader.CriteriaTriggers_register(TRIGGER_TRAIN_STATS);
	}
}
