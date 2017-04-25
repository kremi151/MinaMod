package lu.kremi151.minamod.commands;

import java.util.Optional;

import lu.kremi151.minamod.MinaPermissions;
import lu.kremi151.minamod.capabilities.CapabilityPlayerStats;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class CommandResetStats extends MinaPlayerCommandBase{

	@Override
	public String getName() {
		return "reset";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "/mina stats reset";
	}

	@Override
	public void execute(MinecraftServer server, EntityPlayer player, String[] arg) throws CommandException {
		CapabilityPlayerStats ps = (CapabilityPlayerStats) player.getCapability(CapabilityPlayerStats.CAPABILITY, null);
		ps.resetStats();
	}

	@Override
	public Optional<String> getPermissionNode() {
		return Optional.of(MinaPermissions.CMD_STATS_RESET);
	}

}
