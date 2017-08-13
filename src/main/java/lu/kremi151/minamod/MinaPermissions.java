package lu.kremi151.minamod;

import lu.kremi151.minamod.annotations.MinaPermission;
import net.minecraftforge.server.permission.DefaultPermissionLevel;

public class MinaPermissions {
	
	@MinaPermission(desc = "Allows to list every permission node used by MinaMod in-game", lvl = DefaultPermissionLevel.OP)
	public static final String SHOW_PERM_NODES = "minamod.admin.debug.show_perms";
	
	@MinaPermission(desc = "Allows to tick a block manually by left-clicking it with a blazerod", lvl = DefaultPermissionLevel.OP)
	public static final String TICK_BLOCK_MANUALLY = "minamod.admin.debug.tick_manually";
	
	@MinaPermission(desc = "Allows to enable/disable mobs temporarily", lvl = DefaultPermissionLevel.OP)
	public static final String TOGGLE_MOBS = "minamod.admin.debug.toggle_mobs";
	
	@MinaPermission(desc = "Allows to save the NBT data structure of the currently held item to a file", lvl = DefaultPermissionLevel.OP)
	public static final String ITEM_TO_NBT_CMD = "minamod.admin.debug.item_to_nbt";
	

	@MinaPermission(desc = "Allows to access the entire content of letterboxes from other players", lvl = DefaultPermissionLevel.OP)
	public static final String WATCH_LETTERBOX_CONTENT = "minamod.admin.letterbox.watch";
	
	
	@MinaPermission(desc = "Allows to receive a copy of the herb guide", lvl = DefaultPermissionLevel.OP)
	public static final String CMD_HERB_GUIDE = "minamod.admin.special.herb_guide";
	
	@MinaPermission(desc = "Allows to create a RMP explosion per command", lvl = DefaultPermissionLevel.OP)
	public static final String CMD_EXECUTE_RMP = "minamod.admin.special.rmp_cmd";
	
	@MinaPermission(desc = "Allows to spawn a structure per command", lvl = DefaultPermissionLevel.OP)
	public static final String CMD_SPAWN_STRUCTURE = "minamod.admin.special.spawn_structure";
	
	@MinaPermission(desc = "Allows to show the amount of coins you have", lvl = DefaultPermissionLevel.OP)
	public static final String CMD_COINS = "minamod.admin.coins.show";
	
	@MinaPermission(desc = "Allows to save a schematic file to disk", lvl = DefaultPermissionLevel.OP)
	public static final String CMD_SCHEMATIC_SAVE = "minamod.admin.schematic.save";
	
	@MinaPermission(desc = "Allows to load a schematic file from disk", lvl = DefaultPermissionLevel.OP)
	public static final String CMD_SCHEMATIC_LOAD = "minamod.admin.schematic.load";
	
	@MinaPermission(desc = "Allows to create a selection area for using schematics", lvl = DefaultPermissionLevel.OP)
	public static final String CMD_SCHEMATIC_SELECTION = "minamod.admin.schematic.selection";
	
	@MinaPermission(desc = "Allows to reset player stats", lvl = DefaultPermissionLevel.OP)
	public static final String CMD_STATS_RESET = "minamod.admin.stats.reset";
	
	@MinaPermission(desc = "Allows to repair a mulfunctioning slot machine", lvl = DefaultPermissionLevel.OP)
	public static final String CMD_RESET_SLOT_MACHINE = "minamod.admin.slotmachine.reset";
	
	@MinaPermission(desc = "Allows to set the world time to the next blood moon", lvl = DefaultPermissionLevel.OP)
	public static final String CMD_BLOOD_MOON = "minamod.admin.time.blood_moon";
	

	@MinaPermission(desc = "Allows to use elevators", lvl = DefaultPermissionLevel.ALL)
	public static final String ALLOW_ELEVATOR_USE = "minamod.all.elevator.use";
	
	@MinaPermission(desc = "Allows to use soul pearls", lvl = DefaultPermissionLevel.ALL)
	public static final String ALLOW_SOUL_PEARL_USE = "minamod.all.soulpearl.use";
	
	@MinaPermission(desc = "Allows to gamble (for example to use slot machines)", lvl = DefaultPermissionLevel.ALL)
	public static final String ALLOW_GAMBLING = "minamod.gambling";
}
