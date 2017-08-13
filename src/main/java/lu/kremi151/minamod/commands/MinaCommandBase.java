package lu.kremi151.minamod.commands;

import java.util.Optional;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.server.permission.PermissionAPI;

public abstract class MinaCommandBase extends CommandBase implements IMinaCommand{
	
	private final CommandBase parent;
	
	MinaCommandBase(CommandBase parent){
		this.parent = parent;
	}
	
	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender){
		Optional<String> perm = getPermissionNode();
		return (!perm.isPresent() || PermissionAPI.hasPermission((EntityPlayer)sender, perm.get()));
	}

	@Override
	public final String getUsage(ICommandSender sender) {
		return TextFormatting.GRAY + "/" + TextFormatting.GOLD + this.accumulativeUsageName() + TextFormatting.WHITE + formatArguments(getArguments()) + TextFormatting.RESET + " - " + TextFormatting.ITALIC + TextFormatting.GRAY + this.getDescription();
	}
	
	@Override
	public final String accumulativeUsageName() {
		if(parent instanceof IMinaCommand) {
			return ((IMinaCommand)parent).accumulativeUsageName() + " " + this.getName();
		}else {
			return parent.getName() + " " + this.getName();
		}
	}
	
	private String formatArguments(String args[]) {
		StringBuilder sb = new StringBuilder();
		for(String str : args) {
			sb.append(" ");
			sb.append(str);
		}
		return sb.toString();
	}
	
	protected String[] getArguments() { return new String[0]; }
	
}
