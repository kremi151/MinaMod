package lu.kremi151.minamod.commands;

import java.util.Optional;

import lu.kremi151.minamod.MinaPermissions;
import lu.kremi151.minamod.util.TextHelper;
import lu.kremi151.minamod.worldprovider.WorldProviderOverworldHook;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextFormatting;

public class CommandBloodMoon extends MinaPlayerCommandBase{

	CommandBloodMoon(CommandBase parent) {
		super(parent);
	}

	@Override
	public String getName() {
		return "blood-moon";
	}

	@Override
	public String getDescription() {
		return "Sets the time to the next beginning blood moon";
	}

	@Override
	public void execute(MinecraftServer server, EntityPlayer player, String[] arg) throws CommandException {
		if(!player.world.isRemote) {
			if(player.world.provider.getClass() == WorldProviderOverworldHook.class) {
				((WorldProviderOverworldHook)player.world.provider).setToNextBloodMoon();
				TextHelper.sendTranslateableChatMessage(player, TextFormatting.GREEN, "msg.cmd.blood_moon.success");
			}else {
				TextHelper.sendTranslateableErrorMessage(player, "msg.cmd.blood_moon.not_compatible");
			}
		}
	}

	@Override
	public Optional<String> getPermissionNode() {
		return Optional.of(MinaPermissions.CMD_BLOOD_MOON);
	}

}
