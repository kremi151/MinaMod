package lu.kremi151.minamod.commands;

import java.util.Optional;

import net.minecraft.command.CommandBase;

public class CommandOreInjectorTree extends MinaCommandTreeBase{

	CommandOreInjectorTree(CommandBase parent) {
		super(parent);
		this.addSubcommand(new CommandOreInjectorList(this));
		this.addSubcommand(new CommandOreInjector(this));
		this.addSubcommand(new CommandOreAnalyse(this));
		this.addSubcommand(new CommandOreInjectorCurrentDirectory(this));
		this.addSubcommand(new CommandOreInjectorChangePwd(this));
	}

	@Override
	public String getDescription() {
		return "Tree command for ore regeneration";
	}

	@Override
	public Optional<String> getPermissionNode() {
		return Optional.empty();
	}

	@Override
	public String getName() {
		return "ore";
	}

}
