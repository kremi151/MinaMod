package lu.kremi151.minamod.commands;

import java.io.File;
import java.util.Optional;

import lu.kremi151.minamod.MinaPermissions;
import lu.kremi151.minamod.util.TextHelper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.WorldServer;

public class CommandOreInjectorCurrentDirectory extends MinaCommandBase{

	CommandOreInjectorCurrentDirectory(CommandBase parent) {
		super(parent);
	}

	@Override
	public String getDescription() {
		return "Shows the current directory where the ore injector suggests the region files (*.mca) of the current world are located";
	}

	@Override
	public Optional<String> getPermissionNode() {
		return Optional.of(MinaPermissions.CMD_OREGEN_PWD);
	}

	@Override
	public String getName() {
		return "pwd";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		String worldstr = null;
		
		for(int i = 0 ; i < args.length ; i++) {
			String arg = args[i];
			if(arg.equalsIgnoreCase("-w") || arg.equalsIgnoreCase("-world")){
				if(i + 1 < args.length) {
					worldstr = args[i++];
					break;
				}else {
					throw new CommandException("The world flag (-w or -world) needs to be followed by a valid world name");
				}
			}
		}
		
		if(worldstr != null) {
			WorldServer world = CommandOreInjector.findWorldByName(server.getServer(), worldstr);
			if(world != null) {
				showCurrentDirectory(world, sender);
			}else {
				TextHelper.sendTranslateableErrorMessage(sender, "msg.cmd.ore_injector.regen.not_found", worldstr);
			}
		}else {
			if(sender instanceof EntityPlayer) {
				showCurrentDirectory((WorldServer)((EntityPlayer)sender).world, sender);
			}else {
				TextHelper.sendTranslateableErrorMessage(sender, "msg.cmd.ore_injector.regen.only_for_players");
			}
		}
	}
	
	private void showCurrentDirectory(WorldServer world, ICommandSender sender) {
		File dir = CommandOreInjector.getRegionFilesFolderForWorld(world);
		TextHelper.sendChatMessage(sender, dir.getAbsolutePath());
		if(dir.exists() && dir.isDirectory()) {
			TextHelper.sendChatMessage(sender, TextFormatting.GREEN, "-> Exists, contains " + CommandOreInjector.listRegionFileNames(dir).length + " region file(s) (*.mca)");
			if(dir.canRead()) {
				TextHelper.sendChatMessage(sender, TextFormatting.GREEN, "-> Readable");
			}else {
				TextHelper.sendChatMessage(sender, TextFormatting.RED, "-> Not readable");
			}
			if(dir.canWrite()) {
				TextHelper.sendChatMessage(sender, TextFormatting.GREEN, "-> Writable");
			}else {
				TextHelper.sendChatMessage(sender, TextFormatting.RED, "-> Not writable");
			}
		}else {
			TextHelper.sendChatMessage(sender, TextFormatting.RED, "-> Not present or is not a directory");
		}
	}

}
