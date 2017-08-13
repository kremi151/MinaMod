package lu.kremi151.minamod.commands;

import lu.kremi151.minamod.MinaMod;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.server.command.CommandTreeBase;

public class CommandMinaBase extends CommandTreeBase{
	
	public CommandMinaBase(){
		if(MinaMod.getMinaConfig().isDebugging()){
			this.addSubcommand(new CommandEnableMobs(this, true));
			this.addSubcommand(new CommandEnableMobs(this, false));
		}
		this.addSubcommand(new CommandItemToNBT(this));
		this.addSubcommand(new CommandListPermissions(this));
		this.addSubcommand(new CommandCoins(this));
		this.addSubcommand(new CommandStats(this));
		this.addSubcommand(new CommandResetSlotMachine(this));
		this.addSubcommand(new CommandBloodMoon(this));
	}
	
	@Override
	public String getUsage(ICommandSender icommandsender)
	{
		return "/mina - Main command for the MinaMod";
	}
	
	/*private void showHelp(ICommandSender cs){
		MinaUtils.sendTranslateableChatMessage(cs, "msg.cmd.minamod.list");
		for(Entry<String, IMinaCommand> e : commandRegistry.entrySet()){
			Optional<CommandDescription> help = e.getValue().getHelp();
			if(help.isPresent() && e.getValue().canExecute(cs)){
				ITextComponent line[] = help.get().getLines();
				for(int i = 0 ; i < line.length ; i++){
					cs.sendMessage(line[i]);
				}
			}
		}
	}*/

	@Override
	public String getName() {
		return "mina";
	}

}
