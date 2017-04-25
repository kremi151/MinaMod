package lu.kremi151.minamod.commands;

import net.minecraft.command.ICommandSender;
import net.minecraftforge.server.command.CommandTreeBase;

public class CommandStats extends CommandTreeBase{
	
	public CommandStats(){
		this.addSubcommand(new CommandResetStats());
	}

	@Override
	public String getName() {
		return "stats";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "/mina stats";
	}

}
