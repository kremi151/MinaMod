package lu.kremi151.minamod.commands;

import java.io.File;
import java.util.Optional;

import lu.kremi151.minamod.MinaPermissions;
import lu.kremi151.minamod.util.TextHelper;
import lu.kremi151.minamod.worlddata.MinaWorld;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.WorldServer;

public class CommandOreInjectorChangePwd extends MinaCommandBase{

	CommandOreInjectorChangePwd(CommandBase parent) {
		super(parent);
	}

	@Override
	public String getDescription() {
		return "Modifies the working directory of the ore injector for the current world";
	}

	@Override
	public Optional<String> getPermissionNode() {
		return Optional.of(MinaPermissions.CMD_OREGEN_PWD);
	}

	@Override
	public String getName() {
		return "cd";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		String worldstr = null;
		String path = null;
		
		for(int i = 0 ; i < args.length ; i++) {
			String arg = args[i];
			if(arg.equalsIgnoreCase("-w") || arg.equalsIgnoreCase("-world")){
				if(i + 1 < args.length) {
					worldstr = args[i++];
					break;
				}else {
					throw new CommandException("The world flag (-w or -world) needs to be followed by a valid world name");
				}
			}else {
				if(path == null) {
					path = arg;
				}else {
					throw new CommandException("Too much arguments");
				}
			}
		}
		
		if(path == null) {
			throw new CommandException("No path specialized");
		}
		
		if(worldstr != null) {
			WorldServer world = CommandOreInjector.findWorldByName(server.getServer(), worldstr);
			if(world != null) {
				switchDirectory(world, sender, path);
			}else {
				TextHelper.sendTranslateableErrorMessage(sender, "msg.cmd.ore_injector.regen.not_found", worldstr);
			}
		}else {
			if(sender instanceof EntityPlayer) {
				switchDirectory((WorldServer)((EntityPlayer)sender).world, sender, path);
			}else {
				TextHelper.sendTranslateableErrorMessage(sender, "msg.cmd.ore_injector.regen.only_for_players");
			}
		}
	}
	
	private void switchDirectory(WorldServer world, ICommandSender sender, String path) {
		File dir = new File(path);
		if(dir.exists() && dir.isDirectory()) {
			MinaWorld.forWorld(world).getConfiguration().setCustomOreInjectorWorkingPath(dir);
			TextHelper.sendChatMessage(sender, TextFormatting.GREEN, "Ore injector working path for world " + world.getWorldInfo().getWorldName() + " changed to " + dir.getAbsolutePath());
		}else {
			TextHelper.sendChatMessage(sender, TextFormatting.RED, "The given path " + path + " either does not exist or is not a directory");
		}
	}

}
