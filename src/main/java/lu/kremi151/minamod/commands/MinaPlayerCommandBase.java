package lu.kremi151.minamod.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public abstract class MinaPlayerCommandBase extends MinaCommandBase{
	
	MinaPlayerCommandBase(CommandBase parent){
		super(parent);
	}

	@Override
	@Deprecated
	public final void execute(MinecraftServer server, ICommandSender cs, String[] arg) throws CommandException {
		execute(server, (EntityPlayer)cs, arg);
	}
	
	public abstract void execute(MinecraftServer server, EntityPlayer player, String[] arg) throws CommandException;
	
	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender){
		return sender instanceof EntityPlayer && super.checkPermission(server, sender);
	}

}
