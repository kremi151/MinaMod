package lu.kremi151.minamod.commands;

import java.util.Optional;

import lu.kremi151.minamod.MinaPermissions;
import lu.kremi151.minamod.capabilities.coinhandler.ICoinHandler;
import lu.kremi151.minamod.util.TextHelper;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class CommandCoins extends MinaPlayerCommandBase{
	
	CommandCoins(){}

	@Override
	public String getName() {
		return "coins";
	}

	@Override
	public void execute(MinecraftServer server, EntityPlayer player, String[] arg) throws CommandException {
		if(player.hasCapability(ICoinHandler.CAPABILITY, null)){
			ICoinHandler ch = player.getCapability(ICoinHandler.CAPABILITY, null);
			TextHelper.sendTranslateableChatMessage(player, "gui.coin_bag.amount", ch.getAmountCoins());
		}else{
			throw new CommandException("No capability found");
		}
	}

	@Override
	public Optional<String> getPermissionNode() {
		return Optional.of(MinaPermissions.CMD_COINS);
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "/mina coins - Shows the amount of coins you have";
	}

}
