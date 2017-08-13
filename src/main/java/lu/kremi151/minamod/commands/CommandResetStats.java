package lu.kremi151.minamod.commands;

import java.util.Optional;

import lu.kremi151.minamod.MinaPermissions;
import lu.kremi151.minamod.capabilities.stats.ICapabilityStats;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class CommandResetStats extends MinaPlayerCommandBase{

	CommandResetStats(CommandBase parent) {
		super(parent);
	}

	@Override
	public String getName() {
		return "reset";
	}

	@Override
	public String getDescription() {
		return "Resets player stats to default values";
	}

	@Override
	public void execute(MinecraftServer server, EntityPlayer player, String[] arg) throws CommandException {
		ICapabilityStats ps = player.getCapability(ICapabilityStats.CAPABILITY, null);
		ps.reset();
	}

	@Override
	public Optional<String> getPermissionNode() {
		return Optional.of(MinaPermissions.CMD_STATS_RESET);
	}

}
