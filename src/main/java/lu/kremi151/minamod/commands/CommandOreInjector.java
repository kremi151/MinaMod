package lu.kremi151.minamod.commands;

import java.io.File;
import java.util.ArrayList;
import java.util.Optional;

import lu.kremi151.minamod.MinaPermissions;
import lu.kremi151.minamod.util.OreInjectorManager;
import lu.kremi151.minamod.util.TextHelper;
import lu.kremi151.minamod.worlddata.MinaWorld;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;

public class CommandOreInjector extends MinaCommandBase{

	CommandOreInjector(CommandBase parent) {
		super(parent);
	}

	@Override
	public String getDescription() {
		return "Regenerates missing ores to the specified world";
	}

	@Override
	public Optional<String> getPermissionNode() {
		return Optional.of(MinaPermissions.CMD_REGEN_ORES);
	}

	@Override
	public String getName() {
		return "regen";
	}
	
	static WorldServer findWorldByName(MinecraftServer server, String name) {
		for(WorldServer ws : server.getServer().worlds) {
			if(ws.getWorldInfo().getWorldName().equalsIgnoreCase(name)) {
				return ws;
			}
		}
		return null;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		String worldstr = null;
		ArrayList<String> id_list = new ArrayList<String>(args.length);
		boolean info = false;
		
		for(int i = 0 ; i < args.length ; i++) {
			String arg = args[i];
			if(arg.equalsIgnoreCase("-w") || arg.equalsIgnoreCase("-world")){
				if(i + 1 < args.length) {
					worldstr = args[i++];
					break;
				}else {
					throw new CommandException("The world flag (-w or -world) needs to be followed by a valid world name");
				}
			}else if(arg.equalsIgnoreCase("--info")) {
				info = true;
			}else {
				id_list.add(arg);
			}
		}
		
		String oreInjectors[] = new String[id_list.size()];
		for(int i = 0 ; i < oreInjectors.length ; i++) {
			oreInjectors[i] = id_list.get(i);
		}
		
		if(worldstr != null) {
			WorldServer world = findWorldByName(server.getServer(), worldstr);
			if(world != null) {
				doOreInjection(sender, info, world, oreInjectors);
			}else {
				TextHelper.sendTranslateableErrorMessage(sender, "msg.cmd.ore_injector.regen.not_found", worldstr);
			}
		}else {
			if(sender instanceof EntityPlayer) {
				doOreInjection(sender, info, (WorldServer)((EntityPlayer)sender).world, oreInjectors);
			}else {
				TextHelper.sendTranslateableErrorMessage(sender, "msg.cmd.ore_injector.regen.only_for_players");
			}
		}
	}
	
	static File getRegionFilesFolderForWorld(WorldServer world) {
		return MinaWorld.forWorld(world).getConfiguration().getCustomOreInjectorWorkingPath()
				.orElse(new File(world.getChunkSaveLocation().getAbsolutePath(), "region"));
	}
	
	static String[] listRegionFileNames(File folder) {
		return folder.list((file, str) ->str.toLowerCase().endsWith(".mca"));
	}
	
	private void doOreInjection(ICommandSender sender, boolean info, WorldServer world, String oreInjectors[]) {
		//world.sendQuittingDisconnectingPacket();
		TextHelper.sendTranslateableChatMessage(sender, "msg.cmd.ore_injector.regen.processing");

		File regionFolder = getRegionFilesFolderForWorld(world);
		if(regionFolder.exists() && regionFolder.isDirectory()) {
			String regionFiles[] = listRegionFileNames(regionFolder);
			for(String file : regionFiles) {
				String parts[] = file.split("\\.");
				if(parts.length == 4) {
					try {
						int rx = Integer.parseInt(parts[1]);
						int rz = Integer.parseInt(parts[2]);
						
						int cx = rx * 32;
						int cz = rz * 32;
						final int bcx = cx + 32;
						final int bcz = cz + 32;
						
						for(; cx < bcx ; cx++) {
							for(; cz < bcz ; cz++) {
								if(info)TextHelper.sendChatMessage(sender, String.format("Regenerating chunk at x=%d, z=%d", cx, cz));
								ChunkPos cpos = new ChunkPos(cx, cz);
								if(world.isChunkGeneratedAt(cx, cz)) {
									Chunk chunk = world.getChunkFromChunkCoords(cx, cz);
									OreInjectorManager.performOreInjection(world, chunk, oreInjectors);
								}
							}
						}
					}catch(NumberFormatException e) {}
					
				}
			}
		}
		
		TextHelper.sendTranslateableChatMessage(sender, TextFormatting.GREEN, "msg.cmd.ore_injector.regen.finished");
	}

}
