package lu.kremi151.minamod.commands;

import java.lang.reflect.Field;

import lu.kremi151.minamod.MinaPermissions;
import lu.kremi151.minamod.annotations.MinaPermission;
import lu.kremi151.minamod.util.TextHelper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.server.permission.PermissionAPI;

public class CommandListPermissions extends CommandBase{
	
	CommandListPermissions(){}

	@Override
	public void execute(MinecraftServer server, ICommandSender cs, String[] arg) throws CommandException {
		TextHelper.sendTranslateableChatMessage(cs, TextFormatting.GRAY, "msg.cmd.print_perms.head");
		Field fields[] = MinaPermissions.class.getDeclaredFields();
		for(Field f : fields){
			f.setAccessible(true);
			MinaPermission node = f.getAnnotation(MinaPermission.class);
			if(node != null && f.getType() == String.class){
				try {
					TextHelper.sendChatMessage(cs, "- " + (String) f.get(null));
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender src) {
		return (src == server) || (src instanceof EntityPlayer && PermissionAPI.hasPermission((EntityPlayer)src, MinaPermissions.SHOW_PERM_NODES));
	}

	@Override
	public String getName() {
		return "list-perms";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "/mina list-perms - Lists the permission nodes used by MinaMod";
	}

}
