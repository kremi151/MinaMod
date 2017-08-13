package lu.kremi151.minamod.commands;

import java.util.Optional;

import net.minecraft.command.CommandBase;

public class CommandStats extends MinaCommandTreeBase{
	
	CommandStats(CommandBase parent){
		super(parent);
		this.addSubcommand(new CommandResetStats(this));
	}

	@Override
	public String getName() {
		return "stats";
	}

	@Override
	public String getDescription() {
		return "Tree command to handle player stats";
	}

	@Override
	public Optional<String> getPermissionNode() {
		return Optional.empty();
	}

}
