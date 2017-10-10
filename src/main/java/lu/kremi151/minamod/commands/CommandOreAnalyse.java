package lu.kremi151.minamod.commands;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import lu.kremi151.minamod.MinaPermissions;
import lu.kremi151.minamod.util.MinaUtils;
import lu.kremi151.minamod.util.TextHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandOreAnalyse extends MinaPlayerCommandBase{

	CommandOreAnalyse(CommandBase parent) {
		super(parent);
	}

	@Override
	public String getDescription() {
		return "Analyses a chunk for contained ores";
	}

	@Override
	public Optional<String> getPermissionNode() {
		return Optional.of(MinaPermissions.CMD_ANALYSE_ORES);
	}

	@Override
	public String getName() {
		return "analyze";
	}

	@Override
	public void execute(MinecraftServer server, EntityPlayer player, String[] arg) throws CommandException {
		final int chunkX = player.getPosition().getX() / 16, chunkZ = player.getPosition().getZ() / 16;
		final int sx = chunkX * 16, sz = chunkZ * 16;
		final HashMap<IBlockState, Integer> countMap = new HashMap<>();
		final String regex = (arg.length > 0) ? arg[0] : null;
		for(int x = sx ; x < sx + 16 ; x++) {
			for(int z = sz ; z < sz + 16 ; z++) {
				final int hv = MinaUtils.getHeightValue(player.world, x, z);
				for(int y = 0 ; y < hv ; y++) {
					IBlockState state = player.world.getBlockState(new BlockPos(x, y, z));
					if(state.getBlock() != Blocks.AIR && state.isBlockNormalCube() && (regex == null || state.toString().matches(regex))) {
						countMap.merge(state, 1, (a, b) -> {
							return a + b;
						});
					}
				}
			}
		}
		TextHelper.sendChatMessage(player, "Analyse results:");
		for(Map.Entry<IBlockState, Integer> entry : countMap.entrySet()) {
			TextHelper.sendChatMessage(player, "* " + entry.getKey().toString() + ": " + entry.getValue());
		}
	}

}
