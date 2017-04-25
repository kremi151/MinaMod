package lu.kremi151.minamod.commands;

import java.util.Optional;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.server.permission.PermissionAPI;

public abstract class MinaPlayerCommandBase extends CommandBase{

	@Override
	@Deprecated
	public final void execute(MinecraftServer server, ICommandSender cs, String[] arg) throws CommandException {
		execute(server, (EntityPlayer)cs, arg);
	}
	
	public abstract void execute(MinecraftServer server, EntityPlayer player, String[] arg) throws CommandException;
	
	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender){
		Optional<String> perm = getPermissionNode();
		return sender instanceof EntityPlayer && (!perm.isPresent() || PermissionAPI.hasPermission((EntityPlayer)sender, perm.get()));
	}
	
	public abstract Optional<String> getPermissionNode();

}
