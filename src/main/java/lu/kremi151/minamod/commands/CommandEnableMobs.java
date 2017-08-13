package lu.kremi151.minamod.commands;

import java.util.Optional;

import lu.kremi151.minamod.MinaPermissions;
import lu.kremi151.minamod.util.TextHelper;
import lu.kremi151.minamod.worlddata.MinaWorld;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextFormatting;

public class CommandEnableMobs extends MinaPlayerCommandBase{

	static final String NAME_ENABLE = "enable-mobs";
	static final String NAME_DISABLE = "disable-mobs";
	
	private boolean enable;
	
	CommandEnableMobs(CommandBase parent, boolean enable){
		super(parent);
		this.enable = enable;
	}

	@Override
	public void execute(MinecraftServer server, EntityPlayer player, String[] arg) throws CommandException {
		MinaWorld.forWorld(player.world).setMobsEnabled(enable);
		if(enable){
			TextHelper.sendTranslateableChatMessage(player, TextFormatting.GREEN, "msg.cmd.mobs_enabled");
		}else{
			TextHelper.sendTranslateableChatMessage(player, TextFormatting.GREEN, "msg.cmd.mobs_disabled");
		}
	}

	@Override
	public Optional<String> getPermissionNode() {
		return Optional.of(MinaPermissions.TOGGLE_MOBS);
	}

	@Override
	public String getName() {
		return enable?NAME_ENABLE:NAME_DISABLE;
	}

	@Override
	public String getDescription() {
		return enable?"Temporary enable mobs":"Temporary disable mobs";
	}

}
