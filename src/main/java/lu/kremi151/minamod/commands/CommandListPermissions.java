package lu.kremi151.minamod.commands;

import java.util.Optional;

import lu.kremi151.minamod.MinaPermissions;
import lu.kremi151.minamod.annotations.MinaPermission;
import lu.kremi151.minamod.util.AnnotationProcessor;
import lu.kremi151.minamod.util.TextHelper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextFormatting;

public class CommandListPermissions extends MinaCommandBase{

	CommandListPermissions(CommandBase parent) {
		super(parent);
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender cs, String[] arg) throws CommandException {
		TextHelper.sendTranslateableChatMessage(cs, TextFormatting.GRAY, "msg.cmd.print_perms.head");
		new AnnotationProcessor<>(MinaPermission.class, String.class)
			.processStrict(MinaPermissions.class, (node, pnode) -> TextHelper.sendChatMessage(cs, "- " + pnode));
	}

	@Override
	public String getName() {
		return "list-perms";
	}

	@Override
	public String getDescription() {
		return "Lists the permission nodes used by MinaMod";
	}

	@Override
	public Optional<String> getPermissionNode() {
		return Optional.of(MinaPermissions.SHOW_PERM_NODES);
	}

}
