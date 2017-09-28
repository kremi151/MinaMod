package lu.kremi151.minamod.commands;

import java.util.Optional;
import java.util.Set;

import lu.kremi151.minamod.util.OreInjectorManager;
import lu.kremi151.minamod.util.TextHelper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class CommandOreInjectorList extends MinaCommandBase{

	CommandOreInjectorList(CommandBase parent) {
		super(parent);
	}

	@Override
	public String getDescription() {
		return "Lists available ores for regeneration";
	}

	@Override
	public Optional<String> getPermissionNode() {
		return Optional.empty();
	}

	@Override
	public String getName() {
		return "list";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		TextHelper.sendTranslateableChatMessage(sender, "msg.cmd.ore_injector.list.available");
		Set<String> ids = OreInjectorManager.getAvailableInjectors();
		for(String id : ids) {
			TextHelper.sendChatMessage(sender, "* " + id);
		}
	}

}
